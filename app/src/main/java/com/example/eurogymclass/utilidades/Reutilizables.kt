package com.example.eurogymclass.utilidades

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R


@Composable
fun LogoEuroGym(navHostController: NavHostController) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clickable {
                navHostController.navigate("home") {
                    popUpTo("home") { inclusive = true } //evita que se acumulen pantallas en el backstack.
                }
            }
    )
}


@Composable
fun TopBar(
    navController: NavHostController,
    mostrarVolver: Boolean = true,
    titulo: String? = null
) {
    var menuVisible by remember { mutableStateOf(false) }
    val contexto = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (mostrarVolver) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
        } else {
            Spacer(modifier = Modifier.width(24.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        titulo?.let {
            Text(
                text = it,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Box {
            Icon(
                painter = painterResource(id = R.drawable.ic_icono_perfil),
                contentDescription = "Perfil",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { menuVisible = true }
            )

            DropdownMenu(
                expanded = menuVisible,
                onDismissRequest = { menuVisible = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Ver perfil") },
                    onClick = {
                        menuVisible = false
                        navController.navigate("perfil")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Asistente") },
                    onClick = {
                        menuVisible = false
                        navController.navigate("chatbot")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = {
                        menuVisible = false
                        cerrarSesionCompleta(contexto, navController)
                    }
                )
            }
        }
    }
}
