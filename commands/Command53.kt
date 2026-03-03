package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

// 53 03 FC 89 01 24
// 53 03 FC A8 01 05
// 53 03 FC C8 01 65
// 53 03 FC E7 01 4A
// 53 03 FC 07 01 AA

@ExperimentalUnsignedTypes
class Command53(data: UByteArray) : CommandModel(0x53.toUByte(), (data.count() + 1).toUByte(), data)