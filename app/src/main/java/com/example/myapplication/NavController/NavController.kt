package com.example.myapplication.NavController

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screens.*
import com.example.myapplication.FirebaseConfig

@Composable
fun NavController() {

    val navController = rememberNavController()
    val db = FirebaseConfig.db

    // ESTADOS QUE YA TENÍAS
    var alarmaActiva by remember { mutableStateOf(true) }
    var puertaPrincipalAbierta by remember { mutableStateOf(false) }
    var ventanaPrincipalAbierta by remember { mutableStateOf(false) }
    var puertaHabitacionAbierta by remember { mutableStateOf(false) }

    var notificaciones by remember { mutableStateOf(listOf<Notificacion>()) }

    // 🔥 ESCUCHAR CAMBIOS EN TIEMPO REAL
    LaunchedEffect(true) {

        // ESTADO DEL HOGAR
        db.collection("estadoHogar")
            .document("estadoActual")
            .addSnapshotListener { snap, _ ->
                if (snap != null && snap.exists()) {
                    alarmaActiva = snap.getBoolean("alarmaActiva") ?: true
                    puertaPrincipalAbierta = snap.getBoolean("puertaPrincipalAbierta") ?: false
                    ventanaPrincipalAbierta = snap.getBoolean("ventanaPrincipalAbierta") ?: false
                    puertaHabitacionAbierta = snap.getBoolean("puertaHabitacionAbierta") ?: false
                }
            }

        // NOTIFICACIONES
        db.collection("notificaciones")
            .orderBy("timestamp")
            .addSnapshotListener { snap, _ ->
                if (snap != null) {
                    notificaciones = snap.documents.map {
                        Notificacion(
                            mensaje = it.getString("mensaje") ?: "",
                            abierta = it.getBoolean("abierta") ?: false,
                            hora = it.getString("hora") ?: ""
                        )
                    }.reversed()
                }
            }
    }

    // 🔥🔥🔥 GUARDAR ESTADO GENERAL DEL HOGAR 🔥🔥🔥
    fun guardarEstadoFirebase() {

        val estadoGeneral = !puertaPrincipalAbierta &&
                !ventanaPrincipalAbierta &&
                !puertaHabitacionAbierta

        val data = mapOf(
            "alarmaActiva" to alarmaActiva,
            "puertaPrincipalAbierta" to puertaPrincipalAbierta,
            "ventanaPrincipalAbierta" to ventanaPrincipalAbierta,
            "puertaHabitacionAbierta" to puertaHabitacionAbierta,
            "estadoGeneral" to if (estadoGeneral) "Todo cerrado" else "Hay aperturas",
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("estadoHogar")
            .document("estadoActual")
            .set(data)
    }

    // 🔥🔥🔥 GUARDAR NOTIFICACIÓN 🔥🔥🔥
    fun agregarNotificacionFirebase(notificacion: Notificacion) {

        val data = mapOf(
            "mensaje" to notificacion.mensaje,
            "abierta" to notificacion.abierta,
            "hora" to notificacion.hora,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("notificaciones")
            .add(data)
    }

    // 🔵 NAVEGACIÓN
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("principal") {
            PrincipalScreen(
                navController = navController,
                alarmaActiva = alarmaActiva,
                onAlarmaChange = {
                    alarmaActiva = it
                    guardarEstadoFirebase()
                },
                puertaPrincipalAbierta = puertaPrincipalAbierta,
                onPuertaPrincipalChange = {
                    puertaPrincipalAbierta = it
                    guardarEstadoFirebase()
                },
                ventanaPrincipalAbierta = ventanaPrincipalAbierta,
                onVentanaPrincipalChange = {
                    ventanaPrincipalAbierta = it
                    guardarEstadoFirebase()
                },
                puertaHabitacionAbierta = puertaHabitacionAbierta,
                onPuertaHabitacionChange = {
                    puertaHabitacionAbierta = it
                    guardarEstadoFirebase()
                },
                notificaciones = notificaciones,
                agregarNotificacion = { agregarNotificacionFirebase(it) }
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
