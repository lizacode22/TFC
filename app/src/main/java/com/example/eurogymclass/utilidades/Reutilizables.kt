package com.example.eurogymclass.utilidades

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun TopBar(navController: NavHostController) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

        Spacer(modifier = Modifier.weight(1f))

        Box {
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
                        cerrarSesionCompleta(context, navController)
                    }
                )
            }
        }
    }
}