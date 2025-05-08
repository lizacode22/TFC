package com.example.eurogymclass.utilidades

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eurogymclass.R

@Composable
fun LogoEuroGym(navHostController: NavHostController) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clickable {
                navHostController.navigate("home") {
                    popUpTo("home") { inclusive = true } //evita que se acumulen pantallas en el backstack.
                }
            }
    )
}