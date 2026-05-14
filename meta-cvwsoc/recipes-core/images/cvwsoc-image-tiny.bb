require cvwsoc-image.inc

LICENSE = "CLOSED"

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

# minimal ROOTFS
IMAGE_FEATURES = ""
IMAGE_INSTALL = ""
PACKAGE_INSTALL = "busybox base-files base-passwd"
NO_RECOMMENDATIONS = "1"
IMAGE_LINGUAS = " "

WKS_FILE = "cvwsoc-tiny.wks.in"

DEPENDS:append = "dtc-native"
python do_generate_initrd_info_dtb() {
    import os
    import shutil
    import subprocess

    dtb = d.getVar("CVWSOC_DTS_FILENAME") + ".dtb"
    deploy = d.getVar("DEPLOY_DIR_IMAGE")
    base_dtb = os.path.join(deploy, dtb)
    base_orig_dtb = os.path.join(deploy, dtb + ".orig")
    out_dtb  = base_dtb
    imagename = d.getVar("IMAGE_LINK_NAME")
    initrd   = os.path.join(deploy, imagename + ".cpio")

    print("initrd is: ", initrd)

    initrd_start = int(d.getVar("RAMFS_LOAD_ADDR"), 16)
    initrd_size  = os.path.getsize(initrd)
    initrd_end   = initrd_start + initrd_size

    shutil.copy2(base_dtb, base_orig_dtb)

    subprocess.check_call([
        "fdtput", "-tx", out_dtb, "/chosen",
        "linux,initrd-start", "0x0", hex(initrd_start)
    ])
    subprocess.check_call([
        "fdtput", "-tx", out_dtb, "/chosen",
        "linux,initrd-end", "0x0", hex(initrd_end)
    ])
}

do_generate_initrd_info_dtb[depends] += "dtc-native:do_populate_sysroot virtual/kernel:do_deploy"
addtask generate_initrd_info_dtb after do_image_complete before do_build
