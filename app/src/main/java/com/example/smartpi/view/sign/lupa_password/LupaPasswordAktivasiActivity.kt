package com.example.smartpi.view.sign.lupa_password

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityLupaPasswordAktivasiBinding
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LupaPasswordAktivasiActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    private var TAG = "MyActivity"
    lateinit var preferences: Preferences

    private lateinit var binding: ActivityLupaPasswordAktivasiBinding
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordAktivasiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        changeBackgroundButton()    //ganti background btn jika field sudah diisi
        changeIconEditText()        //memunculkan icon clear text di edit text

        //menghilang keyboard jika menekan enter pada keyboard hp
        editTextHide(binding.etKodeAktivasiLupaAktivasi)
        editTextHide(binding.etPhoneLupaAktivasi)

        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        binding.etPhoneLupaAktivasi.text = Editable.Factory.getInstance().newEditable(phoneNumber)

        binding.tvKembaliLupaAktivasi.setOnClickListener { finish() }
    }

    private suspend fun verfikasiPassword() {

        binding.pbLupaAktivasi.visibility = View.VISIBLE
        binding.btnVerifikasiToken.visibility = View.GONE
        binding.tvKembaliLupaAktivasi.visibility = View.GONE
        val etKodeAktivasi = binding.etKodeAktivasiLupaAktivasi.text.toString()

        //memulai Api
        val network =
            NetworkConfig().resetPassword().verifikasiKodeLupaPassword(phoneNumber, etKodeAktivasi)

        try {
            if (network!!.isSuccessful) {
                binding.pbLupaAktivasi.visibility = View.GONE
                binding.btnVerifikasiToken.visibility = View.VISIBLE
                binding.tvKembaliLupaAktivasi.visibility = View.VISIBLE

                val intent = Intent(this, GantiLupaPasswordActivity::class.java)
                    .putExtra("nmr_telp", phoneNumber)
                startActivity(intent)
                finish()
            } else {
                binding.pbLupaAktivasi.visibility = View.GONE
                binding.btnVerifikasiToken.visibility = View.VISIBLE
                binding.tvKembaliLupaAktivasi.visibility = View.VISIBLE

                Log.d(TAG, network.errorBody().toString())
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private fun changeBackgroundButton() {

        binding.etKodeAktivasiLupaAktivasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etKodeAktivasiLupaAktivasi.length() <= 2) {
                    binding.btnVerifikasiToken.setBackgroundResource(R.drawable.button_grey)
                    binding.btnVerifikasiToken.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnVerifikasiToken.isClickable = false
                } else {
                    binding.btnVerifikasiToken.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnVerifikasiToken.setTextColor(resources.getColor(R.color.white))
                    binding.btnVerifikasiToken.isClickable = true
                    binding.btnVerifikasiToken.setOnClickListener {
                        scope.launch(Dispatchers.Main) {
                            verfikasiPassword()
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun changeIconEditText() {

        binding.etKodeAktivasiLupaAktivasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etKodeAktivasiLupaAktivasi.length() >= 0) {

                    //memunculkan icon X untuk clear input
                    val drawable: Drawable =
                        binding.etKodeAktivasiLupaAktivasi.context.resources.getDrawable(R.drawable.ic_icon_clear)
                    binding.etKodeAktivasiLupaAktivasi.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                    clearEditText()
                } else {
                    binding.etKodeAktivasiLupaAktivasi.setCompoundDrawables(null, null, null, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearEditText() {
        binding.etKodeAktivasiLupaAktivasi.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etKodeAktivasiLupaAktivasi.right - binding.etKodeAktivasiLupaAktivasi.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    binding.etKodeAktivasiLupaAktivasi.text.clear()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@LupaPasswordAktivasiActivity, LupaPasswordActivity::class.java))
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

    private fun editTextHide(editText: EditText) {
        editText.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_NUMPAD_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true

            }
            false
        }
    }
}