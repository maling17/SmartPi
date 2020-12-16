package com.example.smartpi.view.prakerja

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smartpi.adapter.ListGroupClassAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPraKerjaBinding
import com.example.smartpi.model.*
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class PraKerjaActivity : AppCompatActivity() {
    var praKerjaList = ArrayList<PraKerjaItem>()
    lateinit var binding: ActivityPraKerjaBinding
    val TAG = "MyActivity"
    var groupList = ArrayList<ListGroupItem>()
    var groupDetailList = ArrayList<DetailData>()
    var scheduleList = ArrayList<ScheduleDetailItem>()
    var kelasList = ArrayList<KelasItem>()
    var token = ""
    lateinit var preferences: Preferences
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPraKerjaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rvPraKerja.layoutManager = GridLayoutManager(this, 2)
        binding.ivBackPraKerja.setOnClickListener { finish() }
        scope.launch { getPrakerja() }
    }

    private suspend fun getPrakerja() {

        val networkConfig = NetworkConfig().getPrakerja().getListPrakerja()
        try {
            if (networkConfig.isSuccessful) {
                binding.pbLoadingPraKerja.visibility = View.GONE
                for (grup in networkConfig.body()!!.data!!) {
                    groupList.add(grup!!)
                    id = grup.id!!
                    getDetailGroupClass(id)
                    Log.d(TAG, "getGroupClass: $groupList")
                }

            } else {
                binding.pbLoadingPraKerja.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Tidak dapat mengambil pra kerja, Silahkan cek koneksi anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun getDetailGroupClass(id: Int) {
        val networkConfig = NetworkConfig().getGroupClass().getDetailGroupClass(id)

        try {
            if (networkConfig.isSuccessful) {
                val detailData = DetailData()

                for (kelas in networkConfig.body()!!.data!!.kelas!!) {
                    kelasList.add(kelas!!)
                    detailData.kelas = kelasList
                }
                for (schedule in networkConfig.body()!!.data!!.schedule!!) {
                    scheduleList.add(schedule!!)
                    detailData.schedule = scheduleList
                }

                groupDetailList.add(detailData)

                binding.rvPraKerja.adapter = ListGroupClassAdapter(groupDetailList) {}
            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }
}
