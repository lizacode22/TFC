package com.example.eurogymclass.perfil

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.ui.theme.BlueLight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eurogymclass.R
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import coil.compose.AsyncImage
import com.example.eurogymclass.ui.theme.Black
import com.example.eurogymclass.utilidades.TopBar
import com.example.eurogymclass.viewmodels.UsuariosViewModel

@Composable
fun EditarPerfilScreen(
    navController: NavHostController,
    viewModel: UsuariosViewModel = viewModel()
) {
    val usuario = viewModel.usuario
    val contexto = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }

    LaunchedEffect(usuario) {
        if (usuario != null && nombre.isBlank() && apellidos.isBlank()) {
            nombre = usuario.nombre
            apellidos = usuario.apellidos
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        TopBar(navController)

        Spacer(modifier = Modifier.height(32.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            LogoEuroGym(navController)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val user = FirebaseAuth.getInstance().currentUser
            val fotoPerfil = user?.photoUrl
            val correo = user?.email ?: ""
            val inicial = correo.firstOrNull()?.uppercase() ?: "?"

            if (fotoPerfil != null) {
                AsyncImage(
                    model = fotoPerfil,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = inicial, color = Color.White, fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = correo, color = Color.LightGray, fontSize = 14.sp)
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
                if (nombre.isNotBlank() && apellidos.isNotBlank()) {
                    viewModel.actualizarDatos(nombre, apellidos)
                    Toast.makeText(contexto, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                    navController.navigate("perfil") {
                        popUpTo("editarPerfil") { inclusive = true }
                    }
                } else {
                    Toast.makeText(contexto, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BlueLight)
        ) {
            Text("Guardar cambios", color = Color.White)
        }
    }
}