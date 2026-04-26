FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
            https://raw.githubusercontent.com/juanschroeder/cvw/cvwsoc/linux/br2-external-tree/board/wally/busybox.soc.config;name=config \
           "
SRC_URI[config.sha256sum] = "22a0895d3a5a454e4b2a6bacf3ed399853e7807c6c6b515b558c65edcd408ab6"

# Merge our config on top of the base config
do_configure:append() {
    install -D -m644 ${UNPACKDIR}/busybox.soc.config ${B}/busybox.cfg
    merge_config.sh -m .config busybox.cfg
}

# loader fails on the 'tiny' build without static busybox
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'tiny', ' file://fragment-static.cfg ', '', d)}"
do_configure:append() {
    if [ -f "${UNPACKDIR}/fragment-static.cfg" ]; then
        install -D -m644 ${UNPACKDIR}/fragment-static.cfg ${B}
        merge_config.sh -m .config fragment-static.cfg
    fi;
}
