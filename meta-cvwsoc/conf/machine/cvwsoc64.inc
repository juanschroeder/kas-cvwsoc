#@TYPE: Machine
#@NAME: Core-V Wally SoC 32-bit base include
#@SOC: Core-V Wally SoC 32-bits
#@DESCRIPTION: Machine configuration for the 32-bit core

require conf/machine/cvwsoc.inc


# TUNE_CCARGS is generated from TUNE_FEATURES in arch-riscv.inc
#TUNE_CCARGS = "-march=rv64imafdc_zicsr_zifencei_zicbom_zicboz_zba_zbb_zbc_zbs -mabi=lp64d"
TUNE_FEATURES = "rv 64 i m a f d c zicsr zifencei zicbom zicboz zba zbb zbc zbs"


#DEFAULTTUNE ?= "riscv32"

