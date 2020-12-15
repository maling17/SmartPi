package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.NotifItem

class ListNotifAdapter(
    private var data: List<NotifItem>,
    private val listener: (NotifItem) -> Unit
) : RecyclerView.Adapter<ListNotifAdapter.LeagueViewHolder>() {
    lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNotif: TextView = view.findViewById(R.id.tv_notifikasi)
        val tvSubNotif: TextView = view.findViewById(R.id.tv_sub_notifikasi)

        fun bidnItem(
            data: NotifItem,
            listener: (NotifItem) -> Unit,
            context: Context,
            position: Int
        ) {

            tvSubNotif.text = data.message
            itemView.setOnClickListener {
                listener(data)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_notif, parent, false)
        return ListNotifAdapter.LeagueViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.bidnItem(data[position], listener, contextAdapter, position)
    }

    override fun getItemCount(): Int = data.size


}