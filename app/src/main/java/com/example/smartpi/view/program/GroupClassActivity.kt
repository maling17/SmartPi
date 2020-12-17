package com.example.smartpi.view.program

import android.os.Bundle
import android.util.Log
import android.view.View
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
    val TAG = "MyActivity"
    var groupList = ArrayList<ListGroupItem>()
    var groupDetailList = ArrayList<DetailData>()
    var scheduleList = ArrayList<ScheduleDetailItem>()
    var kelasList = ArrayList<KelasItem>()
    var token = ""
    lateinit var preferences: Preferences
    lateinit var binding: ActivityGroupClassBinding
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var id = 0
    var id_group = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupClassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rvGroupClass.layoutManager = GridLayoutManager(this, 2)
        scope.launch { getGroupClass() }
        binding.ivBackGroupClass.setOnClickListener { finish() }
    }

    private suspend fun getGroupClass() {

        val networkConfig = NetworkConfig().getGroupClass().getGroupClass()

        try {
            if (networkConfig.isSuccessful) {
                binding.pbLoadingGroupClass.visibility = View.GONE
                for (grup in networkConfig.body()!!.data!!) {
                    groupList.add(grup!!)
                    id = grup.id!!
                    getDetailGroupClass(id)
                    Log.d(TAG, "getGroupClass: $groupList")
                }
            } else {
                binding.pbLoadingGroupClass.visibility = View.GONE
                binding.llEmpty.visibility = View.VISIBLE
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
                binding.pbLoadingGroupClass.visibility = View.GONE
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
                binding.pbLoadingGroupClass.visibility = View.GONE
                binding.llEmpty.visibility = View.VISIBLE
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}