package com.example.smartpi.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.model.HistoryItem
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListKelasSelesaiAdapter(
    private var data: ArrayList<HistoryItem>,
    private val listener: (HistoryItem) -> Unit

) : RecyclerView.Adapter<ListKelasSelesaiAdapter.LeagueViewHolder>() {

    private lateinit var ContextAdapter: Context
    var scheduleId = ""
    lateinit var preferences: Preferences
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var isiRadioButton = ""

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvPaket = view.findViewById<TextView>(R.id.tv_nama_kelas)
        val btnBelum: Button = view.findViewById(R.id.btn_belum)
        val btnSudah: Button = view.findViewById(R.id.btn_sudah)
        val btnTutup: ImageView = view.findViewById(R.id.iv_tutup)
        lateinit var contextAdapter: Context


        @SuppressLint("SetTextI18n")
        fun bidnItem(
            data: HistoryItem,
            listener: (HistoryItem) -> Unit,
            context: Context,
            position: Int
        ) {

            itemView.setOnClickListener {
                listener(data)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_konfirmasi_kelas, parent, false)
        return ListKelasSelesaiAdapter.LeagueViewHolder(inflatedView)


    }

    override fun onBindViewHolder(holder: ListKelasSelesaiAdapter.LeagueViewHolder, position: Int) {
//        holder.bidnItem(data[position], listener, ContextAdapter, position)
        scheduleId = data[position].id.toString()
        val namaGuru = data[position].teacherName
        val asalGuru = data[position].teacherOrigin
        val namaPaket = data[position].packageName
        val tanggal = data[position].scheduleTime
        val image = data[position].teacherAvatar
        holder.tvPaket.text =
            "Apakah kelas ${data[position].packageName} sudah selesai? kalau sudah yuk konfirmasi dulu..."

        holder.btnSudah.setOnClickListener {
            showPopUpKelasSelesai(
                namaGuru!!,
                namaPaket!!,
                asalGuru!!,
                image!!,
                tanggal!!
            )
        }
        holder.btnBelum.setOnClickListener { showPopUpKelasBermasalah() }
        holder.btnTutup.setOnClickListener { holder.itemView.visibility = View.GONE }

    }

    override fun getItemCount(): Int = data.size

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

    private suspend fun rateKelas(scheduleId: String, rateTeacher: Float, feedback: String) {
        preferences = Preferences(ContextAdapter)
        val token = "Bearer ${preferences.getValues("token")}"
        val networkConfig =
            NetworkConfig().getAfterClass().rateClass(token, scheduleId, rateTeacher, feedback)
        if (networkConfig.isSuccessful) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    ContextAdapter,
                    "Berhasil rate kelas",
                    Toast.LENGTH_LONG
                ).show()

            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    ContextAdapter,
                    "Gagal Rate Kelas",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun inputKelasBermasalah(scheduleId: String, alasan: String) {
        preferences = Preferences(ContextAdapter)
        val token = "Bearer ${preferences.getValues("token")}"
        val networkConfig =
            NetworkConfig().getAfterClass().inputKelasBermasalah(token, scheduleId, alasan)
        if (networkConfig.isSuccessful) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    ContextAdapter,
                    "Berhasil input kelas bermasalah",
                    Toast.LENGTH_LONG
                ).show()

            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    ContextAdapter,
                    "Gagal  input kelas bermasalah",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun showPopUpKelasSelesai(
        namaGuru: String,
        namaPaket: String,
        asal: String,
        image: String,
        tanggal: String
    ) {
        val dialog = Dialog(ContextAdapter)
        dialog.setContentView(R.layout.pop_up_kelas_selesai)
        dialog.setCancelable(false)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnSimpan = dialog.findViewById<Button>(R.id.btn_simpan_pop_up_kelas_selesai)
        val etFeedback: EditText = dialog.findViewById(R.id.et_kesan_pop_up_kelas_selesai)
        val rbRating: RatingBar = dialog.findViewById(R.id.rb_guru_pop_up_kelas_selesai)
        val tvNamaGuru: TextView =
            dialog.findViewById(R.id.tv_nama_teacher_pop_up_kelas_selesai)
        val tvAsalGuru: TextView = dialog.findViewById(R.id.tv_asal_guru_pop_up_kelas_selesai)
        val ivGuru: ImageView = dialog.findViewById(R.id.iv_teacher_pop_up_kelas_selesai)
        val tvPaket: TextView = dialog.findViewById(R.id.tv_nama_paket_pop_up_kelas_selesai)
        val tvTanggal: TextView = dialog.findViewById(R.id.tv_tanggal_pop_up_kelas_selesai)

        tvNamaGuru.text = namaGuru
        tvAsalGuru.text = asal
        Picasso.get().load(image).into(ivGuru)
        tvPaket.text = namaPaket
        tvTanggal.text = tanggal.toDate().formatTo("dd MMMM yyyy")

        btnSimpan.setOnClickListener {
            val feedback = etFeedback.text.toString()
            val rating = rbRating.rating
            scope.launch {
                rateKelas(scheduleId, rating, feedback)
            }

            dialog.dismiss()
        }
        dialog.show()

    }

    private fun showPopUpKelasBermasalah() {
        val dialog = Dialog(ContextAdapter)
        dialog.setContentView(R.layout.pop_up_kelas_bermasalah)
        dialog.setCancelable(false)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnSimpan = dialog.findViewById<Button>(R.id.btn_simpan_history_bermasasalah_pop_up)
        val etAlasan: EditText = dialog.findViewById(R.id.et_alasan_pop_up)
        val rgAlasan: RadioGroup = dialog.findViewById(R.id.rgAlasan_pop_up)

        btnSimpan.setOnClickListener {

            val intSelectButton =
                rgAlasan.checkedRadioButtonId
            val radioButton: RadioButton = dialog.findViewById(intSelectButton)

            when (radioButton.text.toString()) {
                "Jaringan bermasalah" -> isiRadioButton =
                    radioButton.text.toString()
                "Guru tidak hadir" -> isiRadioButton =
                    radioButton.text.toString()
                "Lainnya" -> isiRadioButton =
                    etAlasan.text.toString()
            }

            scope.launch {
                inputKelasBermasalah(scheduleId, isiRadioButton)
            }

            dialog.dismiss()
        }
        dialog.show()

    }

}