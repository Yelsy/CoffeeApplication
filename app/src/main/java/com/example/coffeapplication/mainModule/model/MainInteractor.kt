package com.example.coffeapplication.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.coffeapplication.CoffeeApplication
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.example.coffeapplication.common.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

 val URL = Constants.COFFEE_URL + Constants.GET_ALL_PATH
   /* fun getCoffe(callback:(MutableList<CoffeeEntity>) -> Unit)
    {
        val url=Constants.COFFEE_URL+Constants.GET_ALL_PATH
        val jsonObjectRequest= JsonObjectRequest(Request.Method.GET,url,null,{ response->
            val status=response.getInt(Constants.STATUS_PROPERTY)

            if(status== Constants.SUCCESS) {
                //Log.i("status",status.toString())

                //configuración
                val jsonList=response.getJSONArray(Constants.COFFEE_PROPERTY).toString()
                val mutableLisType=object: TypeToken<MutableList<CoffeeEntity>>(){}.type

                //convertir jsonList a un listado de objeto Coffe
                val coffeeList=Gson().fromJson<MutableList<CoffeeEntity>>(jsonList,mutableLisType)

                //Llamar al API
                callback(coffeeList)
            }

        }, {
            it.printStackTrace()
        })

        CoffeeApplication.coffeeAPI.addToRequestQueue(jsonObjectRequest)
    }

    */

    fun deleteCoffee(coffeeEntity:CoffeeEntity,callback:(CoffeeEntity) -> Unit)
    {
        doAsync {
            CoffeeApplication.database.CoffeeDao().deleteDB(coffeeEntity)

            uiThread {
                callback(coffeeEntity)
            }
        }
    }


   fun getCoffe(callback: (MutableList<CoffeeEntity>) -> Unit) {

       var requestGetAllSong =
           JsonObjectRequest(Request.Method.GET, "$URL",
               null,
               { response ->

                   //La reepsuesta
                   var jsonList = response.getJSONArray("jsonList").toString()
                   Log.i(
                       "Lista---------------------------------------------------------------------->",
                       "" + jsonList
                   )
                   var typeData = object : TypeToken<MutableList<CoffeeEntity>>() {}.type
                   var coffeeList = Gson().fromJson<MutableList<CoffeeEntity>>(jsonList, typeData)

                   callback(coffeeList)
               },
               { error ->
                   Log.i(
                       "Error ---------------------------------------------------------------------->",
                       "" + error.message
                   )
                   error.printStackTrace()
               })

       CoffeeApplication.coffeeAPI.addToRequestQueue(requestGetAllSong)
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