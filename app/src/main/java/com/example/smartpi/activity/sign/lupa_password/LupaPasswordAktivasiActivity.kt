package com.example.smartpi.activity.sign.lupa_password

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.activity_lupa_password_aktivasi.*
import kotlinx.coroutines.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LupaPasswordAktivasiActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    var TAG = "MyActivity"
    lateinit var preferences: Preferences

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password_aktivasi)

        changeBackgroundButton()
        changeIconEditText()

        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        et_phone_lupa_aktivasi.text = Editable.Factory.getInstance().newEditable(phoneNumber)

        btn_verifikasi_token.setOnClickListener {
            scope.launch(Dispatchers.Main) {
                verfikasiPassword()
            }
        }
        tv_kembali_lupa_aktivasi.setOnClickListener {
            finish()
        }

    }

    private suspend fun verfikasiPassword() {

        pb_lupa_aktivasi.visibility = View.VISIBLE
        btn_verifikasi_token.visibility = View.GONE
        tv_kembali_lupa_aktivasi.visibility = View.GONE
        val etKodeAktivasi = et_kode_aktivasi_lupa_aktivasi.text.toString()

        //memulai Api
        val network =
            NetworkConfig().resetPassword().verifikasiKodeLupaPassword(phoneNumber, etKodeAktivasi)

        if (network!!.isSuccessful) {
            pb_lupa_aktivasi.visibility = View.GONE
            btn_verifikasi_token.visibility = View.VISIBLE
            tv_kembali_lupa_aktivasi.visibility = View.VISIBLE

            val intent = Intent(this, GantiLupaPasswordActivity::class.java)
                .putExtra("nmr_telp", phoneNumber)
            startActivity(intent)
        } else {
            pb_lupa_aktivasi.visibility = View.GONE
            btn_verifikasi_token.visibility = View.VISIBLE
            tv_kembali_lupa_aktivasi.visibility = View.VISIBLE
            Log.d(TAG, network.errorBody().toString())
        }
    }

    private fun changeBackgroundButton() {

        et_kode_aktivasi_lupa_aktivasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_kode_aktivasi_lupa_aktivasi.length() <= 2) {
                    btn_verifikasi_token.setBackgroundResource(R.drawable.button_grey)
                    btn_verifikasi_token.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_verifikasi_token.isClickable = false
                } else {
                    btn_verifikasi_token.setBackgroundResource(R.drawable.button_yellow)
                    btn_verifikasi_token.setTextColor(resources.getColor(R.color.white))
                    btn_verifikasi_token.isClickable = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun changeIconEditText() {

        et_kode_aktivasi_lupa_aktivasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_kode_aktivasi_lupa_aktivasi.length() >= 0) {

                    //memunculkan icon X untuk clear input
                    val drawable: Drawable =
                        et_kode_aktivasi_lupa_aktivasi.context.resources.getDrawable(R.drawable.ic_icon_clear)
                    et_kode_aktivasi_lupa_aktivasi.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                    clearEditText()
                } else {
                    et_kode_aktivasi_lupa_aktivasi.setCompoundDrawables(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearEditText() {
        et_kode_aktivasi_lupa_aktivasi.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= et_kode_aktivasi_lupa_aktivasi.right - et_kode_aktivasi_lupa_aktivasi.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    et_kode_aktivasi_lupa_aktivasi.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}