package com.example.smartpi.adapter.groupclass

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.model.PraKerjaItem
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ListPraKerjaAdapter(
    private var data: ArrayList<PraKerjaItem>,
    private val listener: (PraKerjaItem) -> Unit

) : RecyclerView.Adapter<ListPraKerjaAdapter.LeagueViewHolder>() {

    private lateinit var contextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvJudul: TextView = view.findViewById(R.id.tv_nama_pra_kerja)
        private val tvTanggal: TextView = view.findViewById(R.id.tv_durasi_pra_kerja)
        private val tvWaktu: TextView = view.findViewById(R.id.tv_jumlah_sesi)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga_pra_kerja)
        private val ivBanner: ImageView = view.findViewById(R.id.iv_banner_pra_kerja)

        @SuppressLint("SetTextI18n")
        fun bidnItem(
            data: PraKerjaItem,
            listener: (PraKerjaItem) -> Unit,
        ) {

            val namaGroup = data.namaKelas
            val tanggalStart =
                data.duration
            val waktuStart = data.duration
            val harga = data.price

            tvHarga.text = "$harga"
            tvJudul.text = namaGroup
            tvTanggal.text = "$tanggalStart"
            tvWaktu.text = "1x pertemuan @$waktuStart menit"

            Picasso.get().load(data.img).into(ivBanner)

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
        val inflatedView: View = layoutInflater.inflate(R.layout.list_pra_kerja, parent, false)
        return LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bidnItem(data[position], listener)
    }

    override fun getItemCount(): Int = data.size


}