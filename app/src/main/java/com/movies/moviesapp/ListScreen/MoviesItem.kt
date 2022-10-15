package com.movies.moviesapp.ListScreen

import java.io.Serializable

class MoviesItem(
    var poster_path: String,
    var isAdult: Boolean,
    var overview: String,
    var release_date: String,
    var id: String,
    var original_title: String,
    var original_language: String,
    var title: String,
    var backdrop_path: String,
    var popularity: Double,
    var vote_count: Int,
    var isVideo: Boolean,
    var vote_average: Double,
    var isFavorite: Boolean
) : Serializable