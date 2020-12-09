package com.example.smartpi.view.kelas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.JadwalHomeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentJadwalBinding
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.*
import java.net.SocketException

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

        binding.btnLangganan.setOnClickListener {
            startActivity(Intent(context, PilihPaketActivity::class.java))
        }
        binding.fabJadwal.setOnClickListener {
            startActivity(Intent(context, PilihPaketActivity::class.java))
        }


    }

    override fun onStart() {
        super.onStart()
        /*val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            binding.pbListJadwal.visibility = View.GONE
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Checkout Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }*/
        jadwalList.clear()
        preferences = Preferences(requireActivity().applicationContext)
        token = "Bearer ${preferences.getValues("token")}"
        //setting Recylerview jadwal
        binding.rvListJadwal.layoutManager = LinearLayoutManager(context)
        binding.rvListJadwal.isNestedScrollingEnabled = false

        scope.launch {
            checkPackageActive()
        }
    }

    suspend fun getJadwalUser() {

        val networkConfig = NetworkConfig().getJadwalUser().getJadwalUser(token)

        try {
            if (networkConfig.isSuccessful) {
                binding.llBelumBerlangganan.visibility = View.GONE
                for (jadwal in networkConfig.body()!!.data!!) {

                    jadwalList.add(jadwal!!)
                }
                binding.rvListJadwal.adapter = JadwalHomeAdapter(jadwalList) {
                    val intent =
                        Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                    startActivity(intent)

                }
                binding.svJadwal.visibility = View.VISIBLE
                binding.fabJadwal.visibility = View.VISIBLE
                binding.pbListJadwal.visibility = View.GONE
                binding.llBelumBuatJadwal.visibility = View.GONE

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun getGroupClass() {

        jadwalList.clear()
        val network = NetworkConfig().getJadwalUser().getGroupClass(token)
        if (network.isSuccessful) {
            for (grup in network.body()!!.data!!) {
                val jadwalItem = JadwalItem()
                jadwalItem.packageName = grup!!.namaKelas
                jadwalItem.duration = grup.duration.toString()
                jadwalItem.teacherId = grup.teacherId
                jadwalItem.teacherName = grup.teacher

                for (schedule in grup.schedule!!) {
                    jadwalItem.id = schedule!!.id.toString()
                    jadwalItem.scheduleTime = schedule.scheduleTime.toString()
                    jadwalItem.scheduleEnd = schedule.scheduleEnd.toString()
                    jadwalItem.roomCode = schedule.roomCode.toString()
                    jadwalItem.platform = schedule.platform.toString()
                }
                jadwalList.add(jadwalItem)
            }

            binding.rvListJadwal.adapter = JadwalHomeAdapter(jadwalList) {
                val intent =
                    Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                startActivity(intent)

            }
            binding.svJadwal.visibility = View.VISIBLE
            binding.fabJadwal.visibility = View.VISIBLE
            binding.pbListJadwal.visibility = View.GONE
            binding.llBelumBuatJadwal.visibility = View.GONE

        }


    }

    suspend fun checkPackageActive() {
        val network = NetworkConfig().getPackageActive().getActivePackage(token)

        binding.svJadwal.visibility = View.INVISIBLE
        binding.fabJadwal.visibility = View.INVISIBLE
        binding.pbListJadwal.visibility = View.VISIBLE

        try {
            if (network.isSuccessful) {
                scope.launch {
                    val job1 = async { getJadwalUser() }
                    val job2 = async { getGroupClass() }
                    job1.await()
                    job2.await()
                }

                if (jadwalList.isEmpty()) {
                    binding.svJadwal.visibility = View.GONE
                    binding.fabJadwal.visibility = View.VISIBLE
                    binding.pbListJadwal.visibility = View.GONE
                    binding.llBelumBuatJadwal.visibility = View.VISIBLE
                }

            } else {
                binding.llBelumBerlangganan.visibility = View.VISIBLE
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }


}