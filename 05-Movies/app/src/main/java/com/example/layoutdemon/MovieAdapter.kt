package com.example.layoutdemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private val movies: List<Movie>,
    private var layoutResId: Int
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var isHybridMode: Boolean = false

    fun setLayoutResId(newLayoutResId: Int, hybrid: Boolean = false) {
        layoutResId = newLayoutResId
        isHybridMode = hybrid
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHybridMode) {
            if (position < 3) R.layout.item_movie_list else R.layout.item_movie_grid
        } else {
            layoutResId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImage: ImageView? = itemView.findViewById(R.id.movieImage)
        private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        private val movieYear: TextView = itemView.findViewById(R.id.movieYear)

        fun bind(movie: Movie) {
            movieImage?.setImageResource(movie.imageResId)
            movieTitle.text = movie.title
            movieYear.text = movie.year
        }
    }
}