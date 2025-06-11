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
    startInLogin: Boolean,
    onAuthSuccess: () -> Unit
) {
    var isLoginScreen by remember { mutableStateOf(startInLogin) }

    AnimatedContent(
        targetState = isLoginScreen,
        transitionSpec = {
            if (targetState) {
                slideInHorizontally { -it } + fadeIn() with slideOutHorizontally { it } + fadeOut()
            } else {
                slideInHorizontally { it } + fadeIn() with slideOutHorizontally { -it } + fadeOut()
            }
        }
    ) { screen ->
        if (screen) {
            LoginScreen(
                navController = navController,
                auth = auth,
                onLoginSuccess = {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        crearUsuarioSiNoExiste(user)
                    }
                    onAuthSuccess()
                },
                onNavigateToSignUp = { isLoginScreen = false }
            )
        } else {
            SignUpScreen(
                navController = navController,
                auth = auth,
                onSignUpSuccess = {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        crearUsuarioSiNoExiste(user)
                    }
                    onAuthSuccess()
                },
                onBackPressed = { isLoginScreen = true }
            )
        }
    }
}


fun crearUsuarioSiNoExiste(user: FirebaseUser) {
    val db = FirebaseFirestore.getInstance()
    val uid = user.uid
    val userRef = db.collection("usuarios").document(uid)

    userRef.get().addOnSuccessListener { document ->
        if (!document.exists()) {
            val nuevoUsuario = Usuario(
                nombre = user.displayName ?: "",
                apellidos = "",
                email = user.email ?: "",
                uid = uid,
                fechaRegistro = Date(),
                clasesReservadas = emptyList()
            )
            userRef.set(nuevoUsuario)
        }
    }
}