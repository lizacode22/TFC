package com.example.eurogymclass.avisos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.ui.theme.Black
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.example.eurogymclass.viewmodels.AvisosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.eurogymclass.utilidades.TopBar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AvisosScreen(
    navController: NavHostController,
    viewModel: AvisosViewModel = viewModel()
) {
    val avisos by viewModel.avisos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        TopBar(navController)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LogoEuroGym(navController)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("CLASES", color = Color.Gray, fontSize = 16.sp,
                modifier = Modifier.clickable { navController.navigate("clases") })
            Text("AVISOS", color = Color.White, fontSize = 16.sp)
            Text("CONTACTO", color = Color.Gray, fontSize = 16.sp,
                modifier = Modifier.clickable { navController.navigate("contacto") })
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Avisos",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(avisos) { aviso ->
                Column {
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    Text(
                        text = formatoFecha.format(aviso.fecha),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = aviso.titulo.ifEmpty { "Título no disponible" },
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = aviso.descripcion.ifEmpty { "Descripción no disponible" },
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}