package com.example.smartpi.view.program

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityProgramMengajiBinding
import com.example.smartpi.view.pembelian.PilihPaketLanggananActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class ProgramMengajiActivity : AppCompatActivity() {
    lateinit var binding: ActivityProgramMengajiBinding
    var token = ""
    var id_program = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgramMengajiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        scope.launch { getMengaji() }
        binding.btnBeliPaket.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            val intent =
                Intent(this, PilihPaketLanggananActivity::class.java)
            intent.putExtra("id_program", id_program)
            Log.d("TAG", "getMatematika: $id_program")
            startActivity(intent)
            binding.pbLoading.visibility = View.GONE
        }
        binding.ivBackProgramMengaji.setOnClickListener { finish() }


    }

    suspend fun getMengaji() {
        val networkConfig = NetworkConfig().getPrograms().getProgramMengaji()
        try {
            if (networkConfig.isSuccessful) {
                val judul = networkConfig.body()!!.data!!.desc!!.name
                val desc = networkConfig.body()!!.data!!.desc!!.desc
                val duration = networkConfig.body()!!.data!!.desc!!.duration
                id_program = networkConfig.body()!!.data!!.desc!!.id!!.toString()

                binding.tvJudulProgram.text = judul
                binding.tvDescProgram.text = desc
                binding.tvDurasi.text = "1 Pertemuan @$duration Menit"

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}