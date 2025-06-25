package com.example.eurogymclass.screens.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.ui.theme.BlueLight
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun BienvenidaScreen(
    navigateToLogin: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    navController: NavHostController
) {
    var mostrarDialogoRecuperacion by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf("") }
    var mensajeRecuperacion by remember { mutableStateOf("") }

    val contexto = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()

    val opcionesGoogle = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("1013380689733-q65uu9074hlb9uja0pq7hoti39di8sr7.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val clienteGoogle = GoogleSignIn.getClient(contexto, opcionesGoogle)

    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            val tarea = GoogleSignIn.getSignedInAccountFromIntent(resultado.data)
            try {
                val cuenta = tarea.getResult(ApiException::class.java)
                val credencial = GoogleAuthProvider.getCredential(cuenta.idToken, null)

                scope.launch {
                    val resultadoAuth = auth.signInWithCredential(credencial).await()
                    val nombreUsuario = resultadoAuth.user?.displayName ?: "Usuario"
                    Toast.makeText(contexto, "¡Bienvenido/a, $nombreUsuario!", Toast.LENGTH_SHORT).show()

                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de Eurogym",
            modifier = Modifier.size(250.dp)
        )

        if (mostrarDialogoRecuperacion) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoRecuperacion = false
                    correoRecuperacion = ""
                    mensajeRecuperacion = ""
                },
                title = { Text("Recuperar contraseña") },
                text = {
                    Column {
                        Text("Introduce tu correo y te enviaremos un enlace para restablecer tu contraseña.")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = correoRecuperacion,
                            onValueChange = { correoRecuperacion = it },
                            label = { Text("Correo electrónico") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (mensajeRecuperacion.isNotEmpty()) {
                            Text(
                                text = mensajeRecuperacion,
                                color = if ("enviado" in mensajeRecuperacion.lowercase()) Color.Green else Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (correoRecuperacion.isNotEmpty()) {
                            FirebaseAuth.getInstance()
                                .sendPasswordResetEmail(correoRecuperacion)
                                .addOnSuccessListener {
                                    mensajeRecuperacion = "Correo de recuperación enviado correctamente."
                                }
                                .addOnFailureListener {
                                    mensajeRecuperacion = "Error al enviar el correo. Verifica el email."
                                }
                        }
                    }) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        mostrarDialogoRecuperacion = false
                        correoRecuperacion = ""
                        mensajeRecuperacion = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "¡Tu club, siempre contigo!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    shadow = Shadow(
                        color = Color(0xFF42A5F5),
                        offset = Offset(2f, 2f),
                        blurRadius = 6f
                    )
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navigateToSignUp() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Regístrate gratis",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navigateToLogin() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Iniciar sesión",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            BotonGoogle(
                texto = "Continuar con Google",
                alHacerClick = {
                    val intent = clienteGoogle.signInIntent
                    lanzador.launch(intent)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "¿Olvidaste tu contraseña? Recupérala",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    mostrarDialogoRecuperacion = true
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun BotonGoogle(texto: String, alHacerClick: () -> Unit) {
    Button(
        onClick = alHacerClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Logo Google",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = texto,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


