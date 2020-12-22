package com.example.smartpi.view.sign.lupa_password

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityGantiLupaPasswordBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.sign.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GantiLupaPasswordActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    private var TAG = "MyActivity"

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private lateinit var binding: ActivityGantiLupaPasswordBinding

    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGantiLupaPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        binding.etPhoneGantiPassword.text = Editable.Factory.getInstance().newEditable(phoneNumber)

        //ganti background button jika field sudah diisi
        changeBackgroundButton()

        binding.tvKembaliUbahPassword.setOnClickListener {
            finish()
        }

        // jika tekan enter pada keyboard maka keyboard hilang
        editTextHide(binding.etKonfirmasiLupaPassword)
        editTextHide(binding.etPasswordLupaPassword)
        editTextHide(binding.etPhoneGantiPassword)
    }

    private suspend fun gantiPassword() {

        binding.pbUbahPassword.visibility = View.VISIBLE
        binding.btnUbahKataKunci.visibility = View.GONE
        binding.tvKembaliUbahPassword.visibility = View.GONE

        val etPassword = binding.etPasswordLupaPassword.text.toString()
        val etKonfirmasiPassword = binding.etKonfirmasiLupaPassword.text.toString()

        Log.d(
            TAG,
            "no Telepon = $phoneNumber password = $etPassword Konfirmasi Password = $etKonfirmasiPassword"
        )

        //memulai Api
        val network =
            NetworkConfig().resetPassword()
                .ubahPassword(phoneNumber, etPassword, etKonfirmasiPassword)

        try {
            if (network!!.isSuccessful) {
                binding.pbUbahPassword.visibility = View.GONE
                binding.btnUbahKataKunci.visibility = View.VISIBLE
                binding.tvKembaliUbahPassword.visibility = View.VISIBLE

                Log.d(TAG, "gantiPassword: ${network.body()!!.success}")

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.pbUbahPassword.visibility = View.GONE
                binding.btnUbahKataKunci.visibility = View.VISIBLE
                binding.tvKembaliUbahPassword.visibility = View.VISIBLE
                Log.d(TAG, network.errorBody().toString())
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private fun changeBackgroundButton() {

        binding.etKonfirmasiLupaPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etKonfirmasiLupaPassword.length() <= 2) {
                    binding.btnUbahKataKunci.setBackgroundResource(R.drawable.button_grey)
                    binding.btnUbahKataKunci.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnUbahKataKunci.isClickable = false
                } else {
                    binding.btnUbahKataKunci.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnUbahKataKunci.setTextColor(resources.getColor(R.color.white))
                    binding.btnUbahKataKunci.isClickable = true
                    binding.btnUbahKataKunci.setOnClickListener {
                        scope.launch(Dispatchers.Main) { gantiPassword() }
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@GantiLupaPasswordActivity, LupaPasswordActivity::class.java))
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
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }
    }
}