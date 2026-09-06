#@TYPE: Machine
#@NAME: Core-V Wally SoC 32-bit base include
#@SOC: Core-V Wally SoC 32-bits
#@DESCRIPTION: Machine configuration for the 32-bit core

require conf/machine/cva6soc.inc


## FIXME!!!
## FIXME!!!
MACHINEOVERRIDES:append = ":cva6soc32"
MACHINEOVERRIDES:append = ":cvwsoc32"
# NO FLOATING POINT:
# - d extension officially not supported by CV32A6
# - glibc officially doesn't support 'f' extension without 'd' extension
# - Linux doesn't support 'f' extension without 'd' extension
# =>  remove both and enable the compiler flags manually for each package that needs 'f' extension
#TUNE_FEATURES = "rv 32 i m a c zba zbb zbc zbs zcb zicond zicntr zicsr zifencei zihpm"
# PULP v3.0 version supports zicbom
TUNE_FEATURES = "rv 32 i m a c zba zbb zbc zbs zcb zicond zicntr zicsr zifencei zicbom zihpm"
DEFAULTTUNE = "riscv32"
# Override extensions settings on base CVWSoC DTS
# d extension removed: CVA6 version used needs to be patched for double floats
#CVWSOC_DTB_CPU_SPECS = "i m a c zba zbb zbc zbs zcb zicntr zicond zicsr zifencei zihpm"
#CVWSOC_DTB_CPU_SPECS = "i m a c zba zbb zbc zbs zcb zicntr zicond zicsr zifencei zicbom zihpm"

UBOOT_ENTRYPOINT:riscv32 = "0x80200000"

CVWSOC_CPU = "cva6rv32"
