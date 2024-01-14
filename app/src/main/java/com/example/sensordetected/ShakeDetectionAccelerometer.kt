package com.example.sensordetected

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.example.sensordetected.databinding.ActivityMainBinding
import com.example.sensordetected.databinding.ActivityShakeBinding
import kotlin.math.abs

class ShakeDetectionAccelerometer : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityShakeBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private lateinit var vibrator: Vibrator
    private var isSensorDeviceAvailableType = false
    private var itIsNotFirstTime = false
    private var shakeThreshold=5f
    var currentX=0f
    var currentY=0f
    var currentZ=0f

    var lastX=0f
    var lastY=0f
    var lastZ=0f

    var differenceX=0f
    var differenceY=0f
    var differenceZ=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        specificAccelerometer()
    }


    private fun specificAccelerometer() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
            binding.tvX.text = "ACCELEROMETER sensor is available "
            isSensorDeviceAvailableType = true
        } else {
            isSensorDeviceAvailableType = false
            binding.tvX.text = "ACCELEROMETER sensor is not available"
        }
    }


    override fun onSensorChanged(event: SensorEvent) {
       binding.tvX.text="X->"+event.values[0]+"m/s2"
       binding.tvY.text="Y->"+event.values[1]+"m/s2"
       binding.tvZ.text="Z->"+event.values[2]+"m/s2"

        currentX=event.values[0]
        currentY=event.values[1]
        currentZ=event.values[2]

        if(itIsNotFirstTime){
            differenceX= abs(lastX - currentX)
            differenceY= abs(lastY - currentY)
            differenceZ= abs(lastZ - currentZ)


            if((differenceX>shakeThreshold && differenceY>shakeThreshold) ||
                (differenceX>shakeThreshold && differenceZ>shakeThreshold) ||
                (differenceY>shakeThreshold && differenceZ>shakeThreshold)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE))
                }else{
                    vibrator.vibrate(500)
                }
            }
        }

        lastX=currentX
        lastY=currentY
        lastZ=currentZ
        itIsNotFirstTime=true
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        if (isSensorDeviceAvailableType) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSensorDeviceAvailableType) {
            sensorManager.unregisterListener(this)
        }
    }
}