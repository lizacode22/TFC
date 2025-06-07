package com.example.eurogymclass.ChatBot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.navigate("perfil") {
                            popUpTo("chatbot") { inclusive = true }
                        }
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Asistente Virtual",
                fontSize = 22.sp,
                color = BlueLight,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

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