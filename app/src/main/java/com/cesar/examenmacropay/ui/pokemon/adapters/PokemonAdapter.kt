package com.cesar.examenmacropay.ui.pokemon.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.model.Pokemon


class PokemonAdapter(val context: Context?, private var listener: OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM = 0
    private val LOADING = 1

    private var results: MutableList<Pokemon>? = null

    private var isLoadingAdded = false
    interface OnItemClickListener{
       fun onItemClick(pokemon: Pokemon)
    }
    init {
        results = ArrayList<Pokemon>()
    }

    fun getPokemons(): List<Pokemon>? {
        return results
    }

    fun setPokemons(movieResults: MutableList<Pokemon>?) {
        results = movieResults
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val loadingVH: View = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(loadingVH)
            }
        }
        return viewHolder!!
    }

    private fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val pokemonVH: View = inflater.inflate(R.layout.item_pokemon, parent, false)
        viewHolder = PokemonVH(pokemonVH)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon: Pokemon = results!![position] // Movie
        when (getItemViewType(position)) {
            ITEM -> {
                val pokemonVH = holder as PokemonVH
                pokemonVH.mPokemonName?.text = pokemon.name

                pokemonVH.root?.setOnClickListener {
                    listener?.onItemClick(pokemon)
                }
                if (context != null) {
                    pokemonVH.mPokemonImg?.let { imageView ->
                       pokemon.detail.let {

                           Glide.with(context).load(it?.sprites?.frontDefault).listener(object:
                               RequestListener<Drawable>{
                               override fun onLoadFailed(
                                   e: GlideException?,
                                   model: Any?,
                                   target: Target<Drawable>,
                                   isFirstResource: Boolean
                               ): Boolean {
                                   pokemonVH.mProgressBar?.setVisibility(View.GONE);
                                   return false
                               }

                               override fun onResourceReady(
                                   resource: Drawable,
                                   model: Any,
                                   target: Target<Drawable>?,
                                   dataSource: DataSource,
                                   isFirstResource: Boolean
                               ): Boolean {
                                   pokemonVH.mProgressBar?.setVisibility(View.GONE);
                                   return false
                               }
                           }).into(
                               imageView
                           )
                       }
                    }
                }
            }

            LOADING -> {}
        }
    }

    override fun getItemCount(): Int {
        return if (results == null) 0 else results!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == results!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    /*
   Helpers
   _________________________________________________________________________________________________
    */
    fun add(r: Pokemon) {
        results!!.add(r)
        notifyItemInserted(results!!.size - 1)
    }

    fun addAll(moveResults: List<Pokemon>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: Pokemon) {
        val position = results!!.indexOf(r)
        if (position > -1) {
            results!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun change(r: Pokemon) {
        val position = results!!.indexOf(r)
        if (position > -1) {
            results!![position] = r
            notifyItemChanged(position)
        }
    }
    fun clear() {
        isLoadingAdded = false
    }

    fun isEmpty(): Boolean {
        return getItemCount() == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Pokemon())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = results!!.size - 1
        val result: Pokemon = getItem(position)
        if (result != null) {
            results!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Pokemon {
        return results!![position]
    }


    /*
   View Holders
   _________________________________________________________________________________________________
    */

    /*
   View Holders
   _________________________________________________________________________________________________
    */
    protected class PokemonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPokemonName: TextView? = null
        var mPokemonImg: ImageView? = null
        var mProgressBar: ProgressBar? = null
        var root: ConstraintLayout? = null
        init {
            mPokemonName = itemView.findViewById(R.id.tvPokemon)
            mPokemonImg = itemView.findViewById(R.id.imgPokemon)
            mProgressBar = itemView.findViewById(R.id.mProgressBar)
            root = itemView.findViewById(R.id.root)
        }
    }


    protected class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!)

}