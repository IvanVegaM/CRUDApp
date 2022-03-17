package com.ivanvemat.crudapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.ivanvemat.crudapp.ui.theme.CRUDAppTheme

//Llevar la cuenta de la cantidad de usuarios que hay en la base de datos.
//Así se puede utilizar como id para tener mayor control.
var ID = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CRUDAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Navigation(this)
                }
            }
        }
    }
}