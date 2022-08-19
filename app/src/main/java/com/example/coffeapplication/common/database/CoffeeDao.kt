package com.example.coffeapplication.common.database

import androidx.room.*
import com.example.coffeapplication.common.entities.CoffeeEntity

@Dao
interface CoffeeDao {

    @Insert
    fun insertDB(coffeeEntity: CoffeeEntity): Long

    @Update
    fun updateDB(coffeeEntity: CoffeeEntity)

    @Delete
    fun deleteDB(coffeeEntity: CoffeeEntity)

    @Query("SELECT * FROM CoffeeTable WHERE coffeeId IN (:coffeeId)")
    fun findByIdDB(coffeeId:Int): CoffeeEntity

    @Query("SELECT * FROM CoffeeTable")
    fun findAllDB(): MutableList<CoffeeEntity>
}