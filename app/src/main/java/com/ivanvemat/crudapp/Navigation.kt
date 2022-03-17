package com.ivanvemat.crudapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ivanvemat.crudapp.ui.screens.Connect
import com.ivanvemat.crudapp.ui.screens.Detail

@Composable
fun Navigation(context: Context) {
    val navController = rememberNavController()
    val db = Firebase.firestore

    NavHost(
        navController = navController,
        startDestination = Destinations.Main.route
    ) {
        composable(
            route = Destinations.Main.route
        ) {
            Connect(
                db = db,
                navController = navController,
                context
            )
        }
        composable(
            route = Destinations.DetailCreate.route
        ) {
            Detail(
                navController = navController,
                db = db,
                context = context
            )
        }
        composable(
            route = Destinations.DetailModify.route
        ){
            Detail(
                navController = navController,
                db = db,
                context = context,
                userReceived = "Chucho",
                emailReceived = "Chanclas@chanclas.com",
                descriptionReceived = "Unas grandes chanclas",
                extra = true
            )
        }
    }
}