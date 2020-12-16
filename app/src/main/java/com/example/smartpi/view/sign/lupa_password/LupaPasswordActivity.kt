package com.example.smartpi.view.sign.lupa_password

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpi.R
import com.example.smartpi.adapter.PhoneCodeAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityLupaPasswordBinding
import com.example.smartpi.model.DataItem
import com.example.smartpi.view.sign.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class LupaPasswordActivity : AppCompatActivity() {

    private lateinit var phone_number: String
    private lateinit var country_code: String
    var TAG = "myactivity"
    var kodeNegara = "62"
    private val countryCodeList = ArrayList<DataItem>()

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var binding: ActivityLupaPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        //tombol clear inputan
        changeIconEditText()


        binding.tvAwalNmrTlpLupa.setOnClickListener {
            scope.launch(Dispatchers.Main) { showPopupDialog() }
        }
        binding.tvKembaliLupaPassword.setOnClickListener { finish() }
        editTextHide(binding.etPhoneLupaPassword)
    }

    private suspend fun getCountryCode() {

        val countryCode = NetworkConfig().countryCode().getCountryCode()
        try {
            if (countryCode.isSuccessful) {
                for (code in countryCode.body()!!.data!!) {
                    countryCodeList.add(code!!)
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
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
            binding.tvAwalNmrTlpLupa.text = "+$kodeNegara"

            dialog.dismiss()
        }
        rvKodeNegara.layoutManager = LinearLayoutManager(applicationContext)

        dialog.show()
    }

    private suspend fun resetPassword() {

        binding.pbLupaPassword.visibility = View.VISIBLE
        binding.btnUbahPassword.visibility = View.GONE
        binding.tvKembaliLupaPassword.visibility = View.GONE

        phone_number = binding.etPhoneLupaPassword.text.toString()
        country_code = kodeNegara
        val nmrTelp = "$country_code$phone_number"

        //memulai Api
        val network = NetworkConfig().resetPassword().resetPassword(country_code, phone_number)
        try {
            if (network!!.isSuccessful) {
                binding.btnUbahPassword.visibility = View.VISIBLE
                binding.pbLupaPassword.visibility = View.INVISIBLE
                binding.tvKembaliLupaPassword.visibility = View.VISIBLE

                val intent = Intent(this, LupaPasswordAktivasiActivity::class.java)
                    .putExtra("nmr_telp", nmrTelp)
                startActivity(intent)
                finish()
            } else {
                binding.btnUbahPassword.visibility = View.VISIBLE
                binding.pbLupaPassword.visibility = View.INVISIBLE
                binding.tvKembaliLupaPassword.visibility = View.VISIBLE
                Log.d(TAG, network.errorBody().toString())
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private fun changeBackgroundButton() {

        binding.etPhoneLupaPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPhoneLupaPassword.length() <= 4) {
                    binding.btnUbahPassword.setBackgroundResource(R.drawable.button_grey)
                    binding.btnUbahPassword.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnUbahPassword.isClickable = false
                } else {
                    binding.btnUbahPassword.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnUbahPassword.setTextColor(resources.getColor(R.color.white))
                    binding.btnUbahPassword.isClickable = true
                    binding.btnUbahPassword.setOnClickListener {
                        scope.launch(Dispatchers.Main) { resetPassword() }
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun changeIconEditText() {

        binding.etPhoneLupaPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPhoneLupaPassword.length() >= 0) {

                    //memunculkan icon X untuk clear input
                    val drawable: Drawable =
                        binding.etPhoneLupaPassword.context.resources.getDrawable(R.drawable.ic_icon_clear)
                    binding.etPhoneLupaPassword.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                    clearEditText()
                } else {
                    binding.etPhoneLupaPassword.setCompoundDrawables(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearEditText() {
        binding.etPhoneLupaPassword.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etPhoneLupaPassword.right
                    - binding.etPhoneLupaPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    binding.etPhoneLupaPassword.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun editTextHide(editText: EditText) {
        editText.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_NUMPAD_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true

            }
            false
        }
    }
}