package com.mbadin.mmibasic

import com.mbadin.mmibasic.CanFrame.Companion.computeControlByte

@ExperimentalUnsignedTypes
open class CommandModel {
    private var id: UByte
    private var dlc: UByte
    private var crc: UByte
    private var data: UByteArray

    constructor(
        id: UByte,
        dlc: UByte,
        crc: UByte,
        data: UByteArray
    ) {
        this.id = id
        this.dlc = dlc
        this.crc = crc
        this.data = if (dlc.toInt() == data.count()) data else data.plus(
            computeControlByte(
                ubyteArrayOf(
                    id,
                    dlc,
                    crc
                ).plus(data)
            )
        )
    }

    constructor(
        id: UByte,
        dlc: UByte,
        data: UByteArray
    ) {
        this.id = id
        this.dlc = dlc
        this.crc = (255 - dlc.toInt()).toUByte()
        this.data = if (dlc.toInt() == data.count()) data else data.plus(
            computeControlByte(
                ubyteArrayOf(
                    id,
                    dlc,
                    crc
                ).plus(data)
            )
        )
    }

    constructor(
        id: UByte,
        data: UByteArray
    ) {
        this.id = id
        this.dlc = data.count().toUByte()
        this.crc = (255 - this.dlc.toInt()).toUByte()
        this.data = data
    }

    val valid: Boolean
        get() = (255 - dlc.toInt()) == crc.toInt() && dlc.toInt() == data.count() && data.last() == computeControlByte(
            ubyteArrayOf(
                id,
                dlc,
                crc
            ).plus(data.copyOfRange(0, dlc.toInt() - 1))
        )

    fun getId(): UByte {
        return id
    }

    fun setId(id: UByte) {
        this.id = id
    }

    fun getDlc(): UByte {
        return dlc
    }

    fun setDlc(dlc: UByte) {
        this.dlc = dlc
    }

    fun getCrc(): UByte {
        return crc
    }

    fun setCrc(crc: UByte) {
        this.crc = crc
    }

    fun getData(): UByteArray {
        return data
    }

    fun setData(data: UByteArray) {
        this.data = data
    }

    fun getOrder(): UByte {
        return data.first()
    }

    fun getPayload(): UByteArray {
        return data.copyOfRange(1, dlc.toInt() - 1)
    }

    fun getControlByte(): UByte {
        return data.last()
    }

    fun toByteArray(): ByteArray {
        return toUByteArray().toByteArray()
    }

    fun toUByteArray(): UByteArray {
        return ubyteArrayOf(id, dlc, crc).plus(data)
    }

    fun toCSVString(): String {
        return "${id.toHex()};${dlc.toInt()};${crc.toHex()};${data.toHex()};${System.currentTimeMillis() / 1000}"
    }

    override fun toString(): String {
        return toUByteArray().joinToString(" ") { it.toHex() }
    }

    companion object {
        private var TAG = CommandModel::class.simpleName ?: ""
    }
}