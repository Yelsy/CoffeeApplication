package com.example.coffeapplication.editModule.model

import com.example.coffeapplication.CoffeeApplication
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//model
class CoffeeInteractor {

    fun saveCoffee(coffeeEntity: CoffeeEntity, callback:(Long) -> Unit)
    {
        doAsync {
            val newID =CoffeeApplication.database.CoffeeDao().insertDB(coffeeEntity)

            uiThread {


                callback(newID)
            }
        }
    }

    fun updateCoffee(coffeeEntity:CoffeeEntity,callback:(CoffeeEntity) -> Unit)
    {
        doAsync {
            CoffeeApplication.database.CoffeeDao().updateDB(coffeeEntity)

            uiThread {
                callback(coffeeEntity)
            }
        }
    }
}