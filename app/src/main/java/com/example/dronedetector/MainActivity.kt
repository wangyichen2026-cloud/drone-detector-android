package com.example.dronedetector

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.dronedetector.model.DroneData
import com.example.dronedetector.usb.UsbSerialManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var usbManager: UsbSerialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usbManager = UsbSerialManager(this)

        setContent {
            DroneDetectorApp(usbManager)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usbManager.disconnect()
    }
}

@Composable
fun DroneDetectorApp(usbManager: UsbSerialManager) {
    val droneData by usbManager.droneDataFlow.collectAsState()
    val connectionStatus by usbManager.connectionStatusFlow.collectAsState()
    val logs by usbManager.logsFlow.collectAsState()
    val availableDevices = remember { mutableStateOf(emptyList<android.hardware.usb.UsbDevice>()) }
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        availableDevices.value = usbManager.getAvailableDevices()
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1a1a1a))
        ) {
            // 顶部状态栏
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color(0xFF2a2a2a),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "无人机侦测器",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        connectionStatus,
                        fontSize = 14.sp,
                        color = if (connectionStatus.contains("已连接")) Color(0xFF4CAF50) else Color(0xFFFF9800)
                    )
                }
            }

            // Tab 导航
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFF2a2a2a),
                contentColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("设备连接") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("无人机数据") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("日志") }
                )
            }

            // Tab 内容
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> DeviceConnectionTab(usbManager, availableDevices.value)
                    1 -> DroneDataTab(droneData)
                    2 -> LogsTab(logs)
                }
            }
        }
    }
}

@Composable
fun DeviceConnectionTab(usbManager: UsbSerialManager, devices: List<android.hardware.usb.UsbDevice>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a1a))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "可用 USB 设备",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (devices.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF2a2a2a),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "未检测到 USB 设备",
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFBBBBBB)
                    )
                }
            }
        } else {
            items(devices) { device ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            usbManager.connectToDevice(device)
                        },
                    color = Color(0xFF2a2a2a),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            device.deviceName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "VID: ${device.vendorId} PID: ${device.productId}",
                            fontSize = 12.sp,
                            color = Color(0xFFBBBBBB)
                        )
                        Text(
                            "点击连接",
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { usbManager.disconnect() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) {
                Text("断开连接")
            }
        }
    }
}

@Composable
fun DroneDataTab(droneData: DroneData?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a1a))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (droneData == null) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF2a2a2a),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "等待无人机数据...",
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFBBBBBB)
                    )
                }
            }
        } else {
            item {
                Text("无人机信息", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            item {
                DataCard("无人机 ID", droneData.uavId)
            }

            item {
                DataCard("操作员 ID", droneData.operatorId)
            }

            item {
                DataCard("MAC 地址", droneData.mac)
            }

            item {
                DataCard("信号强度", "${droneData.rssi} dBm")
            }

            if (droneData.droneLocation != null) {
                item {
                    Text("无人机位置", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                item {
                    DataCard("纬度", droneData.droneLocation.latitude)
                }

                item {
                    DataCard("经度", droneData.droneLocation.longitude)
                }

                item {
                    DataCard("海拔", "${droneData.droneLocation.altitude} m")
                }

                item {
                    DataCard("速度", "${droneData.droneLocation.speed} m/s")
                }

                item {
                    DataCard("航向", "${droneData.droneLocation.heading}°")
                }

                item {
                    NavigationButton(
                        label = "高德地图导航",
                        url = droneData.droneLocation.amap
                    )
                }

                item {
                    NavigationButton(
                        label = "谷歌地图导航",
                        url = droneData.droneLocation.googleMaps
                    )
                }
            }

            if (droneData.operatorLocation != null) {
                item {
                    Text("飞手位置", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                item {
                    DataCard("纬度", droneData.operatorLocation.latitude)
                }

                item {
                    DataCard("经度", droneData.operatorLocation.longitude)
                }

                item {
                    NavigationButton(
                        label = "高德地图导航",
                        url = droneData.operatorLocation.amap
                    )
                }
            }
        }
    }
}

@Composable
fun LogsTab(logs: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a1a))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(logs) { log ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2a2a2a),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    log,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                    color = when {
                        log.contains("[成功]") -> Color(0xFF4CAF50)
                        log.contains("[错误]") -> Color(0xFFFF5252)
                        log.contains("[数据]") -> Color(0xFF2196F3)
                        else -> Color(0xFFBBBBBB)
                    },
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun DataCard(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF2a2a2a),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 12.sp, color = Color(0xFFBBBBBB))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun NavigationButton(label: String, url: String) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Text(label)
    }
}
