package com.example.smartpi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.PackageActiveItem
import java.text.SimpleDateFormat
import java.util.*

class ListPackageActiveProfileAdapter(
    private var data: List<PackageActiveItem>,
    private val listener: (PackageActiveItem) -> Unit

) : RecyclerView.Adapter<ListPackageActiveProfileAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNamaPaket: TextView = view.findViewById(R.id.tv_paket_aktif)
        val tvSesiOngoing: TextView = view.findViewById(R.id.tv_sesi_berjalan)
        val tvSesi: TextView = view.findViewById(R.id.tv_sesi_tersedia)

        fun bidnItem(
            data: PackageActiveItem,
            listener: (PackageActiveItem) -> Unit,
            context: Context,
            position: Int
        ) {

            tvNamaPaket.text = data.package_name
            tvSesiOngoing.text = data.on_going.toString()
            tvSesi.text = data.session_available.toString()

            itemView.setOnClickListener {
                listener(data)
            }
        }

        private fun String.toDate(
            dateFormat: String = "yyyy-MM-dd",
            timeZone: TimeZone = TimeZone.getTimeZone("UTC")
        ): Date {
            val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
            parser.timeZone = timeZone
            return parser.parse(this)
        }

        private fun Date.formatTo(
            dateFormat: String,
            timeZone: TimeZone = TimeZone.getDefault()
        ): String {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            formatter.timeZone = timeZone
            return formatter.format(this)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_paket_aktif, parent, false)
        return ListPackageActiveProfileAdapter.LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size
}