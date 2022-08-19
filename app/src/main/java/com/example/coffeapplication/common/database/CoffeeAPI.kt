package com.example.coffeapplication.common.database

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class CoffeeAPI constructor(context:Context){

    companion object{
        private var INSTANCE:CoffeeAPI?=null
        fun getInstance(context: Context)= INSTANCE?: synchronized(this){
          INSTANCE?: CoffeeAPI(context).also {
              INSTANCE=it
          }
        }
    }
    //variable que administra las operaciones
    val requestQueue:RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req:Request<T>){
        requestQueue.add(req)
    }
}