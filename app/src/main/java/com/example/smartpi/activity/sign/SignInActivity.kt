package com.example.smartpi.activity.sign

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.adapter.PhoneCodeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.model.DataItem
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SignInActivity : AppCompatActivity() {

    private lateinit var phoneNumber: String
    private lateinit var country_code: String
    var TAG = "myactivity"
    var kodeNegara = "62"
    private val countryCodeList = ArrayList<DataItem>()

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        //tombol clear inputan
        changeIconEditText()

        btn_sign_in.setOnClickListener {
            scope.launch(Dispatchers.Main) { inputNumber() }
        }

        tv_awal_nmr_tlp.setOnClickListener {
            scope.launch(Dispatchers.Main) {

                pb_code_phone.visibility=View.VISIBLE
                tv_awal_nmr_tlp.visibility=View.GONE
                showPopupDialog() }
        }


    }

    private suspend fun getCountryCode() {

        val countryCode = NetworkConfig().countryCode().getCountryCode()
        if (countryCode.isSuccessful) {
            for (code in countryCode.body()!!.data!!) {
                Log.d(TAG, "getCountryCode: ${code!!.name}")
                countryCodeList.addAll(listOf(code))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun showPopupDialog() {

        //Menampilkan kode negara
        getCountryCode()
        pb_code_phone.visibility=View.GONE
        tv_awal_nmr_tlp.visibility=View.VISIBLE

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_phone_code)
        dialog.setCancelable(true)

        //style dialog
        val window = dialog.window!!
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //buat tampil kode negara
        val rvKodeNegara = dialog.findViewById<RecyclerView>(R.id.rv_kode_negara)
        rvKodeNegara.adapter = PhoneCodeAdapter(countryCodeList)
        {
            kodeNegara = it.phonecode.toString()
            tv_awal_nmr_tlp.text = "+$kodeNegara"

            dialog.dismiss()
        }
        rvKodeNegara.layoutManager = LinearLayoutManager(applicationContext)

        dialog.show()
    }

    private suspend fun inputNumber() {

        pb_sign_in.visibility = View.VISIBLE
        btn_sign_in.visibility = View.GONE

        phoneNumber = et_phone.text.toString()
        country_code = kodeNegara
        val nmrTelp = "$country_code$phoneNumber"

        //memulai Api
        val network = NetworkConfig().inputNumber().inputNumber(phoneNumber, country_code)

        if (network!!.isSuccessful) {
            if (network.body()!!.status == "activation" || network.body()!!.status == "new") {
                val intent = Intent(this, KodeAktivasiActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
                pb_sign_in.visibility = View.INVISIBLE
                btn_sign_in.visibility = View.VISIBLE
                finish()
            } else {
                val intent = Intent(this, SignInPasswordActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
                pb_sign_in.visibility = View.INVISIBLE
                btn_sign_in.visibility = View.VISIBLE
                finish()
            }
        } else {
            btn_sign_in.visibility = View.VISIBLE
            pb_sign_in.visibility = View.INVISIBLE
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this@SignInActivity,
                    "Check your connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun changeBackgroundButton() {

        et_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_phone.length() <= 4) {
                    btn_sign_in.setBackgroundResource(R.drawable.button_grey)
                    btn_sign_in.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_sign_in.isClickable = false
                } else {
                    btn_sign_in.setBackgroundResource(R.drawable.button_yellow)
                    btn_sign_in.setTextColor(resources.getColor(R.color.white))
                    btn_sign_in.isClickable = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun changeIconEditText() {

        et_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_phone.length() >= 0) {

                    //memunculkan icon X untuk clear input
                    val drawable: Drawable =
                        et_phone.context.resources.getDrawable(R.drawable.ic_icon_clear)
                    et_phone.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                    clearEditText()
                } else {
                    et_phone.setCompoundDrawables(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearEditText() {
        et_phone.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= et_phone.right - et_phone.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    et_phone.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }
}