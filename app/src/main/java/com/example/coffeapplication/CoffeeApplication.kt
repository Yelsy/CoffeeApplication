package com.example.coffeapplication

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.coffeapplication.common.database.CoffeeAPI
import com.example.coffeapplication.common.database.CoffeeDatabase

class CoffeeApplication: Application() {

    //nos permite acceder al database desde cualquier punto (patrón singleton)
    companion object {
        lateinit var database: CoffeeDatabase
        lateinit var coffeeAPI: CoffeeAPI
    }

    override fun onCreate() {
        super.onCreate()

        //modificar table, agregando nueva columna
        val MIGRATION_1_2=object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table CoffeeTable add column photoUrl text not null default ''")
            }
        }

        //cargar database
        database= Room
            .databaseBuilder(this, CoffeeDatabase::class.java,"CoffeeDatabase")
            .addMigrations(MIGRATION_1_2)
            .build()
        coffeeAPI=CoffeeAPI.getInstance(this)
    }
    //inicializar volley
}