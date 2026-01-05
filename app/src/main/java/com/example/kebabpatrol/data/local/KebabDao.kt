package com.example.kebabpatrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kebabpatrol.data.local.entity.KebabEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KebabDao {

    @Query("SELECT * FROM kebabs")
    fun getAllKebabs(): Flow<List<KebabEntity>>

    @Query("SELECT * FROM kebabs WHERE id = :id")
    suspend fun getKebabById(id: Int): KebabEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKebab(kebab: KebabEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKebabs(kebabs: List<KebabEntity>)

    @Query("DELETE FROM kebabs")
    suspend fun clearKebabs()
}