package com.example.smartpi.view.pembelian

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.PaketLanggananAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityPilihPaketLanggananBinding
import com.example.smartpi.model.PaketItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PilihPaketLanggananActivity : AppCompatActivity() {
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var paketList = ArrayList<PaketItem>()
    lateinit var binding: ActivityPilihPaketLanggananBinding
    var id_paket = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihPaketLanggananBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        id_paket = intent.getStringExtra("id_program")

        scope.launch { getPaketLangganan() }
        binding.ivBackPaketLangganan.setOnClickListener { finish() }

    }

    suspend fun getPaketLangganan() {

        val networkConfig = NetworkConfig().getPaketLangganan().getPricePaket(id_paket)

        try {
            if (networkConfig.isSuccessful) {
                for (paket in networkConfig.body()!!.data!!) {
                    paketList.add(paket!!)
                }
                binding.rvPaketLangganan.layoutManager = LinearLayoutManager(this)
                binding.rvPaketLangganan.adapter = PaketLanggananAdapter(paketList) {

                    val intent = Intent(this, DetailPaketActivity::class.java)
                    intent.putExtra("id_paket", it.id.toString())
                    startActivity(intent)

                }
            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }
}