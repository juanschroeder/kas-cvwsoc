SUMMARY = "Mainline Linux Kernel for Core-V Wally SoCs"

require recipes-kernel/linux/linux-mainline-common.inc

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPES','Image.lzo',' lzop-native','',d)}"

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPE_DISK','Image.lz4',' lz4-native','',d)}"

FILESEXTRAPATHS =. "${THISDIR}/linux:"

# FIXME: put in machine conf?
CVWSOC_DTS = "${CVWSOC_DTS_FILENAME}.dts"
DEPENDS += "u-boot-tools-native"
BRANCH = "master"
KBUILD_DEFCONFIG ?= "linux.soc.config"

SRCREV_BUILDROOT = "45d0afdd82645d529134ffc51c8c73ffa21c3f9b"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=https;branch=${BRANCH} \
            https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/br2-external-tree/board/wally/${KBUILD_DEFCONFIG};name=config \
            https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/devicetree/${CVWSOC_DTS};name=dts \
          "

SRC_URI[config.sha256sum] = "0e1f551eafeeba02c90640126d4b0d7487d097c91cbf1e91c174c5860dc6155a"

SRC_URI[dts.sha256sum] = "${DTS_SHA256}"
DTS_SHA256:cvwsoc-nexysa7 = "a3e66df00181ef8c208ddbd4adb80261782752826fe0d1b1c9a42cf60fa21e18"
DTS_SHA256:cvwsoc-genesys2 = "994f24c60859d10181b530bc7fa1289d10f3ea35a4011b07a004de097d9a557b"
DTS_SHA256:cvwsoc-virt = "6b30cf79d0aa46d7dce34d829d41eb6549371c32509201343cce186ebc5230f1"

# tiny Kernel
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
                'file://fragment-tiny1.cfg \
                file://fragment-tiny2.cfg  \
                file://fragment-tiny3.cfg ', '', d)}"

# EXT2 seems to be slower than cpio
# SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
#                ' file://fragment-reenable-fs.cfg ', '', d)}"


# LINUX_VERSION needed for PV in linux-mainline-common
# FIXME: 6.19 is crashing on boot (check)
#LINUX_VERSION = "6.19"
#SRCREV = "05f7e89ab9731565d8a62e3b5d1ec206485eeb0b"
#LINUX_VERSION = "6.12.8" # 
# SRCREV for v6.12
LINUX_VERSION = "6.12"
SRCREV = "adc218676eef25575469234709c2d87185ca223a"
SRC_URI:append:cvwsoc-nexysa7 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-nexysa7-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-genesys2 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-genesys2-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-virt = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvw-wally-dtb.patch', '', d)}"

COMPATIBLE_MACHINE = "(cvwsoc)"

# copy files where they are expected
do_kernel_metadata:prepend() {
    # We need to copy the defconfig to the source directory for the kernel build to find it
    install -m 644 ${UNPACKDIR}/${KBUILD_DEFCONFIG} ${S}/arch/riscv/configs/linux.soc.config
    install -m 644 ${UNPACKDIR}/${CVWSOC_DTS} ${S}/arch/riscv/boot/dts/${CVWSOC_DTS}
}

# manually generate the lz4 file u-boot accepts
do_deploy:append() {
    if [ "${KERNEL_IMAGETYPE}" = "Image.lz4" ]; then
        src="${B}/arch/riscv/boot/Image"
        out="${DEPLOYDIR}/Image.lz4"

        # keep the legacy one for reference
        if [ -e "${out}" ]; then
            mv -f "${out}" "${out}.orig"
        fi

        # produce "new" LZ4 frame format (no -l)
        ${STAGING_BINDIR_NATIVE}/lz4 -12 --content-size "${src}" "${out}.tmp"
        mv -f "${out}.tmp" "${out}"
    fi
}

# This is broken in current Yocto: https://lists.openembedded.org/g/openembedded-core/message/226911
KERNEL_FEATURES:remove = "${KERNEL_FEATURES_RISCV}"
