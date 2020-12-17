package com.example.smartpi.view.kelas

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpi.adapter.chat.ChatAdapter
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityChatGuruBinding
import com.example.smartpi.model.ChatDataItem
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.net.SocketException

class ChatGuruActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatGuruBinding
    val job = Job()
    val scope = CoroutineScope(job + Dispatchers.Main)
    lateinit var preferences: Preferences
    var chatList = ArrayList<ChatDataItem>()
    var token = ""
    var schedule_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatGuruBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"
        schedule_id = intent.getStringExtra("schedule_id")

        val namaTeacher = intent.getStringExtra("nama_teacher")
        val namaKelas = intent.getStringExtra("nama_kelas")
        val image = intent.getStringExtra("image")

        binding.tvNamaGuru.text = namaTeacher.toString()
        binding.tvNamaKelas.text = namaKelas.toString()
        Picasso.get().load(image).into(binding.ivAvatarChat)
        binding.ivTutupChat.setOnClickListener { finish() }

        binding.rvChat.layoutManager = LinearLayoutManager(this)

        scope.launch {
            while (true) {
                getMessage()
                delay(5000L)
            }

        }
        binding.btnSend.setOnClickListener {
            scope.launch { sendMessage() }
            hideKeyboard()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        scope.launch {
            val jobMessage = scope.launch {
                getMessage()
            }
            jobMessage.cancel()
        }
    }

    private suspend fun getMessage() {
        chatList.clear()
        val networkConfig = NetworkConfig().getMessage().getMessage(token, schedule_id)
        try {
            if (networkConfig.isSuccessful) {
                for (chat in networkConfig.body()!!.data!!) {
                    chatList.add(chat!!)
                }
                binding.rvChat.adapter = ChatAdapter(this, chatList)
            } else {
                Log.d("TAG", "getMessage: gagal")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    private suspend fun sendMessage() {
        val etMessage = binding.etMessage.text
        val networkConfig =
            NetworkConfig().getMessage().sendMessage(token, schedule_id, etMessage.toString())
        binding.etMessage.text.clear()
        try {
            if (networkConfig.isSuccessful) {
                getMessage()
            } else {
                Log.d("TAG", "sendMessage: gagal")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}