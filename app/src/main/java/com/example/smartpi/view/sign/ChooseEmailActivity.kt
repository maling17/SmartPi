package com.example.smartpi.view.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.sign.ChooseEmailAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityChooseEmailBinding
import com.example.smartpi.model.UserInputData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class ChooseEmailActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseEmailBinding
    private var userList = ArrayList<UserInputData>()
    private lateinit var phoneNumber: String
    private lateinit var country_code: String
    private var TAG = "myactivity"

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseEmailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        phoneNumber = intent.getStringExtra("phone_number").toString()
        country_code = intent.getStringExtra("country_code").toString()

        binding.rvEmail.layoutManager = LinearLayoutManager(this)

        scope.launch { getUser() }

        Log.d("TAG", "onCreate: $userList")
    }

    private suspend fun getUser() {
        userList.clear()
        val network = NetworkConfig().inputNumber().inputNumber(phoneNumber, country_code)

        try {
            if (network!!.isSuccessful) {
                for (user in network.body()!!.data!!) {
                    userList.add(user!!)
                }
                binding.rvEmail.adapter = ChooseEmailAdapter(userList) {
                    val intent = Intent(this, SignInEmailActivity::class.java)
                    intent.putExtra("status_email", "1")
                    intent.putExtra("user", it)
                    finish()
                    startActivity(intent)
                }
            } else {
                Log.d(TAG, "getUser: Data gagal diambil")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}