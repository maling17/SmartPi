package com.example.smartpi.activity.sign.lupa_password

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.adapter.PhoneCodeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.model.DataItem
import kotlinx.android.synthetic.main.activity_lupa_password.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LupaPasswordActivity : AppCompatActivity() {

    lateinit var phone_number: String
    lateinit var country_code: String
    var TAG = "myactivity"
    lateinit var getPhoneCode: String
    var kodeNegara = "62"
    private val countryCodeList = ArrayList<DataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        //tombol clear inputan
        changeIconEditText()

        btn_ubah_password.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) { resetPassword() }
        }

        tv_awal_nmr_tlp_lupa.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) { showPopupDialog() }
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
            tv_awal_nmr_tlp_lupa.text = "+$kodeNegara"

            dialog.dismiss()
        }
        rvKodeNegara.layoutManager = LinearLayoutManager(applicationContext)

        dialog.show()
    }

    suspend fun resetPassword() {

        pb_lupa_password.visibility = View.VISIBLE
        btn_ubah_password.visibility = View.GONE
        tv_kembali_lupa_password.visibility = View.GONE

        phone_number = et_phone_lupa_password.text.toString()
        country_code = kodeNegara
        val nmrTelp = "$country_code$phone_number"

        //memulai Api
        val network = NetworkConfig().resetPassword().resetPassword(country_code, phone_number)

        if (network!!.isSuccessful) {
            btn_ubah_password.visibility = View.VISIBLE
            pb_lupa_password.visibility = View.INVISIBLE
            tv_kembali_lupa_password.visibility = View.VISIBLE

           val message= network.body()!!.message
            val intent = Intent(this, LupaPasswordAktivasiActivity::class.java)
                .putExtra("nmr_telp", nmrTelp)
            startActivity(intent)
        } else {
            btn_ubah_password.visibility = View.VISIBLE
            pb_sign_in.visibility = View.INVISIBLE
            tv_kembali_lupa_password.visibility = View.VISIBLE
            Log.d(TAG, network.errorBody().toString())
        }
    }

    private fun changeBackgroundButton() {

        et_phone_lupa_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_phone_lupa_password.length() <= 4) {
                    btn_ubah_password.setBackgroundResource(R.drawable.button_grey)
                    btn_ubah_password.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_ubah_password.isClickable = false
                } else {
                    btn_ubah_password.setBackgroundResource(R.drawable.button_yellow)
                    btn_ubah_password.setTextColor(resources.getColor(R.color.white))
                    btn_ubah_password.isClickable = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    fun changeIconEditText() {

        et_phone_lupa_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_phone_lupa_password.length() >= 0) {

                    //memunculkan icon X untuk clear input
                    val drawable: Drawable =
                        et_phone_lupa_password.context.resources.getDrawable(R.drawable.ic_icon_clear)
                    et_phone_lupa_password.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                    clearEditText()
                } else {
                    et_phone_lupa_password.setCompoundDrawables(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearEditText() {
        et_phone_lupa_password.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= et_phone_lupa_password.right - et_phone_lupa_password.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    et_phone_lupa_password.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

}