package com.example.smartpi.view.program

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
import com.example.smartpi.databinding.ActivityGroupClassBinding
import com.example.smartpi.model.DetailData
import com.example.smartpi.model.KelasItem
import com.example.smartpi.model.ListGroupItem
import com.example.smartpi.model.ScheduleDetailItem
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class GroupClassActivity : AppCompatActivity() {
    private val TAG = "MyActivity"
    private var groupList = ArrayList<ListGroupItem>()
    private var groupDetailList = ArrayList<DetailData>()
    private var scheduleList = ArrayList<ScheduleDetailItem>()
    private var kelasList = ArrayList<KelasItem>()

    lateinit var preferences: Preferences
    lateinit var binding: ActivityGroupClassBinding

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupClassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        groupList.clear()
        kelasList.clear()
        scheduleList.clear()
        groupDetailList.clear()

        binding.rvGroupClass.layoutManager = GridLayoutManager(this, 2)
        scope.launch {
            getGroupClass()
            if (groupDetailList.isEmpty()) {
                binding.llEmpty.visibility = View.VISIBLE
            } else {
                binding.llEmpty.visibility = View.GONE
            }
        }

        binding.ivBackGroupClass.setOnClickListener { finish() }

    }

    private suspend fun getGroupClass() {


        val networkConfig = NetworkConfig().getGroupClass().getGroupClass()

        try {
            if (networkConfig.isSuccessful) {
                binding.pbLoadingGroupClass.visibility = View.GONE
                if (groupList.isEmpty()) {
                    binding.llEmpty.visibility = View.VISIBLE
                }
                for (grup in networkConfig.body()!!.data!!) {
                    groupList.add(grup!!)
                    id = grup.id!!
                    getDetailGroupClass(id)
                    Log.d(TAG, "getGroupClass: $groupList")
                }
            } else {
                binding.pbLoadingGroupClass.visibility = View.GONE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Tidak dapat mengambil group class, Silahkan cek koneksi anda",
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

                binding.rvGroupClass.adapter = ListGroupClassAdapter(groupDetailList) {}
            } else {
                Log.d(TAG, "getDetailGroupClass: Data group class gagal diambil")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}