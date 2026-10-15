inherit core-image
# Add aesd-assignments and openssh to this image's package list.
# do_rootfs then pulls each named package's
# already-built .ipk out of tmp/deploy/ipk/ and unpacks it into this image's own rootfs
# (IMAGE_ROOTFS, under core-image-aesd's own WORKDIR -- not aesd-assignments' WORKDIR).
# The .ipk itself was built earlier, by aesd-assignments' own do_package task.
CORE_IMAGE_EXTRA_INSTALL += "aesd-assignments openssh"
inherit extrausers
# See https://docs.yoctoproject.org/singleindex.html#extrausers-bbclass
# We set a default password of root to match our busybox instance setup
# Don't do this in a production image
# PASSWD below is set to the output of
# printf "%q" $(mkpasswd -m sha256crypt root) to hash the "root" password
# string
PASSWD = "\$5\$2WoxjAdaC2\$l4aj6Is.EWkD72Vt.byhM5qRtF9HcCM/5YpbxpmvNB5"
EXTRA_USERS_PARAMS = "usermod -p '${PASSWD}' root;"
