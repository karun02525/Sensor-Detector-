package com.example.sensordetected

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService
import com.example.sensordetected.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var deviceSensor: List<Sensor>
    private lateinit var sensor: Sensor
    private lateinit var vibrator:Vibrator
    var changeValue=0f
    var isSensorDeviceAvailableType=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        vibrator=getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        deviceSensor = sensorManager.getSensorList(Sensor.TYPE_ALL)

        /*deviceSensor.forEach {
            binding.tv.text="${binding.tv.text}\n ${it.name}"
        }
*/
       // specificSensor()
        specificPROXIMITY()
        //getPower()
    }

    private fun getPower() {
        sensor= sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!
        binding.tv.text=""+sensor.power
    }

    private fun specificSensor() {
        if(sensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) !=null){
            binding.tv.text="this device has HEART BEAT "
            isSensorDeviceAvailableType=true
        }else{
            isSensorDeviceAvailableType=false
            binding.tv.text="this device has no HEART BEAT "
        }
    }

   private fun specificPROXIMITY() {
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) !=null){
            binding.tv.text="this device has PROXIMITY "
            isSensorDeviceAvailableType=true
        }else{
            isSensorDeviceAvailableType=false
            binding.tv.text="this device has no PROXIMITY "
        }
    }



    override fun onSensorChanged(event: SensorEvent) {
        changeValue=event.values[0]
        binding.tv.text=""+ changeValue+" cm"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator.vibrate(500)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onResume() {
        super.onResume()
        if(isSensorDeviceAvailableType) {

            sensorManager.registerListener(
                this,
               // sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if(isSensorDeviceAvailableType) {
            sensorManager.unregisterListener(this)
        }
    }
}