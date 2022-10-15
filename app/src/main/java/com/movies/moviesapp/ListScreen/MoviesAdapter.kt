package com.movies.moviesapp.ListScreen

import com.movies.moviesapp.ListScreen.MoviesItem
import androidx.recyclerview.widget.RecyclerView
import com.movies.moviesapp.ListScreen.MoviesAdapter.CustomViewHolder
import com.bumptech.glide.request.RequestOptions
import android.view.ViewGroup
import android.view.LayoutInflater
import com.movies.moviesapp.R
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.movies.moviesapp.Ressources.URLs
import android.content.Intent
import android.view.View
import android.widget.Filter
import com.movies.moviesapp.MovieDetail.MovieDetailMain
import android.widget.Filter.FilterResults
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Priority
import java.util.ArrayList

class MoviesAdapter(private val context: Context, private val moviesList: ArrayList<MoviesItem>) :
    RecyclerView.Adapter<CustomViewHolder>(), Filterable {
    private var moviesListFiltered: ArrayList<MoviesItem>
    var options: RequestOptions
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CustomViewHolder {
        val layout: View
        layout = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false)
        return CustomViewHolder(layout)
    }

    override fun onBindViewHolder(
        holder: CustomViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        Glide.with(context)
            .load(URLs.PICTURE_BASE_URL + moviesListFiltered[position].backdrop_path)
            .apply(options)
            .into(holder.imageMovie)
        holder.titreMovie.text = moviesListFiltered[position].title
        holder.ratingMovie.text =
            moviesListFiltered[position].vote_average.toString() + " (" + moviesListFiltered[position].vote_count + ")"
        holder.originalLanguageMovie.text = moviesListFiltered[position].original_language
        holder.releasedDateMovie.text = moviesListFiltered[position].release_date
        holder.favoris_iv.setMaxProgress(0.35f)
        holder.favoris_iv.playAnimation()
        if (moviesListFiltered[position].isFavorite) {
            holder.favoris_iv.setMinProgress(0.35f)
            holder.favoris_iv.setMaxProgress(1f)
            holder.favoris_iv.playAnimation()
        }
        holder.itemView.setOnClickListener {
            val goToDetails = Intent(context, MovieDetailMain::class.java)
            goToDetails.putExtra("movieSelected", moviesListFiltered[position])
            context.startActivity(goToDetails)
        }
        holder.favoris_iv_container.setOnClickListener { view: View? ->
            moviesListFiltered[position].isFavorite = !moviesListFiltered[position].isFavorite
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return moviesListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val Key = constraint.toString()
                val lstFiltered = ArrayList<MoviesItem>()
                moviesListFiltered = if (Key.isEmpty()) {
                    moviesList
                } else {
                    for (row in moviesList) {
                        if (row.title.toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row)
                        }
                    }
                    lstFiltered
                }
                val filterResults = FilterResults()
                filterResults.values = moviesListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                moviesListFiltered = results.values as ArrayList<MoviesItem>
                notifyDataSetChanged()
            }
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageMovie: ImageView
        var titreMovie: TextView
        var ratingMovie: TextView
        var releasedDateMovie: TextView
        var originalLanguageMovie: TextView
        var favoris_iv_container: MaterialCardView
        var favoris_iv: LottieAnimationView

        init {
            imageMovie = itemView.findViewById(R.id.imageMovie)
            titreMovie = itemView.findViewById(R.id.titreMovie)
            ratingMovie = itemView.findViewById(R.id.ratingMovie)
            releasedDateMovie = itemView.findViewById(R.id.releasedDateMovie)
            originalLanguageMovie = itemView.findViewById(R.id.originalLanguageMovie)
            favoris_iv_container = itemView.findViewById(R.id.favoris_iv_container)
            favoris_iv = itemView.findViewById(R.id.favoris_iv)
        }
    }

    init {
        moviesListFiltered = moviesList
        options = RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.ic_notfound)
            .error(R.drawable.ic_notfound)
            .priority(Priority.HIGH)
    }
}