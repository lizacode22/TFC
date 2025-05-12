package com.example.eurogymclass.screens.clases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GapScreen(navController: NavHostController) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_icono_perfil),
                contentDescription = "Perfil",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { showMenu = true }
            )

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Ver perfil") },
                    onClick = {
                        showMenu = false
                        navController.navigate("perfil")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = {
                        showMenu = false
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("initial") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LogoEuroGym(navController)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Gap", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.gap),
            contentDescription = "Imagen Gap",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "El entrenamiento GAP es un tipo de ejercicio físico que se centra en tonificar y definir los glúteos, abdominales y piernas. GAP son las siglas en inglés de Glutes, Abs and Piernas.",
            color = Color.LightGray,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Horario", color = Color.White, fontSize = 18.sp)
        Text("• Martes, Miércoles, Jueves – 17:00 pm", color = Color.LightGray)
        Text("• Lunes, Viernes – 8:00 am", color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Duración", color = Color.White, fontSize = 18.sp)
        Text("• 45 minutos.", color = Color.LightGray)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Reservar", color = Color.White)
        }
    }
}