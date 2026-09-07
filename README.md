# Yocto build for 'cvwsoc'

This is the Yocto build for the 'cvwsoc' project [1], which is a fork of OpenHw Core-V Wally [2].

It's Yocto (Kas) based.

## Targets

Platforms: 
- Genesys 2
- Nexys A7
- Virtual (Verilator) platform
- Renode (FU540 Co-simulation with CVWSoC)

Soft CPUs:
- Core-V Wally 32/64 bits
- SiFive FU540 (Renode)

Remark: Currently 32 bits is only supported in Verilator and Genesys 2 (FPGA) platforms.


## Configs / Builds

The builds below are images for specific targets.

- 'cvwsoc-genesys2-bringup':      Minimal image for Genesys 2.
- 'cvwsoc-genesys2-doom':         Image for Genesys 2 that includes stuff for running FB Doom and other extra stuff.
- 'cvwsoc-genesys2rv32-bringup':  Minimal image for Genesys 2 with RV32 CPU.
- 'cvwsoc-genesys2xc7-bringup':   Minimal image for Genesys 2 and OpenXC7 FPGA bitstream.
- 'cvwsoc-nexysa7-bringup':       Minimal image for Nexys A7.
- 'cvwsoc-nexysa7-doom':          Image for Nexys A7 that includes stuff for running FB Doom and other extra stuff.
- 'cvwsoc-nexysa7rv32-doom'
- 'cvwsoc-virt-tiny':             Image/binaries for sim/verilator/Makefile.cvwsoc targets for verilation in [1]. Fastest/minimal boot.
- 'cvwsoc-virt-full':             Idem, but with more stuff.
- 'cvwsoc-renode-u540':           Renode binaries for FU540 co-simulation
- 'cva6soc-genesys2-doom.yml':    CVA6 (also CVA6S+) FB doom image
- 'cva6soc-virt-tiny.yml':        CVA6 simulation image
- 'cva6soc-virt32-tiny.yml':      CV32A6 simulation image
- 'cva6soc-genesys2rv32-bringup.yml': CV32A6 basic image
- 'vexriscvsoc-virt32-tiny.yml':  Vexriscv (RV32) simulation image
- 'vexriscvsoc-nexysa7rv32-doom.yml': Vexriscv image for Nexys A7 (RV32)
- 'vexriscvsoc-genesys2rv32-doom.yml': Vexriscv image for Genesys 2 (RV32)
- etc

E.g. 
```
kas build configs/cvwsoc-virt-tiny.yml
```

Output binaries in: build/tmp/deploy/images/[TARGET]/.

To flash an image (full contents) you can use bmaptool. E.g.:

```
$ sudo bmaptool copy cvwsoc-image-minimal-cvwsoc-genesys2rv32.rootfs.wic.gz /dev/sda
```

# References:

[1] https://github.com/juanschroeder/cvw/tree/cvwsoc

[2] https://github.com/openhwgroup/cvw/

