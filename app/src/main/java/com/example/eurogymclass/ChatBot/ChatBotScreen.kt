package com.example.eurogymclass.ChatBot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.example.eurogymclass.utilidades.TopBar

@Composable
fun ChatBotScreen(navController: NavHostController) {
    var mensaje by remember { mutableStateOf("") }
    var respuesta by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val scrollEstado = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollEstado)
            .background(Color.Black)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navController)

        Spacer(modifier = Modifier.height(24.dp))

        LogoEuroGym(navController)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Asistente Virtual",
            fontSize = 22.sp,
            color = BlueLight,
            fontWeight = FontWeight.Bold
        )

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
                ChatBotApiFinal.enviarMensaje(mensaje) {
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
