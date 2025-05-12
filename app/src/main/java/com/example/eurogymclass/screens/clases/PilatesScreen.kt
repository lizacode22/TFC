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
fun PilatesScreen(navController: NavHostController) {
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

        Text("Pilates", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.pilates),
            contentDescription = "Imagen Pilates",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pilates es un gran programa de ejercicios para desarrollar el control. La respiración profunda y constante es esencial para un movimiento fluido, un equilibrio muscular adecuado y una salud general. Este es el principio de la respiración controlada y es uno de los aspectos centrales de Pilates.",
            color = Color.LightGray,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Horario", color = Color.White, fontSize = 18.sp)
        Text("• Martes, Miércoles, Jueves – 18:00 pm", color = Color.LightGray)
        Text("• Lunes, Viernes – 9:00 am", color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Duración", color = Color.White, fontSize = 18.sp)
        Text("• 50 minutos.", color = Color.LightGray)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Reservar", color = Color.White)
        }
    }
}