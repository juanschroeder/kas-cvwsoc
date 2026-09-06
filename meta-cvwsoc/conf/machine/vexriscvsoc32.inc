#@TYPE: Machine
#@NAME: Core-V Wally SoC 32-bit base include
#@SOC: Core-V Wally SoC 32-bits
#@DESCRIPTION: Machine configuration for the 32-bit core

require conf/machine/vexriscvsoc.inc


## FIXME!!!
## FIXME!!!
MACHINEOVERRIDES:append = ":vexriscvsoc32"
MACHINEOVERRIDES:append = ":cvwsoc32"
TUNE_FEATURES = "rv 32 i m a f d c zicsr zifencei"
DEFAULTTUNE = "riscv32"
# Override extensions settings on base CVWSoC DTS
# d extension removed: CVA6 version used needs to be patched for double floats
#CVWSOC_DTB_CPU_SPECS = "i m a c zba zbb zbc zbs zcb zicntr zicond zicsr zifencei zihpm"
#CVWSOC_DTB_CPU_SPECS = "i m a f d c zicsr zifencei "

UBOOT_ENTRYPOINT:riscv32 = "0x80200000"

CVWSOC_CPU = "vexrv32"