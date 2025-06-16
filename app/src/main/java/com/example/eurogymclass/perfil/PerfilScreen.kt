package com.example.eurogymclass.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import coil.compose.AsyncImage
import com.example.eurogymclass.utilidades.TopBar

@Composable
fun PerfilScreen(navHostController: NavHostController) {
    val usuario = FirebaseAuth.getInstance().currentUser
    val contexto = LocalContext.current


    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("1013380689733-q65uu9074hlb9uja0pq7hoti39di8sr7.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(contexto, gso)

    val correo = usuario?.email ?: ""
    val inicial = correo.firstOrNull()?.uppercase() ?: "?"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 40.dp)
    )  {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { TopBar(navHostController) }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LogoEuroGym(navHostController)
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            if (usuario?.photoUrl != null) {
                AsyncImage(
                    model = usuario.photoUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = inicial,
                        color = Color.White,
                        fontSize = 32.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Text(
                text = usuario?.email ?: "email@ejemplo.com",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item { PerfilOption("Editar Perfil") { navHostController.navigate("editarPerfil") } }
        item { PerfilOption("Próximas clases") { navHostController.navigate("clases") } }
        item { PerfilOption("Historial de reservas") { navHostController.navigate("historialReservas") } }
        item { PerfilOption("Notificaciones") { navHostController.navigate("avisos") } }
        item { PerfilOption("Asistente virtual") { navHostController.navigate("chatbot") } }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            Button(
                onClick = {
                    googleSignInClient.signOut().addOnCompleteListener {
                        FirebaseAuth.getInstance().signOut()
                        navHostController.navigate("initial") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }
    }
}

@Composable
fun PerfilOption(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, color = Color.White, fontSize = 16.sp)
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}