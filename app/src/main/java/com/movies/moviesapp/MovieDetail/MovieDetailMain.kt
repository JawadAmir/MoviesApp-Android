package com.movies.moviesapp.MovieDetail

import androidx.appcompat.app.AppCompatActivity
import com.movies.moviesapp.ListScreen.MoviesItem
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.airbnb.lottie.LottieAnimationView
import android.widget.LinearLayout
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.android.volley.Request
import com.movies.moviesapp.R
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.movies.moviesapp.Ressources.URLs
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import com.android.volley.VolleyError
import com.bumptech.glide.Priority

class MovieDetailMain : AppCompatActivity() {
    private var movieSelected: MoviesItem? = null
    private var imageMovie: ImageView? = null
    private var titreMovie: TextView? = null
    private var genreMovie: TextView? = null
    private var releasedDateMovie: TextView? = null
    private var originalLanguageMovie: TextView? = null
    private var overviewMovie: TextView? = null
    private var favoris_iv_container: MaterialCardView? = null
    private var favoris_iv: LottieAnimationView? = null
    private var revenusContainer: LinearLayout? = null
    private var budgetContainer: LinearLayout? = null
    private var budget: TextView? = null
    private var revenus: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)
        findsViewByIds()
        if (intent != null && intent.extras != null) {
            movieSelected = intent.extras!!.getSerializable("movieSelected") as MoviesItem?
            val options = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_notfound)
                .error(R.drawable.ic_notfound)
                .priority(Priority.HIGH)
            Glide.with(this)
                .load(URLs.PICTURE_BASE_URL + movieSelected!!.backdrop_path)
                .apply(options)
                .into(imageMovie!!)
            titreMovie!!.text = movieSelected!!.title
            genreMovie!!.text = "Drame"
            overviewMovie!!.text = movieSelected!!.overview
            originalLanguageMovie!!.text = movieSelected!!.original_language
            releasedDateMovie!!.text = movieSelected!!.release_date
            favoris_iv!!.setMaxProgress(0.35f)
            favoris_iv!!.playAnimation()
            checkFavorite()
            favoris_iv_container!!.setOnClickListener { view: View? ->
                movieSelected!!.isFavorite = !movieSelected!!.isFavorite
                checkFavorite()
            }
            detailsMovie
        }
    }

    fun checkFavorite() {
        if (movieSelected!!.isFavorite) {
            favoris_iv!!.setMinProgress(0.35f)
            favoris_iv!!.setMaxProgress(1f)
        } else {
            favoris_iv!!.progress = 0f
            favoris_iv!!.setMaxProgress(0.35f)
        }
        favoris_iv!!.playAnimation()
    }

    private fun findsViewByIds() {
        imageMovie = findViewById(R.id.imageMovie)
        titreMovie = findViewById(R.id.titreMovie)
        genreMovie = findViewById(R.id.genreMovie)
        releasedDateMovie = findViewById(R.id.releasedDateMovie)
        originalLanguageMovie = findViewById(R.id.originalLanguageMovie)
        overviewMovie = findViewById(R.id.overviewMovie)
        favoris_iv_container = findViewById(R.id.favoris_iv_container)
        favoris_iv = findViewById(R.id.favoris_iv)
        budgetContainer = findViewById(R.id.budgetContainer)
        revenusContainer = findViewById(R.id.revenusContainer)
        revenus = findViewById(R.id.revenus)
        budget = findViewById(R.id.budget)
    }

    val detailsMovie: Unit
        get() {
            val requestQueue = Volley.newRequestQueue(this@MovieDetailMain)
            val postRequest = StringRequest(
                Request.Method.GET,
                URLs.GET_DETAIL_MOVIE_URL + movieSelected!!.id + "?api_key=" + URLs.API_KEY,
                { response: String ->
                    Log.d("getMovies", "getMovies: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        val genres = jsonObj.getJSONArray("genres")
                        for (i in 0 until genres.length()) {
                            val genresJSONObject = genres.getJSONObject(i)
                            if (i == 0) {
                                genreMovie!!.text = genresJSONObject.getString("name")
                            } else {
                                genreMovie!!.text =
                                    genreMovie!!.text.toString() + ", " + genresJSONObject.getString(
                                        "name"
                                    )
                            }
                        }
                        budgetContainer!!.visibility = View.VISIBLE
                        budget!!.text = jsonObj.getString("budget") + " $"
                        revenusContainer!!.visibility = View.VISIBLE
                        revenus!!.text = jsonObj.getString("revenue") + " $"
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { obj: VolleyError -> obj.printStackTrace() }
            requestQueue.add(postRequest)
        }
}