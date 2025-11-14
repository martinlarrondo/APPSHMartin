package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.Composables.BottomBar
import com.example.myapplication.FirebaseConfig
import java.text.SimpleDateFormat
import java.util.*
import com.example.myapplication.FirebaseConfig.db


data class Notificacion(val mensaje: String, val abierta: Boolean, val hora: String)

@Composable
fun PrincipalScreen(
    navController: NavHostController,
    alarmaActiva: Boolean,
    onAlarmaChange: (Boolean) -> Unit,
    puertaPrincipalAbierta: Boolean,
    onPuertaPrincipalChange: (Boolean) -> Unit,
    ventanaPrincipalAbierta: Boolean,
    onVentanaPrincipalChange: (Boolean) -> Unit,
    puertaHabitacionAbierta: Boolean,
    onPuertaHabitacionChange: (Boolean) -> Unit,
    notificaciones: List<Notificacion>,
    agregarNotificacion: (Notificacion) -> Unit
) {

    val db = FirebaseConfig.db

    val todoCerrado = !puertaPrincipalAbierta && !ventanaPrincipalAbierta && !puertaHabitacionAbierta
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun notificar(mensaje: String, abierta: Boolean) {
        val horaActual = sdf.format(Date())
        val noti = Notificacion(mensaje, abierta, horaActual)

        // Guarda en memoria local
        agregarNotificacion(noti)

        // Guarda en Firebase
        db.collection("notificaciones")
            .add(
                mapOf(
                    "mensaje" to mensaje,
                    "abierta" to abierta,
                    "hora" to horaActual
                )
            )
    }


    // Guarda el estado general en Firebase
    fun guardarEstado() {
        val data = mapOf(
            "alarmaActiva" to alarmaActiva,
            "puertaPrincipalAbierta" to puertaPrincipalAbierta,
            "ventanaPrincipalAbierta" to ventanaPrincipalAbierta,
            "puertaHabitacionAbierta" to puertaHabitacionAbierta
        )
        db.collection("estadoHogar").document("estado").set(data)
    }


    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Estado general del hogar",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (todoCerrado) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (todoCerrado) "Todo cerrado" else "Hay aperturas",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (alarmaActiva) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (alarmaActiva) "Alarma activada" else "Alarma desactivada",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "CONTROL DE ALARMA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onAlarmaChange(!alarmaActiva)
                            guardarEstado()

                            notificar(
                                mensaje = "Alarma ${if (!alarmaActiva) "activada" else "desactivada"}",
                                abierta = !alarmaActiva
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (alarmaActiva) Color(0xFFF44336) else Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = if (alarmaActiva) "Desactivar alarma" else "Activar alarma",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = if (alarmaActiva) "Estado: ACTIVADA" else "Estado: DESACTIVADA",
                        fontWeight = FontWeight.Medium,
                        color = if (alarmaActiva) Color(0xFF4CAF50) else Color(0xFFF44336),
                        fontSize = 16.sp
                    )
                }
            }

            Text(
                text = "Ventanas y Puertas",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            EstadoElemento("Puerta principal", puertaPrincipalAbierta) {
                onPuertaPrincipalChange(!puertaPrincipalAbierta)
                guardarEstado()

                notificar(
                    "Puerta principal ${if (!puertaPrincipalAbierta) "abierta" else "cerrada"}",
                    !puertaPrincipalAbierta
                )
            }

            EstadoElemento("Ventana principal", ventanaPrincipalAbierta) {
                onVentanaPrincipalChange(!ventanaPrincipalAbierta)
                guardarEstado()

                notificar(
                    "Ventana principal ${if (!ventanaPrincipalAbierta) "abierta" else "cerrada"}",
                    !ventanaPrincipalAbierta
                )
            }

            EstadoElemento("Puerta habitación", puertaHabitacionAbierta) {
                onPuertaHabitacionChange(!puertaHabitacionAbierta)
                guardarEstado()

                notificar(
                    "Puerta habitación ${if (!puertaHabitacionAbierta) "abierta" else "cerrada"}",
                    !puertaHabitacionAbierta
                )
            }
        }
    }
}

@Composable
fun EstadoElemento(nombre: String, abierto: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Text(
                text = if (abierto) "Abierta" else "Cerrada",
                color = if (abierto) Color(0xFFF44336) else Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(if (abierto) "Cerrar" else "Abrir")
            }
        }
    }
}


