#@TYPE: Machine
#@NAME: Core-V Wally SoC base include
#@SOC: Core-V Wally SoC
#@DESCRIPTION: Machine configuration for the Core

require conf/machine/include/riscv-common.inc

#============================================
# Preferred Providers
PREFERRED_PROVIDER_virtual/kernel = "linux-mainline-cvwsoc"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-cvwsoc"
PREFERRED_PROVIDER_virtual/zdoom = "fbdoom"

#============================================
CVWSOC_BOARDNAME ?= "nexysa7"

CVWSOC_DTS_FILENAME ?= "wally-${CVWSOC_BOARDNAME}"

#============================================
# SBI Configuration
RISCV_SBI_FDT ?= "${CVWSOC_DTS_FILENAME}.dtb"
unset RISCV_SBI_PAYLOAD
#============================================

#============================================
# Kernel Configuration
KERNEL_DEVICETREE ?= "${CVWSOC_DTS_FILENAME}.dtb"
KERNEL_IMAGETYPE = "Image.lz4"
#============================================

#============================================
# Image Configuration
EXTRA_IMAGEDEPENDS += "u-boot-cvwsoc"
IMAGE_BOOT_FILES:append = " \
    fw_jump.bin      \
    ${RISCV_SBI_FDT}    \
"

IMAGE_FSTYPES += "tar.gz"
#============================================

#============================================
# Uboot Configuration
UBOOT_MACHINE = "openhwgroup_cvwsoc_${CVWSOC_BOARDNAME}_defconfig"
UBOOT_ENTRYPOINT = "0x80200000"
# FIXME
UBOOT_DTB_LOADADDRESS = "0x87000000"
UBOOT_DTB = "1"
# Check if this is really needed (CONFIG_DEFAULT_DEVICE_TREE is set)
UBOOT_DTB_BINARY = "cvwsoc-${CVWSOC_BOARDNAME}.dtb"
#============================================

#============================================
# Machine Features
# USB Ethernet RTL8152
MACHINE_EXTRA_RRECOMMENDS += " linux-firmware-rtl-nic"
#============================================


# double check this
TUNE_CCARGS = "-march=rv64imafdc_zicsr_zifencei_zicbom_zicboz_zba_zbb_zbc_zbs -mabi=lp64d"
TUNE_FEATURES = "rv 64 i m a f d c zicsr zifencei zicbom zicboz zba zbb zbc zbs"

MACHINEOVERRIDES:append = ":cvwsoc"
MACHINE_FEATURES:remove = "rtc"