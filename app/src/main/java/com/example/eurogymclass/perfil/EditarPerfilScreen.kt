package com.example.eurogymclass.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.viewmodels.UsuariosViewModel
import com.example.eurogymclass.ui.theme.BlueLight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eurogymclass.R
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditarPerfilScreen(
    navController: NavHostController,
    viewModel: UsuariosViewModel = viewModel()
) {
    val usuario = viewModel.usuario

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(usuario) {
        nombre = usuario?.nombre ?: ""
        apellidos = usuario?.apellidos ?: ""
    }

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
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("initial") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
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

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagenperfil),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
                    .clickable {  }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = FirebaseAuth.getInstance().currentUser?.email ?: "correo@example.com",
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Editar perfil", color = Color.White, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.actualizarDatos(nombre, apellidos)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BlueLight)
        ) {
            Text("Guardar cambios", color = Color.White)
        }
    }
}
