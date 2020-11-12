package com.example.smartpi.view.sign

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityKodeAktivasiBinding
import kotlinx.coroutines.*

class KodeAktivasiActivity : AppCompatActivity() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    var phoneNumber: String = ""

    private lateinit var binding: ActivityKodeAktivasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKodeAktivasiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        phoneNumber = intent.getStringExtra("nmr_telp")!!.toString()
        val textNmrTlp = "No. Telepon anda ($phoneNumber)"
        binding.tvNmrTelpSignUp.text = textNmrTlp

        scope.launch(Dispatchers.Main) {
            val job1 = async { changeBackgroundButton() }
            val job2 = async { timerKirimKode() }
            job1.await()
            job2.await()

        }


    }

    private fun timerKirimKode() {

        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val second = millisUntilFinished / 1000
                val kirimUlang = "Kirim ulang kode registrasi $second detik"

                binding.tvKirimUlangKode.setTextColor(resources.getColor(R.color.dark_grey))
                binding.tvKirimUlangKode.text = kirimUlang
            }

            override fun onFinish() {
                scope.launch(Dispatchers.Main) {
                    resendCodeActivation()
                }

            }
        }
        timer.start()
    }

    private fun changeBackgroundButton() {

        binding.etKodeAktivasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etKodeAktivasi.length() <= 0) {
                    binding.btnMasukKodeAktivasi.setBackgroundResource(R.drawable.button_grey)
                    binding.btnMasukKodeAktivasi.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnMasukKodeAktivasi.isEnabled = false
                    binding.btnMasukKodeAktivasi.isClickable = false
                } else {
                    binding.btnMasukKodeAktivasi.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnMasukKodeAktivasi.setTextColor(resources.getColor(R.color.white))
                    binding.btnMasukKodeAktivasi.isEnabled = true
                    binding.btnMasukKodeAktivasi.isClickable = true
                    binding.btnMasukKodeAktivasi.setOnClickListener {
                        scope.launch(Dispatchers.Main) {
                            prosesAktivasi()
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }

    suspend fun resendCodeActivation() {
        binding.tvKirimUlangKode.setTextColor(resources.getColor(R.color.blue))
        binding.tvKirimUlangKode.text = resources.getString(R.string.kirim_ulang_kode_registrasi)

        binding.tvKirimUlangKode.setOnClickListener {
            scope.launch(Dispatchers.Main) {
                val network = NetworkConfig().resendActivation().resendCode(phoneNumber)
                if (network!!.isSuccessful) {
                    timerKirimKode()
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            this@KodeAktivasiActivity,
                            "Check your Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private suspend fun prosesAktivasi() {

        val etKode = binding.etKodeAktivasi.text.toString()
        val networkActivation =
            NetworkConfig().inputActivation().inputActivation(phoneNumber, etKode)

        if (networkActivation!!.isSuccessful) {

            val intent = Intent(this, SignUpActivity::class.java)
                .putExtra("nmr_telp", phoneNumber)
            startActivity(intent)
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this@KodeAktivasiActivity,
                    "Kode Aktivasi Salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SignInActivity::class.java))
    }
}