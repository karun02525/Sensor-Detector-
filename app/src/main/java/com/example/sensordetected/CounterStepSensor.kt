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
import com.example.sensordetected.databinding.ActivityCounterStepBinding

class CounterStepSensor : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityCounterStepBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounterSensor: Sensor
    private lateinit var vibrator: Vibrator
    var isSensorDeviceAvailableType=false
    var stepCounter=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterStepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        vibrator=getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        specificSTEPCOUNTER()

    }

    private fun specificSTEPCOUNTER() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!
            isSensorDeviceAvailableType = true
        } else {
            isSensorDeviceAvailableType = false
            binding.stepCounter.text = "TYPE_STEP_COUNTER sensor is not available"
        }
    }


    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor==stepCounterSensor) {
            stepCounter = event.values[0].toInt()
            binding.stepCounter.text = "Step Counter: " + stepCounter

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(500)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onResume() {
        super.onResume()
        if(isSensorDeviceAvailableType) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if(isSensorDeviceAvailableType) {
            sensorManager.unregisterListener(this)
        }
    }
}