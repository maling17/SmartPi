package com.example.smartpi.view.lain

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivitySyaratDanLainBinding
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class SyaratDanLainActivity : AppCompatActivity() {
    lateinit var preferences: Preferences
    var token = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var kategori = ""
    lateinit var binding: ActivitySyaratDanLainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyaratDanLainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.pbLoadingSyarat.visibility = View.VISIBLE
        kategori = intent.getStringExtra("kategori").toString()
        when (kategori) {
            "bantuan" -> {
                binding.tvJudulSyaratDanLain.text = "Bantuan"
                scope.launch { getBantuan() }
            }
            "syarat" -> {
                binding.tvJudulSyaratDanLain.text = "Syarat dan Ketentuan"
                scope.launch { getSyarat() }
            }
            "tentang_kami" -> {
                binding.tvJudulSyaratDanLain.text = "Tentang Kami"
                scope.launch { getTentangKami() }
            }
            "kebijakan" -> {
                binding.tvJudulSyaratDanLain.text = "Kebijakan Privasi"
                scope.launch { getKebijakan() }
            }
        }
        binding.ivBackSyaratDanLain.setOnClickListener { finish() }
    }

    private suspend fun getSyarat() {
        val network = NetworkConfig().syarat().getSyarat()

        try {
            if (network.isSuccessful) {
                val syarat = network.body()!!.data!!.syarat
                val htmlSyarat: Spanned =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Html.fromHtml(syarat, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(syarat)
                    }
                binding.pbLoadingSyarat.visibility = View.GONE
                binding.tvDescSyaratDanLain.text = htmlSyarat
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun getKebijakan() {
        val network = NetworkConfig().syarat().getKebijakan()
        try {
            if (network.isSuccessful) {
                val syarat = network.body()!!.data!!.syarat
                val htmlSyarat: Spanned =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Html.fromHtml(syarat, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(syarat)
                    }
                binding.pbLoadingSyarat.visibility = View.GONE
                binding.tvDescSyaratDanLain.text = htmlSyarat
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun getBantuan() {
        val network = NetworkConfig().syarat().getBantuan()

        try {
            if (network.isSuccessful) {
                val syarat = network.body()!!.data!!.syarat
                val htmlSyarat: Spanned =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Html.fromHtml(syarat, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(syarat)
                    }
                binding.pbLoadingSyarat.visibility = View.GONE
                binding.tvDescSyaratDanLain.text = htmlSyarat
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun getTentangKami() {
        val network = NetworkConfig().syarat().getTentangKami()
        try {
            if (network.isSuccessful) {
                val syarat = network.body()!!.data!!.syarat
                val htmlSyarat: Spanned =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Html.fromHtml(syarat, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(syarat)
                    }
                binding.pbLoadingSyarat.visibility = View.GONE
                binding.tvDescSyaratDanLain.text = htmlSyarat
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}