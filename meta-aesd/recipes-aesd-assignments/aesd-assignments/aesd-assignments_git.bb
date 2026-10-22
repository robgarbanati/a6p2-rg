# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://git@github.com/robgarbanati/a3p2-rg;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "974d04f0a040bfa0ff45007212cf97130396fa82"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/aesdsocket"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
# I don't think I need this since -pthread is in my a3p2-rg Makefile
# TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
    # TODO: Install your binaries/scripts here.
    # Be sure to install the target directory with install -d first
    # Yocto variables ${D} and ${S} are useful here, which you can read about at 
    # https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
    # and
    # https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
    # See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb

    # make
    # ${WORKDIR}/image/usr/bin aka
    # ${TMPDIR}/work/${MULTIMACH_TARGET_SYS}/${PN}/${PV}-${PR}/image/usr/bin aka
    # build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/image/usr/bin/
    install -d ${D}${bindir}
    # copy build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/git/server/aesdsocket to build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/image/usr/bin/aesdsocket
    install -m 0755 ${S}/aesdsocket ${D}${bindir}/aesdsocket
    # make build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/etc/init.d/
    install -d ${D}${sysconfdir}/init.d
    # copy build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/git/server/aesdsocket-start-stop.sh to build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/image/etc/init.d/aesdsocket (renamed in copy)
    install -m 0755 ${S}/aesdsocket-start-stop.sh ${D}${sysconfdir}/init.d/aesdsocket

    # make build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/etc/rc5.d/
    install -d ${D}${sysconfdir}/rc5.d
    # make a symlink at build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/etc/rc5.d/s99aesdsocket with the contents '../init.d/aesdsocket'.
    # s99 prefix means init script starts this last
    ln -sf ../init.d/aesdsocket ${D}{sysconfdir}/rc5.d/s99aesdsocket
    # make build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/etc/rc0.d/
    install -d ${D}${sysconfdir}/rc0.d
    # make a symlink at build/tmp/work/aarch64-poky-linux/aesd-assignments/1.0+git<rev>/etc/rc5.d/K01aesdsocket with the contents '../init.d/aesdsocket'.
    # K01 prefix means exit script calls this with stop last
    ln -sf ../init.d/aesdsocket ${D}{sysconfdir}/rc0.d/K01aesdsocket

}
