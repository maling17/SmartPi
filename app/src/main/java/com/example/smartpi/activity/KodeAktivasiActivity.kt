package com.example.smartpi.activity

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
import com.example.smartpi.activity.sign.SignInPasswordActivity
import com.example.smartpi.api.NetworkConfig
import kotlinx.android.synthetic.main.activity_kode_aktivasi.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KodeAktivasiActivity : AppCompatActivity() {

    var phoneNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kode_aktivasi)

        phoneNumber = intent.getStringExtra("nmr_telp")!!.toString()
        val textNmrTlp = "No. Telepon anda ($phoneNumber detik)"
        tv_nmr_telp_sign_up.text = textNmrTlp

        btn_masuk_sign_up.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                prosesSignUp()
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            changeBackgroundButton()
            timerKirimKode()
        }


    }

    private fun timerKirimKode() {

        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val second = millisUntilFinished / 1000
                val kirimUlang = "Kirim ulang kode registrasi $second"

                tv_kirim_ulang_kode.setTextColor(resources.getColor(R.color.dark_grey))
                tv_kirim_ulang_kode.text = kirimUlang
            }

            override fun onFinish() {
                GlobalScope.launch(Dispatchers.Main) {
                    resendCodeActivation()
                }

            }
        }
        timer.start()
    }

    private fun changeBackgroundButton() {

        et_kode_aktivasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_kode_aktivasi.length() <= 0) {
                    btn_masuk_sign_up.setBackgroundResource(R.drawable.button_grey)
                    btn_masuk_sign_up.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_masuk_sign_up.isClickable = false
                } else {
                    btn_masuk_sign_up.setBackgroundResource(R.drawable.button_yellow)
                    btn_masuk_sign_up.setTextColor(resources.getColor(R.color.white))
                    btn_masuk_sign_up.isClickable = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

    }

    suspend fun resendCodeActivation() {
        tv_kirim_ulang_kode.setTextColor(resources.getColor(R.color.blue))
        tv_kirim_ulang_kode.text = resources.getString(R.string.kirim_ulang_kode_registrasi)

        tv_kirim_ulang_kode.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
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

    suspend fun prosesSignUp() {

        val etKode = et_kode_aktivasi.text.toString()
        val networkActivation =
            NetworkConfig().inputActivation().inputActivation(phoneNumber, etKode)

        if (networkActivation!!.isSuccessful) {

            val intent = Intent(this, SignInPasswordActivity::class.java)
                .putExtra("nmr_telp", phoneNumber)
            startActivity(intent)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}