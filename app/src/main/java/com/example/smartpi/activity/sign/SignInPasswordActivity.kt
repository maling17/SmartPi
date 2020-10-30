package com.example.smartpi.activity.sign

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignInPasswordActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    var TAG = "MyActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_password)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        // mengambil nomor hp
        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        val textNmrTlp = "No. Telepon anda $phoneNumber"
        tv_nmr_telp_sign_in.text = textNmrTlp

        btn_masuk_sign_in.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main){prosesSignIn()}
        }

    }

    suspend fun prosesSignIn() {

        val etPassword = et_password.text.toString()
        val networkActivation =
            NetworkConfig().checkSignIn().checkSignIn(phoneNumber, etPassword)

        if (networkActivation!!.isSuccessful) {
             val intent = Intent(this, MainActivity::class.java)
                 .putExtra("nmr_telp", phoneNumber)
             startActivity(intent)
        } else {

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this@SignInPasswordActivity,
                    "Password Salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun changeBackgroundButton() {

        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password.length() <= 4) {
                    btn_masuk_sign_in.setBackgroundResource(R.drawable.button_grey)
                    btn_masuk_sign_in.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_masuk_sign_in.isClickable = false
                } else {
                    btn_masuk_sign_in.setBackgroundResource(R.drawable.button_yellow)
                    btn_masuk_sign_in.setTextColor(resources.getColor(R.color.white))
                    btn_masuk_sign_in.isClickable = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }
}