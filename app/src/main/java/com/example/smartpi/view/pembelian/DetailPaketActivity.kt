package com.example.smartpi.view.pembelian

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityDetailPaketBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class DetailPaketActivity : AppCompatActivity() {
    var id_paket = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    lateinit var binding: ActivityDetailPaketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPaketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        id_paket = intent.getStringExtra("id_paket").toString()
        scope.launch { getDetailPaket() }
        binding.ivBackDetailPaket.setOnClickListener { finish() }

    }

    suspend fun getDetailPaket() {

        val networkConfig = NetworkConfig().getPaketLangganan().getDetailPaket(id_paket)

        try {
            if (networkConfig.isSuccessful) {
                val data = networkConfig.body()!!.data!!
                binding.tvNamaPaketDetailPaket.text = data.name
                binding.tvDeskripsiDetailPaket.text = data.desc
                binding.tvHargaDetailPaket.text = data.price
                binding.tvDurasiDetailPaket.text = data.durasi
                binding.tvSesiKelasDetailPaket.text = "${data.numberOfSession}x"
                binding.tvMenitDetailPaket.text = "${data.duration} Menit"

                binding.btnLanjutDetailPaket.setOnClickListener {
                    val intent = Intent(this, DetailPembelianActivity::class.java)
                    intent.putExtra("id", data.id.toString())
                    intent.putExtra("nama", data.name)
                    intent.putExtra("harga", data.price.toString())
                    intent.putExtra("durasi", data.durasi.toString())
                    intent.putExtra("jenis", "paket")
                    startActivity(intent)

                }
            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}