package com.cesar.examenmacropay.ui.pokemon

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.model.Pokemon
import com.cesar.examenmacropay.ui.login.LoginActivity
import com.cesar.examenmacropay.ui.pokemon.adapters.PokemonDetailAdapter
import com.google.gson.Gson

class DetailPokemonActivity : AppCompatActivity() {
    private var imgPokemonDetail: ImageView? = null
    private var pokemon: Pokemon? = null
    private var txtID: TextView? = null
    private var txtWeight: TextView? = null
    private var txtHeight: TextView? = null
    private var txtAbilities: TextView? = null
    private var txtType: TextView? = null
    private var txtForms: TextView? = null
    //private var txtWeight: TextView? = null
    private var statsRecyclerView: RecyclerView? = null
    private var adapter: PokemonDetailAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pokemon)
        val actionBar: ActionBar? = supportActionBar

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        intent.extras.let {
            pokemon = Gson().fromJson(it?.getString("pokemon"), Pokemon::class.java)
            setTitle(pokemon?.name);
        }
        initViews()
        initDefaultContent()
    }

    fun initViews(){
        imgPokemonDetail = findViewById(R.id.imgPokemonDetail)
        txtID = findViewById(R.id.txtID)
        txtWeight = findViewById(R.id.txtWeight)
        txtHeight = findViewById(R.id.txtHeight)
        txtAbilities = findViewById(R.id.txtAbilities)
        txtType = findViewById(R.id.txtType)
        txtForms = findViewById(R.id.txtForms)
        statsRecyclerView = findViewById(R.id.statsRecyclerView)

        adapter = PokemonDetailAdapter(this)
        statsRecyclerView?.adapter = adapter

        linearLayoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        statsRecyclerView?.setLayoutManager(linearLayoutManager)

        statsRecyclerView?.setItemAnimator(DefaultItemAnimator())
        //statsRecyclerView?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        Glide.with(this).load(pokemon?.detail?.sprites?.frontDefault).listener(object:
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {

                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {

                return false
            }
        }).into(
            imgPokemonDetail!!
        )
    }

    private fun initDefaultContent() {
        pokemon?.detail?.let {
            txtID?.text = "# ${it.id}"
            txtWeight?.text = "${it.weight} kg"
            txtHeight?.text = "${it.height} m"
            txtAbilities?.text = it.abilities.joinToString(separator = ", ", transform = { it -> it.ability?.name?.uppercase()
                .toString() })
            txtType?.text = it.types.joinToString(separator = ", ", transform = { it -> it.type?.name?.uppercase()
                .toString() })
            txtForms?.text = it.forms.joinToString(separator = ", ", transform = { it -> it.name?.uppercase()
                .toString() })
            adapter?.addAll(it.stats)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}