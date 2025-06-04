package com.example.eurogymclass.screens.ChatBot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.ui.theme.BlueLight
import com.example.eurogymclass.ui.theme.White
import com.example.eurogymclass.utilidades.ChatBotApi

@Composable
fun ChatBotScreen(navController: NavHostController) {
    var mensaje by remember { mutableStateOf("") }
    var respuesta by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Asistente Virtual",
            fontSize = 30.sp,
            color = BlueLight,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = mensaje,
            onValueChange = { mensaje = it },
            label = { Text("Escribe tu pregunta", color = White) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BlueLight,
                focusedBorderColor = BlueLight,
                cursorColor = BlueLight,
                unfocusedLabelColor = White,
                focusedLabelColor = BlueLight,
                focusedTextColor = White,
                unfocusedTextColor = White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                cargando = true
                respuesta = ""
                ChatBotApi.enviarMensaje(mensaje) {
                    respuesta = it
                    cargando = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (cargando) {
            CircularProgressIndicator(color = BlueLight)
        } else if (respuesta.isNotBlank()) {
            Text("Respuesta:", color = White, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = respuesta,
                color = Color.LightGray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}