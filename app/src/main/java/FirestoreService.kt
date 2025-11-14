package com.example.myapplication

import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions

object FirestoreService {

    // Guarda el estado del hogar (igual que el NavController)
    fun guardarEstadoGeneral(
        alarmaActiva: Boolean,
        puertaPrincipalAbierta: Boolean,
        ventanaPrincipalAbierta: Boolean,
        puertaHabitacionAbierta: Boolean
    ) {
        val estado = mapOf(
            "alarmaActiva" to alarmaActiva,
            "puertaPrincipalAbierta" to puertaPrincipalAbierta,
            "ventanaPrincipalAbierta" to ventanaPrincipalAbierta,
            "puertaHabitacionAbierta" to puertaHabitacionAbierta,
            "timestamp" to Timestamp.now()
        )

        FirebaseConfig.db.collection("estadoHogar")
            .document("estado")
            .set(estado, SetOptions.merge())
    }

    // NOTIFICACIONES que sí coinciden con tu NavController
    fun guardarNotificacion(
        mensaje: String,
        abierta: Boolean,
        hora: String
    ) {
        val data = mapOf(
            "mensaje" to mensaje,
            "abierta" to abierta,
            "hora" to hora
        )

        FirebaseConfig.db.collection("historialEventos")
            .add(data)
    }
}
