package com.example.smartpi.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.R
import com.example.smartpi.adapter.JadwalHomeAdapter
import com.example.smartpi.adapter.PromoAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    var token = ""
    lateinit var preferences: Preferences
    val TAG = "MyActivity"
    var jadwalList = ArrayList<JadwalItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        token = "Bearer ${preferences.getValues("token")}"
        Log.d(TAG, "Token: $token")

        //Setting Recylerview promo
        rv_promo.layoutManager = LinearLayoutManager(context)
        rv_promo.isNestedScrollingEnabled = false
        rv_promo.adapter = PromoAdapter()

        //setting Recylerview jadwal
        rv_jadwal.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_jadwal.isNestedScrollingEnabled = false

        GlobalScope.launch(Dispatchers.Main) {
            val job1 = launch(Dispatchers.Main) { getUser() }
            val job2 = launch(Dispatchers.Main) { checkTrial() }

            job1.join()
            job2.join()

        }

    }

    private suspend fun getUser() {

        val networkConfig = NetworkConfig().getUser().getUser(token)
        if (networkConfig.isSuccessful) {
            val username = networkConfig.body()!!.data!!.name
            val textUsername = "Hi, $username"
            tv_nama_home.text = textUsername

        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Check your Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    suspend fun getJadwalUser() {
        pb_jadwal.visibility = View.VISIBLE
        rv_jadwal.visibility = View.INVISIBLE

        val networkConfig = NetworkConfig().getJadwalUser().getJadwalUser(token)
        if (networkConfig.isSuccessful) {
            for (jadwal in networkConfig.body()!!.data!!) {
                Log.d(TAG, "getCountryCode: ${jadwal!!.packageName}")
                jadwalList.addAll(listOf(jadwal))
            }
            rv_jadwal.visibility = View.VISIBLE
            rv_jadwal.adapter = JadwalHomeAdapter(jadwalList) {}
            pb_jadwal.visibility = View.GONE
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Check your Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    suspend fun checkTrial() {
        val checkTrial = NetworkConfig().getCheckTrial().getCheckTrial(token)
        if (checkTrial.isSuccessful) {
            GlobalScope.launch(Dispatchers.Main) {
                if (checkTrial.body()!!.status == "Sudah") {
                    cl_jadwal.visibility = View.VISIBLE
                    ll_trial.visibility = View.INVISIBLE
                    getJadwalUser()
                } else {
                    cl_jadwal.visibility = View.VISIBLE
                    ll_trial.visibility = View.VISIBLE
                    getJadwalUser()
                }
            }
        } else {
            Log.d(TAG, "checkTrial: Something wrong")
        }
    }

}

