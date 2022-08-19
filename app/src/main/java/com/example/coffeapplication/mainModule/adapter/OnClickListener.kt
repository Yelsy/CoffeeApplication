package com.example.coffeapplication.mainModule.adapter

import com.example.coffeapplication.common.entities.CoffeeEntity

interface OnClickListener {

    fun onClick(coffeeEntity: CoffeeEntity)
    fun onClickFavorite(coffeeEntity: CoffeeEntity)
    fun onClickDelete(coffeeEntity: CoffeeEntity)
}