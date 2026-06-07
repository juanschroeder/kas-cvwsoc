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

SRCREV_BUILDROOT = "e068842209ab3c180cc4636a2a0b9c331705137d"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=https;branch=${BRANCH} \
            https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/br2-external-tree/board/wally/${KBUILD_DEFCONFIG};name=config;downloadfilename=${KBUILD_DEFCONFIG}.${SRCREV_BUILDROOT}  \
            https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/devicetree/${CVWSOC_DTS};name=dts;downloadfilename=${CVWSOC_DTS}.${SRCREV_BUILDROOT} \
          "

SRC_URI[config.sha256sum] = "3343beaf711838e49f88cf5c30f2cab7d81f905cfd9e1ea9cbae5cac4415329f"

SRC_URI[dts.sha256sum] = "${DTS_SHA256}"
DTS_SHA256:cvwsoc-nexysa7 = "a3e66df00181ef8c208ddbd4adb80261782752826fe0d1b1c9a42cf60fa21e18"
DTS_SHA256:cvwsoc-genesys2 = "6c55a48369bbb0647cc34f1bb1d536a80ce66e54ae889529e3c34daf980a0ff1"
DTS_SHA256:cvwsoc-genesys2xc7 = "1eb0ea4344ff77ee0b3e5cf6d02be2e9097f973e593a71815f05f560839ab1e1"
DTS_SHA256:cvwsoc-genesys2rv32 = "81cc51afebc57a1d417d4557f7a38a965c54a54c346388427fda6dc9e0aa3d03"
DTS_SHA256:cvwsoc-virt = "67751a85504fe9fd05e4ff6085da4dd1994e28c26bcd2f1bead6fd66d0ec4dac"
DTS_SHA256:cvwsoc-virt32 = "bebb96303c63391e11ad7ae7405d349b5601befac1360b7ec903218680e2a462"

# tiny Kernel
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
                'file://fragment-tiny1.cfg \
                file://fragment-tiny2.cfg  \
                file://fragment-tiny3.cfg ', '', d)}"



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
SRC_URI:append:cvwsoc-genesys2xc7 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-genesys2xc7-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-genesys2rv32 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-genesys2rv32-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-genesys2rv32 = " file://fragment-rv32.cfg "
SRC_URI:append:cvwsoc-virt = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvw-wally-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-virt32 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-virt32-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-virt32 = " file://fragment-rv32.cfg "
# FIXME: this config probably needs to be stripped down
SRC_URI:append:cvwsoc-virt = "  file://fragment-mtd-ram-jffs2.cfg \
                                file://fragment-sdhci.cfg "
SRC_URI:append:cvwsoc-virt32 = " file://fragment-mtd-ram-jffs2.cfg \
                                 file://fragment-sdhci.cfg "

# EXT2 seems to be slower than cpio. Enabled for SDHCI emulation
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
                ' file://fragment-reenable-fs.cfg ', '', d)}"

# FIXME: this driver still needs improvements
SRC_URI:append = " file://0003-sdhci-generic-driver-802935a6a27e48050339a19704700adc0b0ed282.patch \
                   file://0004-sdhci-generic-driver-fixes-v6.12-all.patch \
                   file://0005-mtd-ram-erasesize-property-fix.patch \
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
