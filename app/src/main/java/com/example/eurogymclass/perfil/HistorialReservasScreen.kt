package com.example.eurogymclass.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.data.ClasesViewModel
import com.example.eurogymclass.screens.clases.ClaseCard
import com.example.eurogymclass.screens.clases.TopBar
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eurogymclass.ui.theme.BlueLight

@Composable
fun HistorialReservasScreen(
    navController: NavHostController,
    viewModel: ClasesViewModel = viewModel()
) {
    val clasesReservadas = viewModel.clasesReservadasPorUsuario()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        item {
            TopBar(navController)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Mis reservas", color = Color.White, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (clasesReservadas.isEmpty()) {
            item {
                Text(
                    text = "No estás apuntado a ninguna clase.",
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("clases") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueLight)
                ) {
                    Text("Ver clases disponibles")
                }
            }
        } else {
            items(clasesReservadas) { claseConId ->
                ClaseCard(
                    clase = claseConId.clase,
                    yaReservado = uid in claseConId.clase.usuarios,
                    onToggleReserva = {
                        viewModel.toggleReserva(claseConId.id, claseConId.clase)
                    },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}
