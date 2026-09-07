SUMMARY = "Mainline Linux Kernel for Core-V Wally SoCs"

require recipes-kernel/linux/linux-mainline-common.inc

DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPES','Image.lzo',' lzop-native','',d)}"
DEPENDS:append = "${@bb.utils.contains('KERNEL_IMAGETYPE_DISK','Image.lz4',' lz4-native','',d)}"

DEPENDS:append = " dtc-native"

FILESEXTRAPATHS =. "${THISDIR}/linux:"

# FIXME: put in machine conf?
CVWSOC_DTS = "${CVWSOC_DTS_FILENAME}.dts"
DEPENDS:append:cvwsoc = " u-boot-tools-native"
# Get DTS files to use
DEPENDS:append:cvwsoc = " cvwsoc-dts"
BRANCH = "linux-6.12.y"
KBUILD_DEFCONFIG ?= "linux.soc.config"


SRC_URI = " git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;protocol=https;branch=${BRANCH} \
            file://linux.soc.config \
            "
SRCREV = "1d3a00d3bacff25652c96e1527610c69e91f7c38"
PV = "6.12.93+git"
LINUX_VERSION = "6.12"

# LINUX_VERSION needed for PV in linux-mainline-common
# FIXME: 6.19 is crashing on boot (check)
# LINUX_VERSION = "6.19"
# SRCREV = "05f7e89ab9731565d8a62e3b5d1ec206485eeb0b"

SRC_URI[sha256sum] = "9108b4be5320017c147ef5b638f97f285c4fa3a6c0c6d14d1c00f25d12070471"
SRC_URI[config.sha256sum] = "8eade6062d71cd60664f467fba6392501d3f5bcfa754c1f7d6796cec12ac7a9e"


# tiny Kernel
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', \
                'file://fragment-tiny1.cfg \
                file://fragment-tiny2.cfg  \
                file://fragment-tiny3.cfg ', '', d)}"


# RV32 Kernel
SRC_URI:append:cvwsoc32 = " file://fragment-rv32.cfg "

addtask add_dtb after do_patch before do_configure
do_add_dtb[depends] += "cvwsoc-dts:do_populate_sysroot"
# generate DTB build patch (does not apply for Renode target)
do_add_dtb() {
    :
}
do_add_dtb:cvwsoc() {
    install -m 644 ${RECIPE_SYSROOT}/${datadir}/cvwsoc-dts/*.dts* ${S}/arch/riscv/boot/dts/
    sed -i "s|PLACEHOLDER|${CVWSOC_DTS_FILENAME}|" ${S}/arch/riscv/boot/dts/Makefile
}

SRC_URI:append:cvwsoc = " ${@bb.utils.contains('LINUX_VERSION', '6.12', 'file://0001-add-cvwsoc-dtb.patch', '', d)}"

# FIXME: this config probably needs to be stripped down
SRC_URI:append:cvwsocvirt = " file://fragment-mtd-ram-jffs2.cfg \
                                 file://fragment-reenable-fs.cfg"

SRC_URI:append:cvwsocvirt = " file://fragment-disable-dbg-stuff.cfg "
SRC_URI:append:cvwsoc-renode-u540 = " file://fragment-disable-dbg-stuff.cfg "

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

SRC_URI:append:cvwsoc-renode-u540 = " file://fragment-sifive.cfg "

COMPATIBLE_MACHINE = "(cvwsoc)"

# copy files where they are expected
do_kernel_metadata:prepend() {
    # We need to copy the defconfig to the source directory for the kernel build to find it
    #install -m 644 ${UNPACKDIR}/${KBUILD_DEFCONFIG}.${SRCREV_BUILDROOT} ${S}/arch/riscv/configs/linux.soc.config
    install -m 644 ${UNPACKDIR}/${KBUILD_DEFCONFIG} ${S}/arch/riscv/configs/linux.soc.config
    printf '\n' >> ${S}/arch/riscv/configs/linux.soc.config
}

# # No DTS build for Renode target
# do_kernel_metadata:prepend:cvwsoc() {
#     install -m 644 ${UNPACKDIR}/${CVWSOC_DTS}.${SRCREV_BUILDROOT} ${S}/arch/riscv/boot/dts/${CVWSOC_DTS}
# }

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

    # Deploy vmlinux
    cp ${B}/vmlinux ${DEPLOYDIR}
}



# This is broken in current Yocto: https://lists.openembedded.org/g/openembedded-core/message/226911
KERNEL_FEATURES:remove = "${KERNEL_FEATURES_RISCV}"
