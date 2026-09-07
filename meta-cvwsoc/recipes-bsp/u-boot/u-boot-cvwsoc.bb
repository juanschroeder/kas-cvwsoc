require recipes-bsp/u-boot/u-boot.inc
require recipes-bsp/u-boot/u-boot-common.inc

DEPENDS += " u-boot-tools-native "
# Stage the canonical CVWSoC DTS sources for the later U-Boot DTS integration.
DEPENDS:append:cvwsoc = " cvwsoc-dts"

SRCREV:cvwsoc = "1be75b873a9e3527d240dacd9138cd63710e5df7"

FILESEXTRAPATHS:prepend := "${THISDIR}:${PN}:"
SRC_URI:cvwsoc = "git://github.com/juanschroeder/u-boot.git;protocol=https;nobranch=1"
SRC_URI[sha256sum] = "0f351a760196250a8bc95e839d5ba97006886dc2fa19a7cbde20967307d497e1"
# Why is this always added in the base SRC_URI ???
SRC_URI:remove:cvwsoc = " ${SRC_URI_RISCV}"
SRC_URI:remove:cvwsoc-renode-u540 = " ${SRC_URI_RISCV}"
SRC_URI:append = "  file://fragment-sdhci.cfg \
                    file://fragment-fs.cfg \
                    file://0001-npcm-sdhci-fmax-missing-fix.patch \
                    file://0004-ohci-bugfix-missing-cache-handling.patch \
                 "

LIC_FILES_CHKSUM:cvwsoc = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

# Temporary patches for the WIP xc7 build
SRC_URI:append:cvwsoc-genesys2xc7 = " file://0002-mmc-spi-timeout.patch"
SRC_URI:append:cvwsoc-genesys2xc7 = " file://0003-xc7-change-default-dtb.patch"

# Override generic u-boot board config
SRC_URI:append:cvwsoc = " file://fragment-set-dtb-placeholder.cfg "

#addtask add_dtb after do_patch before do_configure
addtask copy_dtb after do_unpack do_prepare_recipe_sysroot before do_patch
do_copy_dtb[depends] += "cvwsoc-dts:do_populate_sysroot"
# generate DTB build patch (does not apply for Renode target)
do_copy_dtb() {
    :
}
do_copy_dtb:cvwsoc() {
    install -m 644 ${RECIPE_SYSROOT}/${datadir}/cvwsoc-dts/*.dts* ${S}/arch/riscv/dts/
    cp ${S}/arch/riscv/dts/${CVWSOC_DTS_FILENAME}.dts ${S}/arch/riscv/dts/${UBOOT_DTS}.dts
}

# patch DTB placeholder
do_configure:append:cvwsoc() {
    sed -i "s|PLACEHOLDER|${UBOOT_DTS}|g" ${B}/.config
}

# No float for CV32A6
SRC_URI:append:cva6soc32 = " file://fragment-disable-float.cfg"

# VexRiscv has no Zbb 
SRC_URI:append:vexriscvsoc = " file://fragment-disable-zbb.cfg"


IMAGE_BOOT_FILES:remove = "boot.scr.uimg"
# disable env for now
UBOOT_ENV = ""
COMPATIBLE_MACHINE = "(cvwsoc|cva6soc|vexriscvsoc|cvwsoc-renode-u540)"
