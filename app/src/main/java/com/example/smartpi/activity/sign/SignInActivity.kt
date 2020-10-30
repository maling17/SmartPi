package com.example.smartpi.activity.sign

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.activity.KodeAktivasiActivity
import com.example.smartpi.adapter.PhoneCodeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.model.DataItem
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SignInActivity : AppCompatActivity() {

    lateinit var phone_number: String
    lateinit var country_code: String
    var TAG = "myactivity"
    lateinit var getPhoneCode: String
    var kodeNegara = "62"
    private val countryCodeList = ArrayList<DataItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        btn_sign_in.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) { inputNumber() }
        }

        tv_awal_nmr_tlp.setOnClickListener {
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
            tv_awal_nmr_tlp.text = "+$kodeNegara"

            dialog.dismiss()
        }
        rvKodeNegara.layoutManager = LinearLayoutManager(applicationContext)

        dialog.show()
    }

    suspend fun inputNumber() {

        pb_sign_in.visibility = View.VISIBLE
        btn_sign_in.visibility = View.GONE

        phone_number = et_phone.text.toString()
        country_code = kodeNegara
        val nmrTelp = "$country_code$phone_number"

        //memulai Api
        val network = NetworkConfig().inputNumber().inputNumber(phone_number, country_code)

        if (network!!.isSuccessful) {
            if (network.body()!!.status == "activation") {
                pb_sign_in.visibility = View.INVISIBLE
                btn_sign_in.visibility = View.VISIBLE
                val intent = Intent(this, KodeAktivasiActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
            } else {
                pb_sign_in.visibility = View.INVISIBLE
                btn_sign_in.visibility = View.VISIBLE
                val intent = Intent(this, SignInPasswordActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
            }
        } else {
            btn_sign_in.visibility = View.VISIBLE
            pb_sign_in.visibility = View.INVISIBLE
            Log.d(TAG, network.errorBody().toString())
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
}