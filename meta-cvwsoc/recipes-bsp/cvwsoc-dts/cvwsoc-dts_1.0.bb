SUMMARY = "Shared CVWSoC device tree sources"
LICENSE = "CLOSED"

S = "${UNPACKDIR}"

SRC_URI = " \
    file://cvwsoc.dtsi \
    file://cvwsoc-pmods.dtsi \
    file://cvwsoc-board-genesys2.dtsi \
    file://cvwsoc-board-nexysa7.dtsi \
    file://cvwsoc-board-virt.dtsi \
    file://cvwsoc-cpu-cva6.dtsi \
    file://cvwsoc-cpu-cva6rv32.dtsi \
    file://cvwsoc-cpu-vexrv32.dtsi \
    file://cvwsoc-cpu-wally.dtsi \
    file://cvwsoc-cpu-wallyrv32.dtsi \
    file://cvwsoc-cva6-genesys2.dts \
    file://cvwsoc-cva6-nexysa7.dts \
    file://cvwsoc-cva6-virt.dts \
    file://cvwsoc-cva6rv32-genesys2.dts \
    file://cvwsoc-cva6rv32-nexysa7.dts \
    file://cvwsoc-cva6rv32-virt.dts \
    file://cvwsoc-cva6sp-genesys2.dts \
    file://cvwsoc-cva6sprv32-genesys2.dts \
    file://cvwsoc-cva6sprv32-nexysa7.dts \
    file://cvwsoc-vexrv32-genesys2.dts \
    file://cvwsoc-vexrv32-nexysa7.dts \
    file://cvwsoc-vexrv32-virt.dts \
    file://cvwsoc-wally-genesys2.dts \
    file://cvwsoc-wally-nexysa7.dts \
    file://cvwsoc-wally-virt.dts \
    file://cvwsoc-wallyrv32-genesys2.dts \
    file://cvwsoc-wallyrv32-nexysa7.dts \
    file://cvwsoc-wallyrv32-virt.dts \
    "

do_install() {
    install -d ${D}${datadir}/cvwsoc-dts
    install -m 0644 ${UNPACKDIR}/cvwsoc*.dts ${D}${datadir}/cvwsoc-dts/
    install -m 0644 ${UNPACKDIR}/cvwsoc*.dtsi ${D}${datadir}/cvwsoc-dts/
}

FILES:${PN} = "${datadir}/cvwsoc-dts"

COMPATIBLE_MACHINE = "(cvwsoc|cva6soc|vexriscvsoc)"
