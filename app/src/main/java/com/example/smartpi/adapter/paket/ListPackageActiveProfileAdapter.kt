package com.example.smartpi.adapter.paket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.PackageActiveItem

class ListPackageActiveProfileAdapter(
    private var data: List<PackageActiveItem>,
    private val listener: (PackageActiveItem) -> Unit

) : RecyclerView.Adapter<ListPackageActiveProfileAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNamaPaket: TextView = view.findViewById(R.id.tv_paket_aktif)
        private val tvSesiOngoing: TextView = view.findViewById(R.id.tv_sesi_berjalan)
        private val tvSesi: TextView = view.findViewById(R.id.tv_sesi_tersedia)

        fun bidnItem(
            data: PackageActiveItem,
            listener: (PackageActiveItem) -> Unit,
        ) {

            tvNamaPaket.text = data.package_name
            tvSesiOngoing.text = data.on_going.toString()
            tvSesi.text = data.session_available.toString()

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_paket_aktif, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener)
    }

    override fun getItemCount(): Int = data.size
}