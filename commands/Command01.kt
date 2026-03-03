package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel


// 01 02 FD 00 FE

@ExperimentalUnsignedTypes
class Command01(data: UByteArray) : CommandModel(0x01.toUByte(), 0x02.toUByte(), data)