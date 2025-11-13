package com.example.myapplication.NavController

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screens.*

@Composable
fun NavController() {
    val navController = rememberNavController()

    // Estados persistentes de la app
    var alarmaActiva by remember { mutableStateOf(true) }
    var puertaPrincipalAbierta by remember { mutableStateOf(false) }
    var ventanaPrincipalAbierta by remember { mutableStateOf(false) }
    var puertaHabitacionAbierta by remember { mutableStateOf(false) }
    var notificaciones by remember { mutableStateOf(listOf<Notificacion>()) }

    fun agregarNotificacion(nueva: Notificacion) {
        notificaciones = notificaciones + nueva
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("principal") {
            PrincipalScreen(
                navController = navController,
                alarmaActiva = alarmaActiva,
                onAlarmaChange = { nueva -> alarmaActiva = nueva },
                puertaPrincipalAbierta = puertaPrincipalAbierta,
                onPuertaPrincipalChange = { nueva -> puertaPrincipalAbierta = nueva },
                ventanaPrincipalAbierta = ventanaPrincipalAbierta,
                onVentanaPrincipalChange = { nueva -> ventanaPrincipalAbierta = nueva },
                puertaHabitacionAbierta = puertaHabitacionAbierta,
                onPuertaHabitacionChange = { nueva -> puertaHabitacionAbierta = nueva },
                notificaciones = notificaciones,
                agregarNotificacion = { agregarNotificacion(it) }
            )
        }
        composable("segunda") {
            SegundaScreen(
                navController = navController,
                notificaciones = notificaciones
            )
        }
    }
}
