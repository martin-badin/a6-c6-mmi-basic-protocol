package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

@ExperimentalUnsignedTypes
class Command02() : CommandModel(0x02.toUByte(), ubyteArrayOf(0x01.toUByte(), 0x01.toUByte(), 0xFF.toUByte(), 0xF2.toUByte()))