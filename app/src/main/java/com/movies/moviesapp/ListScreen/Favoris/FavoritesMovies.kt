package com.movies.moviesapp.ListScreen.Favoris

import android.content.Context
import com.movies.moviesapp.ListScreen.MoviesItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movies.moviesapp.R
import com.movies.moviesapp.ListScreen.MoviesAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.ArrayList

class FavoritesMovies(contextActivity: Context?, private val moviesList: ArrayList<MoviesItem>) :
    BottomSheetDialogFragment() {
    private var customView: View? = null
    private var moviesRV: RecyclerView? = null
    private var aucunContenuTrouver: LinearLayout? = null
    private var btnAucuneItem: TextView? = null
    private var textAucunItem: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.favorite_movies_fragment, container, false)
        customView = rootView
        val mContext = rootView.context
        findsViewByIds()
        btnAucuneItem!!.setOnClickListener { view: View? -> dismiss() }
        if (moviesList.size > 0) {
            val moviesAdapter = MoviesAdapter(mContext, moviesList)
            moviesRV!!.adapter = moviesAdapter
            moviesRV!!.layoutManager = LinearLayoutManager(mContext)
            showContenu()
        } else {
            showAucunContenu()
        }
        return rootView
    }

    fun showAucunContenu() {
        aucunContenuTrouver!!.visibility = View.VISIBLE
        textAucunItem!!.text = resources.getString(R.string.aucun_film_disponible)
        moviesRV!!.visibility = View.GONE
    }

    fun showContenu() {
        aucunContenuTrouver!!.visibility = View.GONE
        moviesRV!!.visibility = View.VISIBLE
    }

    private fun findsViewByIds() {
        moviesRV = customView!!.findViewById(R.id.moviesRV)
        aucunContenuTrouver = customView!!.findViewById(R.id.aucunContenuTrouver)
        btnAucuneItem = customView!!.findViewById(R.id.btnAucuneItem)
        textAucunItem = customView!!.findViewById(R.id.textAucunItem)
    }
}