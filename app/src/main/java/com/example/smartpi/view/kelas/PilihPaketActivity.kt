package com.example.smartpi.view.kelas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListPackageActiveAdapter
import com.example.smartpi.adapter.ListTeacherProductAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPilihPaketBinding
import com.example.smartpi.model.PackageActiveItem
import com.example.smartpi.model.TeacherItem
import com.example.smartpi.utils.Preferences
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
    private var user_avalaible_id = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var binding: ActivityPilihPaketBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihPaketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"

        binding.rvPilihPaket.layoutManager = LinearLayoutManager(this)
        binding.rvPilihGuru.layoutManager = LinearLayoutManager(this)

        scope.launch(Dispatchers.Main) { getPackageActive() }

        binding.ivBackPilihPaket.setOnClickListener { finish() }
    }

    suspend fun getPackageActive() {
        binding.pbPilihPaket.visibility = View.VISIBLE
        val network = NetworkConfig().getPackageActive().getActivePackage(token)
        if (network.isSuccessful) {
            for (paket in network.body()!!.data!!) {

                binding.pbPilihPaket.visibility = View.GONE
                packageList.add(paket!!)

            }
            binding.rvPilihPaket.adapter = ListPackageActiveAdapter(packageList) {
                val indexArrayList = packageList.indexOf(it) //mengindex dulu isi yang ada di array
                val kode_teacher =
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

        binding.rvPilihGuru.visibility = View.VISIBLE
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
}