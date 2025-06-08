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
import androidx.compose.foundation.lazy.LazyColumn
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
fun CicloScreen(navController: NavHostController) {
    var showMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
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
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LogoEuroGym(navController)
            }
        }

        item {
            Text("Ciclo", fontSize = 24.sp, color = Color.White)
        }

        item {
            Image(
                painter = painterResource(id = R.drawable.claseciclo),
                contentDescription = "Imagen Ciclo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        item {
            Text(
                text = "Es un ejercicio aeróbico y cardiovascular que se realiza sobre una bicicleta estática, en la que se trabaja el tren inferiror: las piernas y los glúteos principalmente. Su finalidad es perder perso y la tonificación de los músculos, además de mejorar la fuerza y la resistencia",
                color = Color.LightGray,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }

        item {
            Text("Horario", color = Color.White, fontSize = 18.sp)
            Text("• Martes, Miércoles, Jueves – 19:00 pm", color = Color.LightGray)
            Text("• Lunes, Viernes – 18:00 pm", color = Color.LightGray)
        }

        item {
            Text("Duración", color = Color.White, fontSize = 18.sp)
            Text("• 55 minutos.", color = Color.LightGray)
        }
    }
}