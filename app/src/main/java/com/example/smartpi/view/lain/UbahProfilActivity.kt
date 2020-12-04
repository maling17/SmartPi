package com.example.smartpi.view.lain

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityUbahProfilBinding
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UbahProfilActivity : AppCompatActivity() {
    lateinit var binding: ActivityUbahProfilBinding
    lateinit var preferences: Preferences
    var token = ""
    var namaUser = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahProfilBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        val name = preferences.getValues("nama").toString()
        token = "Bearer ${preferences.getValues("token")}"
        binding.etNamaUbah.text = Editable.Factory.getInstance().newEditable(name)

        binding.btnSimpanProfile.setOnClickListener {
            binding.clLoadingUbahProfile.visibility = View.VISIBLE
            namaUser = binding.etNamaUbah.text.toString()
            preferences.setValues("nama", namaUser)
            scope.launch { ubahProfile(namaUser) }
        }
        binding.ivBackUbahProfil.setOnClickListener { finish() }
    }

    private suspend fun ubahProfile(nama: String) {

        val networkConfig = NetworkConfig().profile().ubahProfile(token, nama)
        if (networkConfig.isSuccessful) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Update Profile Berhasil",
                    Toast.LENGTH_LONG
                ).show()
                binding.clLoadingUbahProfile.visibility = View.INVISIBLE
                finish()
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Update Profile Gagal",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}