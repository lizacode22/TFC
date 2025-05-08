package com.example.eurogymclass.screens.auth

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eurogymclass.R
import com.example.eurogymclass.ui.theme.BlueLight


@Preview(showBackground = true)
@Composable
fun BienvenidaScreen(
    navigateToLogin: () -> Unit = {},
    navigateToSignUp: () -> Unit = {}
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var resetMessage by remember { mutableStateOf("") }

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

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = {
                    showResetDialog = false
                    resetEmail = ""
                    resetMessage = ""
                },
                title = { Text("Recuperar contraseña") },
                text = {
                    Column {
                        Text("Introduce tu correo y recibirás un enlace para restablecer tu contraseña.")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Correo electrónico") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (resetMessage.isNotEmpty()) {
                            Text(
                                text = resetMessage,
                                color = if ("enviado" in resetMessage.lowercase()) Color.Green else Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (resetEmail.isNotEmpty()) {
                            com.google.firebase.auth.FirebaseAuth.getInstance()
                                .sendPasswordResetEmail(resetEmail)
                                .addOnSuccessListener {
                                    resetMessage = "Correo de recuperación enviado."
                                }
                                .addOnFailureListener {
                                    resetMessage = "Error: correo inválido o no registrado."
                                }
                        }
                    }) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showResetDialog = false
                        resetEmail = ""
                        resetMessage = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Tu gimansio en tu bolsillo",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    shadow = Shadow(
                        color = Color(0xFF42A5F5), // Azul clarito
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
                onClick = { navigateToSignUp() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { navigateToLogin() }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            GoogleButton(
                title = "Continuar con Google",
                onClick = { /* TODO: Acción login Google */ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "¿Olvidaste la contraseña? Recupérala",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    showResetDialog = true
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun GoogleButton(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

