package com.example.eurogymclass.screens.ChatBot

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eurogymclass.utilidades.ChatBotApi

@Composable
fun ChatBotScreen(navController: NavHostController) {
    var mensaje by remember { mutableStateOf("") }
    var respuesta by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Asistente Virtual EuroGymclass",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = mensaje,
            onValueChange = { mensaje = it },
            label = { Text("Escribe la duda que tengas!") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                cargando = true
                respuesta = ""
                ChatBotApi.enviarMensaje(mensaje) {
                    respuesta = it
                    cargando = false
                }
            },
            enabled = mensaje.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else if (respuesta.isNotBlank()) {
            Text(
                text = "Respuesta:",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = respuesta,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}