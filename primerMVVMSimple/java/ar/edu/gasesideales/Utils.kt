package ar.edu.gasesideales

import java.util.Locale

fun Double.formato(decimales: Int): String {
    return "%.${decimales}f".format(Locale.US, this)
}