SECTION = "games"
DESCRIPTION = "FB Doom"
HOMEPAGE = "https://github.com/maximevince/fbDOOM"
PRIORITY = "optional"
#LICENSE = "GPL"
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://COPYING.txt;md5=f4bc057015de5afef5e56f1cd5dfbae1"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI = "git://github.com/maximevince/fbdoom.git;protocol=https;branch=master \
            file://0001-patch-backbuffer-path.patch \
            "
SRCREV = "6c599f50e9e8e9436a5c064f42836eb48ff6bde0"
SRC_URI[sha256sum] = "bdfe857256245da04fd38a19f4d3bff1b6d9971def7c06e246340a69057586ba"

SECURITY_CFLAGS = ""
SECURITY_LDFLAGS = ""
TARGET_CFLAGS = ""
TARGET_CPPFLAGS = ""

EXTRA_OEMAKE:cvwsoc64 = '\
    CC="${CC}" \
    CFLAGS="-O3 -flto -march=rv64gc -mabi=lp64d \
          -fno-exceptions -fno-asynchronous-unwind-tables \
          -fno-unwind-tables -fomit-frame-pointer -std=c99 -D_GNU_SOURCE" \
    LDFLAGS="${LDFLAGS}" \
    '

EXTRA_OEMAKE:cvwsoc32 = '\
    CC="${CC}" \
    CFLAGS="-O3 -flto -march=rv32gc -mabi=ilp32d \
          -fno-exceptions -fno-asynchronous-unwind-tables \
          -fno-unwind-tables -fomit-frame-pointer -std=c99 -D_GNU_SOURCE" \
    LDFLAGS="${LDFLAGS}" \
    '

do_compile() {
    cd ${S}/fbdoom
    oe_runmake clean
    oe_runmake NOSDL=1 V=1    
}

do_install() {
	install -d ${D}/${bindir}
    install -m 0544 ${S}/fbdoom/fbdoom ${D}/${bindir}
}

# Needed for meta-doom dependencies
PROVIDES = "virtual/zdoom"
RPROVIDES:${PN}:class-target = "zdoom"



