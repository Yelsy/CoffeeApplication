package com.example.coffeapplication.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.example.coffeapplication.editModule.model.CoffeeInteractor

//viewModel
class CoffeeViewModel: ViewModel() {

    //propiedades para la vista
    private val coffeeSelected=MutableLiveData<CoffeeEntity>()
    private val showFab=MutableLiveData<Boolean>()
    private val result=MutableLiveData<Any>()
    private val interactor:CoffeeInteractor

    init {
        interactor= CoffeeInteractor()
    }

    //getters and setters
    fun setCoffeeSelected(coffeeEntity:CoffeeEntity) {
        coffeeSelected.value=coffeeEntity
    }
    fun getCoffeeSelected(): LiveData<CoffeeEntity> {
        return coffeeSelected
    }

    fun setShowFab(isVisible:Boolean) {
        showFab.value=isVisible
    }
    fun getShowFab(): LiveData<Boolean> {
        return showFab
    }

    fun setResult(value:Any) {
        result.value=value
    }
    fun getResult(): LiveData<Any> {
        return result
    }

    fun saveCoffee(coffeeEntity:CoffeeEntity)
    {
        interactor.saveCoffee(coffeeEntity,{newID->
            result.value=newID
        })
    }

    fun updateCoffee(coffeeEntity:CoffeeEntity)
    {
        interactor.updateCoffee(coffeeEntity,{coffeeUpdate->
            result.value=coffeeUpdate
        })
    }
}