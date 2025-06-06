package com.example.eurogymclass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eurogymclass.screens.bienvenida.AuthScreen
import com.example.eurogymclass.screens.auth.BienvenidaScreen
import com.example.eurogymclass.screens.bienvenida.HomeScreen
import com.example.eurogymclass.screens.clases.CicloScreen
import com.example.eurogymclass.screens.clases.ClasesScreen
import com.example.eurogymclass.screens.clases.CrossgymScreen
import com.example.eurogymclass.screens.clases.FuncionalScreen
import com.example.eurogymclass.screens.clases.GapScreen
import com.example.eurogymclass.screens.clases.PilatesScreen
import com.example.eurogymclass.avisos.AvisosScreen
import com.example.eurogymclass.screens.contacto.ContactoScreen
import com.example.eurogymclass.perfil.EditarPerfilScreen
import com.example.eurogymclass.perfil.HistorialReservasScreen
import com.example.eurogymclass.perfil.PerfilScreen
import com.example.eurogymclass.ChatBot.ChatBotScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navHostController: NavHostController, auth: FirebaseAuth) {
    NavHost(navController = navHostController, startDestination = "initial") {

        composable("initial") {
            BienvenidaScreen(
                navigateToLogin = { navHostController.navigate("auth/true") }, //true para login
                navigateToSignUp = { navHostController.navigate("auth/false") }, //false para registro
                navController = navHostController
            )
        }
        composable(
            route = "auth/{startInLogin}",
            arguments = listOf(
                navArgument("startInLogin") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val startInLogin = backStackEntry.arguments?.getBoolean("startInLogin") ?: true
            AuthScreen(
                navController = navHostController,
                auth = auth,
                startInLogin = startInLogin,
                onAuthSuccess = { navHostController.navigate("home") }
            )
        }
        composable("home") { HomeScreen(navHostController) }

        composable("clases") { ClasesScreen(navHostController) }

        composable("avisos") { AvisosScreen(navHostController) }

        composable("contacto") { ContactoScreen(navHostController) }

        composable("perfil") { PerfilScreen(navHostController) }

        composable("pilates") { PilatesScreen(navHostController) }

        composable("ciclo") { CicloScreen(navHostController) }

        composable("gap") { GapScreen(navHostController) }

        composable("funcional") { FuncionalScreen(navHostController) }

        composable("crossgym") { CrossgymScreen(navHostController) }

        composable("historialReservas") { HistorialReservasScreen(navHostController) }

        composable("editarPerfil") { EditarPerfilScreen(navHostController) }

        composable("chatbot") { ChatBotScreen(navHostController) }
    }
}