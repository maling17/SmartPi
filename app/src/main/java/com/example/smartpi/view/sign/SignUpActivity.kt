package com.example.smartpi.view.sign

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivitySignUpBinding
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SignUpActivity : AppCompatActivity() {
    private var phoneNumber: String? = null
    lateinit var preferences: Preferences

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        preferences = Preferences(this)

        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        val textNmrTlp = "No. Telepon anda $phoneNumber"
        binding.tvNmrTelpSignUp.text = textNmrTlp

        changeBackgroundButton()

    }

    private suspend fun signUp() {

        val namaUser = binding.etNamaSignUp.text.toString()
        val passwordUser = binding.etPasswordSignUp.text.toString()
        val passwordKonfirmasi = binding.etKonfimasiPasswordSignUp.text.toString()

        val network =
            NetworkConfig().signUp().signUp(phoneNumber, passwordUser, namaUser, passwordKonfirmasi)

        if (network!!.isSuccessful) {
            val token = network.body()!!.meta.toString()
            val midToken = token.drop(17)
            val finalToken = midToken.dropLast(1)
            val intent = Intent(this, MainActivity::class.java)

            preferences.setValues("token", finalToken)
            preferences.setValues("status", "1")
            startActivity(intent)
            finish()

        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    this@SignUpActivity,
                    "Password atau Konfirmasi password ada yang salah",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun changeBackgroundButton() {

        binding.etKonfimasiPasswordSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etKonfimasiPasswordSignUp.length() <= 4) {
                    binding.btnMasukSignUp.setBackgroundResource(R.drawable.button_grey)
                    binding.btnMasukSignUp.setTextColor(resources.getColor(R.color.dark_grey))
                    binding.btnMasukSignUp.isEnabled = false

                } else {
                    binding.btnMasukSignUp.setBackgroundResource(R.drawable.button_yellow)
                    binding.btnMasukSignUp.setTextColor(resources.getColor(R.color.white))
                    binding.btnMasukSignUp.isEnabled = true
                    binding.btnMasukSignUp.setOnClickListener {
                        scope.launch(Dispatchers.Main) {
                            signUp()
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

}