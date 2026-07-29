
# override jump addr for 'generic'
EXTRA_OEMAKE:append:cvwsoc = " FW_JUMP_ADDR=${UBOOT_ENTRYPOINT}"

# prevent relocation to default 0x02200000 (0x82200000)
EXTRA_OEMAKE:append:cvwsoc-renode-u540 = " FW_PAYLOAD_FDT_ADDR=0x87000000"