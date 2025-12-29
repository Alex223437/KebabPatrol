package com.example.kebabpatrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kebabpatrol.data.local.entity.KebabEntity

@Dao
interface KebabDao {

    // ВЗЯТЬ ВСЁ (для оффлайна)
    @Query("SELECT * FROM kebabs")
    suspend fun getKebabs(): List<KebabEntity>

    // ПОЛОЖИТЬ ПАРТИЮ (если есть совпадения - перезаписать)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKebabs(kebabs: List<KebabEntity>)

    // ОЧИСТИТЬ ОБЩАК (перед загрузкой новых)
    @Query("DELETE FROM kebabs")
    suspend fun clearKebabs()
}