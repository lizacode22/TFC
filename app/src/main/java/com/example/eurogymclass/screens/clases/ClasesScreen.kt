package com.example.eurogymclass.screens.clases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.data.Clase
import com.example.eurogymclass.data.ClasesViewModel
import com.example.eurogymclass.ui.theme.BlueLight
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.example.eurogymclass.utilidades.cerrarSesionCompleta
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import com.example.eurogymclass.utilidades.TopBar
import java.time.LocalDateTime
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClasesScreen(
    navController: NavHostController,
    viewModel: ClasesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val clases = viewModel.clases
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    var diaSeleccionado by remember { mutableStateOf("Lunes") }
    val hoy = LocalDate.now()

    val lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val domingo = hoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val textoSemana = "Semana del ${lunes.format(formatter)} al ${domingo.format(formatter)}"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        item {
            TopBar(navController)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LogoEuroGym(navController)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = textoSemana,
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            DiaSelector(
                dias = dias,
                seleccionado = diaSeleccionado,
                onDiaSeleccionado = { diaSeleccionado = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Clases", color = Color.White, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }

        val clasesDelDia = clases
            .filter { it.clase.dia == diaSeleccionado }
            .sortedBy { it.clase.hora }

        if (clasesDelDia.isEmpty()) {
            item {
                Text(
                    "No hay clases disponibles para este día",
                    color = Color.LightGray,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
        } else {
            items(clasesDelDia) { claseConId ->
                TarjetaClase(
                    clase = claseConId.clase,
                    yaReservado = uid in claseConId.clase.usuarios,
                    onToggleReserva = {
                        viewModel.alternarReserva(claseConId.id, claseConId.clase)
                    },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun DiaSelector(
    dias: List<String>,
    seleccionado: String,
    onDiaSeleccionado: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        dias.forEach { dia ->
            Button(
                onClick = { onDiaSeleccionado(dia) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (dia == seleccionado) Color.White else Color.DarkGray
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(dia.take(1), color = if (dia == seleccionado) Color.Black else Color.White)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaClase(
    clase: Clase,
    yaReservado: Boolean,
    onToggleReserva: () -> Unit,
    navController: NavHostController
) {
    val estaLlena = clase.inscritos >= clase.capacidad
    val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    val hoy = LocalDateTime.now()
    val lunesSemana = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val indiceDiaClase = diasSemana.indexOf(clase.dia)
    val fechaClase = lunesSemana.plusDays(indiceDiaClase.toLong())

    val horaClase = try {
        LocalTime.parse(clase.hora)
    } catch (e: Exception) {
        LocalTime.MIDNIGHT
    }

    val fechaYHoraClase = LocalDateTime.of(fechaClase, horaClase)
    val claseYaPaso = fechaYHoraClase.isBefore(hoy)

    val diaSemana = when (clase.dia.lowercase()) {
        "lunes" -> DayOfWeek.MONDAY
        "martes" -> DayOfWeek.TUESDAY
        "miércoles" -> DayOfWeek.WEDNESDAY
        "jueves" -> DayOfWeek.THURSDAY
        "viernes" -> DayOfWeek.FRIDAY
        else -> null
    }

    val fechaClaseFormateada = if (claseYaPaso && diaSemana != null) {
        LocalDate.now()
            .with(TemporalAdjusters.previousOrSame(diaSemana))
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } else null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = clase.titulo,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    navController.navigate(clase.titulo.lowercase())
                }
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (claseYaPaso && fechaClaseFormateada != null) {
                Text("$fechaClaseFormateada | ${clase.hora}", color = Color.LightGray, fontSize = 16.sp)
            } else {
                Text("${clase.dia} | ${clase.hora}", color = Color.LightGray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("${clase.inscritos} / ${clase.capacidad} plazas", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onToggleReserva,
            enabled = !claseYaPaso && (!estaLlena || yaReservado),
            colors = ButtonDefaults.buttonColors(
                containerColor = when {
                    claseYaPaso -> Color.Gray
                    yaReservado -> Color.Red
                    estaLlena -> Color.Gray
                    else -> BlueLight
                }
            ),
            modifier = Modifier
                .align(Alignment.End)
                .height(48.dp)
        ) {
            Text(
                text = when {
                    claseYaPaso -> "Finalizada"
                    estaLlena && !yaReservado -> "Completa"
                    yaReservado -> "Cancelar"
                    else -> "Reservar"
                },
                color = Color.White,
                fontSize = 16.sp
            )
        }

        if (claseYaPaso) {
            Text(
                text = "Finalizada",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}