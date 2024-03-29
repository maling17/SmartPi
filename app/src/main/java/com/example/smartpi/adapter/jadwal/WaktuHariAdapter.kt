package com.example.smartpi.adapter.jadwal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R

class WaktuHariAdapter(
    private var data: List<String>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<WaktuHariAdapter.LeagueViewHolder>() {
    private lateinit var contextAdapter: Context
    private var rowIndex: Int? = null

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam = view.findViewById<TextView>(R.id.tv_waktu_hari)!!
        val llSlot = view.findViewById<LinearLayout>(R.id.ll_waktu_hari)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.waktu_hari_list, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.tvJam.text = data[position]

        if (rowIndex == position) {
            holder.llSlot.setBackgroundColor(Color.parseColor("#ECEBFF"))
        } else {
            holder.llSlot.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        holder.itemView.setOnClickListener {
            listener(data[position])
            rowIndex = position
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int = data.size

}