# Yocto build for 'cvwsoc'

This is the Yocto build for the 'cvwsoc' project: https://github.com/juanschroeder/cvw/tree/cvwsoc [1]

It's Yocto (Kas) based.

## Targets

Hardware: 
- Genesys 2
- Nexys A7
- Virtual (Verilator) platform


## Configs / Builds

- 'cvwsoc-genesys2-bringup': Minimal image for Genesys 2.
- 'cvwsoc-genesys2-doom':    Image for Genesys 2 that includes stuff for running FB Doom and other extra stuff.
- 'cvwsoc-nexysa7-bringup':  Minimal image for Nexys A7.
- 'cvwsoc-nexysa7-doom':     Image for Nexys A7 that includes stuff for running FB Doom and other extra stuff.
- 'cvwsoc-virt-tiny':        Image/binaries for sim/verilator/Makefile.cvwsoc targets for verilation in [1]. Fastest/minimal boot.
- 'cvwsoc-virt-full':        Idem, but with more stuff.

E.g. 
```
kas build configs/cvwsoc-virt-tiny.yml
```

