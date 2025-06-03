package com.example.eurogymclass

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.eurogymclass.ui.theme.EurogymClassTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessaging
import android.app.NotificationChannel
import android.app.NotificationManager


class MainActivity : ComponentActivity() {

    private lateinit var navHostController :NavHostController
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        auth = Firebase.auth

        val db = Firebase.firestore

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channelName = "Avisos EuroGym"
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = android.app.NotificationChannel(channelId, channelName, importance).apply {
                description = "Canal para notificaciones push de la app EuroGym"
            }

            val notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }

        //Con esto conseguimos que cualquier usuario que abra la app (con sesión iniciada) quede suscrito al topic
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", "Token: $token")

                FirebaseMessaging.getInstance().subscribeToTopic("avisos")
                    .addOnCompleteListener { topicTask ->
                        if (topicTask.isSuccessful) {
                            Log.d("FCM_TOPIC", "Suscripción al topic 'avisos' exitosa")
                        } else {
                            Log.w("FCM_TOPIC", "Error al suscribirse al topic 'avisos'", topicTask.exception)
                        }
                    }

            } else {
                Log.w("FCM_TOKEN", "No se pudo obtener el token", task.exception)
            }
        }


        setContent {
            navHostController = rememberNavController()
            EurogymClassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(navHostController, auth)
                }
            }
        }
    }
}




