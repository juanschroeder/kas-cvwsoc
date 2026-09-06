#@TYPE: Machine
#@NAME: Core-V Wally SoC 32-bit base include
#@SOC: Core-V Wally SoC 32-bits
#@DESCRIPTION: Machine configuration for the 32-bit core

require conf/machine/cva6soc.inc

## FIXME!!!
## FIXME!!!
MACHINEOVERRIDES:append = ":cva6soc64"
MACHINEOVERRIDES:append = ":cvwsoc64"
# zca and zcd do not need to be added to the compiler tune: they are implied by c and d
#TUNE_FEATURES = "rv 64 i m a f d c zicsr zifencei zba zbb zbc zbs zcb zicond"
# pulp-3.0.0 includes zicbom
TUNE_FEATURES = "rv 64 i m a f d c zicsr zifencei zicbom zba zbb zbc zbs zcb zicond"
# Override extensions settings on base CVWSoC DTS
#CVWSOC_DTB_CPU_SPECS = "i m a f d c zba zbb zbc zbs zca zcb zcd zicntr zicond zicsr zifencei zihpm"
# pulp-3.0.0 includes zicbom
#CVWSOC_DTB_CPU_SPECS = "i m a f d c zba zbb zbc zbs zca zcb zcd zicntr zicond zicsr zifencei zicbom zihpm"

CVWSOC_CPU = "cva6"