package com.cesar.examenmacropay.ui.pokemon.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.model.Pokemon
import com.cesar.examenmacropay.data.model.Stats

class PokemonDetailAdapter(val context: Context?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var results: MutableList<Stats>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        viewHolder = getViewHolder(parent, inflater)

        return viewHolder!!
    }
    init {
        results = ArrayList<Stats>()
    }

    override fun getItemCount(): Int {
        return if (results == null) 0 else results!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val stat: Stats = results!![position]

        val pokemonStatsVH = holder as PokemonStatsVH
        pokemonStatsVH.mStatName?.text = stat.stat?.name
        pokemonStatsVH.mProgressBar?.setProgress(stat.baseStat!!, true)
        pokemonStatsVH.mProgressText?.text = stat.baseStat.toString()
    }


    private fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val pokemonVH: View = inflater.inflate(R.layout.item_stats, parent, false)
        viewHolder = PokemonStatsVH(pokemonVH)
        return viewHolder
    }

    protected class PokemonStatsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mStatName: TextView? = null
        var mProgressBar: ProgressBar? = null
        var mProgressText: TextView? = null

        var root: ConstraintLayout? = null
        init {
            mStatName = itemView.findViewById(R.id.tvPokemon)
            mProgressBar = itemView.findViewById(R.id.mProgressBar)
            mProgressText = itemView.findViewById(R.id.txtProgress)
            root = itemView.findViewById(R.id.root)
        }
    }

    fun add(r: Stats) {
        results!!.add(r)
        notifyItemInserted(results!!.size - 1)
    }

    fun addAll(moveResults: List<Stats>) {
        for (result in moveResults) {
            add(result)
        }
    }

}