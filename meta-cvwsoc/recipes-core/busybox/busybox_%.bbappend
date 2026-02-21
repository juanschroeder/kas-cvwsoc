FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
            https://raw.githubusercontent.com/juanschroeder/cvw/cvwsoc/linux/br2-external-tree/board/wally/busybox.config;name=config \
           "
SRC_URI[config.sha256sum] = "22a0895d3a5a454e4b2a6bacf3ed399853e7807c6c6b515b558c65edcd408ab6"

# Merge our config on top of the base config
do_configure:append() {
    install -D -m644 ${UNPACKDIR}/busybox.config ${B}/busybox.cfg
    merge_config.sh -m .config busybox.cfg
}

 