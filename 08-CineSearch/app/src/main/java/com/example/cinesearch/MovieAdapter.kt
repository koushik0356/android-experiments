package com.example.cinesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    private var movieList: List<MovieItem>,
    private val onItemClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.ivItemPoster)
        val tvTitle: TextView = view.findViewById(R.id.tvItemTitle)
        val tvYear: TextView = view.findViewById(R.id.tvItemYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.tvTitle.text = movie.title
        holder.tvYear.text = movie.year

        if (movie.poster != "N/A" && movie.poster.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(movie.poster)
                .into(holder.ivPoster)
        } else {
            holder.ivPoster.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    override fun getItemCount(): Int = movieList.size

    fun updateData(newList: List<MovieItem>) {
        movieList = newList
        notifyDataSetChanged()
    }
}