require recipes-bsp/u-boot/u-boot.inc
require recipes-bsp/u-boot/u-boot-common.inc

DEPENDS += "u-boot-tools-native"

SRCREV:cvwsoc = "81b8f6412d081345d0a64a744f886a98361734dc"

FILESEXTRAPATHS:prepend := "${THISDIR}:${PN}:"
SRC_URI:cvwsoc = "git://github.com/juanschroeder/u-boot.git;protocol=https;nobranch=1"
SRC_URI[sha256sum] = "0f351a760196250a8bc95e839d5ba97006886dc2fa19a7cbde20967307d497e1"
# Why is this always added in the base SRC_URI ???
SRC_URI:remove:cvwsoc = " ${SRC_URI_RISCV}"
SRC_URI:append = "  file://fragment-sdhci.cfg \
                    file://0001-npcm-sdhci-fmax-missing-fix.patch "


# Temporary patches for the WIP xc7 build
SRC_URI:append:cvwsoc-genesys2xc7 = " file://0002-mmc-spi-timeout.patch"
SRC_URI:append:cvwsoc-genesys2xc7 = " file://0003-xc7-change-default-dtb.patch"

LIC_FILES_CHKSUM:cvwsoc = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

IMAGE_BOOT_FILES:remove = "boot.scr.uimg"


# disable env for now
UBOOT_ENV = ""
COMPATIBLE_MACHINE = "(cvwsoc)"
