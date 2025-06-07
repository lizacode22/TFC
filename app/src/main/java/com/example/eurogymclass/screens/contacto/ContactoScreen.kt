package com.example.eurogymclass.screens.contacto

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R
import com.example.eurogymclass.utilidades.LogoEuroGym
import com.example.eurogymclass.utilidades.TopBar

@Composable
fun ContactoScreen(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
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

        Text("Contacto", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(24.dp))

        ContactInfoRow(Icons.Filled.Place, "Calle Pintores, 6\n28923 Alcorcón, Madrid")
        Text("Horario", color = Color.White, fontSize = 18.sp)
        ContactInfoRow(Icons.Filled.Info, "Lunes a Viernes: 7:00 AM - 23:00 PM")
        ContactInfoRow(Icons.Filled.Info, "Sábados y Domingos: 8:00 AM - 14:30 PM")
        ContactInfoRow(Icons.Filled.Phone, "+34 625 68 83 23")
        ContactInfoRow(Icons.Filled.Email, "recepcion@euroindoorpadel.com")

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.mapa_location),
            contentDescription = "Mapa",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=EuroGym+Alcorcón")
                    )
                    context.startActivity(intent)
                }
        )
    }
}

@Composable
fun ContactInfoRow(icon: ImageVector, text: String) {
    val context = LocalContext.current
    val isEmail = text.contains("@")
    val isPhone = text.startsWith("+34")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = isEmail || isPhone) {
                when {
                    isEmail -> {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$text")
                        }
                        context.startActivity(intent)
                    }

                    isPhone -> {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$text")
                        }
                        context.startActivity(intent)
                    }
                }
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1E90FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = Color.White, fontSize = 16.sp)
    }
}