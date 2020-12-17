package com.example.smartpi.view.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.example.smartpi.adapter.ListKelasSelesaiAdapter
import com.example.smartpi.adapter.PromoAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.FragmentHomeBinding
import com.example.smartpi.model.HistoryItem
import com.example.smartpi.model.JadwalItem
import com.example.smartpi.model.PackageActiveItem
import com.example.smartpi.model.PromoItem
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.kelas.DetailKelasActivity
import com.example.smartpi.view.kelas.PilihPaketActivity
import com.example.smartpi.view.kelas.PilihTrialActivity
import com.example.smartpi.view.notifikasi.NotifikasiActivity
import com.example.smartpi.view.prakerja.PraKerjaActivity
import com.example.smartpi.view.profile.ProfileUserActivity
import com.example.smartpi.view.program.GroupClassActivity
import com.example.smartpi.view.program.ProgramInggrisActivity
import com.example.smartpi.view.program.ProgramMatematikaActivity
import com.example.smartpi.view.program.ProgramMengajiActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.*
import java.net.SocketException


class HomeFragment : Fragment() {

    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var jadwalList = ArrayList<JadwalItem>()
    private var promoList = ArrayList<PromoItem>()
    private var packageList = ArrayList<PackageActiveItem>()
    private var kelasList = ArrayList<HistoryItem>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var stateTrial = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
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
        binding.ivAkademik.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ProgramMatematikaActivity::class.java
                )
            )
        }
        binding.ivInggris.setOnClickListener {
            startActivity(Intent(context, ProgramInggrisActivity::class.java))
        }
        binding.ivMengaji.setOnClickListener {
            startActivity(Intent(context, ProgramMengajiActivity::class.java))
        }
        binding.ivGroupClass.setOnClickListener {
            startActivity(Intent(context, GroupClassActivity::class.java))
        }
        binding.ivToProfile.setOnClickListener {
            startActivity(Intent(context, ProfileUserActivity::class.java))
        }
        binding.ivPraKerja.setOnClickListener {
            startActivity(Intent(context, PraKerjaActivity::class.java))
        }
        binding.ivNotif.setOnClickListener {
            startActivity(Intent(context, NotifikasiActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        jadwalList.clear()
        preferences = Preferences(requireActivity().applicationContext)
        token = "Bearer ${preferences.getValues("token")}"

        //ambil value untuk memunculkan dialog pilih trial
        stateTrial = preferences.getValues("state_trial").toString()
        Log.d(TAG, "Token: $token")

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            binding.pbJadwal.visibility = View.GONE
            binding.pbPromo.visibility = View.GONE
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Checkout Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //setting Recylerview jadwalene
        binding.rvJadwal.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvJadwal.isNestedScrollingEnabled = false

        //Setting Recylerview promo
        binding.rvPromo.layoutManager = LinearLayoutManager(context)
        binding.rvPromo.isNestedScrollingEnabled = false

        binding.rvKonfirmasiKelas.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        scope.launch(exceptionHandler) {

            val job1 = async { getUser() }
            val job2 = async { checkTrial() }
            val job3 = async { getAllPromo() }
            val job4 = async { checkPackage() }
//            val job5 = async { checkVersion() }
            val job6 = async { checkNotif() }
            val job7 = async { getKelasSelesai() }

            job1.await()
            job2.await()
            job3.await()
            job4.await()
//            job5.await()
            job6.await()
            job7.await()

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
                Log.d(TAG, "getUser: ${networkConfig.errorBody()}")

            }
        } catch (e: SocketException) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Tidak Ada Jaringan ,Mohon Periksa Jaringan Anda",
                    Toast.LENGTH_LONG
                ).show()
            }

            e.printStackTrace()
        }

    }

    suspend fun getJadwalUser() {
        jadwalList.clear()
        binding.pbJadwal.visibility = View.VISIBLE
        binding.rvJadwal.visibility = View.INVISIBLE

        val networkConfig = NetworkConfig().getJadwalUser().getJadwalUser(token)
        try {
            if (networkConfig.isSuccessful) {

                for (jadwal in networkConfig.body()!!.data!!) {
                    jadwalList.add(jadwal!!)

                }
                if (jadwalList.isEmpty()) {
                    binding.tvJadwalEmpty.visibility = View.VISIBLE
                    binding.rvJadwal.visibility = View.GONE
                    binding.pbJadwal.visibility = View.GONE
                } else {
                    binding.rvJadwal.visibility = View.VISIBLE
                    binding.tvJadwalEmpty.visibility = View.GONE
                    binding.rvJadwal.adapter = JadwalHomeAdapter(jadwalList) {
                        val intent =
                            Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                        startActivity(intent)

                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, it.id)
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, it.packageName)
                        mFirebaseAnalytics!!.logEvent(
                            FirebaseAnalytics.Event.SELECT_CONTENT,
                            bundle
                        )

                    }
                    binding.pbJadwal.visibility = View.GONE
                }
            } else {
                binding.pbJadwal.visibility = View.GONE
                Log.d(TAG, "getUser: ${networkConfig.errorBody()}")

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }


    }

    private suspend fun getGroupClass() {

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
                if (jadwalList.isEmpty()) {
                    binding.tvJadwalEmpty.visibility = View.VISIBLE
                    binding.rvJadwal.visibility = View.GONE
                    binding.pbJadwal.visibility = View.GONE
                } else {
                    binding.rvJadwal.visibility = View.VISIBLE
                    binding.rvJadwal.adapter = JadwalHomeAdapter(jadwalList) {
                        val intent =
                            Intent(context, DetailKelasActivity::class.java).putExtra("data", it)
                        startActivity(intent)
                    }
                    binding.pbJadwal.visibility = View.GONE
                }
            } else {
                binding.pbJadwal.visibility = View.GONE
                Log.d(TAG, "getUser: ${network.errorBody()}")

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

                }
                if (packageList.isEmpty()) {
                    binding.btnBikinJadwal.visibility = View.GONE
                } else {
                    if (packageList.size <= 1) {
                        binding.tvPaketHome.text =
                            "Paket yang aktif : ${packageList[0].package_name}"
                    } else {
                        binding.tvPaketHome.text =
                            "paket yang aktif : ${packageList.size} Paket "
                    }
                }

            } else {
                binding.btnBikinJadwal.visibility = View.GONE
                Log.d(TAG, "getUser: ${network.errorBody()}")

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

                        // jika sudah pilih trial tapi belum pilih jadwal
                        if (checkTrial.body()!!.isFirstTimeTrialUsed == "false") {
                            if (stateTrial == "0") {
                                showPopUpPilihJadwalTrial()
                            }

                        }
                        binding.clJadwal.visibility = View.VISIBLE
                        val job1 = async { getJadwalUser() }
                        val job2 = async { getGroupClass() }

                        job1.await()
                        job2.await()
                    } else {
                        binding.clJadwal.visibility = View.VISIBLE

                        if (stateTrial == "0") {
                            showPopUpPilihTrial()
                        }
                        val job1 = async { getJadwalUser() }
                        val job2 = async { getGroupClass() }

                        job1.await()
                        job2.await()
                    }
                }
            } else {
                Log.d(TAG, "getUser: ${checkTrial.errorBody()}")

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private fun showPopUpPilihTrial() {
        val dialog = Dialog(requireActivity())
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
        ivCancel.setOnClickListener {
            preferences.setValues("state_trial", "1").toString()
            dialog.dismiss()
        }

        btnPilihTrial.setOnClickListener {
            dialog.dismiss()
            preferences.setValues("state_trial", "1").toString()
            startActivity(Intent(context, PilihTrialActivity::class.java))
        }
        dialog.show()

    }

    private fun showPopUpPilihJadwalTrial() {
        val dialog = Dialog(requireActivity())
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
        ivCancel.setOnClickListener {
            preferences.setValues("state_trial", "1").toString()
            dialog.dismiss()
        }

        btnPilihJadwalTrial.setOnClickListener {
            dialog.dismiss()
            preferences.setValues("state_trial", "1").toString()
            startActivity(Intent(context, PilihPaketActivity::class.java))
        }
        dialog.show()

    }

    private fun showPopUpUpdateAplikasi() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.pop_up_update_version)
        dialog.setCancelable(false)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnUpdate = dialog.findViewById<Button>(R.id.btn_update)

        btnUpdate.setOnClickListener {
            toGooglePlay()
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
                Log.d(TAG, "getUser: ${network.errorBody()}")

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

    fun toGooglePlay() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=co.smartpi.student"
            )
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }

    suspend fun checkVersion() {
        val versionName = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName

        val networkConfig = NetworkConfig().getVersion().getVersion()
        try {
            if (networkConfig.isSuccessful) {
                for (versi in networkConfig.body()!!.data!!) {
                    Log.d(TAG, "checkVersion: ${versi!!.versionName}, $versionName")
                    if (versionName != versi.versionName.toString()) {
                        showPopUpUpdateAplikasi()
                    }
                }
            } else {
                Log.d(TAG, "getUser: ${networkConfig.errorBody()}")

            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

    suspend fun checkNotif() {
        val networkConfig = NetworkConfig().getUser().getNotif(token)
        try {
            if (networkConfig.isSuccessful) {
                binding.llNotif.visibility = View.VISIBLE
                if (networkConfig.body()!!.data!!.detail!!.jsonMemberNew.toString() == "0") {
                    binding.llNotif.visibility = View.GONE
                } else {
                    binding.tvNotifikasi.text =
                        networkConfig.body()!!.data!!.detail!!.jsonMemberNew.toString()
                }

            } else {
                Log.d(TAG, "getUser: ${networkConfig.errorBody()}")

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun getKelasSelesai() {
        val networkConfig = NetworkConfig().getAfterClass().getHistory(token)

        try {
            if (networkConfig.isSuccessful) {
                for (kelas in networkConfig.body()!!.data!!) {
                    if (kelas!!.status == 3) {
                        if (kelas.teacherRate.toString() == "null") {
                            kelasList.add(kelas)
                            binding.rvKonfirmasiKelas.visibility = View.VISIBLE
                        }
                    }
                }
                binding.rvKonfirmasiKelas.adapter = ListKelasSelesaiAdapter(kelasList) {}
            } else {
                Log.d(TAG, "getUser: ${networkConfig.errorBody()}")

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

}

