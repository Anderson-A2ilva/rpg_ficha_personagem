package com.example.mvvm_rpg_ficha.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvm_rpg_ficha.model.Ficha
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaDao {

    @Query("SELECT * FROM Ficha")
    fun buscaTodos(): Flow<List<Ficha>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(ficha: Ficha)

    @Delete
    suspend fun delete(ficha: Ficha)

    @Query("SELECT * FROM Ficha WHERE id = :id")
     fun buscaPorId(id: Long): Flow<Ficha?>

}
