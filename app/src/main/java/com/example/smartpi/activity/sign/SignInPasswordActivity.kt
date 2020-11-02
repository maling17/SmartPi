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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.activity.sign.lupa_password.LupaPasswordActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignInPasswordActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    var TAG = "MyActivity"
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_password)

        preferences = Preferences(this)

        //ganti button background jika field sudah diisi
        changeBackgroundButton()

        // mengambil nomor hp
        phoneNumber = intent.getStringExtra("nmr_telp")!!.toString()
        val textNmrTlp = "No. Telepon anda $phoneNumber"
        tv_nmr_telp_sign_in.text = textNmrTlp

        btn_masuk_sign_in.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) { prosesSignIn() }
        }
        tv_lupa_password.setOnClickListener {
            startActivity(Intent(this, LupaPasswordActivity::class.java))
        }

    }

    private suspend fun prosesSignIn() {

        pb_sign_in_password.visibility = View.VISIBLE
        btn_masuk_sign_in.visibility = View.GONE
        tv_lupa_password.visibility = View.INVISIBLE

        val etPassword = et_password.text.toString()
        val networkActivation =
            NetworkConfig().checkSignIn().checkSignIn(phoneNumber, etPassword)

        if (networkActivation!!.isSuccessful) {
            pb_sign_in_password.visibility = View.INVISIBLE
            btn_masuk_sign_in.visibility = View.VISIBLE
            tv_lupa_password.visibility = View.VISIBLE

            val token = networkActivation.body()!!.meta.toString()
            val midToken = token.drop(11)
            val finalToken = midToken.dropLast(1)

            Log.d(TAG, "prosesSignIn: $finalToken")

            val intent = Intent(this, MainActivity::class.java)
            preferences.setValues("token", finalToken)
            startActivity(intent)
            finish()
        } else {
            pb_sign_in_password.visibility = View.INVISIBLE
            btn_masuk_sign_in.visibility = View.VISIBLE
            tv_lupa_password.visibility = View.VISIBLE

            et_password.requestFocus()
            et_password.error = "Password Salah"

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

        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password.length() <= 4) {
                    btn_masuk_sign_in.setBackgroundResource(R.drawable.button_grey)
                    btn_masuk_sign_in.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_masuk_sign_in.isClickable = false
                    changeStatePassword()
                } else {
                    btn_masuk_sign_in.setBackgroundResource(R.drawable.button_yellow)
                    btn_masuk_sign_in.setTextColor(resources.getColor(R.color.white))
                    btn_masuk_sign_in.isClickable = true
                    changeStatePassword()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    fun changeStatePassword() {

        var status: Boolean = false
        et_password.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {

                //untuk mengklik gambar mata agar state password bisa diganti
                if (event.rawX >= et_password.right - et_password.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    if (!status) {
                        status = true
                        et_password.inputType = 129

                        val drawable: Drawable =
                            et_password.context.resources.getDrawable(R.drawable.ic_icon_eye_pass_enable)

                        et_password.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )
                        return@OnTouchListener true

                    } else {
                        status = false
                        et_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

                        val drawable: Drawable =
                            et_password.context.resources.getDrawable(R.drawable.ic_icon_eye_pass_disable)
                        et_password.setCompoundDrawablesWithIntrinsicBounds(
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