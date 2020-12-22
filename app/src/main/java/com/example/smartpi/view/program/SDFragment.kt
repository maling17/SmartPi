package com.example.smartpi.view.program

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentSdBinding
import com.example.smartpi.view.pembelian.PilihPaketLanggananActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class SDFragment : Fragment() {

    private var _binding: FragmentSdBinding? = null
    private val binding get() = _binding!!
    private var id_program = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scope.launch { getSD() }

        binding.btnBeliPaket.setOnClickListener {
            val intent =
                Intent(activity, PilihPaketLanggananActivity::class.java)
            intent.putExtra("id_program", id_program)
            Log.d("TAG", "getMatematika: $id_program")
            startActivity(intent)
        }

    }

    private suspend fun getSD() {
        val networkConfig = NetworkConfig().getPrograms().getProgramMatematika()
        try {
            if (networkConfig.isSuccessful) {
                val judul = networkConfig.body()!!.data!!.sd!!.desc!!.name
                val desc = networkConfig.body()!!.data!!.sd!!.desc!!.desc
                val duration = networkConfig.body()!!.data!!.sd!!.desc!!.duration
                id_program = networkConfig.body()!!.data!!.sd!!.desc!!.id!!.toString()

                binding.tvJudulProgram.text = judul
                binding.tvDescProgram.text = desc
                binding.tvDurasi.text = "1 Pertemuan @$duration Menit"

            } else {
                Log.d("TAG", "getSD: Data gagal diambil")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}