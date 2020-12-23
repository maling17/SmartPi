package com.example.smartpi.adapter.kelas

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.WaitingItem
import java.text.SimpleDateFormat
import java.util.*

class ListWaitingConfirmationAdapter(
    private var data: List<WaitingItem>,
    private val listener: (WaitingItem) -> Unit

) : RecyclerView.Adapter<ListWaitingConfirmationAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvPaket = view.findViewById<TextView>(R.id.tv_nama_paket_waiting)
        private val tvNamaGuru = view.findViewById<TextView>(R.id.tv_nama_guru_waiting)
        private val tvTglPaket = view.findViewById<TextView>(R.id.tv_tanggal_waiting)

        @SuppressLint("SetTextI18n")
        fun bidnItem(
            data: WaitingItem,
            listener: (WaitingItem) -> Unit,
        ) {
            val tanggal = data.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
            tvTglPaket.text = tanggal
            tvNamaGuru.text = "Guru: ${data.teacherName}"
            tvPaket.text = "${data.packageName}"

            itemView.setOnClickListener {
                listener(data)
            }
        }
        private fun String.toDate(
            dateFormat: String = "yyyy-MM-dd HH:mm:ss",
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
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_waiting, parent, false)
        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener)
    }

    override fun getItemCount(): Int = data.size
}