package com.example.eurogymclass.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var mostrarDialogoRecuperacion by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf("") }
    var mensajeRecuperacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "¡Bienvenido de nuevo!",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = BlueLight,
                cursorColor = BlueLight
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (contrasenaVisible)
                    Icons.Default.VisibilityOff
                else
                    Icons.Default.Visibility

                IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = BlueLight
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = BlueLight,
                cursorColor = BlueLight
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = BlueLight,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { mostrarDialogoRecuperacion  = true }
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (correo.isEmpty() || contrasena.isEmpty()) {
                    mostrarError = true
                    mensajeError = "El correo electrónico y la contraseña no pueden estar vacíos!"
                    return@Button
                }

                auth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onLoginSuccess()
                    } else {
                        mostrarError  = true
                        val firebaseError = task.exception?.localizedMessage?.lowercase() ?: ""

                        mensajeError = when {
                            "password is invalid" in firebaseError -> "La contraseña es incorrecta."
                            "no user record" in firebaseError -> "No existe ninguna cuenta registrada con ese correo."
                            "auth credential is incorrect" in firebaseError -> "Las credenciales son incorrectas o han caducado."
                            "badly formatted" in firebaseError -> "El formato del correo es inválido."
                            else -> "Error al iniciar sesión. Revisa los datos introducidos."
                        }

                        Log.e("Login", "Error: ${task.exception}")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueLight)
        ) {
            Text(
                text = "Iniciar sesión",
                color = Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (mostrarError) {
            Text(
                text = mensajeError,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Text(
            text = "¿No tienes cuenta? Regístrate",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onNavigateToSignUp() }
        )

        if (mostrarDialogoRecuperacion) {
            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoRecuperacion = false
                    correoRecuperacion  = ""
                    mensajeRecuperacion = ""
                },
                title = { Text("Restablecer contraseña") },
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
                            FirebaseAuth.getInstance().sendPasswordResetEmail(correoRecuperacion)
                                .addOnSuccessListener {
                                    mensajeRecuperacion  = "Correo de recuperación enviado."
                                }
                                .addOnFailureListener {
                                    mensajeRecuperacion  = "Error al enviar el correo. Verifica el email."
                                }
                        }
                    }) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        mostrarDialogoRecuperacion  = false
                        correoRecuperacion = ""
                        mensajeRecuperacion  = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }

    }
}