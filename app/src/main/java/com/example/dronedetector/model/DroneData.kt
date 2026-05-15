package com.example.dronedetector.model

import com.google.gson.annotations.SerializedName

/**
 * 无人机数据模型
 */
data class DroneData(
    @SerializedName("type")
    val type: String = "",

    @SerializedName("timestamp")
    val timestamp: Long = 0,

    @SerializedName("mac")
    val mac: String = "",

    @SerializedName("rssi")
    val rssi: Int = 0,

    @SerializedName("uav_id")
    val uavId: String = "",

    @SerializedName("operator_id")
    val operatorId: String = "",

    @SerializedName("drone_location")
    val droneLocation: Location? = null,

    @SerializedName("operator_location")
    val operatorLocation: Location? = null
) {
    data class Location(
        @SerializedName("latitude")
        val latitude: String = "",

        @SerializedName("longitude")
        val longitude: String = "",

        @SerializedName("altitude")
        val altitude: Int = 0,

        @SerializedName("speed")
        val speed: Int = 0,

        @SerializedName("heading")
        val heading: Int = 0,

        @SerializedName("google_maps")
        val googleMaps: String = "",

        @SerializedName("amap")
        val amap: String = ""
    )

    fun isValid(): Boolean {
        return type == "drone_detected" && mac.isNotEmpty()
    }
}
