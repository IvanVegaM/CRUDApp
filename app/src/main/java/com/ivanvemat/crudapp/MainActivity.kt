package com.ivanvemat.crudapp

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ivanvemat.crudapp.ui.theme.CRUDAppTheme
import java.util.stream.IntStream.range

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        setContent {
            CRUDAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Connect(db)
                }
            }
        }
    }
}

@Composable
fun Connect(db: FirebaseFirestore) {

    var visible by remember{ mutableStateOf(false)}
    var text by remember{ mutableStateOf("") }
    var temp by remember{ mutableStateOf("") }
    var id by remember{ mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                onClick = {
                    // Create a new user with a first and last name
                    val user = hashMapOf(
                        "first" to "Ada",
                        "last" to "Lovelace",
                        "born" to 1815
                    )

                    id = "1"

                    // Add a new document with a generated ID
                    db.collection("users")
                        .document(id)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            ) {
                Text(
                    text = "Connect One",
                    fontSize = 35.sp
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                onClick = {
                    // Create a new user with a first and last name
                    val user = hashMapOf(
                        "first" to "Alan",
                        "middle" to "Mathison",
                        "last" to "Turing",
                        "born" to 1912
                    )

                    id = "2"

                    // Add a new document with a generated ID
                    db.collection("users")
                        .document(id)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
            ) {
                Text(
                    text = "Connect Two",
                    fontSize = 35.sp
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                onClick = {
                    // Add a new document with a generated ID
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            temp = ""
                            for(documents in result) {
                                temp += "${documents.id}: ${documents.data} \n"
                            }
                            visible = true
                            text = temp
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
            ) {
                Text(
                    text = "Fetch",
                    fontSize = 35.sp
                )
            }
        }

        if(visible) {
            Text(
                text = text,
                fontSize = 20.sp
            )
        }
    }
}