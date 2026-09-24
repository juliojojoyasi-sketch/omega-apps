package com.example.omega.gps

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

class CompassSensor(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val _azimuth = MutableStateFlow(0f)
    val azimuth: StateFlow<Float> = _azimuth.asStateFlow()

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null
    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private var isListening = false
    private var currentFilteredAzimuth = 0f

    fun start() {
        if (isListening || sensorManager == null) return

        if (rotationSensor != null) {
            sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
            magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        }
        isListening = true
    }

    fun stop() {
        if (!isListening) return
        sensorManager?.unregisterListener(this)
        isListening = false
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientation)
            val deg = (Math.toDegrees(orientation[0].toDouble()) + 360.0).toFloat() % 360f
            updateSmoothAzimuth(deg)
        } else {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values.clone()
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic = event.values.clone()
            }
            val g = gravity
            val m = geomagnetic
            if (g != null && m != null) {
                val r = FloatArray(9)
                val i = FloatArray(9)
                if (SensorManager.getRotationMatrix(r, i, g, m)) {
                    val o = FloatArray(3)
                    SensorManager.getOrientation(r, o)
                    val deg = (Math.toDegrees(o[0].toDouble()) + 360.0).toFloat() % 360f
                    updateSmoothAzimuth(deg)
                }
            }
        }
    }

    private fun updateSmoothAzimuth(targetDeg: Float) {
        // Low-pass filter with circular wrap-around
        var diff = targetDeg - currentFilteredAzimuth
        while (diff < -180f) diff += 360f
        while (diff > 180f) diff -= 360f

        if (abs(diff) > 0.5f) {
            currentFilteredAzimuth = (currentFilteredAzimuth + diff * 0.25f + 360f) % 360f
            _azimuth.value = currentFilteredAzimuth
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
