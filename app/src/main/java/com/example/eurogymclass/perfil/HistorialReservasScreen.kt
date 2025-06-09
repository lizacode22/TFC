package com.example.eurogymclass.perfil

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eurogymclass.ui.theme.BlueLight
import com.example.eurogymclass.utilidades.TopBar
import java.time.LocalDate
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistorialReservasScreen(
    navController: NavHostController,
    viewModel: ClasesViewModel = viewModel()
) {
    val clasesReservadas = viewModel.clasesReservadasPorUsuario()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    val ahora = LocalDate.now().atTime(java.time.LocalTime.now())

    val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    val lunesSemana = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))

    val (clasesFuturas, clasesPasadas) = clasesReservadas.partition { claseConId ->
        try {
            val indiceDia = diasSemana.indexOf(claseConId.clase.dia)
            if (indiceDia == -1) return@partition false

            val fechaClase = lunesSemana.plusDays(indiceDia.toLong())
            val hora = claseConId.clase.hora.split(":").map { it.toInt() }
            val fechaHora = fechaClase.atTime(hora[0], hora[1])
            fechaHora.isAfter(ahora)
        } catch (e: Exception) {
            false
    }
}

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

        if (clasesFuturas.isEmpty()) {
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
                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            item {
                Text("Próximas clases", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(clasesFuturas) { claseConId ->
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

        if (clasesPasadas.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text("Clases anteriores", color = Color.Gray, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(clasesPasadas) { claseConId ->
                ClaseCard(
                    clase = claseConId.clase,
                    yaReservado = false,
                    onToggleReserva = {},
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
