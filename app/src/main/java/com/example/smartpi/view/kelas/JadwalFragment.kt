package com.example.smartpi.view.kelas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.JadwalHomeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentJadwalBinding
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class JadwalFragment : Fragment() {

    val TAG = "MyActivity"
    var jadwalList = ArrayList<JadwalItem>()
    var token = ""
    lateinit var preferences: Preferences
    private var _binding: FragmentJadwalBinding? = null
    private val binding get() = _binding!!
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJadwalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jadwalList.clear()
        preferences = Preferences(activity!!.applicationContext)
        token = "Bearer ${preferences.getValues("token")}"
        //setting Recylerview jadwal
        binding.rvListJadwal.layoutManager =
            LinearLayoutManager(context)
        binding.rvListJadwal.isNestedScrollingEnabled = false

        binding.btnLangganan.setOnClickListener {
            Toast.makeText(context, "Berhasil Langganan", Toast.LENGTH_SHORT).show()
        }
        binding.fabJadwal.setOnClickListener {
            startActivity(Intent(context, PilihPaketActivity::class.java))
        }

        scope.launch(Dispatchers.Main) {
            checkPackageActive()
        }

    }

    suspend fun getJadwalUser() {

        binding.svJadwal.visibility = View.INVISIBLE
        binding.fabJadwal.visibility = View.INVISIBLE
        binding.pbListJadwal.visibility = View.VISIBLE

        val networkConfig = NetworkConfig().getJadwalUser().getJadwalUser(token)
        if (networkConfig.isSuccessful) {
            binding.llBelumBerlangganan.visibility = View.GONE
            for (jadwal in networkConfig.body()!!.data!!) {

                jadwalList.add(jadwal!!)
            }

            binding.svJadwal.visibility = View.VISIBLE
            binding.fabJadwal.visibility = View.VISIBLE
            binding.pbListJadwal.visibility = View.GONE
            binding.llBelumBuatJadwal.visibility = View.GONE

            binding.rvListJadwal.adapter = JadwalHomeAdapter(jadwalList) {
                val intent = Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                startActivity(intent)

            }

        } else {

            binding.svJadwal.visibility = View.GONE
            binding.fabJadwal.visibility = View.VISIBLE
            binding.pbListJadwal.visibility = View.GONE
            binding.llBelumBuatJadwal.visibility = View.VISIBLE

        }
    }

    suspend fun checkPackageActive() {
        val network = NetworkConfig().getPackageActive().getActivePackage(token)
        if (network.isSuccessful) {
            getJadwalUser()
        } else {
            binding.llBelumBerlangganan.visibility = View.VISIBLE
        }
    }
}