package com.ivanvemat.crudapp.ui.screens

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.ivanvemat.crudapp.Destinations
import com.ivanvemat.crudapp.ID

@Composable
fun Connect(db: FirebaseFirestore, navController: NavController, context: Context) {

    var visible by remember{ mutableStateOf(false) }
    var data by remember{ mutableStateOf("") }
    var temp by remember{ mutableStateOf("") }
    var firstTime by remember{ mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    if(firstTime) {
                        navController.navigate(Destinations.DetailCreate.route)
                    } else {
                        Toast.makeText(context, "First fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    text = "Create",
                    fontSize = 32.sp
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    if(firstTime) {
                        navController.navigate(Destinations.DetailModify.route)
                    } else {
                        Toast.makeText(context, "First fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    text = "Modify",
                    fontSize = 32.sp
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    if(visible) {
                        ID--

                        db.collection("users")
                            .document((ID+1).toString())
                            .delete()
                            .addOnSuccessListener { _ ->

                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error deleting document", e)
                            }

                    }
                    else {
                        Toast.makeText(context, "First fetch data", Toast.LENGTH_SHORT).show()
                    }

                }
            ) {
                Text(
                    text = "Delete",
                    fontSize = 32.sp
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    firstTime = true
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            temp = ""
                            ID = 0
                            for(documents in result) {
                                temp += "${documents.id}: ${documents.data} \n"
                                ID++
                            }
                            data = temp
                            visible = data.isNotEmpty()
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error fetching collection", e)
                        }
                }
            ) {
                Text(
                    text = "Fetch",
                    fontSize = 32.sp
                )
            }
        }

        AnimatedVisibility(visible) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = data,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}