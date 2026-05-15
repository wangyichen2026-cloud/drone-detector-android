package com.example.dronedetector.usb

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.google.gson.Gson
import com.example.dronedetector.model.DroneData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * USB 串口管理器
 * 负责连接 ESP32 并读取 JSON 数据
 */
class UsbSerialManager(private val context: Context) {
    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    private var usbSerialDevice: UsbSerialDevice? = null
    private val gson = Gson()

    private val _droneDataFlow = MutableStateFlow<DroneData?>(null)
    val droneDataFlow: StateFlow<DroneData?> = _droneDataFlow

    private val _connectionStatusFlow = MutableStateFlow("未连接")
    val connectionStatusFlow: StateFlow<String> = _connectionStatusFlow

    private val _logsFlow = MutableStateFlow<List<String>>(emptyList())
    val logsFlow: StateFlow<List<String>> = _logsFlow

    private val logs = mutableListOf<String>()
    private var jsonBuffer = StringBuilder()

    companion object {
        private const val TAG = "UsbSerialManager"
        private const val BAUD_RATE = 115200
    }

    /**
     * 获取可用的 USB 设备列表
     */
    fun getAvailableDevices(): List<UsbDevice> {
        return usbManager.deviceList.values.toList()
    }

    /**
     * 连接到 USB 设备
     */
    fun connectToDevice(device: UsbDevice): Boolean {
        return try {
            val connection = usbManager.openDevice(device) ?: run {
                addLog("[错误] 无法打开设备: ${device.deviceName}")
                return false
            }

            usbSerialDevice = UsbSerialDevice.createUsbSerialDevice(device, connection)
            if (usbSerialDevice == null) {
                addLog("[错误] 不支持的 USB 设备")
                connection.close()
                return false
            }

            if (!usbSerialDevice!!.open()) {
                addLog("[错误] 无法打开串口")
                return false
            }

            // 配置串口参数
            usbSerialDevice!!.setBaudRate(BAUD_RATE)
            usbSerialDevice!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
            usbSerialDevice!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
            usbSerialDevice!!.setParity(UsbSerialInterface.PARITY_NONE)
            usbSerialDevice!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)

            // 设置读取回调
            usbSerialDevice!!.read(onDataReceived)

            _connectionStatusFlow.value = "已连接: ${device.deviceName}"
            addLog("[成功] 已连接到 ${device.deviceName}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "连接失败", e)
            addLog("[错误] 连接失败: ${e.message}")
            false
        }
    }

    /**
     * 断开连接
     */
    fun disconnect() {
        try {
            usbSerialDevice?.close()
            usbSerialDevice = null
            _connectionStatusFlow.value = "未连接"
            addLog("[断开] 已断开连接")
        } catch (e: Exception) {
            Log.e(TAG, "断开连接失败", e)
        }
    }

    /**
     * 数据接收回调
     */
    private val onDataReceived = UsbSerialInterface.UsbReadCallback { data ->
        try {
            val receivedData = String(data)
            jsonBuffer.append(receivedData)

            // 查找完整的 JSON 对象
            val jsonText = jsonBuffer.toString()
            val startIndex = jsonText.indexOf('{')
            val endIndex = jsonText.indexOf('}')

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                val jsonString = jsonText.substring(startIndex, endIndex + 1)
                try {
                    val droneData = gson.fromJson(jsonString, DroneData::class.java)
                    if (droneData.isValid()) {
                        _droneDataFlow.value = droneData
                        addLog("[数据] 已接收无人机数据: ${droneData.uavId}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "JSON 解析失败", e)
                    addLog("[错误] JSON 解析失败: ${e.message}")
                }

                // 清空已处理的数据
                jsonBuffer = StringBuilder(jsonText.substring(endIndex + 1))
            }
        } catch (e: Exception) {
            Log.e(TAG, "数据处理失败", e)
        }
    }

    /**
     * 添加日志
     */
    private fun addLog(message: String) {
        logs.add(message)
        if (logs.size > 100) {
            logs.removeAt(0)
        }
        _logsFlow.value = logs.toList()
        Log.d(TAG, message)
    }

    /**
     * 检查是否已连接
     */
    fun isConnected(): Boolean {
        return usbSerialDevice != null && usbSerialDevice!!.isOpen
    }
}
