package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

@ExperimentalUnsignedTypes
class Command00() : CommandModel(0x00.toUByte(), ubyteArrayOf(0x00.toUByte(), 0xFF.toUByte()))