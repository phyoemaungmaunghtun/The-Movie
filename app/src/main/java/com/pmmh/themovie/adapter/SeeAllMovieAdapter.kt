package com.pmmh.themovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pmmh.themovie.R
import com.pmmh.themovie.model.Result
import com.pmmh.themovie.utilities.Utils
import android.widget.Filter
import android.widget.Filterable

class SeeAllMovieAdapter(val ctx: Context) :
    RecyclerView.Adapter<SeeAllMovieAdapter.MyViewHolder>() , Filterable{

    var movies: ArrayList<Result> = arrayListOf()
    var searchMovieList: ArrayList<Result> = arrayListOf()
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(ctx)
            .inflate(R.layout.single_movie_see_all, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val movie: Result = movies[position]

        Glide
            .with(ctx)
            .load(Utils.posterUrlMake(movie.posterPath))
            .into(viewHolder.poster)

        viewHolder.itemView.setOnClickListener {
            val movieId: String = movie.id.toString()
            onItemClick?.invoke(movieId)
        }

    }

    fun addMovie(movie: List<Result>) {
        searchMovieList.addAll(movie)
        movies.addAll(movie)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    movies = searchMovieList as ArrayList<Result>
                } else {
                    val resultList = ArrayList<Result>()
                    for (row in searchMovieList) {
                        if (row.title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    movies = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = movies
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movies = results?.values as ArrayList<Result>
                notifyDataSetChanged()
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster = itemView.findViewById<ImageView>(R.id.imageView_single_movie_seeAll)

    }
}