package com.example.eurogymclass.utilidades

import android.content.Context
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

fun cerrarSesionCompleta(context: Context, navController: NavHostController) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("1013380689733-q65uu9074hlb9uja0pq7hoti39di8sr7.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    googleSignInClient.signOut().addOnCompleteListener {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("initial") {
            popUpTo(0) { inclusive = true }
        }
    }
}