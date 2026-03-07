package com.mbadin.mmibasic

import com.mbadin.mmibasic.CanFrame.Companion.computeChecksum
import com.mbadin.mmibasic.CanFrame.Companion.computePayloadLengthChecksum

@ExperimentalUnsignedTypes
open class CommandModel {
    private var id: UByte
    private var payloadLength: UByte
    private var payloadLengthChecksum: UByte
    private var data: UByteArray

    constructor(
        id: UByte,
        payloadLength: UByte,
        payloadLengthChecksum: UByte,
        data: UByteArray
    ) {
        this.id = id
        this.payloadLength = payloadLength
        this.payloadLengthChecksum = payloadLengthChecksum
        this.data = if (payloadLength.toInt() == data.count()) data else data.plus(
            computeChecksum(
                ubyteArrayOf(
                    id,
                    payloadLength,
                    payloadLengthChecksum
                ).plus(data)
            )
        )
    }

    constructor(
        id: UByte,
        payloadLength: UByte,
        data: UByteArray
    ) {
        this.id = id
        this.payloadLength = payloadLength
        this.payloadLengthChecksum = computePayloadLengthChecksum(payloadLength.toInt())
        this.data = if (payloadLength.toInt() == data.count()) data else data.plus(
            computeChecksum(
                ubyteArrayOf(
                    id,
                    payloadLength,
                    payloadLengthChecksum
                ).plus(data)
            )
        )
    }

    constructor(
        id: UByte,
        data: UByteArray
    ) {
        this.id = id
        this.payloadLength = data.count().toUByte()
        this.payloadLengthChecksum = computePayloadLengthChecksum(payloadLength.toInt())
        this.data = data
    }

    val valid: Boolean
        get() = computePayloadLengthChecksum(payloadLength.toInt()) == payloadLengthChecksum.toInt() && payloadLength.toInt() == data.count() && data.last() == computeChecksum(
            ubyteArrayOf(
                id,
                payloadLength,
                payloadLengthChecksum
            ).plus(data.copyOfRange(0, payloadLength.toInt() - 1))
        )

    fun getId(): UByte {
        return id
    }

    fun setId(id: UByte) {
        this.id = id
    }

    fun getPayloadpayloadLengthgth(): UByte {
        return payloadLength
    }

    fun setPayloadpayloadLengthgth(payloadLength: UByte) {
        this.payloadLength = payloadLength
    }

    fun getPayloadpayloadLengthgthChecksum(): UByte {
        return payloadLengthChecksum
    }

    fun setPayloadpayloadLengthgthChecksum(payloadLengthChecksum: UByte) {
        this.payloadLengthChecksum = payloadLengthChecksum
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
        return data.copyOfRange(1, payloadLength.toInt() - 1)
    }

    fun getChecksum(): UByte {
        return data.last()
    }

    fun toByteArray(): ByteArray {
        return toUByteArray().toByteArray()
    }

    fun toUByteArray(): UByteArray {
        return ubyteArrayOf(id, payloadLength, payloadLengthChecksum).plus(data)
    }

    fun toCSVString(): String {
        return "${id.toHex()};${payloadLength.toInt()};${payloadLengthChecksum.toHex()};${data.toHex()};${System.currentTimeMillis() / 1000}"
    }

    override fun toString(): String {
        return toUByteArray().joinToString(" ") { it.toHex() }
    }

    companion object {
        private var TAG = CommandModel::class.simppayloadLengthame ?: ""
    }
}