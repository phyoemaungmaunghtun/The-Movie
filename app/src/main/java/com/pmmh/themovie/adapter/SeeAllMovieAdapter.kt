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


class SeeAllMovieAdapter(val ctx: Context) :
    RecyclerView.Adapter<SeeAllMovieAdapter.MyViewHolder>() {

    var movies: ArrayList<Result> = arrayListOf()

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

        /*viewHolder.itemView.setOnClickListener {
            val intent = Intent(ctx,MovieDetailsActivity::class.java)
            val movieId:String = movie.id.toString()
            intent.putExtra("MovieIdPass",movieId)
            ctx.startActivity(intent)
        }*/

    }

    fun addMovie(movie: List<Result>) {
        movies.addAll(movie)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster = itemView.findViewById<ImageView>(R.id.imageView_single_movie_seeAll)

    }
}