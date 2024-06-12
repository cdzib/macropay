package com.cesar.examenmacropay.ui.pokemon

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.LocalData
import com.cesar.examenmacropay.data.model.Pokemon
import com.cesar.examenmacropay.ui.login.LoginActivity
import com.cesar.examenmacropay.ui.login.LoginViewModelFactory
import com.cesar.examenmacropay.ui.login.viewmodel.LoginViewModel
import com.cesar.examenmacropay.ui.pokemon.adapters.PokemonAdapter
import com.cesar.examenmacropay.ui.pokemon.viewmodel.PokemonViewModel
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), PokemonAdapter.OnItemClickListener {
    private var adapter: PokemonAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    private lateinit var pokemonViewModel: PokemonViewModel
    private var pokemonRecyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var displayName = ""
    private var view_error: LinearLayout? = null
    private var retry_button: MaterialButton? = null

    private var view_empty: LinearLayout? = null
    private var empty_button: MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent.extras.let {
            displayName = it?.getString("displayName","").toString()
        }

        pokemonViewModel = ViewModelProvider(this, PokemonViewModelFactory())[PokemonViewModel::class.java]

        pokemonViewModel.pokemonFormState.observe(this@MainActivity, Observer {
            val state = it ?: return@Observer

            if (state.resultError != null) {

            }
        })

        pokemonViewModel.pokemonResult.observe(this@MainActivity, Observer {
            val pokemonResult = it ?: return@Observer


            if (pokemonResult.error != null) {
                view_error?.visibility = View.VISIBLE
                pokemonRecyclerView?.visibility = View.GONE
            }
            if (pokemonResult.success != null) {
                updateUiWithPokemon(pokemonResult.success)
            }
        })
        initViews()
        initDefaultContent()
        initListeners()
        loadFirstPage()

    }

    override fun onResume() {
        super.onResume()
    }
    fun initViews() {
        pokemonRecyclerView = findViewById(R.id.pokemonRecyclerView)
        progressBar = findViewById(R.id.loading)

        adapter = PokemonAdapter(this, this)

        linearLayoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        pokemonRecyclerView?.setLayoutManager(linearLayoutManager)

        pokemonRecyclerView?.setItemAnimator(DefaultItemAnimator())
        pokemonRecyclerView?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        pokemonRecyclerView?.setAdapter(adapter)

        view_empty = findViewById(R.id.view_empty)
        view_error = findViewById(R.id.view_error)

        empty_button = findViewById(R.id.empty_button)
        retry_button = findViewById(R.id.retry_button)
    }
    fun initDefaultContent() {

    }
    fun initListeners() {
        linearLayoutManager?.let {
            pokemonRecyclerView?.addOnScrollListener(object : PaginationScrollListener(it) {
                @SuppressLint("SuspiciousIndentation")
                override fun loadMoreItems() {
                    pokemonViewModel.isLoading = true
                    pokemonViewModel.currentOffset += 20
                    if (pokemonViewModel.currentOffset <= pokemonViewModel.TOTAL_PAGES)
                    loadNextPage()
                }

                override val totalPageCount: Int
                    get() = pokemonViewModel.TOTAL_PAGES
                override val isLastPage: Boolean
                    get() = pokemonViewModel.isLastPage
                override val isLoading: Boolean
                    get() = pokemonViewModel.isLoading
                override val isActiveSearch: Boolean
                    get() = false

                override fun load() {
                }

                override fun terminate() {

                }

            })
        }
        empty_button?.setOnClickListener {
            loadFirstPage()
        }
        retry_button?.setOnClickListener {
            loadFirstPage()
        }
    }
    private fun updateUiWithPokemon(success: LoggedInPokemonView) {
        pokemonViewModel.isLoading = false

        if (!displayName.isNullOrBlank() && displayName != "null"){
            showAlertDialogWelcome()
            displayName = ""
        }
        val results: List<Pokemon> = success.pokemons
        adapter!!.addAll(results)
        results.forEach {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
           scope.launch {
               it.name?.let { name ->
                   val result = pokemonViewModel.getPokemon(name)
                   it.detail = result
                   adapter!!.change(it)

               }
            }
        }
        if (pokemonViewModel.currentOffset !== pokemonViewModel.TOTAL_PAGES) adapter!!.addLoadingFooter() else pokemonViewModel.isLastPage = true
    }

    private fun showPokemonToast(string: String) {
        Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
    }
    private fun loadFirstPage() {
        view_error?.visibility = View.GONE
        pokemonRecyclerView?.visibility = View.VISIBLE
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            pokemonViewModel.getPokemons(pokemonViewModel.offset, pokemonViewModel.limit)
        }
    }
    private fun loadNextPage() {
        adapter!!.removeLoadingFooter()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            pokemonViewModel.getPokemons(pokemonViewModel.currentOffset, pokemonViewModel.limit)
        }
    }

    override fun onItemClick(pokemon: Pokemon) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        intent.putExtra("pokemon", Gson().toJson(pokemon))
        startActivity(intent)
    }

    @SuppressLint("MissingInflatedId")
    fun showAlertDialogWelcome() {
        // Create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.welcome)

        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.dialog_custom_layout, null)
        val textView = customLayout.findViewById<TextView>(R.id.username)
        textView.text = displayName
        builder.setView(customLayout)

        // add a button
        builder.setPositiveButton("Aceptar") { dialog, which ->

        }
        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_name ->{
                LocalData.getInstance(this).remove()
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}