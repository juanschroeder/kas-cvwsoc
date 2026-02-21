require cvwsoc-image.inc

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

IMAGE_INSTALL += " \
    dropbear \
    iproute2 \
    ethtool \
    kmod \
    ifupdown \
    net-tools \
    iputils \
    usbutils \
    htop \
    strace \
    "

# Doom and graphics related
IMAGE_INSTALL += " \
    fbset \
    fbdoom \
    freedm \
    "
