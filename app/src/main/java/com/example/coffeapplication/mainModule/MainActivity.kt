package com.example.coffeapplication.mainModule
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeapplication.CoffeeApplication
import com.example.coffeapplication.R
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.example.coffeapplication.common.utils.MainAux
import com.example.coffeapplication.databinding.ActivityMainBinding
import com.example.coffeapplication.editModule.CoffeeFragment
import com.example.coffeapplication.editModule.viewModel.CoffeeViewModel
import com.example.coffeapplication.mainModule.adapter.CoffeeAdapter
import com.example.coffeapplication.mainModule.adapter.OnClickListener
import com.example.coffeapplication.mainModule.viewModel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener //MainAux
{
     lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: CoffeeAdapter
    private lateinit var mGridLayout: GridLayoutManager

    // patrón MVVM
    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mCoffeeViewModel: CoffeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //nuevo sistema de vinculación de vistas
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        //evento del Button
        /*  mBinding.btnSave.setOnClickListener {
            val coffee=CoffeeEntity(name=mBinding.etName.text.toString().trim())


            Thread {
             val coffeeId =CoffeeApplication.database.CoffeeDao().insertDB(coffee)
                coffee.coffeeId=coffeeId
            }.start()
            mAdapter.insertMemory(coffee)
        }

       */

        //evento del boton flotante
        mBinding.fabAddCoffee.setOnClickListener {
            launchFragment()
        }

        setupVideModel()

        //configurar el RecyclerView
        mAdapter = CoffeeAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)


        //cargar la collection
      /*  doAsync {
            val coffeeDB = CoffeeApplication.database.CoffeeDao().findAllDB()

            uiThread {
                mAdapter.setCollection(coffeeDB)
            }
        }

       */

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = mGridLayout
        }
    }


    override fun onClick(coffeeEntity: CoffeeEntity) {
        launchFragment(coffeeEntity)
    }

    override fun onClickFavorite(coffeeEntity: CoffeeEntity) {
        //coffeeEntity.isFavorite = !coffeeEntity.isFavorite


       /* doAsync {
            CoffeeApplication.database.CoffeeDao().updateDB(coffeeEntity)

            uiThread {
                mAdapter.updateMemory(coffeeEntity)
            }
        }

        */
        mMainViewModel.updateCoffee(coffeeEntity)

    }

    override fun onClickDelete(coffeeEntity: CoffeeEntity) {

        //opciones
        val items=arrayOf("Eliminar","Llamar","Ir al sitio web")

        //venatana de dialogo como menu
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Que desea hacer?")
            .setItems(items, DialogInterface.OnClickListener { dialogInterface, i ->

                when(i) {
                    0 -> confirmDelete(coffeeEntity)
                    1 -> callPhone(coffeeEntity.phone)
                    2 -> goToWebsite(coffeeEntity.website)

                }
            }).show()
    }

    //lanzar fragmento
    private fun launchFragment(coffeeEntity: CoffeeEntity=CoffeeEntity()) {
        val fragment = CoffeeFragment()

        mCoffeeViewModel.setCoffeeSelected(coffeeEntity)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        //transacción para el fragemnto
        fragmentTransaction.add(R.id.containerMain, fragment)

        //volver al fragmento principal
        fragmentTransaction.addToBackStack(null)

        //transacción que aplque los cambios
        fragmentTransaction.commit()

        //ocultar boton flotante
        mCoffeeViewModel.setShowFab(false)
    }
   /* override fun insertMemory(coffeeEntity: CoffeeEntity) {
        mAdapter.insertMemory(coffeeEntity)
    }

    override fun updateMemory(coffeeEntity: CoffeeEntity) {
        mAdapter.updateMemory(coffeeEntity)
    }

    */
    private fun confirmDelete(coffeeEntity: CoffeeEntity)
    {
        //ventana de dialogo
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Eliminar coffee?")
            .setPositiveButton("Eliminar",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    //proceder con la eliminación
                   /* doAsync {
                        CoffeeApplication.database.CoffeeDao().deleteDB(coffeeEntity)

                        uiThread {
                            mAdapter.deleteMemory(coffeeEntity)
                        }
                    }

                    */
                    mMainViewModel.deleteCoffee(coffeeEntity)

                })
            .setNegativeButton("Cancelar",null).show()
    }

    private fun callPhone(phone:String)
    {
        val call= Intent().apply {
            action= Intent.ACTION_DIAL //acción de llamar a teléfonos
            data= Uri.parse("tel:$phone") //data del número que se quiere marcar
        }
        startActivity(call)
        if(call.resolveActivity(packageManager)!=null) {
            //realizar de actividad de llamadas
            startActivity(call)
        }


        else {
            Toast.makeText(this, R.string.error_no_resolve,Toast.LENGTH_LONG).show()
        }


    }

    private fun goToWebsite(website:String)
    {
        val call=Intent().apply {
            action=Intent.ACTION_VIEW //acción de llamar a vistas
            data=Uri.parse(website) //data del website que se quiere marcar
        }

        if(call.resolveActivity(packageManager)!=null) {
            //realizar de actividad de llamadas
            startActivity(call)
        }
        else {
            Toast.makeText(this, R.string.error_no_resolve,Toast.LENGTH_LONG).show()
        }
    }

    //inicializar ViewModel
    private fun setupVideModel()
    {
        mMainViewModel= ViewModelProvider(this).get(MainViewModel::class.java)

        mMainViewModel.getCoffe().observe(this,{coffe ->
             mAdapter.setCollection(coffe)
        })
        mCoffeeViewModel=ViewModelProvider(this).get(CoffeeViewModel::class.java)

        //visualización del floating action button
        mCoffeeViewModel.getShowFab().observe(this,{isVisible->
            if(isVisible) {
                mBinding.fabAddCoffee.show()
            } else {
                mBinding.fabAddCoffee.hide()
            }
        })

        //memory
        mCoffeeViewModel.getCoffeeSelected().observe(this,{coffeeEntity->
            mAdapter.saveMemory(coffeeEntity)
        })
    }

}

