package com.example.smartpi.view.kelas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListFilterAdapter
import com.example.smartpi.adapter.ListFilterKhususAdapter
import com.example.smartpi.adapter.ListPackageActiveAdapter
import com.example.smartpi.adapter.ListTeacherProductAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPilihPaketBinding
import com.example.smartpi.model.*
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.kelas.filter.FilterWaktuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PilihPaketActivity : AppCompatActivity() {

    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var packageList = ArrayList<PackageActiveItem>()
    private var teacherList = ArrayList<TeacherItem>()
    private var filterTeacherList = ArrayList<FilterUmumItem>()
    private var filterTeacherKhususList = ArrayList<FilterKhususItem>()
    private var user_avalaible_id = ""
    private var statusFilter = "0"
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var kode_teacher = ""
    private lateinit var binding: ActivityPilihPaketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihPaketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        val getStatusFilter = intent.getStringExtra("statusFilter")
        if (getStatusFilter.isNullOrEmpty()) {
            statusFilter = "0"
            scope.launch(Dispatchers.Main) { getPackageActive() }
        } else {
            statusFilter = getStatusFilter
            if (statusFilter == "1") {
                scope.launch {
                    user_avalaible_id = intent.getStringExtra("user_available_id")
                    filterUmumGuru()
                }
            } else {
                scope.launch {
                    user_avalaible_id = intent.getStringExtra("user_available_id")
                    filterKhususGuru()
                }

            }
        }

        Log.d(TAG, "onCreate: $getStatusFilter")
        binding.rvPilihPaket.layoutManager = LinearLayoutManager(this)
        binding.rvPilihGuru.layoutManager = LinearLayoutManager(this)

        binding.ivBackPilihPaket.setOnClickListener { finish() }
        binding.btnFilter.setOnClickListener {
            binding.pbLoadingPilihPaket.visibility = View.VISIBLE
            val intent = Intent(this, FilterWaktuActivity::class.java)
            preferences.setValues("kode_teacher", kode_teacher)
            preferences.setValues("user_available_id", user_avalaible_id)
            startActivity(intent)
            binding.pbLoadingPilihPaket.visibility = View.GONE
        }

    }

    suspend fun getPackageActive() {
        packageList.clear()
        binding.pbPilihPaket.visibility = View.VISIBLE
        val network = NetworkConfig().getPackageActive().getActivePackage(token)
        if (network.isSuccessful) {
            for (paket in network.body()!!.data!!) {

                binding.pbPilihPaket.visibility = View.GONE
                packageList.add(paket!!)

            }
            binding.rvPilihPaket.adapter = ListPackageActiveAdapter(packageList) {
                val indexArrayList = packageList.indexOf(it) //mengindex dulu isi yang ada di array
                kode_teacher =
                    packageList[indexArrayList].kode_teacher.toString() //mengfilter dan mengambil value kode_teacher
                user_avalaible_id = packageList[indexArrayList].id.toString()
                scope.launch(Dispatchers.Main) { getTeacherProduct(kode_teacher) }
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Tidak dapat mengambil paket",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private suspend fun getTeacherProduct(kode_teacher: String) {
        teacherList.clear()
        binding.clGuru.visibility = View.VISIBLE
        binding.rvPilihPaket.visibility = View.GONE
        binding.pbPilihPaket.visibility = View.VISIBLE

        val network = NetworkConfig().getTeacher().getTeacherProduct(token, kode_teacher)
        if (network.isSuccessful) {
            for (teacher in network.body()!!.teacher!!) {
                binding.pbPilihPaket.visibility = View.GONE
                teacherList.add(teacher!!)

            }
            binding.rvPilihGuru.adapter = ListTeacherProductAdapter(teacherList) {

                val intent = Intent(this, PilihJadwalActivity::class.java).putExtra("data", it)
                    .putExtra("user_avalaible_id", user_avalaible_id)
                startActivity(intent)
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Tidak dapat mengambil guru",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    suspend fun filterUmumGuru() {

        val filter = intent.getParcelableExtra<FilterUmum>("filter")!!
        val range_akhir = filter.range_end
        val hayu_atuh = filter.range_start
        val hari = filter.hari
        kode_teacher = filter.kode_teacher.toString()
        val timezone = filter.time_zone

        binding.clGuru.visibility = View.VISIBLE
        binding.rvPilihPaket.visibility = View.GONE
        binding.pbPilihPaket.visibility = View.VISIBLE

        Log.d(TAG, "filterUmumGuru: $range_akhir, $hari , $hayu_atuh,$kode_teacher,$timezone")

        val networkConfig = NetworkConfig().getTeacher()
            .filterTeacherUmum(token, range_akhir, hari, kode_teacher, timezone, hayu_atuh)

        if (networkConfig.isSuccessful) {
            for (teacher in networkConfig.body()!!.data!!) {
                binding.pbPilihPaket.visibility = View.GONE
                filterTeacherList.add(teacher!!)

            }
            binding.rvPilihGuru.adapter = ListFilterAdapter(filterTeacherList) {

                val teacherItem = TeacherItem()
                teacherItem.recommended = it.recommended
                teacherItem.rating = it.rating
                teacherItem.name = it.name
                teacherItem.avatar = it.avatar
                teacherItem.id = it.id
                val intent =
                    Intent(this, PilihJadwalActivity::class.java).putExtra("data", teacherItem)
                        .putExtra("user_avalaible_id", user_avalaible_id)
                startActivity(intent)
            }
        }


    }

    suspend fun filterKhususGuru() {

        val waktu = intent.getStringExtra("waktu")
        val timezone = intent.getStringExtra("timezone")
        val kode_teacher = intent.getStringExtra("kode_teacher")

        binding.clGuru.visibility = View.VISIBLE
        binding.rvPilihPaket.visibility = View.GONE
        binding.pbPilihPaket.visibility = View.VISIBLE

        val networkConfig = NetworkConfig().getTeacher()
            .filterTeacherKhusus(token, waktu, timezone, kode_teacher)

        if (networkConfig.isSuccessful) {
            for (teacher in networkConfig.body()!!.data!!) {
                binding.pbPilihPaket.visibility = View.GONE
                filterTeacherKhususList.add(teacher!!)

            }
            binding.rvPilihGuru.adapter = ListFilterKhususAdapter(filterTeacherKhususList) {
                binding.pbLoadingPilihPaket.visibility = View.VISIBLE
                val teacherItem = TeacherItem()
                teacherItem.recommended = it.recommended
                teacherItem.rating = it.rating
                teacherItem.name = it.name
                teacherItem.avatar = it.avatar
                teacherItem.id = it.id
                val intent =
                    Intent(this, PilihJadwalActivity::class.java).putExtra("data", teacherItem)
                        .putExtra("user_avalaible_id", user_avalaible_id)
                startActivity(intent)
                binding.pbLoadingPilihPaket.visibility = View.GONE
            }
        }


    }

}