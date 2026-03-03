package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

//0x08 0x02 0xFD 0x04 0xF3

@ExperimentalUnsignedTypes
class Command08(data: UByteArray) : CommandModel(0x08.toUByte(), data)