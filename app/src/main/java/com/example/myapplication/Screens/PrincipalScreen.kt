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

@Composable
fun PrincipalScreen(navController: NavHostController) {
    // --- Estados simulados ---
    var alarmaActiva by remember { mutableStateOf(true) }
    var puertaPrincipalAbierta by remember { mutableStateOf(false) }
    var ventanaPrincipalAbierta by remember { mutableStateOf(false) }
    var puertaHabitacionAbierta by remember { mutableStateOf(false) }

    // Estado general (si todo cerrado)
    val todoCerrado = !puertaPrincipalAbierta && !ventanaPrincipalAbierta && !puertaHabitacionAbierta

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
                // Estado general del hogar
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
                            text = if (todoCerrado) "Todo cerradao" else "Hay aperturas",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Control de Alarma
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

            // --- Control de alarma ---
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
                        onClick = { alarmaActiva = !alarmaActiva },
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

            // --- Estado de puertas y ventanas ---
            Text(
                text = "Ventanas y Puertas",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            EstadoElemento("Puerta principal", puertaPrincipalAbierta) {
                puertaPrincipalAbierta = !puertaPrincipalAbierta
            }

            EstadoElemento("Ventana principal", ventanaPrincipalAbierta) {
                ventanaPrincipalAbierta = !ventanaPrincipalAbierta
            }

            EstadoElemento("Puerta habitación", puertaHabitacionAbierta) {
                puertaHabitacionAbierta = !puertaHabitacionAbierta
            }
        }
    }
}

// 🔹 COMPONENTE ACTUALIZADO 🔹
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
            Text(
                text = nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

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
