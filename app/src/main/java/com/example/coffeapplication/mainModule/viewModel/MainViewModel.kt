package com.example.coffeapplication.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.example.coffeapplication.mainModule.model.MainInteractor

class MainViewModel: ViewModel()
{

    private var interactor:MainInteractor

    private var coffeeList:MutableList<CoffeeEntity>

    init {
        interactor= MainInteractor()
        coffeeList=mutableListOf()

    }
    //inicialización por lazy
    private val coffe: MutableLiveData<List<CoffeeEntity>> by lazy {
        MutableLiveData<List<CoffeeEntity>>().also {
            loadCoffe()
        }
    }
    //encapsulando
    fun getCoffe(): LiveData<List<CoffeeEntity>> {
        return coffe
    }

    private fun loadCoffe()
    {

        interactor.getCoffe {
            coffe.value=it
            coffeeList=it

        }
    }
    fun deleteCoffee(coffeeEntity:CoffeeEntity)
    {
        interactor.deleteCoffee(coffeeEntity) {
            val index = coffeeList.indexOf(coffeeEntity)

            if (index != -1) {
                coffeeList.removeAt(index)
                coffe.value = coffeeList
            }
        }
    }

    fun updateCoffee(coffeeEntity:CoffeeEntity)
    {
        coffeeEntity.isFavorite=!coffeeEntity.isFavorite

        interactor.updateCoffee(coffeeEntity) {
            val index = coffeeList.indexOf(coffeeEntity)

            if (index != -1) {
                coffeeList.set(index, coffeeEntity)
                coffe.value = coffeeList
            }
        }
    }
}