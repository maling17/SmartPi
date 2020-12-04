package com.example.smartpi.view.sign

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class SignInPasswordActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    var TAG = "MyActivity"
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
        }

    }

    private suspend fun prosesSignIn() {

        binding.pbSignInPassword.visibility = View.VISIBLE
        binding.btnMasukSignIn.visibility = View.GONE
        binding.tvLupaPassword.visibility = View.INVISIBLE

        val etPassword = binding.etPassword.text.toString()
        val networkActivation =
            NetworkConfig().checkSignIn().checkSignIn(phoneNumber, etPassword)

        if (networkActivation!!.isSuccessful) {
            binding.pbSignInPassword.visibility = View.INVISIBLE
            binding.btnMasukSignIn.visibility = View.VISIBLE
            binding.tvLupaPassword.visibility = View.VISIBLE

            val token = networkActivation.body()!!.meta.toString()
            val midToken = token.drop(11)
            val finalToken = midToken.dropLast(1)

            Log.d(TAG, "prosesSignIn: $finalToken")

            val intent = Intent(this, MainActivity::class.java)
            preferences.setValues("token", finalToken)
            preferences.setValues("nama", networkActivation.body()!!.data!!.username!!)
            preferences.setValues("status", "1")
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
    }
}