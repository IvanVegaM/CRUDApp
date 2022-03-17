package com.ivanvemat.crudapp.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.ivanvemat.crudapp.Destinations
import com.ivanvemat.crudapp.ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.stream.IntStream.range

@Composable
fun Detail(
    navController: NavController,
    db: FirebaseFirestore,
    context: Context,
    userReceived: String = "",
    emailReceived: String = "",
    descriptionReceived: String = "",
    extra:Boolean = false
) {
    var user by remember { mutableStateOf(userReceived) }
    var email by remember { mutableStateOf(emailReceived) }
    var description by remember{ mutableStateOf(descriptionReceived) }
    var showDialog by remember{ mutableStateOf(extra) }
    var modified by remember{ mutableStateOf(false) }

    if(showDialog){
        ModifyDialog(
            onDismissRequest = { showDialog = false },
            onClickSave = {
                db.collection("users")
                    .document(ID.toString())
                    .get()
                    .addOnSuccessListener { document ->
                        user = document["user"].toString()
                        email = document["email"].toString()
                        description = document["description"].toString()
                    }
                showDialog = false
                modified = true
            },
            onClickDismiss = {
                showDialog = false
                navController.navigate(Destinations.Main.route)
            },
            context = context,
            navController = navController
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, end = 10.dp, start = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            modifier = Modifier.fillMaxWidth(.9f),
            value = user,
            onValueChange = {
                user = it
            },
            placeholder = {
                Text("User")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(.9f),
            value = email,
            onValueChange = {
                email = it
            },
            placeholder = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(.9f),
            value = description,
            onValueChange = {
                description = it
            },
            singleLine = true,
            placeholder = {
                Text("Description")
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    if(user.isNotEmpty() && email.isNotEmpty() && description.isNotEmpty()) {
                        val userDB = hashMapOf(
                            "user" to user,
                            "email" to email,
                            "description" to description
                        )

                        if(!modified){
                            ID++
                        }

                        db.collection("users")
                            .document(ID.toString())
                            .set(userDB)
                            .addOnSuccessListener { _ ->
                                Log.d(ContentValues.TAG, "User added successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding user", e)
                            }
                        navController.navigate(Destinations.Main.route)
                    }
                    else {
                        Toast.makeText(context, "Fill every text field", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
            }

            Text(
                text = "Save",
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ModifyDialog(
    onDismissRequest: () -> Unit,
    onClickSave: () -> Unit,
    onClickDismiss: () -> Unit,
    context: Context,
    navController: NavController
) {
    var textDialog by remember{ mutableStateOf("") }
    var found by remember{ mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .height(150.dp)
                .width(300.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colors.primary
        ){
            Column(
                modifier = Modifier.padding(10.dp)
            ){
                Text("Enter the id of the user you want to change:")
                TextField(
                    value = textDialog,
                    onValueChange = {
                        textDialog = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {

                            for(number in 0..ID){
                                println(number)
                                found = textDialog.toInt() == number

                                if(found) break
                            }

                            if(found) {
                                ID = textDialog.toInt()
                                Log.d("LOL", "se encuentra")
                            }
                            else {
                                Log.d("LOL", "No se encuentra")
                                keyboardController?.hide()
                                navController.navigate(Destinations.Main.route)
                                CoroutineScope(Dispatchers.Main).launch{
                                    delay(250)
                                    Toast.makeText(context, "No existe ese id", Toast.LENGTH_SHORT).show()
                                }
                            }
                            onClickSave()
                        }
                    ) {
                        Text(
                            "Save",
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                    TextButton(
                        onClick = onClickDismiss
                    ) {
                        Text(
                            "Cancel",
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
            }
        }
    }
}