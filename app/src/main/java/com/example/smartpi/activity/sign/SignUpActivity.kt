package com.example.smartpi.activity.sign

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SignUpActivity : AppCompatActivity() {
    var phoneNumber: String? = null
    lateinit var preferences: Preferences

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        preferences = Preferences(this)

        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        val textNmrTlp = "No. Telepon anda $phoneNumber"
        tv_nmr_telp_sign_up.text = textNmrTlp

        changeStatePassword(et_konfimasi_password_sign_up)
        changeStatePassword(et_password_sign_up)
        changeBackgroundButton()

        btn_masuk_sign_up.setOnClickListener {
            scope.launch(Dispatchers.Main) {
                signUp()
            }
        }

    }

    private suspend fun signUp() {

        val namaUser = et_nama_sign_up.text.toString()
        val passwordUser = et_password_sign_up.text.toString()
        val passwordKonfirmasi = et_konfimasi_password_sign_up.text.toString()

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

        et_konfimasi_password_sign_up.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_konfimasi_password_sign_up.length() <= 4) {
                    btn_masuk_sign_up.setBackgroundResource(R.drawable.button_grey)
                    btn_masuk_sign_up.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_masuk_sign_up.isClickable = false

                } else {
                    btn_masuk_sign_up.setBackgroundResource(R.drawable.button_yellow)
                    btn_masuk_sign_up.setTextColor(resources.getColor(R.color.white))
                    btn_masuk_sign_up.isClickable = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    fun changeStatePassword(editText: EditText) {

        var status: Boolean = false
        editText.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {

                //untuk mengklik gambar mata agar state password bisa diganti
                if (event.rawX >= editText.right - editText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    if (!status) {
                        status = true
                        editText.inputType = 129

                        val drawable: Drawable =
                            editText.context.resources.getDrawable(R.drawable.ic_icon_eye_pass_enable)

                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )
                        return@OnTouchListener true

                    } else {
                        status = false
                        editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

                        val drawable: Drawable =
                            editText.context.resources.getDrawable(R.drawable.ic_icon_eye_pass_disable)
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

    }
}