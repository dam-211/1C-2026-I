package ar.edu.ciclovida

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    //Declaramos la variable para el reprosductos, que podría ser nula en un principio
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private const val TAG = "CicloDeVida"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Estoy en onCreate; La actividad ha sido creada con éxito!")

        mediaPlayer = MediaPlayer.create(this, R.raw.spaceship_alarm)
        mediaPlayer?.isLooping = true

        // Agregamos la interfaz gráfica con Jetpack Compose
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    Log.d(TAG, "Botón presionado: Ejecutando finish()")
                    finish() // <--- Solicita al OS destruir la Activity
                }) {
                    Text(text = "Finalizar Actividad (finish)")
                }
            }
        }

    }

    override fun onStart() {
        //1 Lugar
        super.onStart()
        Log.d(TAG, "Estoy en onStart(); La actividad es visible en pantalla")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Estoy en onResume(); La actividad tiene foco y esta lista para interactura")

        if(mediaPlayer != null && !mediaPlayer!!.isPlaying) {

            mediaPlayer?.start()

            Log.d(TAG, "Media Player Iniciando reproducción")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Estoy en onPause(); La actividad pierde el foco....")

        if(mediaPlayer != null && mediaPlayer!!.isPlaying) {

            mediaPlayer?.pause()

            Log.d(TAG, "Media Player esta en pausa")
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Estoy en onStop(); La actividad ya no es visible en pantalla")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Estoy en OnDestroy() La actividad será eiliminada de la RAM")

        mediaPlayer?.release()
        mediaPlayer = null

        Log.d(TAG, "Recursos liberados con éxito!!!")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "Estoy en onRestart(); el usuario volvio a la app, pasando de stoped a started")
    }
}
