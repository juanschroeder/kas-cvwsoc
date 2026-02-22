SUMMARY = "Mainline Linux Kernel for Core-V Wally SoCs"

require recipes-kernel/linux/linux-mainline-common.inc

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPES','Image.lzo',' lzop-native','',d)}"

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPE_DISK','Image.lz4',' lz4-native','',d)}"

FILESEXTRAPATHS =. "${THISDIR}/linux:"

# FIXME: put in machine conf?
CVWSOC_DTS = "${CVWSOC_DTS_FILENAME}.dts"
DEPENDS += "u-boot-tools-native"
BRANCH = "master"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=https;branch=${BRANCH} \
            https://raw.githubusercontent.com/juanschroeder/cvw/cvwsoc/linux/br2-external-tree/board/wally/linux.config;name=config \
            https://raw.githubusercontent.com/juanschroeder/cvw/cvwsoc/linux/devicetree/${CVWSOC_DTS};name=dts \
          "

SRC_URI[config.sha256sum] = "0e1f551eafeeba02c90640126d4b0d7487d097c91cbf1e91c174c5860dc6155a"
SRC_URI[dts.sha256sum] = "a3e66df00181ef8c208ddbd4adb80261782752826fe0d1b1c9a42cf60fa21e18"


KBUILD_DEFCONFIG ?= "linux.soc.config"

# LINUX_VERSION needed for PV in linux-mainline-common
# FIXME: 6.19 is crashing on boot (check)
#LINUX_VERSION = "6.19"
#SRCREV = "05f7e89ab9731565d8a62e3b5d1ec206485eeb0b"
#LINUX_VERSION = "6.12.8" # 
# SRCREV for v6.12
LINUX_VERSION = "6.12"
SRCREV = "adc218676eef25575469234709c2d87185ca223a"
SRC_URI:append:cvwsoc-nexysa7 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-nexysa7-dtb.patch', '', d)}"

COMPATIBLE_MACHINE = "(cvwsoc)"

# copy files where they are expected
do_kernel_metadata:prepend() {
    # We need to copy the defconfig to the source directory for the kernel build to find it
    install -m 644 ${UNPACKDIR}/linux.config ${S}/arch/riscv/configs/linux.soc.config
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
