package com.example.smartpi.activity.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartpi.R
import kotlinx.android.synthetic.main.activity_sign_in_password.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val phoneNumber = intent.getStringExtra("nmr_telp").toString()
        val textNmrTlp = "No. Telepon anda $phoneNumber"
        tv_nmr_telp_sign_in.text = textNmrTlp
    }
}