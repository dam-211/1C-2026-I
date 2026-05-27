package ar.edu.sensoresintro

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.sqrt

/**
 * Clase encargada de gestionar la lectura del acelerómetro del dispositivo.
 * * Implementa [SensorEventListener] dado que en Android los sensores están basados
 * en eventos: no leemos el sensor a demanda (ej. x = leer()), sino que nos
 * suscribimos y Android nos avisa cada vez que hay una nueva medición.
 *
 * @param context Requerido para acceder a los servicios internos del sistema operativo.
 */
class AccelerometerReader(context: Context): SensorEventListener {

    @SuppressLint("ServiceCast")
    // 1. Obtenemos el SensorManager a través del servicio de sistema (Context.SENSOR_SERVICE).
    // Este manager es la "puerta de entrada" para acceder a todos los sensores físicos del dispositivo.
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // 2. Solicitamos específicamente el acelerómetro por defecto del dispositivo.
    // Usamos el tipo "Sensor?" (nullable) porque es posible que el dispositivo físico
    // no cuente con un acelerómetro (aunque hoy en día es muy raro).
    private val acelerometro: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // 3. Variable para almacenar un callback (función lambda).
    // Esta función tomará las mediciones (AccelerometerMeasurement) y no devolverá nada (Unit).
    // Se invocará automáticamente cada vez que el sensor registre un cambio.
    private var onMeasurementChanged: ((AccelerometerMeasurement) -> Unit)? = null


    /**
     * Inicia la suscripción al sensor y comienza a recibir mediciones.
     *
     * @param onMeasurementChanged Función lambda que se ejecutará al recibir nuevos datos.
     * @return `true` si el sensor se registró con éxito, `false` si el dispositivo no tiene acelerómetro.
     */
    fun startReading(onMeasurementChanged: (AccelerometerMeasurement) -> Unit): Boolean {

        // Usamos el operador Elvis (?:) para verificar si el acelerómetro existe.
        // Si 'acelerometro' es null, salimos de la función inmediatamente devolviendo false.
        val sensor = acelerometro ?: return false

        // Guardamos la función recibida por parámetro en la propiedad de la clase (this)
        // para poder invocarla más adelante dentro de onSensorChanged.
        this.onMeasurementChanged = onMeasurementChanged

        // Registramos esta misma clase (this) como "oyente" (listener) del sensor.
        // SENSOR_DELAY_UI indica la frecuencia de muestreo, ideal para actualizar interfaces de usuario.
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_UI
        )

        return true
    }

    /**
     * Detiene la lectura del sensor.
     * Es crucial llamar a esta función (por ejemplo, en el onPause o onDestroy del Activity/Fragment)
     * para no agotar la batería del dispositivo innecesariamente.
     */
    fun stopReading() {
        // Desregistramos el listener y liberamos el callback
        sensorManager.unregisterListener(this)
        onMeasurementChanged = null
    }

    /**
     * Método invocado automáticamente por Android cuando el sensor registra un nuevo valor.
     */
    override fun onSensorChanged(event: SensorEvent?) {

        // Evitamos procesar eventos nulos
        if (event == null) return

        // Verificamos que el evento provenga efectivamente del acelerómetro
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            // Sistema de coordenadas relativas al dispositivo en su orientación natural:
            // X: Horizontal (izquierda/derecha)
            // Y: Vertical (arriba/abajo)
            // Z: Profundidad (acercar/alejar de la cara)
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calculamos la magnitud del vector aceleración (Fuerza G total).
            // Usamos el teorema de Pitágoras en 3D: √(x² + y² + z²)
            val fuerza = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            // Si la fuerza supera un umbral (en este caso 15 m/s²), lo consideramos un movimiento brusco.
            // (Nota: La gravedad estándar en reposo es ~9.81 m/s²)
            if (fuerza > 15f) {
                Log.d("Sensor", "¡Movimiento brusco detectado!")
            }

            // Empaquetamos los datos en nuestro modelo de datos personalizado
            val medidas = AccelerometerMeasurement(x = x, y = y, z = z)

            // Usamos la llamada segura (?.) para invocar el callback solo si no es nulo.
            // Esto pasará las mediciones hacia la clase que haya llamado a 'startReading'.
            onMeasurementChanged?.invoke(medidas)
        }
    }

    /**
     * Método invocado por Android cuando la precisión del sensor cambia (ej. necesidad de calibración).
     * Obligatorio de implementar por la interfaz [SensorEventListener], pero no lo utilizamos aquí.
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se requiere implementación para este caso de uso.
    }
}
