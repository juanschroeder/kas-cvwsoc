SUMMARY = "Mainline Linux Kernel for Core-V Wally SoCs"

require recipes-kernel/linux/linux-mainline-common.inc

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPES','Image.lzo',' lzop-native','',d)}"

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPE_DISK','Image.lz4',' lz4-native','',d)}"

FILESEXTRAPATHS =. "${THISDIR}/linux:"

# FIXME: put in machine conf?
CVWSOC_DTS = "${CVWSOC_DTS_FILENAME}.dts"
DEPENDS += "u-boot-tools-native"
BRANCH = "linux-6.12.y"
KBUILD_DEFCONFIG ?= "linux.soc.config"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;protocol=https;branch=${BRANCH} \
           https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/br2-external-tree/board/wally/${KBUILD_DEFCONFIG};name=config;downloadfilename=${KBUILD_DEFCONFIG}.${SRCREV_BUILDROOT} \
           https://raw.githubusercontent.com/juanschroeder/cvw/${SRCREV_BUILDROOT}/linux/devicetree/${CVWSOC_DTS};name=dts;downloadfilename=${CVWSOC_DTS}.${SRCREV_BUILDROOT} \
          "
SRCREV_BUILDROOT = "d22961130270e904c5695492eab5e675314c5214"
SRCREV = "1d3a00d3bacff25652c96e1527610c69e91f7c38"
PV = "6.12.93+git"
LINUX_VERSION = "6.12"
SRCREV = "1d3a00d3bacff25652c96e1527610c69e91f7c38"

# LINUX_VERSION needed for PV in linux-mainline-common
# FIXME: 6.19 is crashing on boot (check)
# LINUX_VERSION = "6.19"
# SRCREV = "05f7e89ab9731565d8a62e3b5d1ec206485eeb0b"

SRC_URI[sha256sum] = "9108b4be5320017c147ef5b638f97f285c4fa3a6c0c6d14d1c00f25d12070471"
SRC_URI[config.sha256sum] = "8eade6062d71cd60664f467fba6392501d3f5bcfa754c1f7d6796cec12ac7a9e"

SRC_URI[dts.sha256sum] = "${DTS_SHA256}"
DTS_SHA256:cvwsoc-nexysa7 = "8d004000e2cdda8d48b68d4d672e69d3dcf4df1457d734750ee2605d92a24f2a"
DTS_SHA256:cvwsoc-genesys2 = "9b1c74801afc8bc018c0eb592127518b8df90f0363827d117e8b840178b7b4be"
DTS_SHA256:cvwsoc-genesys2xc7 = "e0afd354829d06bffb920d0d5f586749ed1235ecf38dfd82ba8cc7f577340ca0"
DTS_SHA256:cvwsoc-genesys2rv32 = "b1595c7f1d453c889105d8b9080da67f9adaa890c767693252b917c8f2493479"
DTS_SHA256:cvwsoc-virt = "fc9c72ad13ab865bc23f99ab6a9aafa890cae4a5acc558edde7c8416a2f8c750"
DTS_SHA256:cvwsoc-virt32 = "b650c2278b42daf7768dfde6f0975af563fe00a2f85d8de5fa350ad27990c6f7"

# tiny Kernel
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
                'file://fragment-tiny1.cfg \
                file://fragment-tiny2.cfg  \
                file://fragment-tiny3.cfg ', '', d)}"



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
                   file://0006-usb-ohci-platform-use-local-memory.patch \
                "


SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'fbcon', \
                'file://fragment-usb-snd.cfg \
                 file://fragment-kbd.cfg \
                 file://fragment-fb-console.cfg', '', d)}"

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
