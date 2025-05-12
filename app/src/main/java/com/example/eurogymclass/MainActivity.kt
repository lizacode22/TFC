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


class MainActivity : ComponentActivity() {

    private lateinit var navHostController :NavHostController
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        auth = Firebase.auth

        val db = Firebase.firestore

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




