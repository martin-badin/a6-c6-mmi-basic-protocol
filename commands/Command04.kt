package com.mbadin.mmibasic.commands

import com.mbadin.mmibasic.CommandModel

//04 0B F4 02 53 10 31 30 00 50 00 E0 11 1A

@ExperimentalUnsignedTypes
class Command04 : CommandModel(
    0x04.toUByte(),
    DEFAULT_DATA
) {

    companion object {
        private val DEFAULT_DATA = ubyteArrayOf(
            0x02.toUByte(),
            0x53.toUByte(),
            0x10.toUByte(),
            0x31.toUByte(),
            0x30.toUByte(),
            0x00.toUByte(),
            0x50.toUByte(),
            0x00.toUByte(),
            0xE0.toUByte(),
            0x11.toUByte(),
            0x1A.toUByte()
        )
    }
    
     /**
     * | height | width |
     * | 00 50  | 00 E0 |
     *
     * @return Pair(height, width)
     */
    fun getDisplaySize(): Pair<Int, Int> {
        val data = getData()

        val height = (data[5].toInt() shl 8) or data[6].toInt()
        val width = (data[7].toInt() shl 8) or data[8].toInt()

        return height to width
    }
}