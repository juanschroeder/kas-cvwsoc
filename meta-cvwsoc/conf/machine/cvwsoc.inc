#@TYPE: Machine
#@NAME: Core-V Wally SoC base include
#@SOC: Core-V Wally SoC
#@DESCRIPTION: Machine configuration for the Core

require conf/machine/include/riscv-common.inc

#####################################
# Missing settings for these extensions in upstream openembedded arch-riscv.inc

# Cache-block extensions
TUNEVALID[zicboz] = "Cache-block zero extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicboz", "_zicboz", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicboz", "_zicboz", "", d)}"

TUNEVALID[zicbop] = "Cache-block prefetch extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicbop", "_zicbop", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicbop", "_zicbop", "", d)}"

# Compressed
TUNEVALID[zcb] = "Compressed basic extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcb", "_zcb", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcb", "_zcb", "", d)}"

# Integer conditional operations
TUNEVALID[zicond] = "Integer conditional operations extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicond", "_zicond", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicond", "_zicond", "", d)}"

# Counters
TUNEVALID[zicntr] = "Base counters and timers extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicntr", "_zicntr", "", d)}"

TUNEVALID[zihpm] = "Hardware performance counters extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihpm", "_zihpm", "", d)}"

#####################################


#============================================
# Preferred Providers
PREFERRED_PROVIDER_virtual/kernel = "linux-mainline-cvwsoc"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-cvwsoc"
PREFERRED_PROVIDER_virtual/zdoom = "fbdoom"

#============================================
CVWSOC_BOARDNAME ?= "nexysa7"

CVWSOC_DTS_FILENAME ?= "cvwsoc-${CVWSOC_CPU}-${CVWSOC_BOARDNAME}"

#============================================
# SBI Configuration
RISCV_SBI_FDT ?= "${CVWSOC_DTS_FILENAME}.dtb"
unset RISCV_SBI_PAYLOAD
#============================================

#============================================
# Kernel Configuration
KERNEL_DEVICETREE ?= "${CVWSOC_DTS_FILENAME}.dtb"
KERNEL_IMAGETYPE ?= "Image.lz4"
#============================================

#============================================
# Image Configuration
EXTRA_IMAGEDEPENDS += "u-boot-cvwsoc"
IMAGE_BOOT_FILES:append = " \
    fw_jump.bin      \
    ${RISCV_SBI_FDT}    \
"

IMAGE_FSTYPES += "tar.gz cpio.gz"
# in case rootfs is in RAM
RAMFS_LOAD_ADDR = "0x84200000"
#============================================

#============================================
# Uboot Configuration
UBOOT_MACHINE = "openhwgroup_cvwsoc_${CVWSOC_BOARDNAME}_defconfig"
UBOOT_ENTRYPOINT = "0x80200000"
# FIXME
UBOOT_DTB_LOADADDRESS = "0x87000000"
UBOOT_DTB = "1"
UBOOT_DTS = "uboot-${CVWSOC_DTS_FILENAME}"
# Check if this is really needed (CONFIG_DEFAULT_DEVICE_TREE is set)
UBOOT_DTB_BINARY = "${UBOOT_DTS}.dtb"
#============================================

#============================================
# Machine Features
# USB Ethernet RTL8152
MACHINE_EXTRA_RRECOMMENDS += " linux-firmware-rtl-nic"
#============================================



MACHINEOVERRIDES:append = ":cvwsoc"
MACHINE_FEATURES:remove = "rtc"
