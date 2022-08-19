package com.example.coffeapplication.common.utils

import com.example.coffeapplication.common.entities.CoffeeEntity

interface MainAux {


    fun insertMemory(coffeeEntity: CoffeeEntity)
    fun updateMemory(coffeeEntity: CoffeeEntity)
}