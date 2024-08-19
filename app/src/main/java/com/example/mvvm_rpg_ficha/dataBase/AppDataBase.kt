package com.example.mvvm_rpg_ficha.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvm_rpg_ficha.model.Ficha

@Database(entities = [Ficha::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun fichaDao(): FichaDao

    companion object {
        @Volatile
        private var db: AppDataBase? = null
        fun instancia(context: Context): AppDataBase {
            return db ?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "ficha.db"
            ).build()
                .also {
                    db = it
                }
        }
    }
}