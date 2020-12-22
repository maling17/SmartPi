package com.example.smartpi.adapter

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
import com.example.smartpi.model.TeacherItem
import com.example.smartpi.view.profile.ProfileGuruActivity
import com.squareup.picasso.Picasso

class ListTeacherProductAdapter(
    private var data: List<TeacherItem>,
    private val listener: (TeacherItem) -> Unit

) : RecyclerView.Adapter<ListTeacherProductAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNamaTeacher = view.findViewById<TextView>(R.id.tv_nama_teacher_package)
        val tvRating = view.findViewById<TextView>(R.id.tv_rating)
        val btnLihatProfile = view.findViewById<Button>(R.id.btn_lihat_profile)
        val ivPhoto = view.findViewById<ImageView>(R.id.iv_teacher_product)
        fun bidnItem(
            data: TeacherItem,
            listener: (TeacherItem) -> Unit,
            context: Context,
            position: Int
        ) {

            if (data.rating.isNullOrEmpty()) {
                tvRating.text = 5F.toString()
            } else {
                tvRating.text = data.rating
            }
            tvNamaTeacher.text = data.name

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
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_teacher_product, parent, false)
        return ListTeacherProductAdapter.LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size
}