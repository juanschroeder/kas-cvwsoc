#@TYPE: Machine
#@NAME: Core-V Wally SoC 32-bit base include
#@SOC: Core-V Wally SoC 32-bits
#@DESCRIPTION: Machine configuration for the 32-bit core

require conf/machine/cvwsoc.inc

MACHINEOVERRIDES:append = ":cvwsoc32"

# override 64-bit base settings

# TUNE_CCARGS is generated from TUNE_FEATURES in arch-riscv.inc
#TUNE_FEATURES = "rv 32 i m a f d c zicsr zifencei zicbom zicboz zba zbb zbc zbs zcb zicond"
TUNE_FEATURES = "rv 32 i m a f d c zicsr zifencei zicbom zicboz zicbop zba zbb zbc zbs zcb zicond"

DEFAULTTUNE = "riscv32"

CVWSOC_CPU = "wallyrv32"
UBOOT_ENTRYPOINT:riscv32 = "0x80200000"

