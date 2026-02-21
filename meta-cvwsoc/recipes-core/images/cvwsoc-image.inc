inherit core-image

LICENSE = "MIT"

IMAGE_LINGUAS = " "

# root/blank user
EXTRA_IMAGE_FEATURES += "allow-empty-password allow-root-login empty-root-password"
# cvw user
INHERIT += "extrausers"
EXTRA_USERS_PARAMS = "useradd cvw; \
                        usermod -P cvw cvw; \
                        groupadd sudo; \    
                        usermod -a -G sudo,video,input cvw;\
                        "

# reference wic image. Must be overridden for other partition setup (e.g. initramfs)
WKS_FILES:remove = "core-image-minimal.wks"
WKS_FILE ?= "cvwsoc.wks.in"
