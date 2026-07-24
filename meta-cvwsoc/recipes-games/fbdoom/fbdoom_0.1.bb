SECTION = "games"
DESCRIPTION = "FB Doom"
HOMEPAGE = "https://github.com/maximevince/fbDOOM"
PRIORITY = "optional"
#LICENSE = "GPL"
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://COPYING.txt;md5=f4bc057015de5afef5e56f1cd5dfbae1"


inherit pkgconfig

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI = "git://github.com/maximevince/fbdoom.git;protocol=https;branch=master "
SRC_URI:append:cvwsoc = "file://0001-patch-backbuffer-path.patch "
# the 32-bit patch seems to make it a bit slower
#SRC_URI:append:cvwsoc32 = "file://0001-patch-backbuffer-path-32b.patch "
SRC_URI:append = "  file://0002-enable-sound.patch \
                    file://0004-2chan.patch \
                    file://0005-bugfix-kbd-restore.patch \
                 "

# This patch is not useful unless used with a 22 KHz capable sound card (and it introduces rate conversion)
#                    file://0003-samplerate_22KHz.patch

# Only for virtual targets (QEMU)
SRC_URI:append:cvwsocvirt =     " 0006-maxslicetime-fix-virtio-qemu.patch "

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

DEPENDS += "libsdl libsdl-mixer"
RDEPENDS:${PN} += "libsdl libsdl-mixer"

do_compile() {
    cd ${S}/fbdoom
    oe_runmake clean
    oe_runmake FEATURE_SOUND=1 FEATURE_MUSIC=0 V=1
}

do_install() {
	install -d ${D}/${bindir}
    install -m 0544 ${S}/fbdoom/fbdoom ${D}/${bindir}
}

# Needed for meta-doom dependencies
PROVIDES = "virtual/zdoom"
RPROVIDES:${PN}:class-target = "zdoom"



