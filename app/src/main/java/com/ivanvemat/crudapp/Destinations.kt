package com.ivanvemat.crudapp

sealed class Destinations(val route: String){
    object Main: Destinations("main_screen")
    object DetailCreate: Destinations("detail_create_screen")
    object DetailModify: Destinations("detail_modify_screen")
}
