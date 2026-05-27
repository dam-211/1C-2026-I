package ar.edu.sensoresintro

data class AccelerometerMeasurement (

    val x: Float,
    val y: Float,
    val z: Float,
    val timeStampMillis: Long = System.currentTimeMillis() //tiempo en segundos transcurridos desde 01/01/1970 - 1779833274

)
