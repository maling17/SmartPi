package com.example.smartpi.view.lain

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityUbahKataSandiBinding
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UbahKataSandiActivity : AppCompatActivity() {
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    lateinit var preferences: Preferences
    var token = ""
    lateinit var binding: ActivityUbahKataSandiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahKataSandiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"


        changeBackgroundButton()
    }

    suspend fun gantiPassword() {
        val newPassword = binding.etPasswordBaru.text.toString()
        val confrimationPassword = binding.etKonfirmasiPasswordBaru.text.toString()
        val oldPassword = binding.etPasswordLama.text.toString()
        val networkConfig = NetworkConfig().profile()
            .ubahKataSandi(token, oldPassword, newPassword, confrimationPassword)
        if (networkConfig.isSuccessful) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, "Ubah Kata Sandi Berhasil", Toast.LENGTH_LONG).show()
                finish()
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this,
                    "Ubah Kata Sandi Gagal, Silahkan coba lagi atau cek jaringan anda",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun changeBackgroundButton() {

        binding.etKonfirmasiPasswordBaru.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etKonfirmasiPasswordBaru.length() <= 2) {
                    binding.btnSimpanGantiPassword.setBackgroundColor(resources.getColor(R.color.grey))
                    binding.btnSimpanGantiPassword.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnSimpanGantiPassword.isEnabled = false
                } else {
                    binding.btnSimpanGantiPassword.setBackgroundColor(resources.getColor(R.color.yellow))
                    binding.btnSimpanGantiPassword.setTextColor(resources.getColor(R.color.white))
                    binding.btnSimpanGantiPassword.isEnabled = true

                    binding.btnSimpanGantiPassword.setOnClickListener {
                        scope.launch { gantiPassword() }
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }
}