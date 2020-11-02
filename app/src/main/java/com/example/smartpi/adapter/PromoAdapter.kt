package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.JadwalItem

class PromoAdapter(
    /*private var data: List<DataItem>,
    private val listener: (DataItem) -> Unit*/
) : RecyclerView.Adapter<PromoAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bidnItem(
            data: JadwalItem,
            listener: (JadwalItem) -> Unit,
            context: Context,
            position: Int
        ) {
            itemView.setOnClickListener {
                listener(data)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoAdapter.LeagueViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.promo_list, parent, false)
        return LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: PromoAdapter.LeagueViewHolder, position: Int) {
        /*holder.bidnItem(data[position], listener, ContextAdapter, position)*/
    }

    override fun getItemCount(): Int = 7

}