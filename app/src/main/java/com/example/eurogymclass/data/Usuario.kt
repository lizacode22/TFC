package com.example.eurogymclass.data

import java.util.Date

data class Usuario(
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val uid: String = "",
    val fechaRegistro: Date = Date(),
    val clasesReservadas: List<String> = emptyList()
)