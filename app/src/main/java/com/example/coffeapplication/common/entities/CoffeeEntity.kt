package com.example.coffeapplication.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="CoffeeTable")
data class CoffeeEntity(@PrimaryKey(autoGenerate=true) var coffeeId: Long = 0,
    var name: String,
     var phone:String="",
    var website: String = "",
    var photoUrl:String,
    var isFavorite: Boolean = false
)
{
    //constructor vacío
    constructor():this(name="",phone="",photoUrl="")
    //para evitar la duplicidad por id
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoffeeEntity

        if (coffeeId != other.coffeeId) return false

        return true
    }

    override fun hashCode(): Int {
        return coffeeId.hashCode()
    }
}
