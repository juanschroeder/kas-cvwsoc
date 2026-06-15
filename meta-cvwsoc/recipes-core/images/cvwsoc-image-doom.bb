require cvwsoc-image.inc

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

IMAGE_INSTALL += " \
    devmem2 \
    memtester \
    dropbear \
    iproute2 \
    ethtool \
    kmod \
    ifupdown \
    net-tools \
    iputils \
    usbutils \
    htop \
    iperf3 \
    "

# strace does not support RV32
IMAGE_INSTALL:append:cvwsoc64 = " strace "

# Doom and graphics related
IMAGE_INSTALL += " \
    fbset \
    fbdoom \
    freedm \
    "

# audio / usb audio support
IMAGE_INSTALL:append = " alsa-lib \
                        alsa-utils-aplay \
                        alsa-utils-amixer \
                        alsa-utils-speakertest \
                        alsa-utils-alsactl "


# FIXME: Remove this hack and do it properly
ROOTFS_POSTPROCESS_COMMAND += "create_asound_conf; "
create_asound_conf() {
    mkdir -p ${IMAGE_ROOTFS}/etc
    echo -e '# defaut card config\npcm.!default {\n    type plug\n    slave.pcm "hw:0,0"\n}\n\nctl.!default {\n    type hwbin\n    card 0\n}' > ${IMAGE_ROOTFS}/etc/asound.conf
}

