package com.example.eurogymclass.screens.auth

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.ui.theme.Black
import com.example.eurogymclass.ui.theme.SelectedField
import com.example.eurogymclass.ui.theme.UnselectedField
import com.example.eurogymclass.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    alRegistrarExito: () -> Unit,
    alPulsarVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }
    var confirmarVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Volver",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() },
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear cuenta",
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Completa tus datos",
            color = White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                    Icon(
                        imageVector = if (contrasenaVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = White
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmarContrasena,
            onValueChange = { confirmarContrasena = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (confirmarVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmarVisible = !confirmarVisible }) {
                    Icon(
                        imageVector = if (confirmarVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = White
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField,
                focusedLabelColor = White,
                unfocusedLabelColor = White
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(visible = mostrarError, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (contrasena != confirmarContrasena) {
                    mostrarError = true
                    mensajeError = "Las contraseñas no coinciden"
                    return@Button
                }

                auth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener { tarea ->
                    if (tarea.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val db = FirebaseFirestore.getInstance()

                        val usuario = hashMapOf(
                            "nombre" to nombre,
                            "apellidos" to apellidos,
                            "email" to correo,
                            "fechaRegistro" to com.google.firebase.Timestamp.now(),
                            "clasesReservadas" to emptyList<String>()
                        )

                        uid?.let {
                            db.collection("usuarios").document(it).set(usuario)
                                .addOnSuccessListener {
                                    Log.i("Firestore", "Usuario guardado correctamente")
                                    alRegistrarExito()
                                }
                                .addOnFailureListener { error ->
                                    mostrarError = true
                                    mensajeError = "Error al guardar datos del usuario"
                                    Log.e("Firestore", "Error: ${error.message}")
                                }
                        }
                    } else {
                        mostrarError = true
                        mensajeError = tarea.exception?.localizedMessage ?: "No se pudo registrar el usuario"
                        Log.e("Registro", "Error: ${tarea.exception}")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Registrar", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { alPulsarVolver() }) {
            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                color = White,
                fontSize = 14.sp
            )
        }
    }
}