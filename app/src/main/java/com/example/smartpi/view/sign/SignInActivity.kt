package com.example.smartpi.view.sign

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.adapter.sign.PhoneCodeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivitySignInBinding
import com.example.smartpi.model.DataItem
import com.example.smartpi.model.UserInputData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException


class SignInActivity : AppCompatActivity() {

    private lateinit var phoneNumber: String
    private lateinit var country_code: String
    private var TAG = "myactivity"
    private var kodeNegara = "62"
    private val countryCodeList = ArrayList<DataItem>()
    private val userList = ArrayList<UserInputData>()

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

        binding.tvSignEmail.setOnClickListener {
            val intent = Intent(this, SignInEmailActivity::class.java)
            intent.putExtra("status_email", "0")
            startActivity(intent)
            finish()
        }

        binding.etPhone.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_NUMPAD_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }


    }

    private suspend fun getCountryCode() {

        val countryCode = NetworkConfig().countryCode().getCountryCode()
        try {
            if (countryCode.isSuccessful) {
                for (code in countryCode.body()!!.data!!) {
                    Log.d(TAG, "getCountryCode: ${code!!.name}")
                    countryCodeList.add(code)
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    @SuppressLint("SetTextI18n")
    private suspend fun showPopupDialog() {

        getCountryCode()    //Menampilkan kode negara
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

        userList.clear()
        binding.pbSignIn.visibility = View.VISIBLE
        binding.btnSignIn.visibility = View.GONE
        binding.tvAtau.visibility = View.GONE
        binding.tvSignEmail.visibility = View.GONE

        phoneNumber = binding.etPhone.text.toString()
        country_code = kodeNegara
        val nmrTelp = "$country_code$phoneNumber"

        //memulai Api
        val network = NetworkConfig().inputNumber().inputNumber(phoneNumber, country_code)

        try {
            if (network!!.isSuccessful) {
                if (network.body()!!.status == "activation" || network.body()!!.status == "new") {
                    val intent = Intent(this, KodeAktivasiActivity::class.java)
                        .putExtra("nmr_telp", nmrTelp)
                    binding.pbSignIn.visibility = View.INVISIBLE
                    binding.btnSignIn.visibility = View.VISIBLE
                    binding.tvAtau.visibility = View.VISIBLE
                    binding.tvSignEmail.visibility = View.VISIBLE
                    startActivity(intent)
                    finish()
                } else {
                    if (network.body()!!.status == "choose") {

                        for (user in network.body()!!.data!!) {
                            userList.add(user!!)
                        }
                        val intent = Intent(this, ChooseEmailActivity::class.java)
                        intent.putExtra("country_code", country_code)
                        intent.putExtra("phone_number", phoneNumber)
                        binding.pbSignIn.visibility = View.INVISIBLE
                        binding.btnSignIn.visibility = View.VISIBLE
                        binding.tvAtau.visibility = View.VISIBLE
                        binding.tvSignEmail.visibility = View.VISIBLE
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, SignInPasswordActivity::class.java)
                            .putExtra("nmr_telp", nmrTelp)
                        binding.pbSignIn.visibility = View.INVISIBLE
                        binding.btnSignIn.visibility = View.VISIBLE
                        binding.tvAtau.visibility = View.VISIBLE
                        binding.tvSignEmail.visibility = View.VISIBLE
                        startActivity(intent)
                        finish()
                    }

                }
            } else {
                binding.btnSignIn.visibility = View.VISIBLE
                binding.btnSignIn.visibility = View.INVISIBLE
                binding.tvAtau.visibility = View.VISIBLE
                binding.tvSignEmail.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this@SignInActivity,
                        "Check your connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
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

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}