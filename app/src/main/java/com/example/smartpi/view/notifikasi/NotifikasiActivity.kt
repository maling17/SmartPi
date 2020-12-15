package com.example.smartpi.view.notifikasi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListNotifAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityNotifikasiBinding
import com.example.smartpi.model.NotifItem
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.*
import java.net.SocketException

class NotifikasiActivity : AppCompatActivity() {
    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var notifList = ArrayList<NotifItem>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    lateinit var binding: ActivityNotifikasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        binding.rvNotifikasi.layoutManager = LinearLayoutManager(this)
        binding.ivBackNotifikasi.setOnClickListener { finish() }
        scope.launch {
            val job1 = async { getNotifikasi() }
            val job2 = async { updateNotifikasi() }
            job1.await()
            job2.await()
        }

    }

    suspend fun getNotifikasi() {

        val networkConfig = NetworkConfig().getUser().getNotif(token)
        try {
            if (networkConfig.isSuccessful) {
                for (notif in networkConfig.body()!!.data!!.notif!!) {
                    notifList.add(notif!!)
                }
                binding.rvNotifikasi.adapter = ListNotifAdapter(notifList) {

                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    suspend fun updateNotifikasi() {
        val networkConfig = NetworkConfig().getUser().getUpdateNotif(token)

        try {
            if (networkConfig.isSuccessful) {
                Log.d(TAG, "updateNotifikasi: Berhasil")
            } else {
                Log.d(TAG, "updateNotifikasi: gagal")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }


    }
}