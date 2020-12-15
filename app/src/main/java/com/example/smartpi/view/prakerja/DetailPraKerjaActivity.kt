package com.example.smartpi.view.prakerja

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityDetailPraKerjaBinding
import com.example.smartpi.model.PraKerjaItem
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.pembelian.DetailPembelianActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailPraKerjaActivity : AppCompatActivity() {

    val TAG = "MyActivity"
    var token = ""
    lateinit var preferences: Preferences
    lateinit var binding: ActivityDetailPraKerjaBinding
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var id = 0
    var price = 0F
    var nama = ""
    var durasi = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPraKerjaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getData()
        binding.btnBeli.setOnClickListener {
            scope.launch { createGroupClass() }
        }
        binding.ivBackDetailKelas.setOnClickListener {
            finish()
        }
    }

    fun getData() {
        val data = intent.getParcelableExtra<PraKerjaItem>("data")
        binding.tvDeskripsi.text = data.desc.toString()
        binding.tvHarga.text = data.price
        binding.tvNamaPraKerja.text = data.namaKelas
        binding.tvCalendar.text = data.duration.toString()
        binding.tvDurasi.text = "1 pertemuan @${data.duration} menit"
        Picasso.get().load(data.img).into(binding.ivBannerDetailPraKerja)
        val beforePrice = data.price
        price = beforePrice!!.replace("IDR ", "").toFloat()
        id = data.id!!.toInt()
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
}