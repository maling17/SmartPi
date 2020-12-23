package com.example.smartpi.adapter.paket

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

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNamaPaket = view.findViewById<TextView>(R.id.tv_nama_paket_langganan)
        private val tvDeskripsi = view.findViewById<TextView>(R.id.tv_deskripsi_paket_langganan)
        private val tvHargra = view.findViewById<TextView>(R.id.tv_harga_paket_langganan)

        fun bindItem(
            data: PaketItem,
            listener: (PaketItem) -> Unit,
        ) {

            tvNamaPaket.text = data.name
            tvDeskripsi.text = data.desc
            tvHargra.text = data.price

            itemView.setOnClickListener {
                listener(data)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_paket_langganan, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.bindItem(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

}