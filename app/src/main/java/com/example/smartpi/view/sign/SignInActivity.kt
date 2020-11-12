package com.example.smartpi.view.sign

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
import com.example.smartpi.databinding.ActivitySignInBinding
import com.example.smartpi.model.DataItem
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

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        //tombol clear inputan
        changeIconEditText()

        binding.tvAwalNmrTlp.setOnClickListener {
            scope.launch(Dispatchers.Main) {

                binding.pbCodePhone.visibility = View.VISIBLE
                binding.tvAwalNmrTlp.visibility = View.GONE
                showPopupDialog()
            }
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
        binding.pbCodePhone.visibility = View.GONE
        binding.tvAwalNmrTlp.visibility = View.VISIBLE

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
            binding.tvAwalNmrTlp.text = "+$kodeNegara"

            dialog.dismiss()
        }
        rvKodeNegara.layoutManager = LinearLayoutManager(applicationContext)

        dialog.show()
    }

    private suspend fun inputNumber() {

        binding.pbSignIn.visibility = View.VISIBLE
        binding.btnSignIn.visibility = View.GONE

        phoneNumber = binding.etPhone.text.toString()
        country_code = kodeNegara
        val nmrTelp = "$country_code$phoneNumber"

        //memulai Api
        val network = NetworkConfig().inputNumber().inputNumber(phoneNumber, country_code)

        if (network!!.isSuccessful) {
            if (network.body()!!.status == "activation" || network.body()!!.status == "new") {
                val intent = Intent(this, KodeAktivasiActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
                binding.pbSignIn.visibility = View.INVISIBLE
                binding.btnSignIn.visibility = View.VISIBLE
                finish()
            } else {
                val intent = Intent(this, SignInPasswordActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
                binding.pbSignIn.visibility = View.INVISIBLE
                binding.btnSignIn.visibility = View.VISIBLE
                finish()
            }
        } else {
            binding.btnSignIn.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.INVISIBLE
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

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPhone.length() <= 4) {
                    binding.btnSignIn.isEnabled = false
                    binding.btnSignIn.isClickable = false
                    binding.btnSignIn.setBackgroundResource(R.drawable.button_grey)
                    binding.btnSignIn.setTextColor(resources.getColor(R.color.dark_grey))

                } else {
                    binding.btnSignIn.isEnabled = true
                    binding.btnSignIn.isClickable = true
                    binding.btnSignIn.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnSignIn.setTextColor(resources.getColor(R.color.white))

                    binding.btnSignIn.setOnClickListener {
                        scope.launch(Dispatchers.Main) { inputNumber() }
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun changeIconEditText() {

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPhone.length() >= 0) {

                    //memunculkan icon X untuk clear input
                    val drawable: Drawable =
                        binding.etPhone.context.resources.getDrawable(R.drawable.ic_icon_clear)
                    binding.etPhone.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                    clearEditText()
                } else {
                    binding.etPhone.setCompoundDrawables(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearEditText() {
        binding.etPhone.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etPhone.right - binding.etPhone.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    binding.etPhone.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }
}