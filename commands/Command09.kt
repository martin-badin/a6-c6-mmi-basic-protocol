package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

// 09 02 FD 02 F4

@ExperimentalUnsignedTypes
class Command09(data: UByteArray) : CommandModel(0x09.toUByte(), (data.count() + 1).toUByte(), data)