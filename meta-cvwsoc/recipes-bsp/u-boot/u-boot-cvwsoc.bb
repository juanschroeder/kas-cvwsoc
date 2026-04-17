require recipes-bsp/u-boot/u-boot.inc
require recipes-bsp/u-boot/u-boot-common.inc

DEPENDS += "u-boot-tools-native"

SRCREV:cvwsoc = "69b91a93f42f6e3d6507a42e27c6d17c60f85d6a"

SRC_URI:cvwsoc = "git://github.com/juanschroeder/u-boot.git;protocol=https;nobranch=1"
SRC_URI[sha256sum] = "0f351a760196250a8bc95e839d5ba97006886dc2fa19a7cbde20967307d497e1"
# Why is this always added in the base SRC_URI ???
SRC_URI:remove:cvwsoc = " ${SRC_URI_RISCV}"

FILESEXTRAPATHS:prepend := "${THISDIR}:${PN}:"

LIC_FILES_CHKSUM:cvwsoc = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

IMAGE_BOOT_FILES:remove = "boot.scr.uimg"


# disable env for now
UBOOT_ENV = ""
COMPATIBLE_MACHINE = "(cvwsoc)"
