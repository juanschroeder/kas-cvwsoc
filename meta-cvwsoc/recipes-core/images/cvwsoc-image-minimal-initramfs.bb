SUMMARY = "Minimal initramfs for CVW SoC"
LICENSE = "MIT"

inherit core-image
inherit extrausers


IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_LINGUAS = " "
IMAGE_FEATURES = ""
PACKAGE_EXCLUDE = "kernel-image-*"

# Override the machine's wic/ext4 formats.
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
WKS_FILE = ""
EXTRA_IMAGEDEPENDS = ""

IMAGE_ROOTFS_SIZE = "65536"
IMAGE_ROOTFS_EXTRA_SPACE = "4096"

# prevent the .rootfs suffix
IMAGE_NAME_SUFFIX = ""

IMAGE_FEATURES += "empty-root-password serial-autologin-root"
EXTRA_USERS_PARAMS = "\
    usermod -p '' root; \
    useradd -p '' -s /bin/sh cvw; \
    groupadd -f sudo; \
    usermod -a -G sudo,video,input cvw; \
"

IMAGE_INSTALL:append = " kernel-modules"
IMAGE_INSTALL += " \
    devmem2 \
    memtester \
    kmod \
    usbutils \
    htop \
    "

# audio / usb audio support
IMAGE_INSTALL:append = " alsa-lib \
                        alsa-utils-aplay \
                        alsa-utils-amixer \
                        alsa-utils-speakertest \
                        alsa-utils-alsactl "


disable_udev_startup() {
    rm -f ${IMAGE_ROOTFS}/etc/rcS.d/S04udev
}

create_initial_console_nodes() {
    mknod -m 600 ${IMAGE_ROOTFS}/dev/console c 5 1
    mknod -m 666 ${IMAGE_ROOTFS}/dev/null c 1 3
}

ROOTFS_POSTPROCESS_COMMAND += "disable_udev_startup; create_initial_console_nodes; "
