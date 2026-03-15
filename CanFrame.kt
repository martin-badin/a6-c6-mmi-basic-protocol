package com.mbadin.mmibasic

@ExperimentalUnsignedTypes
class CanFrame {
    private var rest = ubyteArrayOf()

    fun parse(bytes: UByteArray): Array<CommandModel> {
        val newBytes: UByteArray = rest.plus(bytes)
        var models = arrayOf<CommandModel>()

        val count = newBytes.count()

        var index = 0
        var lastIndex = 0

        while (index < count) {
            val id = newBytes.getOrElse(index) { 0.toUByte() }
            val dlc = newBytes.getOrElse(index + 1) { 0.toUByte() }
            val crc = newBytes.getOrElse(index + 2) { 0.toUByte() }

            val fromIndex = index + 3
            val toIndex = dlc.toInt() + fromIndex

            if (count > fromIndex && count >= toIndex) {
                val data = newBytes.copyOfRange(fromIndex, toIndex)

                if (data.isNotEmpty()) {
                    val model = CommandModel(id, dlc, crc, data)

                    if (model.valid) {
                        lastIndex = toIndex
                        index = toIndex

                        models = models.plus(CommandModel(id, dlc, crc, data))

                        continue
                    }
                }
            }

            index++
        }

        rest = newBytes.copyOfRange(lastIndex, count)

        return models
    }
}