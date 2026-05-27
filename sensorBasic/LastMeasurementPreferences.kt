package ar.edu.sensoresintro

import android.content.Context

class LastMeasurementPreferences(context: Context) {

    private val preferencias = context.getSharedPreferences("sensor_preference", Context.MODE_PRIVATE)

    fun saveLastMeasurement(measurement: AccelerometerMeasurement) {

        preferencias.edit()
            .putFloat(KEY_X, measurement.x)
            .putFloat(KEY_Y, measurement.y)
            .putFloat(KEY_Z, measurement.z)
            .putLong(KEY_TIMESTAMP, measurement.timeStampMillis)
            .apply()

    }


    fun getLastMeasurement(): AccelerometerMeasurement? {

        if(!preferencias.contains(KEY_TIMESTAMP)){
            return null
        }
        return AccelerometerMeasurement (

            x = preferencias.getFloat(KEY_X, 0f),
            y = preferencias.getFloat(KEY_Y, 0f),
            z = preferencias.getFloat(KEY_Y, 0f),
            timeStampMillis = preferencias.getLong(KEY_TIMESTAMP, 0L)

        )

    }
    companion object {
        private const val KEY_X = "last_x"
        private const val KEY_Y = "last_y"
        private const val KEY_Z = "last_z"
        private const val KEY_TIMESTAMP = "last_timestamp"
    }
}
