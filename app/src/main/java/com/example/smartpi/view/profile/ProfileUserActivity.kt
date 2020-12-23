package com.example.smartpi.view.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.groupclass.ListGroupClassActiveAdapter
import com.example.smartpi.adapter.paket.ListPackageActiveProfileAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityProfileUserBinding
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.model.PackageActiveItem
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.*
import java.net.SocketException

class ProfileUserActivity : AppCompatActivity() {
    private var token = ""
    private var nama = ""
    private var email = ""
    private var phone = ""
    lateinit var preferences: Preferences
    private var packageList = ArrayList<PackageActiveItem>()
    private var jadwalList = ArrayList<JadwalItem>()
    private val TAG = "MyActivity"
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    lateinit var binding: ActivityProfileUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        nama = preferences.getValues("nama").toString()
        phone = preferences.getValues("phone").toString()
        email = preferences.getValues("email").toString()

        binding.tvNamaProfileUser.text = nama
        binding.tvNamaProfileUser2.text = nama
        binding.tvEmailProfileUser.text = email
        binding.tvNoHpProfileUser.text = phone
        binding.ivBackProfilUser.setOnClickListener { finish() }

        binding.rvPaketAktif.layoutManager = LinearLayoutManager(this)
        binding.rvGroupClassAktif.layoutManager = LinearLayoutManager(this)

        scope.launch {
            val job1 = async { getPackageActive() }
            val job2 = async { getGroupClass() }
            job1.await()
            job2.await()
        }
    }

    private suspend fun getPackageActive() {
        packageList.clear()
        binding.pbLoadingPaketAktif.visibility = View.VISIBLE

        val network = NetworkConfig().getPackageActive().getActivePackage(token)
        try {
            if (network.isSuccessful) {
                Log.d(TAG, "getPackageActive: ${network.body()}")
                for (paket in network.body()!!.data!!) {

                    binding.pbLoadingPaketAktif.visibility = View.GONE
                    packageList.add(paket!!)
                }

                binding.rvPaketAktif.adapter = ListPackageActiveProfileAdapter(packageList) {}
            } else {
                binding.pbLoadingPaketAktif.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Tidak dapat mengambil paket",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

    private suspend fun getGroupClass() {
        jadwalList.clear()
        binding.pbLoadingGroupClassAktif.visibility = View.VISIBLE

        val network = NetworkConfig().getJadwalUser().getGroupClass(token)
        try {
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

                binding.rvGroupClassAktif.adapter = ListGroupClassActiveAdapter(jadwalList) {}
                binding.pbLoadingGroupClassAktif.visibility = View.GONE

            } else {
                binding.pbLoadingGroupClassAktif.visibility = View.GONE
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}