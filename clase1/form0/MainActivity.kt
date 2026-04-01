// En Python definiríamos una función normal: def formulario_saludo():
// En Kotlin Compose, @Composable indica que esta función dibuja UI en pantalla
@Composable
fun FormularioSaludo() {

    // En Python usaríamos simplemente: nombre = ""
    // Aquí necesitamos "remember" para que Kotlin RECUERDE el valor
    // entre redibujos de pantalla. Sin él, se resetearía a "" cada vez.
    // mutableStateOf("") es como una variable "reactiva": cuando cambia,
    // la pantalla se actualiza automáticamente
    var nombre by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Column apila elementos de arriba a abajo, como una lista vertical
    Column(
        modifier = Modifier
            .fillMaxSize()                                   // Ocupa toda la pantalla
            .padding(horizontal = 32.dp, vertical = 48.dp), // Márgenes internos (dp = pixeles independientes de la pantalla)
        horizontalAlignment = Alignment.CenterHorizontally,  // Centra horizontalmente
        verticalArrangement = Arrangement.Center             // Centra verticalmente
    ) {

        // Equivalente a un print() pero en pantalla gráfica
        Text(
            text = "Ingresa tu nombre",
            fontSize = 20.sp  // sp = tamaño que respeta la accesibilidad del celular
        )

        // Spacer es un espacio en blanco entre elementos, como un salto de línea
        Spacer(modifier = Modifier.height(12.dp))

        // Campo de texto editable, como input() pero visual
        // value = lo que se muestra en el campo
        OutlinedTextField(
            value = nombre,

            // onValueChange es una instrucción que le dice a Kotlin:
            // "cada vez que el usuario escriba algo, ejecutá este bloque de código"
            // Las llaves { } representan ese bloque de código a ejecutar
            // "it" es el nombre automático que Kotlin le da al nuevo texto escrito
            // Es decir: cuando el usuario escribe, "it" contiene lo que escribió
            onValueChange = { nombre = it },

            label = { Text("Nombre") }, // Texto que describe el campo
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón clickeable
        // onClick le dice a Kotlin: "cuando el usuario toque el botón,
        // ejecutá el bloque de código que está entre las llaves { }"
        Button(
            onClick = {
                // Todo lo que esté acá adentro se ejecuta al presionar el botón
                // El $ inserta el valor de la variable dentro del texto
                // En Python sería: mensaje = f"Hola {nombre}"
                mensaje = "Hola $nombre"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            // Lo que esté acá adentro es lo que se dibuja DENTRO del botón
            Text("Saludar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Este Text se actualiza automáticamente cuando "mensaje" cambia,
        // gracias a mutableStateOf. No hace falta llamar a ningún "refresh()"
        Text(
            text = mensaje,
            fontSize = 24.sp
        )
    }
}
