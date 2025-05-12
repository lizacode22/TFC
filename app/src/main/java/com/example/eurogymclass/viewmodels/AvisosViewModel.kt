package com.example.eurogymclass.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log

data class Aviso(val fecha: String = "", val titulo: String = "", val descripcion: String = "")

class AvisosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    val avisos: StateFlow<List<Aviso>> = _avisos

    init {
        fetchAvisos()
    }

    private fun fetchAvisos() {
        db.collection("avisos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("AvisosDebug", "Error al cargar avisos", exception)
                    _avisos.value = listOf(
                        Aviso(
                            fecha = "Error",
                            titulo = "No se pudieron cargar los avisos",
                            descripcion = "Intenta nuevamente más tarde"
                        )
                    )
                    return@addSnapshotListener
                }

                Log.d("AvisosDebug", "📦 Avisos recibidos: ${snapshot?.documents?.size}")

                val lista = snapshot?.documents?.mapNotNull { document ->
                    val descripcion = document.getString("descripcion") ?: return@mapNotNull null
                    val fechaTimestamp = document.getTimestamp("fecha") ?: return@mapNotNull null
                    val fecha = fechaTimestamp.toDate().toString()
                    val titulo = document.getString("titulo") ?: return@mapNotNull null

                    Aviso(
                        fecha = fecha,
                        titulo = titulo,
                        descripcion = descripcion
                    )
                }?.sortedByDescending { it.fecha } ?: emptyList()

                _avisos.value = lista
            }
    }

}