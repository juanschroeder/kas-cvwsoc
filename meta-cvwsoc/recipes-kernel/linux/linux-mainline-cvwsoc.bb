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
SRCREV_BUILDROOT = "c8f8954b462d890f41bb57903fd6aa1c08eb1b59"
SRCREV = "1d3a00d3bacff25652c96e1527610c69e91f7c38"
PV = "6.12.93+git"
LINUX_VERSION = "6.12"

# LINUX_VERSION needed for PV in linux-mainline-common
# FIXME: 6.19 is crashing on boot (check)
# LINUX_VERSION = "6.19"
# SRCREV = "05f7e89ab9731565d8a62e3b5d1ec206485eeb0b"

SRC_URI[sha256sum] = "9108b4be5320017c147ef5b638f97f285c4fa3a6c0c6d14d1c00f25d12070471"
SRC_URI[config.sha256sum] = "8eade6062d71cd60664f467fba6392501d3f5bcfa754c1f7d6796cec12ac7a9e"

# DTS SHA256
SRC_URI[dts.sha256sum] = "${DTS_SHA256}"
DTS_SHA256:cvwsoc-nexysa7 = "593f57e0d92909c8e54159d90595f910b18c49d5c1319683517bb4749687410b"
DTS_SHA256:cvwsoc-nexysa7rv32 = "435ce440aec9a43205d8e09a83a570575c52cac6900c767e1a763d8029a7da4c"
DTS_SHA256:cvwsoc-genesys2 = "c14842243dc96b391f9f804baa6a00f13a7e6f23c8e63000c5ebaf8861b45cab"
DTS_SHA256:cvwsoc-genesys2xc7 = "e0afd354829d06bffb920d0d5f586749ed1235ecf38dfd82ba8cc7f577340ca0"
DTS_SHA256:cvwsoc-genesys2rv32 = "d943cc21f9d19e2879c2784daa038802c7c98f6e074c91a78903b8b0910e4908"
DTS_SHA256:cvwsoc-virt = "f430363c1e6f060653c090b8f07c3d5948501b2f3eb424f8e35edccea98f0456"
DTS_SHA256:cvwsoc-virt32 = "b650c2278b42daf7768dfde6f0975af563fe00a2f85d8de5fa350ad27990c6f7"

# tiny Kernel
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
                'file://fragment-tiny1.cfg \
                file://fragment-tiny2.cfg  \
                file://fragment-tiny3.cfg ', '', d)}"


# RV32 Kernel
SRC_URI:append:cvwsoc32 = " file://fragment-rv32.cfg "

SRC_URI:append:cvwsoc-nexysa7 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-nexysa7-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-nexysa7rv32 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-nexysa7-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-genesys2 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-genesys2-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-genesys2xc7 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-genesys2xc7-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-genesys2rv32 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-genesys2rv32-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-virt = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvw-wally-dtb.patch', '', d)}"
SRC_URI:append:cvwsoc-virt32 = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-virt32-dtb.patch', '', d)}"

# FIXME: this config probably needs to be stripped down
SRC_URI:append:cvwsocvirt = " file://fragment-mtd-ram-jffs2.cfg \
                                 file://fragment-reenable-fs.cfg"

# FIXME: SDHCI driver still needs improvements
SRC_URI:append = " file://0003-sdhci-generic-driver-802935a6a27e48050339a19704700adc0b0ed282.patch \
                   file://0004-sdhci-generic-driver-fixes-v6.12-all.patch \
                   file://fragment-sdhci.cfg "

SRC_URI:append = "  file://0005-mtd-ram-erasesize-property-fix.patch "

# iDMA and audio patches (NEEDS REWORK AND CLEANUP)
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'fbcon', \
                    'file://fragment-usb-snd.cfg \
                    file://fragment-kbd.cfg \
                    file://fragment-fb-console.cfg \
                    file://0006-usb-ohci-platform-use-local-memory.patch \
                    file://0007-idma-base-cheshire-6.12-pr-driver.patch \
                    file://0008-idma-first-rv32-bugfixes.patch \
                    file://0009-idma-bugfix-mising-spinlock-init.patch \
                    file://0010-idma-more-bugfixes-rv32.patch \
                    file://0011-idma-more-bugfixes.patch \
                    file://0012-idma-cvwsoc-customisations.patch \
                    file://0013-idma-engine-fix-wrongly-reworked-changes.patch \
                    file://0014-idma-another-bugfix-and-cleanup.patch \
                    file://0015-idma-cyclic-dma-support-and-fixes.patch \
                    file://0016-asoc-i2s-driver.patch \
                    file://0017-bugfix-physmap-rounding-power2-down.patch \
                    file://fragment-idma-engine-proxy.cfg \
                    file://fragment-cvwsoc-i2s.cfg \
                    file://fragment-pcm5102a.cfg \
                    file://fragment-preempt.cfg \
                ', '', d)}"

# dev
SRC_URI:append = " file://fragment-dev-remove-dirty.cfg "

# For QEMU virtio (framebuffer, audio)
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'fbcon', \
                    'file://fragment-drm-virtio-fbdev.cfg \
                    file://fragment-virtio-pci.cfg \
                    file://fragment-virtio-input.cfg \
                    file://fragment-virtio-snd.cfg \
                ', '', d)}"


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
