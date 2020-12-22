package com.example.smartpi.view.sign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivitySignInPasswordBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.sign.lupa_password.LupaPasswordActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class SignInPasswordActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    private var TAG = "MyActivity"
    lateinit var preferences: Preferences

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private lateinit var binding: ActivitySignInPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInPasswordBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        preferences = Preferences(this)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        // mengambil nomor hp
        phoneNumber = intent.getStringExtra("nmr_telp")!!.toString()
        val textNmrTlp = "No. Telepon anda $phoneNumber"
        binding.tvNmrTelpSignIn.text = textNmrTlp

        binding.tvLupaPassword.setOnClickListener {
            startActivity(Intent(this, LupaPasswordActivity::class.java))
            finish()
        }
        binding.etPassword.setOnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

    }

    private suspend fun prosesSignIn() {

        binding.pbSignInPassword.visibility = View.VISIBLE
        binding.btnMasukSignIn.visibility = View.GONE
        binding.tvLupaPassword.visibility = View.INVISIBLE

        val etPassword = binding.etPassword.text.toString()
        val networkActivation =
            NetworkConfig().checkSignIn().checkSignIn(phoneNumber, etPassword)
        try {
            if (networkActivation!!.isSuccessful) {
                binding.pbSignInPassword.visibility = View.INVISIBLE
                binding.btnMasukSignIn.visibility = View.VISIBLE
                binding.tvLupaPassword.visibility = View.VISIBLE

                //untuk mengubah token menjadi $token
                val token = networkActivation.body()!!.meta.toString()
                val midToken = token.drop(11)
                val finalToken = midToken.dropLast(1)

                Log.d(TAG, "prosesSignIn: $finalToken")

                val intent = Intent(this, MainActivity::class.java)
                preferences.setValues("token", finalToken)
                preferences.setValues("nama", networkActivation.body()!!.data!!.username!!)
                preferences.setValues("email", networkActivation.body()!!.data!!.email!!)
                preferences.setValues("phone", networkActivation.body()!!.data!!.phone!!)
                preferences.setValues("status", "1")
                preferences.setValues("firstTime", "1")
                preferences.setValues("user_id", networkActivation.body()!!.data!!.id.toString())
                startActivity(intent)
                finish()

            } else {
                binding.pbSignInPassword.visibility = View.INVISIBLE
                binding.btnMasukSignIn.visibility = View.VISIBLE
                binding.tvLupaPassword.visibility = View.VISIBLE

                binding.etPassword.requestFocus()
                binding.etPassword.error = "Password Salah"

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this@SignInPasswordActivity,
                        "Password Salah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private fun changeBackgroundButton() {

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPassword.length() <= 4) {
                    binding.btnMasukSignIn.setBackgroundResource(R.drawable.button_grey)
                    binding.btnMasukSignIn.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnMasukSignIn.isEnabled = false

                } else {
                    binding.btnMasukSignIn.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnMasukSignIn.setTextColor(resources.getColor(R.color.white))
                    binding.btnMasukSignIn.isEnabled = true

                    binding.btnMasukSignIn.setOnClickListener {
                        scope.launch(Dispatchers.Main) { prosesSignIn() }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
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
}