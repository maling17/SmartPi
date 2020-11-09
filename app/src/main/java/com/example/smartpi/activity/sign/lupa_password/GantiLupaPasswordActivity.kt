package com.example.smartpi.activity.sign.lupa_password

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.R
import com.example.smartpi.activity.sign.SignInActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.activity_ganti_lupa_password.*
import kotlinx.coroutines.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GantiLupaPasswordActivity : AppCompatActivity() {
    private var phoneNumber: String = ""
    var TAG = "MyActivity"

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_lupa_password)

        phoneNumber = intent.getStringExtra("nmr_telp").toString()
        et_phone_ganti_password.text = Editable.Factory.getInstance().newEditable(phoneNumber)

        scope.launch(Dispatchers.Main) {
            changeBackgroundButton()
            val jobPassword = async { changeStatePassword(et_password_lupa_password) }
            val jobKonfirmasiPassword = async { changeStatePassword(et_konfirmasi_lupa_password) }

            jobPassword.await()
            jobKonfirmasiPassword.await()

        }

        btn_ubah_kata_kunci.setOnClickListener {
            scope.launch(Dispatchers.Main) { gantiPassword() }
        }

        tv_kembali_ubah_password.setOnClickListener {
            finish()
        }

    }

    private suspend fun gantiPassword() {

        pb_ubah_password.visibility = View.VISIBLE
        btn_ubah_kata_kunci.visibility = View.GONE
        tv_kembali_ubah_password.visibility = View.GONE

        val etPassword = et_password_lupa_password.text.toString()
        val etKonfirmasiPassword = et_konfirmasi_lupa_password.text.toString()

        Log.d(
            TAG,
            "no Telepon = ${phoneNumber} password = $etPassword Konfirmasi Password = $etKonfirmasiPassword"
        )

        //memulai Api
        val network =
            NetworkConfig().resetPassword()
                .ubahPassword(phoneNumber, etPassword, etKonfirmasiPassword)

        if (network!!.isSuccessful) {
            pb_ubah_password.visibility = View.GONE
            btn_ubah_kata_kunci.visibility = View.VISIBLE
            tv_kembali_ubah_password.visibility = View.VISIBLE

            Log.d(TAG, "gantiPassword: ${network.body()!!.success}")

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        } else {
            pb_ubah_password.visibility = View.GONE
            btn_ubah_kata_kunci.visibility = View.VISIBLE
            tv_kembali_ubah_password.visibility = View.VISIBLE
            Log.d(TAG, network.errorBody().toString())
        }
    }

    private fun changeBackgroundButton() {

        et_konfirmasi_lupa_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_konfirmasi_lupa_password.length() <= 2) {
                    btn_ubah_kata_kunci.setBackgroundResource(R.drawable.button_grey)
                    btn_ubah_kata_kunci.setTextColor(resources.getColor(R.color.dark_grey))
                    btn_ubah_kata_kunci.isClickable = false
                } else {
                    btn_ubah_kata_kunci.setBackgroundResource(R.drawable.button_yellow)
                    btn_ubah_kata_kunci.setTextColor(resources.getColor(R.color.white))
                    btn_ubah_kata_kunci.isClickable = true
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}