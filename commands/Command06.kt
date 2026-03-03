package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

// Vypnutie displeja

// 06 02 FD 16 EF
// 06 02 FD 92 6B
// 06 02 FD 3F C6
// 06 02 FD 4F B6

@ExperimentalUnsignedTypes
class Command06(data: UByteArray) : CommandModel(0x06.toUByte(), data)