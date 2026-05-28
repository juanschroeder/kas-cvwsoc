# Yocto build for 'cvwsoc'

This is the Yocto build for the 'cvwsoc' project [1], which is a fork of OpenHw Core-V Wally [2].

It's Yocto (Kas) based.

## Targets

Platforms: 
- Genesys 2
- Nexys A7
- Virtual (Verilator) platform

Soft CPUs:
- Core-V Wally 32/64 bits

Remark: Currently 32 bits is only supported in Verilator and Genesys 2 (FPGA) platforms.


## Configs / Builds

The builds below are images for specific targets.

- 'cvwsoc-genesys2-bringup': Minimal image for Genesys 2.
- 'cvwsoc-genesys2-doom':    Image for Genesys 2 that includes stuff for running FB Doom and other extra stuff.
- 'cvwsoc-genesys2rv32-bringup':  Minimal image for Genesys 2 with RV32 CPU.
- 'cvwsoc-genesys2xc7-bringup':   Minimal image for Genesys 2 and OpenXC7 FPGA bitstream.
- 'cvwsoc-nexysa7-bringup':  Minimal image for Nexys A7.
- 'cvwsoc-nexysa7-doom':     Image for Nexys A7 that includes stuff for running FB Doom and other extra stuff.
- 'cvwsoc-virt-tiny':        Image/binaries for sim/verilator/Makefile.cvwsoc targets for verilation in [1]. Fastest/minimal boot.
- 'cvwsoc-virt-full':        Idem, but with more stuff.

E.g. 
```
kas build configs/cvwsoc-virt-tiny.yml
```

Output binaries in: build/tmp/deploy/images/[TARGET]/
To flash an image you can use bmaptool. E.g.:

```
$ sudo bmaptool copy cvwsoc-image-minimal-cvwsoc-genesys2rv32.rootfs.wic.gz /dev/sda
```

# References:

[1] https://github.com/juanschroeder/cvw/tree/cvwsoc

[2] https://github.com/openhwgroup/cvw/

