package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.PaketItem

class PaketLanggananAdapter(
    private val data: List<PaketItem>,
    private val listener: (PaketItem) -> Unit
) : RecyclerView.Adapter<PaketLanggananAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNamaPaket = view.findViewById<TextView>(R.id.tv_nama_paket_langganan)
        val tv_deskripsi = view.findViewById<TextView>(R.id.tv_deskripsi_paket_langganan)
        val tvHargra = view.findViewById<TextView>(R.id.tv_harga_paket_langganan)

        fun bindItem(
            data: PaketItem,
            listener: (PaketItem) -> Unit,
            context: Context,
            position: Int
        ) {

            tvNamaPaket.text = data.name
            tv_deskripsi.text = data.desc
            tvHargra.text = data.price

            itemView.setOnClickListener {
                listener(data)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_paket_langganan, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size


}