package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.FilterKhususItem
import com.squareup.picasso.Picasso

class ListFilterKhususAdapter(
    private var data: List<FilterKhususItem>,
    private val listener: (FilterKhususItem) -> Unit

) : RecyclerView.Adapter<ListFilterKhususAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNamaTeacher = view.findViewById<TextView>(R.id.tv_nama_teacher_package)
        val tvRating = view.findViewById<TextView>(R.id.tv_rating)
        val btnLihatProfile = view.findViewById<Button>(R.id.btn_lihat_profile)
        val ivPhoto = view.findViewById<ImageView>(R.id.iv_teacher_product)
        fun bidnItem(
            data: FilterKhususItem,
            listener: (FilterKhususItem) -> Unit,
            context: Context,
            position: Int
        ) {

            tvNamaTeacher.text = data.name
            tvRating.text = data.rating
            Picasso.get().load(data.avatar).into(ivPhoto)

            itemView.setOnClickListener {
                listener(data)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_teacher_product, parent, false)
        return ListFilterKhususAdapter.LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size
}