package com.example.coffeapplication.editModule

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.coffeapplication.CoffeeApplication
import com.example.coffeapplication.mainModule.MainActivity
import com.example.coffeapplication.R
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.example.coffeapplication.databinding.FragmentCoffeeBinding
import com.example.coffeapplication.editModule.viewModel.CoffeeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//Escenario para el diseño de la vista Registrar y Editar
class CoffeeFragment : Fragment() {
    private lateinit var mBinding: FragmentCoffeeBinding
    private var mActivity: MainActivity?=null
    private var mIsEditMode:Boolean=false
    private lateinit var mCoffeeEntity: CoffeeEntity
    private lateinit var mCoffeeViewModel:CoffeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inicializando
        mCoffeeViewModel=ViewModelProvider(requireActivity()).get(CoffeeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?): View?
    {
        mBinding=FragmentCoffeeBinding.inflate(inflater,container, false)
        return mBinding.root
    }

    //representa ciclo de vida del fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

       // val coffeeId=arguments?.getLong("keyId",0)

       /* if(coffeeId!=null && coffeeId!=0L) {
            mIsEditMode=true
            doAsync {
             mCoffeeEntity= CoffeeApplication.database.CoffeeDao().findByIdDB(coffeeId.toInt())

                uiThread {
                 with(mBinding){

                     ietName.text=mCoffeeEntity?.name?.editable()
                     ietPhone.text=mCoffeeEntity?.phone?.editable()
                     ietWebsite.text=mCoffeeEntity?.website?.editable()
                     ietPhotoUrl.text=mCoffeeEntity?.photoUrl?.editable()
                    }
                }
            }
        }
        else {
            //Modo registrar
            mIsEditMode=false

            //inicializar
            mCoffeeEntity= CoffeeEntity(name="",phone="",photoUrl="")
            //Toast.makeText(activity,coffeeId.toString(), Toast.LENGTH_SHORT).show()
        }

        */



        //configuracion para insertar imagenes
       /* mBinding.ietPhotoUrl.addTextChangedListener {
            Glide.with(this).load(mBinding.ietPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(mBinding.imgCoffee)
        }

        */
        setupViewModel()

        mBinding.ietName.addTextChangedListener {
            validateOther(mBinding.tilName)
        }

        mBinding.ietPhone.addTextChangedListener {
            validateOther(mBinding.tilPhone)
        }

        mBinding.ietWebsite.addTextChangedListener {
            validateOther(mBinding.tilWebsite)
        }

        mBinding.ietPhotoUrl.addTextChangedListener {
            //validateOther(mBinding.tilPhotoUrl)

            //codigo pora mostrar imagen gris si no hay contenido
            if(validateOther(mBinding.tilPhotoUrl))
            {
                Glide.with(this)
                    .load(mBinding.ietPhotoUrl.text.toString())
                    .diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).centerCrop().into(mBinding.imgCoffee)
            }
            else {
                mBinding.imgCoffee.setImageResource(R.drawable.ic_image)
            }
        }


    }
    //llamar al menu al momento de empezar la actividad
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //ejecutar eventos dentro del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when(item.itemId)
        {
            android.R.id.home -> {
                //code de la flecha de retroceso
                mActivity?.onBackPressed()
                true
            }

            R.id.action_save -> {
                //si no hay nada de que validar
                if(validateOther(mBinding.tilPhotoUrl,mBinding.tilWebsite,mBinding.tilPhone,mBinding.tilName)) {
                     with(mCoffeeEntity)
                     {
                         name = mBinding.ietName.text.toString().trim()
                         phone=mBinding.ietPhone.text.toString().trim()
                         website=mBinding.ietWebsite.text.toString().trim()
                         photoUrl=mBinding.ietPhotoUrl.text.toString().trim()
                     }

                   if (mIsEditMode) {
                        //editar

                        mCoffeeViewModel.updateCoffee(mCoffeeEntity)
                    } else {
                        //registrar
                        mCoffeeViewModel.saveCoffee(mCoffeeEntity)

                    }

                }
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    //se ejecuta antes del onDestroy
    override fun onDestroyView() {
        hideKeyboard() //no hace el efecto
        super.onDestroyView()
    }

    //cerrar correctamente el fragmento
    override fun onDestroy()
    {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)

      //  mActivity?.mBinding?.fabAddCoffee?.show()
       mCoffeeViewModel.setShowFab(true)
       mCoffeeViewModel.setResult(Any())
        setHasOptionsMenu(false)

        super.onDestroy()
    }

    //método para ocultar teclado
    @SuppressLint("UseRequireInsteadOfGet")
    private fun hideKeyboard()
    {
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(view!=null)
        {
            imm.hideSoftInputFromWindow(view!!.windowToken,0)
        }
    }
    //buenas practicas
    private fun String.editable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }

    //validar formulario
    private fun validate(): Boolean
    {
        var isValid=true

        with(mBinding)
        {
            if(ietPhotoUrl.text.toString().trim().isEmpty())
            {
                mBinding.tilPhotoUrl.error=getString(R.string.helper_required)
                mBinding.ietPhotoUrl.requestFocus()
                isValid=false
            }
            else {
                mBinding.tilPhotoUrl.error=null
            }

            if(ietWebsite.text.toString().trim().isEmpty())
            {
                mBinding.tilWebsite.error=getString(R.string.helper_required)
                mBinding.ietWebsite.requestFocus()
                isValid=false
            }
            else {
                mBinding.tilWebsite.error=null
            }

            if(ietPhone.text.toString().trim().isEmpty())
            {
                mBinding.tilPhone.error=getString(R.string.helper_required)
                mBinding.ietPhone.requestFocus()
                isValid=false
            }
            else {
                mBinding.tilPhone.error=null
            }

            if(ietName.text.toString().trim().isEmpty())
            {
                mBinding.tilName.error=getString(R.string.helper_required)
                mBinding.ietName.requestFocus()
                isValid=false
            }
            else {
                //quitar border rojo
                mBinding.tilName.error=null
            }
        }

        return isValid
    }

    //validar formulario
    private fun validateOther(vararg txtArray: TextInputLayout): Boolean
    {
        var isValid=true

        for(txt in txtArray)
        {
            if(txt.editText?.text.toString().trim().isEmpty())
            {
                txt.error=getString(R.string.helper_required)
                txt.editText?.requestFocus()
                isValid=false
            }
            else {
                txt.error=null
            }
        }

        return isValid
    }

    private fun setupViewModel()
    {
        mCoffeeViewModel.getCoffeeSelected().observe(viewLifecycleOwner,{
            mCoffeeEntity=it

            if(it.coffeeId!=0L) {
                mIsEditMode=true //modo editar

                with(mBinding)
                {
                    ietName.text=it?.name?.editable()
                    ietPhone.text=it?.phone?.editable()
                    ietWebsite.text=it?.website?.editable()
                    ietPhotoUrl.text=it?.photoUrl?.editable()
                }
            }
            else {
                mIsEditMode=false //modo registrar
            }

            mActivity=activity as? MainActivity

            //mostrar flecha de retroceso
            mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            //mostrar título
            mActivity?.supportActionBar?.title=getString(R.string.coffee_title_add)

            //acceso al menu
            setHasOptionsMenu(true)

            //titulo según la acción
            mActivity?.supportActionBar?.title=
                if(mIsEditMode) {
                    getString(R.string.title_edit)
                }
                else {
                    getString(R.string.coffee_title_add)
                }
        })

        mCoffeeViewModel.getResult().observe(viewLifecycleOwner,{result->
            hideKeyboard()

            when(result)
            {
                //registrar
                is Long -> {
                    mCoffeeEntity.coffeeId=result
                    mCoffeeViewModel.setCoffeeSelected(mCoffeeEntity)

                    Toast.makeText(mActivity,R.string.coffee_save, Toast.LENGTH_LONG).show()
                    mActivity?.onBackPressed()
                }

                //actualizar
                is CoffeeEntity -> {
                    mCoffeeViewModel.setCoffeeSelected(mCoffeeEntity)
                    Toast.makeText(mActivity,R.string.coffee_update, Toast.LENGTH_LONG).show()
                }
            }
        })
    }



}