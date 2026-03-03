package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

//0x3F 0x03 0xFC 0xA6 0x00 0x66
//0x3F 0x03 0xFC 0xF3 0x00 0x33

@ExperimentalUnsignedTypes
class Command3F(data: UByteArray) : CommandModel(0x3F.toUByte(), (data.count() + 1).toUByte(), data)