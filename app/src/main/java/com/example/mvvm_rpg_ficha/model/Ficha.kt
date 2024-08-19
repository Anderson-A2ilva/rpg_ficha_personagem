package com.example.mvvm_rpg_ficha.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Ficha (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val raca: String,
    val classe: String,
    val forca: Int? = null,
    val destreza: Int? = null,
    val constituicao: Int? = null,
    val inteligencia: Int? = null,
    val sabedoria: Int? = null,
    val carisma: Int? = null,
    val imgClass: Int? = null
): Parcelable