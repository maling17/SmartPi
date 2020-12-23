package com.example.smartpi.adapter.filter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.FilterUmumItem
import com.example.smartpi.view.profile.ProfileGuruActivity
import com.squareup.picasso.Picasso

class ListFilterAdapter(
    private var data: List<FilterUmumItem>,
    private val listener: (FilterUmumItem) -> Unit

) : RecyclerView.Adapter<ListFilterAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNamaTeacher = view.findViewById<TextView>(R.id.tv_nama_teacher_package)
        private val tvRating = view.findViewById<TextView>(R.id.tv_rating)
        private val btnLihatProfile = view.findViewById<Button>(R.id.btn_lihat_profile)
        private val ivPhoto = view.findViewById<ImageView>(R.id.iv_teacher_product)
        fun bidnItem(
            data: FilterUmumItem,
            listener: (FilterUmumItem) -> Unit,
            context: Context,
        ) {

            tvNamaTeacher.text = data.name
            tvRating.text = data.rating
            Picasso.get().load(data.avatar).into(ivPhoto)

            btnLihatProfile.setOnClickListener {
                val intent = Intent(context, ProfileGuruActivity::class.java)
                intent.putExtra("id_guru", data.id.toString())
                intent.putExtra("nama_guru", data.name)
                intent.putExtra("rating", data.rating.toString())
                context.startActivity(intent)
            }

            itemView.setOnClickListener {
                listener(data)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_teacher_product, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, contextAdapter)
    }

    override fun getItemCount(): Int = data.size
}