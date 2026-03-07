package com.mbadin.mmibasic

import com.mbadin.mmibasic.CanFrame.Companion.computePayloadLengthChecksum
import com.mbadin.mmibasic.CanFrame.Companion.computeChecksum
import com.mbadin.mmibasic.commands.Command31
import com.mbadin.mmibasic.commands.Command39

@OptIn(ExperimentalUnsignedTypes::class)
fun loadScreen(): Array<CommandModel> {
    val m = intArrayOf(0xF8, 0xF8, 0x08, 0x08, 0x08, 0xF8, 0xF0, 0x08, 0x08, 0x08, 0xF8, 0xF0, 0x00)
    val u = intArrayOf(0x78, 0xF8, 0x80, 0x80, 0x80, 0xF8, 0xF8, 0x00)
    val l = intArrayOf(0xFF, 0xFF, 0x00)
    val t = intArrayOf(0x08, 0x7E, 0xFE, 0x88, 0x88, 0x00)
    val i = intArrayOf(0xFA, 0xFA, 0x00)
    val space = intArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
    val e = intArrayOf(0x70, 0xF8, 0xA8, 0xA8, 0xA8, 0xB8, 0xB0, 0x00)
    val d = intArrayOf(0x70, 0xF8, 0x88, 0x88, 0x88, 0xFF, 0xFF, 0x00)
    val a = intArrayOf(0x40, 0xE8, 0xA8, 0xA8, 0xA8, 0xF8, 0xF0, 0x00)
    val n = intArrayOf(0xF8, 0xF8, 0x08, 0x08, 0x08, 0xF8, 0xF0, 0x00)
    val r = intArrayOf(0xF8, 0xF8, 0x10, 0x18, 0x18, 0x00)
    val f = intArrayOf(0x08, 0xFE, 0xFF, 0x09, 0x09)
    val c = intArrayOf(0x70, 0xF8, 0x88, 0x88, 0x88, 0x88, 0x00)

    val mmi = m.plus(u).plus(l).plus(t).plus(i).plus(space).plus(m).plus(e).plus(d).plus(i).plus(a)
        .plus(space).plus(i).plus(n).plus(t).plus(e).plus(r).plus(f).plus(a).plus(c).plus(e)

    val A = intArrayOf(
        0b00000000, 0b00000000, 0b00010000,
        0b00000000, 0b00000000, 0b00011100,
        0b00000000, 0b00000000, 0b00011110,
        0b00000000, 0b10000000, 0b00011111,
        0b00000000, 0b11000000, 0b00011111,
        0b00000000, 0b11110000, 0b00011111,
        0b00000000, 0b11111000, 0b00001111,
        0b00000000, 0b11111110, 0b00000011,
        0b00000000, 0b11111111, 0b00000001,
        0b11000001, 0b11111111, 0b00000001,
        0b11100011, 0b11011111, 0b00000001,
        0b11111111, 0b11000111, 0b00000001,
        0b11111111, 0b11000011, 0b00000001,
        0b11111111, 0b11000000, 0b00000001,
        0b01111111, 0b11000000, 0b00000001,
        0b11111111, 0b11000000, 0b00000001,
        0b11111111, 0b11000011, 0b00000001,
        0b11111100, 0b11000111, 0b00000001,
        0b11111000, 0b11011111, 0b00000001,
        0b11100000, 0b11111111, 0b00000001,
        0b11000000, 0b11111111, 0b00000001,
        0b00000000, 0b11111111, 0b00000001,
        0b00000000, 0b11111110, 0b00000011,
        0b00000000, 0b11111000, 0b00001111,
        0b00000000, 0b11110000, 0b00011111,
        0b00000000, 0b11000000, 0b00011111,
        0b00000000, 0b10000000, 0b00011111,
        0b00000000, 0b00000000, 0b00011110,
        0b00000000, 0b00000000, 0b00011100,
        0b00000000, 0b00000000, 0b00010000
    )

    val U = intArrayOf(
        0b11000000, 0b11111111, 0b00000000,
        0b11000000, 0b11111111, 0b00000011,
        0b11000000, 0b11111111, 0b00000111,
        0b11000000, 0b11111111, 0b00001111,
        0b11000000, 0b11111111, 0b00001111,
        0b11000000, 0b11111111, 0b00011111,
        0b00000000, 0b10000000, 0b00011111,
        0b00000000, 0b00000000, 0b00011111,
        0b00000000, 0b00000000, 0b00011110,
        0b00000000, 0b00000000, 0b00011110,
        0b00000000, 0b00000000, 0b00011110,
        0b00000000, 0b00000000, 0b00011110,
        0b00000000, 0b00000000, 0b00011111,
        0b00000000, 0b10000000, 0b00011111,
        0b11000000, 0b11111111, 0b00011111,
        0b11000000, 0b11111111, 0b00001111,
        0b11000000, 0b11111111, 0b00001111,
        0b11000000, 0b11111111, 0b00000111,
        0b11000000, 0b11111111, 0b00000011,
        0b11000000, 0b11111111, 0b00000000
    )

    val SPACE = intArrayOf(0, 0, 0, 0, 0, 0)

    val D = intArrayOf(
        0b00000000, 0b11111000, 0b00000000,
        0b00000000, 0b11111110, 0b00000011,
        0b00000000, 0b11111111, 0b00000111,
        0b10000000, 0b11111111, 0b00000111,
        0b10000000, 0b11111111, 0b00001111,
        0b11000000, 0b11011111, 0b00001111,
        0b11000000, 0b00000111, 0b00011111,
        0b11000001, 0b00000011, 0b00011110,
        0b11000011, 0b00000011, 0b00011110,
        0b11000111, 0b00000001, 0b00011100,
        0b11001111, 0b00000001, 0b00011100,
        0b11011111, 0b00000001, 0b00011100,
        0b11111111, 0b00000001, 0b00011100,
        0b11111110, 0b00000011, 0b00011110,
        0b11111100, 0b00000011, 0b00011110,
        0b11111000, 0b00000111, 0b00011111,
        0b11110000, 0b11011111, 0b00001111,
        0b11100000, 0b11111111, 0b00001111,
        0b11000000, 0b11111111, 0b00000111,
        0b10000000, 0b11111111, 0b00000111,
        0b00000000, 0b11111111, 0b00000011,
        0b00000000, 0b11111100, 0b00000000
    )

    val I = intArrayOf(
        0b11000000, 0b11111111, 0b00011111,
        0b11000000, 0b11111111, 0b00011111,
        0b11000000, 0b11111111, 0b00011111,
        0b11000000, 0b11111111, 0b00011111,
        0b11000000, 0b11111111, 0b00011111,
        0b11000000, 0b11111111, 0b00011111
    )

    val audi = A.plus(U).plus(SPACE).plus(D).plus(SPACE).plus(I)

    val offset = 43
    val mmiCount = mmi.count()
    val audiCount = audi.count()

    val audiTextSize = 8 * 3

    val audiData = intArrayOf(0x01, 0x01, 0x00, offset + mmiCount - audiCount / 3, 40 - audiTextSize, audiTextSize, audiCount / 3).plus(audi).map { it.toUByte() }.toUByteArray()
    val lineData1 = intArrayOf(0x02, 0x01, 0x00, 0x00, 40, 0x01, offset + mmiCount).map { it.toUByte() }.toUByteArray()
    val lineData2 = intArrayOf(0x03, 0x01, 0x00, 0x00, 41, 0x01, offset + mmiCount).map { it.toUByte() }.toUByteArray()
    val mmiData = intArrayOf(0x04, 0x01, 0x00, offset, 41 + 2 + 1, 0x08, mmiCount).plus(mmi).map { it.toUByte() }.toUByteArray()

    val controlByte1 = computeChecksum(ubyteArrayOf(0x31.toUByte(), (audiData.count() + 1).toUByte(), computePayloadLengthChecksum((audiData.count() + 1).toUByte())).plus(audiData))
    val controlByte2 = computeChecksum(ubyteArrayOf(0x39.toUByte(), (lineData1.count() + 1).toUByte(), computePayloadLengthChecksum((lineData1.count() + 1).toUByte())).plus(lineData1))
    val controlByte3 = computeChecksum(ubyteArrayOf(0x39.toUByte(), (lineData2.count() + 1).toUByte(), computePayloadLengthChecksum((lineData2.count() + 1).toUByte())).plus(lineData2))
    val controlByte4 = computeChecksum(ubyteArrayOf(0x31.toUByte(), (mmiData.count() + 1).toUByte(), computePayloadLengthChecksum((mmiData.count() + 1).toUByte())).plus(mmiData))

    return arrayOf(
        Command31(audiData.plus(controlByte1)),
        Command39(lineData1.plus(controlByte2)),
        Command39(lineData2.plus(controlByte3)),
        Command31(mmiData.plus(controlByte4)),
    )
}