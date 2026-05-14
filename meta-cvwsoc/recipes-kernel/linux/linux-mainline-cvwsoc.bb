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

SRCREV_BUILDROOT = "ad7828b1e09e7f780779934c65620c8f271b8220"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=https;branch=${BRANCH} \
            https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/br2-external-tree/board/wally/${KBUILD_DEFCONFIG};name=config;downloadfilename=${KBUILD_DEFCONFIG}.${SRCREV_BUILDROOT}  \
            https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/devicetree/${CVWSOC_DTS};name=dts;downloadfilename=${CVWSOC_DTS}.${SRCREV_BUILDROOT} \
          "

SRC_URI[config.sha256sum] = "3343beaf711838e49f88cf5c30f2cab7d81f905cfd9e1ea9cbae5cac4415329f"

SRC_URI[dts.sha256sum] = "${DTS_SHA256}"
DTS_SHA256:cvwsoc-nexysa7 = "a3e66df00181ef8c208ddbd4adb80261782752826fe0d1b1c9a42cf60fa21e18"
DTS_SHA256:cvwsoc-genesys2 = "eacd0903402338c7d8f7d5caa64d01086a3be6f070dc9942db48cd7b9d87f3ed"
DTS_SHA256:cvwsoc-virt = "d1ef262492ba204b9d2eda0afbe99b80ea752141e3b4756795740b8d1c96fde2"
DTS_SHA256:cvwsoc-virt32 = "d843b70589f26504a917290c4d2c3b6102603c393100d32afded9316557fdace"

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
SRC_URI:append:cvwsoc-virt32 = " file://fragment-rv32.cfg ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-virt32-dtb.patch', '', d)}"

# FIXME: this driver still needs improvements
SRC_URI:append = " file://0003-sdhci-generic-driver-802935a6a27e48050339a19704700adc0b0ed282.patch \
                    file://0004-sdhci-generic-driver-fixes-v6.12.patch \
                "

COMPATIBLE_MACHINE = "(cvwsoc)"

# copy files where they are expected
do_kernel_metadata:prepend() {
    # We need to copy the defconfig to the source directory for the kernel build to find it
    install -m 644 ${UNPACKDIR}/${KBUILD_DEFCONFIG}.${SRCREV_BUILDROOT} ${S}/arch/riscv/configs/linux.soc.config
    printf '\n' >> ${S}/arch/riscv/configs/linux.soc.config
    install -m 644 ${UNPACKDIR}/${CVWSOC_DTS}.${SRCREV_BUILDROOT} ${S}/arch/riscv/boot/dts/${CVWSOC_DTS}
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
