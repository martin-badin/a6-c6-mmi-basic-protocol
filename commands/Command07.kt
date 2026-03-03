package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

// 07 02 FD 03 FB
// 07 02 FD 86 7E
// 07 02 FD A6 5E
// 07 02 FD F6 0E
// 07 02 FD 16 EE
// 07 02 FD 35 CD
// 07 02 FD 55 AD
// 07 02 FD 74 8C
// 07 02 FD 94 6C
// 07 02 FD B3 4B

@ExperimentalUnsignedTypes
class Command07(data: UByteArray) : CommandModel(0x07.toUByte(), (data.count() + 1).toUByte(), data)