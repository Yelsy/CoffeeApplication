package com.example.coffeapplication.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coffeapplication.common.entities.CoffeeEntity

@Database(entities=arrayOf(CoffeeEntity::class),version=2)
abstract class CoffeeDatabase: RoomDatabase() {

    abstract fun CoffeeDao(): CoffeeDao
}

