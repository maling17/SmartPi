package com.example.smartpi.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.R
import com.example.smartpi.activity.kelas.DetailKelasActivity
import com.example.smartpi.activity.kelas.PilihTrialActivity
import com.example.smartpi.adapter.JadwalHomeAdapter
import com.example.smartpi.adapter.PromoAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.model.PromoItem
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var jadwalList = ArrayList<JadwalItem>()
    private var promoList = ArrayList<PromoItem>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        jadwalList.clear()
        preferences = Preferences(activity!!.applicationContext)
        token = "Bearer ${preferences.getValues("token")}"
        Log.d(TAG, "Token: $token")

        //setting Recylerview jadwal
        rv_jadwal.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_jadwal.isNestedScrollingEnabled = false

        //Setting Recylerview promo
        rv_promo.layoutManager = LinearLayoutManager(context)
        rv_promo.isNestedScrollingEnabled = false


        scope.launch(Dispatchers.Main) {

            val job1 = async { getUser() }
            val job2 = async { checkTrial() }
            val job3 = async { getAllPromo() }

            job1.await()
            job2.await()
            job3.await()

        }

    }

    private suspend fun getUser() {

        val networkConfig = NetworkConfig().getUser().getUser(token)
        if (networkConfig.isSuccessful) {
            val username = networkConfig.body()!!.data!!.name
            val textUsername = "Hi, $username"
            tv_nama_home?.text = textUsername

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

    @SuppressLint("SetTextI18n")
    suspend fun getJadwalUser() {
        pb_jadwal?.visibility = View.VISIBLE
        rv_jadwal?.visibility = View.INVISIBLE

        val networkConfig = NetworkConfig().getJadwalUser().getJadwalUser(token)
        if (networkConfig.isSuccessful) {

            for (jadwal in networkConfig.body()!!.data!!) {

                Log.d(TAG, " ${jadwal!!.packageName}")

                jadwalList.add(jadwal)

                if (jadwalList.size >= 1) {
                    tv_paket_home?.text = "Paket yang aktif : ${jadwalList[0].packageName}"
                } else {
                    tv_paket_home?.text = "paket yang aktif : ${jadwalList.size} Paket "
                }

            }
            rv_jadwal?.visibility = View.VISIBLE
            rv_jadwal?.adapter = JadwalHomeAdapter(jadwalList) {

                val intent = Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                startActivity(intent)

            }
            pb_jadwal?.visibility = View.GONE
        } else {
            pb_jadwal?.visibility = View.GONE

        }
    }

    private suspend fun checkTrial() {
        val checkTrial = NetworkConfig().getCheckTrial().getCheckTrial(token)
        if (checkTrial.isSuccessful) {
            scope.launch(Dispatchers.Main) {
                if (checkTrial.body()!!.status == "Sudah") {
                    if (checkTrial.body()!!.isFirstTimeTrialUsed == "false") {
                        showPopUpPilihJadwalTrial()
                    }
                    cl_jadwal?.visibility = View.VISIBLE
                    getJadwalUser()
                } else {
                    cl_jadwal?.visibility = View.VISIBLE
                    showPopUpPilihTrial()
                    getJadwalUser()
                }
            }
        } else {
            Log.d(TAG, "checkTrial: Something wrong")
        }
    }

    private fun showPopUpPilihTrial() {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.pop_up_pilih_trial)
        dialog.setCancelable(false)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnPilihTrial = dialog.findViewById<Button>(R.id.btn_pilih_trial_pop_up)
        val ivCancel = dialog.findViewById<ImageView>(R.id.iv_cancel_popUp_pilih_trial)
        ivCancel.setOnClickListener { dialog.dismiss() }

        btnPilihTrial.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun showPopUpPilihJadwalTrial() {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.pop_up_pilih_jadwal_trial)
        dialog.setCancelable(false)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnPilihJadwalTrial = dialog.findViewById<Button>(R.id.btn_pilih_jadwa_trial_pop_up)
        val ivCancel = dialog.findViewById<ImageView>(R.id.iv_cancel_popUp_pilih_jadwal_trial)
        ivCancel.setOnClickListener { dialog.dismiss() }

        btnPilihJadwalTrial.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(context, PilihTrialActivity::class.java))
        }
        dialog.show()

    }

    private suspend fun getAllPromo() {

        pb_promo?.visibility = View.VISIBLE
        promoList.clear()

        val network = NetworkConfig().getPromo().getPromo(token)
        if (network.isSuccessful) {

            for (promo in network.body()!!.data!!) {
                Log.d(TAG, " ${promo!!.gambar}")
                promoList.add(promo)
            }
            pb_promo?.visibility = View.GONE

            rv_promo?.adapter = PromoAdapter(promoList) {

            }

        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Tidak Dapat mengambil promo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}

