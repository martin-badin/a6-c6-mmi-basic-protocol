package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel
import com.mbadin.mmibasic.toBinary

// 31 38 C7 07 01 00 C9 0C 1B 0C 03 00 00 00 0D 00 00 00 31 00 00 00 C1 00 00 00 07 03 00 00 18 1C 00 00 60 01 02 18 0D 37 B0 62 39 08 F7 36 01 02 14 17 23 04 D7 39 08 F7 37 01 02
// 31 0D F2 60 01 00 72 2B 07 05 00 42 7F 40 00 89
// 31 0D F2 61 01 00 78 2B 07 05 3E 0D 02 FD 08 FA
// 31 0D F2 6C 01 00 64 2B 07 05 01 01 79 05 03 91
// 31 0D F2 6D 01 00 6A 2B 07 05 3E 41 41 41 3E A0
// 31 09 F6 6E 01 00 70 2E 04 01 09 F3
// 31 0D F2 6F 01 00 72 2B 07 05 00 42 7F 40 00 86
// 31 0D F2 70 01 00 78 2B 07 05 00 42 7F 40 0D 02
// 31 0D F2 7A 01 00 64 2B 07 05 01 01 79 05 03 87
// 31 0D F2 7B 01 00 6A 2B 07 05 3E 41 41 41 3E B6
// 31 0F F0 DB 01 00 01 02 07 ED 01 00 C9 29 1B 0C 00 00
// 31 0A F5 02 01 00 77 00 07 02 00 00 BF
// 31 0C F3 04 01 00 7A 02 07 04 7F 09 09 01 CE
// 31 1E E1 09 01 01 5D 46 09 0B FF 01 FF 01 FB 01 83 01 FB 01 FF 01 83 01 EB 01 F7 01 FF 01 FF 01 3C
// 31 0D F2 0B 01 00 69 47 07 05 00 42 7F 40 00 95
// 31 0D F2 0C 01 00 6F 47 07 05 06 49 49 49 3E 98
// 31 09 F6 0D 01 00 75 4A 04 01 09 F1
// 31 1E E1 33 01 01 5D 46 09 0B FF 01 FF 01 FB 01 83 01 FB 01 FF 01 83 01 EB 01 F7 01 FF 01 FF 01 06
// 31 0D F2 35 01 00 69 47 07 05 00 42 7F 40 00 AB
// 31 0D F2 36 01 00 6F 47 07 05 06 49 49 49 3E A2
// 31 09 F6 37 01 00 75 4A 04 0D 02 FD

@ExperimentalUnsignedTypes
class Command31(data: UByteArray) : CommandModel(0x31.toUByte(), data) {
    /**
     * 0x00 - both colors
     * 0x01 - red color if bit is 1
     * 0x02 - red color if bit is 0
     */
    fun getColorType(): UByte {
        return getData()[2]
    }

    // [x, y]
    fun getCoords(): Array<UByte> {
        return arrayOf(getData()[3], getData()[4])
    }

    // [height, width]
    fun getSize(): Array<UByte> {
        return arrayOf(getData()[5], getData()[6])
    }

    fun getBytes(): List<String> {
        val width = getSize()[1]

        val bytes = getData().copyOfRange(7, getData().count() - 1)

        val count = bytes.count() / width.toInt()

        return Array(width.toInt()) { it }.map { index ->
            val start = count * index
            val end = start + count

            bytes
                .copyOfRange(start, end)
                .joinToString("") { it.toBinary().reversed() }
        }
    }
}