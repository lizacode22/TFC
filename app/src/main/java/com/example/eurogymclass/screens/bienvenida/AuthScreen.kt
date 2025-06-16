package com.example.eurogymclass.screens.bienvenida

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.eurogymclass.data.Usuario
import com.example.eurogymclass.screens.auth.LoginScreen
import com.example.eurogymclass.screens.auth.SignUpScreen
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    iniciarEnLogin: Boolean,
    alAutenticarse: () -> Unit
) {
    var mostrarPantallaLogin by remember { mutableStateOf(iniciarEnLogin) }

    AnimatedContent(
        targetState = mostrarPantallaLogin,
        transitionSpec = {
            if (targetState) {
                slideInHorizontally { -it } + fadeIn() with slideOutHorizontally { it } + fadeOut()
            } else {
                slideInHorizontally { it } + fadeIn() with slideOutHorizontally { -it } + fadeOut()
            }
        }
    ) { esLogin ->
        if (esLogin) {
            LoginScreen(
                navController = navController,
                auth = auth,
                onLoginSuccess = {
                    val usuarioActual = FirebaseAuth.getInstance().currentUser
                    usuarioActual?.let { crearUsuarioSiNoExiste(it) }
                    alAutenticarse()
                },
                onNavigateToSignUp = { mostrarPantallaLogin = false }
            )
        } else {
            SignUpScreen(
                navController = navController,
                auth = auth,
                alRegistrarExito = {
                    val usuarioActual = FirebaseAuth.getInstance().currentUser
                    usuarioActual?.let { crearUsuarioSiNoExiste(it) }
                    alAutenticarse()
                },
                alPulsarVolver = { mostrarPantallaLogin = true }
            )
        }
    }
}

fun crearUsuarioSiNoExiste(usuario: FirebaseUser) {
    val db = FirebaseFirestore.getInstance()
    val uid = usuario.uid
    val referencia = db.collection("usuarios").document(uid)

    referencia.get().addOnSuccessListener { documento ->
        if (!documento.exists()) {
            val nuevo = Usuario(
                nombre = usuario.displayName ?: "",
                apellidos = "",
                email = usuario.email ?: "",
                uid = uid,
                fechaRegistro = Date(),
                clasesReservadas = emptyList()
            )
            referencia.set(nuevo)
        }
    }
}