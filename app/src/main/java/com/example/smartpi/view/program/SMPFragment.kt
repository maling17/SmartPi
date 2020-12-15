package com.example.smartpi.view.program

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentSmpBinding
import com.example.smartpi.view.pembelian.PilihPaketLanggananActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class SMPFragment : Fragment() {

    var _binding: FragmentSmpBinding? = null
    val binding get() = _binding!!
    var token = ""
    var id_program = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scope.launch { getSMP() }
        binding.btnBeliPaket.setOnClickListener {

            val intent =
                Intent(activity, PilihPaketLanggananActivity::class.java)
            intent.putExtra("id_program", id_program)
            Log.d("TAG", "getMatematika: $id_program")
            startActivity(intent)
        }

    }

    suspend fun getSMP() {
        val networkConfig = NetworkConfig().getPrograms().getProgramMatematika()
        try {
            if (networkConfig.isSuccessful) {
                val judul = networkConfig.body()!!.data!!.smp!!.desc!!.name
                val desc = networkConfig.body()!!.data!!.smp!!.desc!!.desc
                val duration = networkConfig.body()!!.data!!.smp!!.desc!!.duration
                id_program = networkConfig.body()!!.data!!.smp!!.desc!!.id!!.toString()

                binding.tvJudulProgram.text = judul
                binding.tvDescProgram.text = desc
                binding.tvDurasi.text = "1 Pertemuan @$duration Menit"

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}