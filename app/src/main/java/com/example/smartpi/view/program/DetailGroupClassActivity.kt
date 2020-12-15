package com.example.smartpi.view.program

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityDetailGroupClassBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.pembelian.DetailPembelianActivity
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
    var token = ""
    var id = 0
    var price = 0F
    var nama = ""
    var durasi = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGroupClassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        scope.launch { getData() }
        binding.btnBeli.setOnClickListener {
            scope.launch {
                createGroupClass()
            }
        }
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

                price = kelas.price!!.toFloat()
                nama = kelas.namaKelas.toString()
                durasi = "-"
            }
            for (schedule in networkConfig.body()!!.data!!.schedule!!) {

                val tanggal = schedule!!.scheduleTime!!.toDate().formatTo("EEEE, dd MMMM yyyy")
                val waktuStart = schedule.scheduleTime!!.toDate().formatTo("HH:mm")
                val waktuEnd = schedule.scheduleEnd!!.toDate().formatTo("HH:mm")

                binding.tvCalendar.text = "$tanggal, $waktuStart-$waktuEnd"

            }
        }


    }

    private suspend fun createGroupClass() {
        if (price == 0F) {
            val networkConfig = NetworkConfig().getGroupClass().createGroupScheduleFree(token, id)
            if (networkConfig.isSuccessful) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this, "Pembuatan Group Class Berhasil", Toast.LENGTH_LONG).show()
                }
                finish()
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Pembuatan Group Class Gagal, Silahkan coba lagi atau cek jaringan anda",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            val intent = Intent(this, DetailPembelianActivity::class.java)
            intent.putExtra("id", id.toString())
            intent.putExtra("nama", nama)
            intent.putExtra("harga", price.toString())
            intent.putExtra("durasi", durasi.toString())
            intent.putExtra("jenis", "group")
            startActivity(intent)
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

