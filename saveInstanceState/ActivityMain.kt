package ar.edu.ciclovida

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    // Declaramos la variable para el reproductor, que puede ser nula al principio
    private var mediaPlayer: MediaPlayer? = null

    private val NOMBRE_ARCHIVO = "log_ciclo_vida.txt"
    private val KEY_CONTADOR = "contador_clicks"

    private var contadorPresiones by mutableIntStateOf(0)

    companion object {
        private const val TAG = "CicloDeVida"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            contadorPresiones = savedInstanceState.getInt(KEY_CONTADOR, 0)
            registrarEvento("Estoy en onCreate(); restaurando el estado de la Activity")
        } else {
            registrarEvento("Estoy en onCreate(); la Activity fue creada por primera vez")
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.spaceship_alarm)
        mediaPlayer?.isLooping = true

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Clicks: $contadorPresiones",
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        contadorPresiones++
                        Log.d(TAG, "Botón incrementar: ahora es $contadorPresiones")
                    }
                ) {
                    Text(text = "Incrementar contador")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        Log.d(TAG, "Botón presionado: ejecutando finish()")
                        finish()
                    }
                ) {
                    Text(text = "Finalizar Activity")
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_CONTADOR, contadorPresiones)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        registrarEvento("Estoy en onStart(); la Activity es visible en pantalla")
    }

    override fun onResume() {
        super.onResume()
        registrarEvento("Estoy en onResume(); la Activity tiene foco y está lista para interactuar")

        if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            Log.d(TAG, "MediaPlayer inició la reproducción")
        }
    }

    override fun onPause() {
        super.onPause()
        registrarEvento("Estoy en onPause(); la Activity pierde el foco")

        if (mediaPlayer != null && mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            registrarEvento("MediaPlayer está en pausa")
        }
    }

    override fun onStop() {
        super.onStop()
        registrarEvento("Estoy en onStop(); la Activity ya no es visible en pantalla")
    }

    override fun onDestroy() {
        super.onDestroy()
        registrarEvento("Estoy en onDestroy(); la Activity será eliminada de la RAM")

        mediaPlayer?.release()
        mediaPlayer = null

        registrarEvento("Recursos liberados con éxito")
    }

    override fun onRestart() {
        super.onRestart()
        registrarEvento("Estoy en onRestart(); el usuario volvió a la app, pasando de stopped a started")
    }

    private fun registrarEvento(mensaje: String) {
        Log.d(TAG, mensaje)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        val timestamp = sdf.format(Date())
        val lineaLog = "[$timestamp] $mensaje\n"

        try {
            val fileOutputStream: FileOutputStream =
                openFileOutput(NOMBRE_ARCHIVO, MODE_APPEND)

            fileOutputStream.write(lineaLog.toByteArray())
            fileOutputStream.close()

        } catch (e: Exception) {
            Log.e(TAG, "Error al escribir en el archivo de trazas", e)
        }
    }
}
