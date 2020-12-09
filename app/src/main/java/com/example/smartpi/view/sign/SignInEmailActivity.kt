package com.example.smartpi.view.sign

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivitySignInEmailBinding
import com.example.smartpi.model.UserInputData
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.sign.lupa_password.LupaPasswordActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInEmailActivity : AppCompatActivity() {

    var statusEmail = "0"
    var email = ""
    var password = ""
    lateinit var preferences: Preferences

    lateinit var binding: ActivitySignInEmailBinding
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInEmailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        preferences = Preferences(this)

        statusEmail = intent.getStringExtra("status_email").toString()

        val user = intent.getParcelableExtra<UserInputData>("user")

        binding.tvLupaPasswordSignEmail.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LupaPasswordActivity::class.java
                )
            )
        }
        binding.tvMasukNomorHp.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                )
            )
        }
        if (statusEmail == "1") {

            binding.etEmailSignIn.visibility = View.INVISIBLE
            binding.tvEmailSignEmail.visibility = View.VISIBLE
            binding.tvNamaSignEmail.visibility = View.VISIBLE

            email = user!!.email.toString()

            binding.tvNamaSignEmail.text = user.name
            binding.tvEmailSignEmail.text = email
            changeBackgroundButton()

        } else {
            binding.etEmailSignIn.visibility = View.VISIBLE
            binding.tvEmailSignEmail.visibility = View.INVISIBLE
            binding.tvNamaSignEmail.visibility = View.INVISIBLE

            changeBackgroundButton()
        }

    }

    private suspend fun prosesSignIn(email: String) {

        binding.pbSignEmail.visibility = View.VISIBLE
        binding.btnMasukSignEmail.visibility = View.GONE
        binding.tvLupaPasswordSignEmail.visibility = View.GONE
        binding.tvMasukNomorHp.visibility = View.GONE

        val etPassword = binding.etPasswordSignEmail.text.toString()
        val networkActivation =
            NetworkConfig().checkSignIn().checkSignInEmail(email, etPassword)

        if (networkActivation!!.isSuccessful) {

            val token = networkActivation.body()!!.meta.toString()
            val midToken = token.drop(11)
            val finalToken = midToken.dropLast(1)
            binding.pbSignEmail.visibility = View.GONE
            binding.btnMasukSignEmail.visibility = View.VISIBLE
            binding.tvLupaPasswordSignEmail.visibility = View.VISIBLE
            binding.tvMasukNomorHp.visibility = View.VISIBLE

            val intent = Intent(this, MainActivity::class.java)
            preferences.setValues("token", finalToken)
            preferences.setValues("status", "1")
            startActivity(intent)
            finish()
        } else {
            binding.pbSignEmail.visibility = View.INVISIBLE
            binding.btnMasukSignEmail.visibility = View.VISIBLE
            binding.tvLupaPasswordSignEmail.visibility = View.VISIBLE
            binding.tvMasukNomorHp.visibility = View.VISIBLE

            binding.etPasswordSignEmail.requestFocus()
            binding.etPasswordSignEmail.error = "Password Salah"

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this@SignInEmailActivity,
                    "Password Salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun changeBackgroundButton() {

        binding.etPasswordSignEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPasswordSignEmail.length() <= 4) {
                    binding.btnMasukSignEmail.setBackgroundResource(R.drawable.button_grey)
                    binding.btnMasukSignEmail.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnMasukSignEmail.isEnabled = false

                } else {
                    binding.btnMasukSignEmail.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnMasukSignEmail.setTextColor(resources.getColor(R.color.white))
                    binding.btnMasukSignEmail.isEnabled = true

                    if (statusEmail == "1") {
                        binding.btnMasukSignEmail.setOnClickListener {
                            scope.launch(Dispatchers.Main) { prosesSignIn(email) }
                        }
                    } else {
                        binding.btnMasukSignEmail.setOnClickListener {
                            val email = binding.etEmailSignIn.text.toString()
                            scope.launch(Dispatchers.Main) { prosesSignIn(email) }

                        }
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

}