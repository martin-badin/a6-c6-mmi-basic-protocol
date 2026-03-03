package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

// 03 02 FD 01 FD

@ExperimentalUnsignedTypes
class Command03(data: UByteArray) : CommandModel(0x03.toUByte(), (data.count() + 1).toUByte(), data)