package com.movies.moviesapp.ListScreen

import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.airbnb.lottie.LottieAnimationView
import com.movies.moviesapp.ListScreen.MoviesItem
import com.movies.moviesapp.ListScreen.MoviesAdapter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.movies.moviesapp.R
import com.movies.moviesapp.ListScreen.Favoris.FavoritesMovies
import com.movies.moviesapp.ListScreen.Recherche.RechercheMain
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.movies.moviesapp.Ressources.MethodesUtiles
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.movies.moviesapp.Ressources.URLs
import org.json.JSONObject
import org.json.JSONArray
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import org.json.JSONException
import com.android.volley.VolleyError
import java.lang.Exception
import java.util.*

class MoviesMain : AppCompatActivity() {
    private var pullToRefresh: SwipeRefreshLayout? = null
    private var moviesRV: RecyclerView? = null
    private var chargementItems: LinearLayout? = null
    private var aucunContenuTrouver: LinearLayout? = null
    private var btnAucuneItem: TextView? = null
    private var textAucunItem: TextView? = null
    private var favoris_iv_container: MaterialCardView? = null
    private var favoris_iv: LottieAnimationView? = null
    private var recherche_iv_container: MaterialCardView? = null
    private var recherche_iv: LottieAnimationView? = null
    private var moviesList: ArrayList<MoviesItem>? = null
    private var favoriteMoviesList: ArrayList<MoviesItem>? = null
    private var moviesAdapter: MoviesAdapter? = null
    fun setLangue(localCode: String?) {
        val locale = Locale(localCode)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setLangue(Locale.getDefault().language)
        } catch (e: Exception) {
            Log.e("ErreurLangue", ": ", e)
        }
        setContentView(R.layout.movies_activity)
        findsViewByIds()
        Handler().postDelayed({
            favoris_iv!!.progress = 0f
            favoris_iv!!.setMaxProgress(0.35f)
            favoris_iv!!.playAnimation()
            recherche_iv!!.progress = 0f
            recherche_iv!!.playAnimation()
        }, 500)
        movies
        btnAucuneItem!!.setOnClickListener { view: View? -> movies }
        favoris_iv_container!!.setOnClickListener { view: View? ->
            favoris_iv!!.progress = 0f
            favoris_iv!!.playAnimation()
            favoriteMoviesList = ArrayList()
            for (movie in moviesList!!) {
                if (movie.isFavorite) {
                    favoriteMoviesList!!.add(movie)
                }
            }
            FavoritesMovies(this@MoviesMain, favoriteMoviesList!!).show(
                supportFragmentManager,
                "BottomSheet Fragment"
            )
        }
        recherche_iv_container!!.setOnClickListener { view: View? ->
            recherche_iv!!.progress = 0f
            recherche_iv!!.playAnimation()
            RechercheMain(this@MoviesMain, moviesList!!).show(
                supportFragmentManager,
                "BottomSheet Fragment"
            )
        }
        pullToRefresh!!.setOnRefreshListener { movies }
    }

    val movies: Unit
        get() {
            moviesList = ArrayList()
            showChargement()
            if (MethodesUtiles.isInternetAvailable(this@MoviesMain)) {
                val requestQueue = Volley.newRequestQueue(this@MoviesMain)
                val postRequest: StringRequest = object : StringRequest(
                    Method.GET,
                    URLs.GET_MOVIES_URL + "?api_key=" + URLs.API_KEY,
                    Response.Listener { response: String ->
                        Log.d("getMovies", "getMovies: $response")
                        try {
                            val jsonObj = JSONObject(response)
                            val res = jsonObj.getJSONArray("results")
                            for (i in 0 until res.length()) {
                                val movie_res = res.getJSONObject(i)
                                moviesList!!.add(
                                    MoviesItem(
                                        movie_res.getString("poster_path"),
                                        movie_res.getBoolean("adult"),
                                        movie_res.getString("overview"),
                                        movie_res.getString("release_date"),
                                        movie_res.getString("id"),
                                        movie_res.getString("original_title"),
                                        movie_res.getString("original_language"),
                                        movie_res.getString("title"),
                                        movie_res.getString("backdrop_path"),
                                        movie_res.getDouble("popularity"),
                                        movie_res.getInt("vote_count"),
                                        movie_res.getBoolean("video"),
                                        movie_res.getDouble("vote_average"),
                                        false
                                    )
                                )
                            }
                            pullToRefresh!!.isRefreshing = false
                            if (moviesList!!.size > 0) {
                                moviesAdapter = MoviesAdapter(this@MoviesMain, moviesList!!)
                                moviesRV!!.adapter = moviesAdapter
                                moviesRV!!.layoutManager = LinearLayoutManager(this@MoviesMain)
                                showContenu()
                            } else {
                                showAucunContenu()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            showError()
                        }
                    },
                    Response.ErrorListener { error: VolleyError ->
                        error.printStackTrace()
                        showError()
                    }
                ) {}
                requestQueue.add(postRequest)
            } else {
                pullToRefresh!!.isRefreshing = false
                showError()
            }
        }

    fun showChargement() {
        aucunContenuTrouver!!.visibility = View.GONE
        chargementItems!!.visibility = View.VISIBLE
        moviesRV!!.visibility = View.GONE
    }

    fun showAucunContenu() {
        aucunContenuTrouver!!.visibility = View.VISIBLE
        chargementItems!!.visibility = View.GONE
        textAucunItem!!.text = resources.getString(R.string.aucun_film_disponible)
        moviesRV!!.visibility = View.GONE
    }

    fun showContenu() {
        aucunContenuTrouver!!.visibility = View.GONE
        chargementItems!!.visibility = View.GONE
        moviesRV!!.visibility = View.VISIBLE
    }

    fun showError() {
        aucunContenuTrouver!!.visibility = View.VISIBLE
        chargementItems!!.visibility = View.GONE
        moviesRV!!.visibility = View.GONE
        textAucunItem!!.text = resources.getString(R.string.verifier_votre_connexion_internet)
    }

    private fun findsViewByIds() {
        pullToRefresh = findViewById(R.id.pullToRefresh)
        moviesRV = findViewById(R.id.moviesRV)
        chargementItems = findViewById(R.id.chargementItems)
        aucunContenuTrouver = findViewById(R.id.aucunContenuTrouver)
        btnAucuneItem = findViewById(R.id.btnAucuneItem)
        textAucunItem = findViewById(R.id.textAucunItem)
        favoris_iv_container = findViewById(R.id.favoris_iv_container)
        favoris_iv = findViewById(R.id.favoris_iv)
        recherche_iv_container = findViewById(R.id.recherche_iv_container)
        recherche_iv = findViewById(R.id.recherche_iv)
    }
}