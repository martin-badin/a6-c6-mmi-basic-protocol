package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CanFrame.Companion.computeControlByte
import com.mbadin.mmibasic.CommandModel

// 55 04 FB 04 51 E0 1F
// 55 04 FB 87 51 E0 9C
// 55 04 FB A7 51 E0 BC
// 55 04 FB C6 51 E0 DD
// 55 04 FB E6 51 E0 FD
// 55 04 FB 05 51 E0 1E
// 55 04 FB 25 51 E0 3E
// 55 04 FB 44 51 E0 5F
// 55 04 FB 64 51 E0 7F
// 55 04 FB 83 51 E0 98

@ExperimentalUnsignedTypes
class Command55(data: UByteArray) : CommandModel(0x55.toUByte(), 0x04.toUByte(), data) {
    private val brightness = 0x0F.toUByte() // min: 0, max: 255
    private val unknown = 0xE0.toUByte() // unknown

    init {
        if (data.count() == 1) {
            val aa = data.plus(brightness).plus(unknown)

            setData(aa.plus(computeControlByte(ubyteArrayOf(getId(), getDlc(), getCrc()).plus(aa))))
        }
    }

    fun getBrightness(): UByte {
        return getData()[1]
    }
}