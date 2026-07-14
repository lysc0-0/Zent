package com.flowstudent.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: String, // Ahora es String (ej. "Comida")
    val timestamp: Long, // Fecha y hora completa
    val note: String = "",
    val type: String // "GASTO" o "INGRESO"
)
