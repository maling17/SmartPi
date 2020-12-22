package com.example.smartpi.view.program

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentCalistungTkBinding
import com.example.smartpi.view.pembelian.PilihPaketLanggananActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class CalistungTkFragment : Fragment() {

    private var _binding: FragmentCalistungTkBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private var id_program = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalistungTkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scope.launch { getCalistung() }
        binding.btnBeliPaket.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            val intent =
                Intent(activity, PilihPaketLanggananActivity::class.java)
            intent.putExtra("id_program", id_program)
            Log.d("TAG", "getMatematika: $id_program")
            startActivity(intent)
            binding.pbLoading.visibility = View.GONE
        }

    }

    @SuppressLint("SetTextI18n")
    private suspend fun getCalistung() {
        val networkConfig = NetworkConfig().getPrograms().getProgramMatematika()
        try {
            if (networkConfig.isSuccessful) {
                val judul = networkConfig.body()!!.data!!.tk!!.desc!!.name
                val desc = networkConfig.body()!!.data!!.tk!!.desc!!.desc
                val duration = networkConfig.body()!!.data!!.tk!!.desc!!.duration
                id_program = networkConfig.body()!!.data!!.tk!!.desc!!.id!!.toString()

                binding.tvJudulProgram.text = judul
                binding.tvDescProgram.text = desc
                binding.tvDurasi.text = "1 Pertemuan @$duration Menit"

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}