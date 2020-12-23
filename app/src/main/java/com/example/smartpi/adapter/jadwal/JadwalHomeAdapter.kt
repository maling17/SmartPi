package com.example.smartpi.adapter.jadwal

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

class JadwalHomeAdapter(
    private var data: List<JadwalItem>,
    private val listener: (JadwalItem) -> Unit
) : RecyclerView.Adapter<JadwalHomeAdapter.LeagueViewHolder>() {
    lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTanggalJadwal = view.findViewById<TextView>(R.id.tv_tgl_jadwal)
        private val tvKelas = view.findViewById<TextView>(R.id.tv_kelas)
        private val tvJamKelas = view.findViewById<TextView>(R.id.tv_jam_kelas)
        private val tvGuru = view.findViewById<TextView>(R.id.tv_guru)

        fun bidnItem(
            data: JadwalItem,
            listener: (JadwalItem) -> Unit,
        ) {

            val kelas = data.packageName.toString()
            val guru = data.teacherName.toString()
            val jadwal = data.scheduleTime.toString()

            val tanggalJadwal = jadwal.toDate().formatTo("dd, MMMM yyyy")
            val waktuJadwal=jadwal.toDate().formatTo("HH:mm")

            tvKelas.text = kelas
            tvGuru.text = guru
            tvTanggalJadwal.text = tanggalJadwal
            tvJamKelas.text=waktuJadwal


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
        val inflatedView: View = layoutInflater.inflate(R.layout.list_jadwal_home, parent, false)
        return LeagueViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {

        holder.bidnItem(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

}