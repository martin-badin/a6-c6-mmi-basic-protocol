package com.mbadin.mmibasic

import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import com.mbadin.mmibasic.utils.AppLog

@ExperimentalUnsignedTypes
class USBDevice(manager: UsbManager, private val listener: Listener) :
    SerialInputOutputManager.Listener {
    private val canFrame = CanFrame()
    lateinit var port: UsbSerialPort

    init {
        try {
            val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)

            if (drivers.isNotEmpty()) {
                val driver: UsbSerialDriver = drivers.first()
                port = driver.ports.first() // Most devices have just one port (port 0)
                port.open(manager.openDevice(driver.device))
                port.setParameters(
                    BAUD_RATE,
                    UsbSerialPort.DATABITS_8,
                    UsbSerialPort.STOPBITS_1,
                    UsbSerialPort.PARITY_EVEN
                )
                SerialInputOutputManager(port, this).start()
            }
        } catch (e: Exception) {
            AppLog.error(TAG, e.toString())
        }
    }

    interface Listener {
        fun onNewData(models: Array<CommandModel>)
    }

    override fun onNewData(data: ByteArray) {
        val converted = data.toUByteArray()

        // AppFile("data.txt").write(converted.joinToString(" ") { it.toHex() })

        try {
            listener.onNewData(canFrame.parse(converted))
        } catch (e: Exception) {
            AppLog.error(TAG, converted.toHex())
            AppLog.error(TAG, e.toString())
        }
    }

    override fun onRunError(e: java.lang.Exception?) {
        AppLog.error(TAG, e.toString())
    }

    companion object {
        const val BAUD_RATE: Int = 500000
        var TAG = USBDevice::class.simpleName ?: ""
    }
}