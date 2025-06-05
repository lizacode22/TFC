package com.example.eurogymclass.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.eurogymclass.R
import com.google.firebase.auth.FirebaseAuth
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import coil.compose.AsyncImage
import com.example.eurogymclass.utilidades.TopBar
import com.example.eurogymclass.utilidades.defaultScreenPadding

@Composable
fun PerfilScreen(navHostController: NavHostController) {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current


    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("1013380689733-q65uu9074hlb9uja0pq7hoti39di8sr7.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TopBar(navHostController)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LogoEuroGym(navHostController)
        }

        Spacer(modifier = Modifier.height(32.dp))

        val user = FirebaseAuth.getInstance().currentUser
        val correo = user?.email ?: ""
        val inicial = correo.firstOrNull()?.uppercase() ?: "?"

        if (user?.photoUrl != null) {
            AsyncImage(
                model = user.photoUrl,
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user?.email ?: "email@ejemplo.com",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        PerfilOption(text = "Editar Perfil") {
            navHostController.navigate("editarPerfil")
        }

        PerfilOption(text = "Próximas clases") {
            navHostController.navigate("clases")
        }

        PerfilOption(text = "Historial de reservas") {
            navHostController.navigate("historialReservas")
        }

        PerfilOption(text = "Notificaciones") {
            navHostController.navigate("avisos")
        }

        PerfilOption(text = "Asistente virtual") {
            navHostController.navigate("chatbot")
        }

        Spacer(modifier = Modifier.height(32.dp))

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
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
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