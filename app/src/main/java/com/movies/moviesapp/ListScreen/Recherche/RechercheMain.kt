package com.movies.moviesapp.ListScreen.Recherche

import android.content.Context
import com.movies.moviesapp.ListScreen.MoviesItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.movies.moviesapp.ListScreen.MoviesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.movies.moviesapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import java.util.ArrayList

class RechercheMain(
    private val contextActivity: Context,
    private val moviesList: ArrayList<MoviesItem>
) : BottomSheetDialogFragment() {
    private var customView: View? = null
    private var mContext: Context? = null
    private var moviesRV: RecyclerView? = null
    private var aucunContenuTrouver: LinearLayout? = null
    private var btnAucuneItem: TextView? = null
    private var textAucunItem: TextView? = null
    private var rechercheInput: TextInputEditText? = null
    private var moviesAdapter: MoviesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.recherche_movies_fragment, container, false)
        customView = rootView
        mContext = rootView.context
        findsViewByIds()
        btnAucuneItem!!.setOnClickListener { view: View? -> dismiss() }
        if (moviesList.size > 0) {
            moviesAdapter = MoviesAdapter(rootView.context, moviesList)
            moviesRV!!.adapter = moviesAdapter
            moviesRV!!.layoutManager = LinearLayoutManager(mContext)
            showContenu()
        } else {
            showAucunContenu()
        }
        rechercheInput!!.setText("")
        rechercheInput!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                moviesAdapter!!.filter.filter(rechercheInput!!.text.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
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
        rechercheInput = customView!!.findViewById(R.id.rechercheInput)
    }
}