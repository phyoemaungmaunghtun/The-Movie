package com.pmmh.themovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pmmh.themovie.R
import com.pmmh.themovie.model.movieCredit.Cast
import com.pmmh.themovie.utilities.Utils

class MovieCastAdapter(val ctx: Context, val people: List<Cast>) :
    RecyclerView.Adapter<MovieCastAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(ctx)
            .inflate(R.layout.cast_or_crew_single, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val cast: Cast = people[position]

        Glide
            .with(ctx)
            .load(Utils.posterUrlMake(cast.profilePath))
            .into(viewHolder.profile_cast_single)


        viewHolder.title_cast_single.text = cast.originalName
        viewHolder.title2_cast_single.text = cast.character

    }

    override fun getItemCount(): Int {
        return people.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile_cast_single = itemView.findViewById<ImageView>(R.id.profile_cast_single)
        val title_cast_single = itemView.findViewById<TextView>(R.id.title_cast_single)
        val title2_cast_single = itemView.findViewById<TextView>(R.id.title2_cast_single)
    }
}