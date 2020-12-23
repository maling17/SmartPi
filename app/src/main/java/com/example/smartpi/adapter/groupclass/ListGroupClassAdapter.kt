package com.example.smartpi.adapter.groupclass

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.DetailData
import com.example.smartpi.view.program.DetailGroupClassActivity
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ListGroupClassAdapter(
    private var data: ArrayList<DetailData>,
    private val listener: (DetailData) -> Unit

) : RecyclerView.Adapter<ListGroupClassAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvJudul: TextView = view.findViewById(R.id.tv_nama_group)
        private val tvTanggal: TextView = view.findViewById(R.id.tv_tanggal_group_class)
        private val tvWaktu: TextView = view.findViewById(R.id.tv_waktu_group_class)
        private val tvKuota: TextView = view.findViewById(R.id.tv_kuota_group_class)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga_group_class)
        private val ivBanner: ImageView = view.findViewById(R.id.iv_banner)

        @SuppressLint("SetTextI18n")
        fun bidnItem(
            data: DetailData,
            listener: (DetailData) -> Unit,
            context: Context,
            position: Int
        ) {

            val namaGroup = data.kelas!![position]!!.namaKelas
            val tanggalStart =
                data.schedule!![position]!!.scheduleTime!!.toDate().formatTo("dd MMMM yyyy")
            val tanggalEnd =
                data.schedule!![position]!!.scheduleEnd!!.toDate().formatTo("dd MMMM yyyy")
            val waktuStart = data.schedule!![position]!!.scheduleTime!!.toDate().formatTo("HH:mm")
            val waktuEnd = data.schedule!![position]!!.scheduleEnd!!.toDate().formatTo("HH:mm")
            val kuota = data.kelas!![position]!!.kuota
            val harga = data.kelas!![position]!!.price

            tvHarga.text = "Rp$harga"
            tvJudul.text = namaGroup
            tvTanggal.text = "$tanggalStart - $tanggalEnd"
            tvWaktu.text = "$waktuStart - $waktuEnd"
            tvKuota.text = "Sisa Kuota $kuota"

            Picasso.get().load(data.kelas!![position]!!.gambar).into(ivBanner)

            itemView.setOnClickListener {
                listener(data)
                val intent = Intent(context, DetailGroupClassActivity::class.java).putExtra(
                    "id_group",
                    data.kelas!![position]!!.id.toString()
                )
                context.startActivity(intent)
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
        val inflatedView: View = layoutInflater.inflate(R.layout.list_group_class, parent, false)
        return LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener, contextAdapter, position)
    }

    override fun getItemCount(): Int = data.size


}