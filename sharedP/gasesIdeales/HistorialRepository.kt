package ar.edu.gasesideales

import android.content.Context

class HistorialRepository(context: Context) {

    private val pref = context.getSharedPreferences("historial_gases", Context.MODE_PRIVATE)

    companion object {
        private const val CLAVE_HISTORIAL = "clave_historial"
        private const val SEPARADOR = "|*|*|"
    }

    fun agregarRegistro(registro: String) {

        val historialActual = obtenerHistorial().toMutableList()

        historialActual.add(0, registro)

        val ultimosCinco = historialActual.take(5)

        pref.edit()
        .putString(CLAVE_HISTORIAL, ultimosCinco.joinToString (SEPARADOR))
            .apply ()
    }

    fun obtenerHistorial(): List<String> {

        val datos = pref.getString(CLAVE_HISTORIAL, "") ?: ""

        if (datos.isBlank()) return emptyList()

        return datos.split(SEPARADOR)
    }


}
