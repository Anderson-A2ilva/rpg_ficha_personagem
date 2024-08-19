package com.example.mvvm_rpg_ficha.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario (
    @PrimaryKey
    val id: String,
    val nome: String,
    val senha: String
)