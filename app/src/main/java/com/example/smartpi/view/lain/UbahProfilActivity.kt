package com.example.smartpi.view.lain

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityUbahProfilBinding
import com.example.smartpi.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class UbahProfilActivity : AppCompatActivity() {
    lateinit var binding: ActivityUbahProfilBinding
    lateinit var preferences: Preferences
    private var token = ""
    private var namaUser = ""
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahProfilBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        val name = preferences.getValues("nama").toString()
        token = "Bearer ${preferences.getValues("token")}"
        binding.etNamaUbah.text = Editable.Factory.getInstance().newEditable(name)

        binding.btnSimpanProfile.setOnClickListener {
            binding.clLoadingUbahProfile.visibility = View.VISIBLE
            namaUser = binding.etNamaUbah.text.toString()
            preferences.setValues("nama", namaUser)
            scope.launch { ubahProfile(namaUser) }
        }
        binding.ivBackUbahProfil.setOnClickListener { finish() }
        editTextHide(binding.etNamaUbah)
    }

    private suspend fun ubahProfile(nama: String) {

        val networkConfig = NetworkConfig().profile().ubahProfile(token, nama)
        try {
            if (networkConfig.isSuccessful) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Update Profile Berhasil",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.clLoadingUbahProfile.visibility = View.INVISIBLE
                    finish()
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        "Update Profile Gagal",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }


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