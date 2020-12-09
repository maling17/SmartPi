package com.example.smartpi.view.program

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityDetailGroupClassBinding
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailGroupClassActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailGroupClassBinding
    lateinit var preferences: Preferences
    private val job = Job()

    private val scope = CoroutineScope(job + Dispatchers.Main)
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGroupClassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        scope.launch { getData() }
    }

    private suspend fun getData() {
        id = intent.getStringExtra("id_group").toInt()

        val networkConfig = NetworkConfig().getGroupClass().getDetailGroupClass(id)
        if (networkConfig.isSuccessful) {

            for (kelas in networkConfig.body()!!.data!!.kelas!!) {
                binding.tvNamaGroup.text = kelas!!.namaKelas
                binding.tvDeskripsi.text = kelas.desc
                Picasso.get().load(kelas.gambar).into(binding.ivBannerDetailGroup)
                binding.tvHarga.text = "Rp${kelas.price}"
                binding.tvDurasi.text = "${kelas.duration} Jam"
                binding.tvNarasumber.text = kelas.teacher
                binding.tvKuota.text = "Kuota kelas max.${kelas.kuota} orang"
            }
            for (schedule in networkConfig.body()!!.data!!.schedule!!) {

                val tanggal = schedule!!.scheduleTime!!.toDate().formatTo("EEEE, dd MMMM yyyy")
                val waktuStart = schedule.scheduleTime!!.toDate().formatTo("HH:mm")
                val waktuEnd = schedule.scheduleEnd!!.toDate().formatTo("HH:mm")

                binding.tvCalendar.text = "$tanggal, $waktuStart-$waktuEnd"

            }
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