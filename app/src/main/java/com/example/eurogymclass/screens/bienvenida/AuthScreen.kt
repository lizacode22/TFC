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
import com.example.eurogymclass.screens.auth.LoginScreen
import com.example.eurogymclass.screens.auth.SignUpScreen


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
                onLoginSuccess = { onAuthSuccess() },
                onNavigateToSignUp = { isLoginScreen = false }
            )
        } else {
            SignUpScreen(
                navController = navController,
                auth = auth,
                onSignUpSuccess = { onAuthSuccess() },
                onBackPressed = { isLoginScreen = true }
            )
        }
    }
}