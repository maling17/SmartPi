package com.example.smartpi.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentPembelianBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.kelas.PilihTrialActivity
import com.example.smartpi.view.pembelian.LanggananPaketActivity
import com.example.smartpi.view.prakerja.PraKerjaActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class PembelianFragment : Fragment() {

    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var _binding: FragmentPembelianBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPembelianBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireActivity().applicationContext)
        token = "Bearer ${preferences.getValues("token")}"

        scope.launch(Dispatchers.Main) { checkTrial() }

        binding.btnLihatLangganan.setOnClickListener {
            startActivity(Intent(context, LanggananPaketActivity::class.java))
        }

        binding.btnLihatTrial.setOnClickListener {
            startActivity(Intent(context, PilihTrialActivity::class.java))
        }

        binding.btnLihatPraKerja.setOnClickListener {
            startActivity(Intent(context, PraKerjaActivity::class.java))
        }

    }

    private suspend fun checkTrial() {

        val checkTrial = NetworkConfig().getCheckTrial().getCheckTrial(token)

        //jika trial sudah diambil maka tombol beli trial hilang
        try {
            if (checkTrial.isSuccessful) {
                scope.launch(Dispatchers.Main) {
                    if (checkTrial.body()!!.status == "Sudah") {
                        binding.constraintLayout3.visibility = View.GONE
                        binding.pbPembelianFragment.visibility = View.GONE
                    } else {
                        binding.constraintLayout3.visibility = View.VISIBLE
                        binding.pbPembelianFragment.visibility = View.GONE
                    }
                }
            } else {
                Log.d(TAG, "checkTrial: Something wrong")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }
}