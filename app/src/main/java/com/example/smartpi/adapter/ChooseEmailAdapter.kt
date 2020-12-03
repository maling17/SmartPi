package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.UserInputData

class ChooseEmailAdapter(
    var data: List<UserInputData>,
    var listener: (UserInputData) -> Unit
) : RecyclerView.Adapter<ChooseEmailAdapter.LeagueViewHolder>() {
    lateinit var context: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNama = view.findViewById<TextView>(R.id.tv_nama_user)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email_user)
        fun bindItem(
            data: UserInputData,
            listener: (UserInputData) -> Unit,
            context: Context,
            position: Int
        ) {

            tvNama.text = data.name
            tvEmail.text = data.email

            itemView.setOnClickListener {
                listener(data)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_email, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.bindItem(data[position], listener, context, position)
    }

    override fun getItemCount(): Int = data.size
}