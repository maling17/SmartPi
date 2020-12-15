package com.example.smartpi.view.kelas

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.ListTrialAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPilihTrialBinding
import com.example.smartpi.model.TrialItem
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.pembelian.DetailPembelianActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class PilihTrialActivity : AppCompatActivity() {

    var token = ""
    lateinit var preferences: Preferences
    private val TAG = "MyActivity"
    private var trialList = ArrayList<TrialItem>()
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var binding: ActivityPilihTrialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihTrialBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        binding.rvListTrial.layoutManager = LinearLayoutManager(this)

        scope.launch(Dispatchers.Main) {
            getListTrial()
        }

        binding.ivBackPilihTrial.setOnClickListener { finish() }

    }

    private suspend fun getListTrial() {

        binding.pbListTrial.visibility = View.VISIBLE
        binding.rvListTrial.visibility = View.INVISIBLE
        val network = NetworkConfig().packageTrial().getListTrial(token)

        try {
            if (network.isSuccessful) {
                for (trial in network.body()!!.data!!) {
                    trialList.add(trial!!)
                }
                binding.rvListTrial.visibility = View.VISIBLE
                binding.rvListTrial.adapter = ListTrialAdapter(trialList) {

                    val intent = Intent(this, DetailPembelianActivity::class.java)
                    intent.putExtra("id", it.idPaket.toString())
                    intent.putExtra("nama", it.namaPaket.toString())
                    intent.putExtra("harga", "0")
                    intent.putExtra("durasi", "1 Bulan")
                    intent.putExtra("jenis", "trial")
                    startActivity(intent)


                }
                binding.pbListTrial.visibility = View.GONE
            } else {
                binding.pbListTrial.visibility = View.GONE

            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

}