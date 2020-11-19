package com.example.smartpi.view.fragment

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
import com.example.smartpi.adapter.JadwalHomeAdapter
import com.example.smartpi.adapter.PromoAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentHomeBinding
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.model.PackageActiveItem
import com.example.smartpi.model.PromoItem
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.kelas.DetailKelasActivity
import com.example.smartpi.view.kelas.PilihPaketActivity
import com.example.smartpi.view.kelas.PilihTrialActivity
import kotlinx.coroutines.*
import java.net.SocketException

class HomeFragment : Fragment() {

    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var jadwalList = ArrayList<JadwalItem>()
    private var promoList = ArrayList<PromoItem>()
    private var packageList = ArrayList<PackageActiveItem>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val kelasFragment = KelasFragment()
        binding.tvLihatSemua.setOnClickListener {
            setFragment(kelasFragment)
        }

        binding.btnBikinJadwal.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PilihPaketActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        jadwalList.clear()
        preferences = Preferences(activity!!.applicationContext)
        token = "Bearer ${preferences.getValues("token")}"
        Log.d(TAG, "Token: $token")

        //setting Recylerview jadwal
        binding.rvJadwal.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvJadwal.isNestedScrollingEnabled = false

        //Setting Recylerview promo
        binding.rvPromo.layoutManager = LinearLayoutManager(context)
        binding.rvPromo.isNestedScrollingEnabled = false

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            binding.pbPromo.visibility = View.GONE
            binding.pbJadwal.visibility = View.GONE
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Checkout Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        scope.launch(exceptionHandler) {

            val job1 = async { getUser() }
            val job2 = async { checkTrial() }
            val job3 = async { getAllPromo() }
            val job4 = async { checkPackage() }

            job1.await()
            job2.await()
            job3.await()
            job4.await()

        }

    }

    private suspend fun getUser() {

        val networkConfig = NetworkConfig().getUser().getUser(token)

        try {
            if (networkConfig.isSuccessful) {
                val username = networkConfig.body()!!.data!!.name
                val textUsername = "Hi, $username"
                binding.tvNamaHome.text = textUsername

            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Tidak Dapat Mengambil data user",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        } catch (e: SocketException) {
            /*   Handler(Looper.getMainLooper()).post {
                   Toast.makeText(
                       context,
                       "Tidak Ada Jaringan ,Mohon Periksa Jaringan Anda",
                       Toast.LENGTH_LONG
                   ).show()
               }*/

            e.printStackTrace()
        }

    }

    suspend fun getJadwalUser() {
        binding.pbJadwal.visibility = View.VISIBLE
        binding.rvJadwal.visibility = View.INVISIBLE

        val networkConfig = NetworkConfig().getJadwalUser().getJadwalUser(token)
        try {
            if (networkConfig.isSuccessful) {

                for (jadwal in networkConfig.body()!!.data!!) {
                    jadwalList.add(jadwal!!)

                }

                binding.rvJadwal.visibility = View.VISIBLE
                binding.rvJadwal.adapter = JadwalHomeAdapter(jadwalList) {
                    val intent =
                        Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }
                binding.pbJadwal.visibility = View.GONE
            } else {
                binding.pbJadwal.visibility = View.GONE
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }


    }

    @SuppressLint("SetTextI18n")
    suspend fun checkPackage() {
        packageList.clear()
        val network = NetworkConfig().getPackageActive().getActivePackage(token)

        try {
            if (network.isSuccessful) {
                for (paket in network.body()!!.data!!) {

                    packageList.add(paket!!)

                    if (packageList.size <= 1) {
                        binding.tvPaketHome.text =
                            "Paket yang aktif : ${packageList[0].package_name}"
                    } else {
                        binding.tvPaketHome.text = "paket yang aktif : ${packageList.size} Paket "
                    }

                }
            } else {
                binding.btnBikinJadwal.visibility = View.GONE
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }


    }

    private suspend fun checkTrial() {
        val checkTrial = NetworkConfig().getCheckTrial().getCheckTrial(token)

        try {
            if (checkTrial.isSuccessful) {
                scope.launch(Dispatchers.Main) {
                    if (checkTrial.body()!!.status == "Sudah") {
                        if (checkTrial.body()!!.isFirstTimeTrialUsed == "false") {
                            showPopUpPilihJadwalTrial()
                        }
                        binding.clJadwal.visibility = View.VISIBLE
                        getJadwalUser()
                    } else {
                        binding.clJadwal.visibility = View.VISIBLE
                        showPopUpPilihTrial()
                        getJadwalUser()
                    }
                }
            } else {
                Log.d(TAG, "checkTrial: Something wrong")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
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
            startActivity(Intent(context, PilihTrialActivity::class.java))
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
        }
        dialog.show()

    }

    private suspend fun getAllPromo() {

        binding.pbPromo.visibility = View.VISIBLE
        promoList.clear()

        val network = NetworkConfig().getPromo().getPromo(token)
        try {
            if (network.isSuccessful) {

                for (promo in network.body()!!.data!!) {
                    promoList.add(promo!!)
                }
                binding.pbPromo.visibility = View.GONE

                binding.rvPromo.adapter = PromoAdapter(promoList) {

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
        } catch (e: SocketException) {
            e.printStackTrace()
        }


    }

    private fun setFragment(fragment: Fragment) {

        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.commit()
    }


}
