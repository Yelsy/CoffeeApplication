package com.example.coffeapplication.mainModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.coffeapplication.R
import com.example.coffeapplication.common.entities.CoffeeEntity
import com.example.coffeapplication.databinding.ItemCoffeeBinding

class CoffeeAdapter(private var coffe:MutableList<CoffeeEntity>,
                    private var listener: OnClickListener
): RecyclerView.Adapter<CoffeeAdapter.ViewHolder>()
{
    private lateinit var mContext: Context
    //clase interna que recibe una vista
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        //referencia a la vista item_coffee
      val binding= ItemCoffeeBinding.bind(view)
        //función que recibe un comercio

        fun setListener(coffeeEntity: CoffeeEntity)
        {
            //click normal
            binding.root.setOnClickListener {
                listener.onClick(coffeeEntity)
            }

            //click largo -> evento onClickDelete
            binding.root.setOnLongClickListener {
                listener.onClickDelete(coffeeEntity)
                true
            }

            //evento onclicfavorite
            binding.cbFavorite.setOnClickListener {
                listener.onClickFavorite(coffeeEntity)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext=parent.context
        val view= LayoutInflater.from(mContext).inflate(R.layout.item_coffee,parent,false)

        return ViewHolder(view)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coffee=coffe.get(position)

        with(holder)
        {
            //escuchar el coffee
            setListener(coffee)

            //pintar name
            binding.milkNameCoffee.text=coffee.name

            //pintar favorite
            binding.cbFavorite.isChecked=coffee.isFavorite

            //pintar imagen
            Glide.with(mContext)
                .load(coffee.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int {
        return coffe.size
    }

    /* fun setCollection(coffeeDB:MutableList<CoffeeEntity>)
    {
        this.cofee=coffeeDB
        notifyDataSetChanged() //refrescar los cambios
    }

     */
    fun setCollection(coffee:List<CoffeeEntity>)
    {
        this.coffe=coffee as MutableList<CoffeeEntity>
        notifyDataSetChanged() //refrescar los cambios
    }

    /*fun insertMemory(coffeeEntity: CoffeeEntity)
    {
        coffe.add(coffeeEntity)
        notifyDataSetChanged()
    }

     */

    //insert or update
    fun saveMemory(coffeeEntity: CoffeeEntity)
    {
        if(coffeeEntity.coffeeId!=0L)
        {
            //si coffee no existe en la lista de coffee
            if(!coffe.contains(coffeeEntity))
            {
                coffe.add(coffeeEntity)
                notifyItemInserted(coffe.size-1)
            }
            else {
                updateMemory(coffeeEntity)
            }
        }
    }

   private fun updateMemory(coffeeEntity: CoffeeEntity)
    {
        val index=coffe.indexOf(coffeeEntity)

        if(index!=-1)
        {
            coffe.set(index,coffeeEntity)
            notifyItemChanged(index)
        }
    }

  /*  fun deleteMemory(coffeeEntity: CoffeeEntity)
    {
        val index=coffe.indexOf(coffeeEntity)

        if(index!=-1)
        {
            coffe.removeAt(index)
            notifyItemRemoved(index) //refrescar los cambios
        }
    }

   */

}


