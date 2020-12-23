package com.example.smartpi.adapter.groupclass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.JadwalItem
import java.text.SimpleDateFormat
import java.util.*

class ListGroupClassActiveAdapter(
    private var data: List<JadwalItem>,
    private val listener: (JadwalItem) -> Unit
) : RecyclerView.Adapter<ListGroupClassActiveAdapter.LeagueViewHolder>() {
    lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNamaPaket: TextView = view.findViewById(R.id.tv_paket_aktif)
        private val tvSesiOngoing: TextView = view.findViewById(R.id.tv_sesi_berjalan)
        private val tvSesi: TextView = view.findViewById(R.id.tv_sesi_tersedia)

        fun bidnItem(
            data: JadwalItem,
            listener: (JadwalItem) -> Unit,
        ) {

            tvNamaPaket.text = data.packageName
            tvSesiOngoing.text = "-"
            tvSesi.text = "-"

            itemView.setOnClickListener {
                listener(data)

            }

        }

        fun String.toDate(
            dateFormat: String = "yyyy-MM-dd HH:mm:ss",
            timeZone: TimeZone = TimeZone.getTimeZone("UTC")
        ): Date {
            val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
            parser.timeZone = timeZone
            return parser.parse(this)
        }

        fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            formatter.timeZone = timeZone
            return formatter.format(this)
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