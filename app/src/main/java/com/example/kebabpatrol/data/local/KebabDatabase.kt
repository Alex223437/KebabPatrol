package com.example.kebabpatrol.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kebabpatrol.data.local.entity.KebabEntity

@Database(
    entities = [KebabEntity::class],
    version = 1
)
abstract class KebabDatabase : RoomDatabase() {
    abstract val dao: KebabDao
}