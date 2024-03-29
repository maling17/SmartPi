package com.example.smartpi.view.profile

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.api.NetworkConfig
import com.example.smartpi.databinding.ActivityProfileGuruBinding
import com.example.smartpi.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketException

class ProfileGuruActivity : AppCompatActivity() {

    private var token = ""
    private var id_guru = 0
    private var nama_guru = ""
    private var rating: Float = 0F
    lateinit var preferences: Preferences
    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)
    lateinit var binding: ActivityProfileGuruBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileGuruBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        preferences = Preferences(this)
        token = "Bearer ${preferences.getValues("token")}"

        binding.ivBackProfileGuru.setOnClickListener { finish() }
        scope.launch { getDetailGuru() }
    }

    private suspend fun getDetailGuru() {
        id_guru = intent.getStringExtra("id_guru").toInt()
        nama_guru = intent.getStringExtra("nama_guru")
        rating = intent.getStringExtra("rating").toFloat()

        if (rating.isNaN()) {
            rating = 5F
        }

        val networkConfig = NetworkConfig().getTeacher().getDetailTeacher(token, id_guru)
        try {
            if (networkConfig.isSuccessful) {
                binding.pbLoading.visibility = View.GONE

                var str: String = networkConfig.body()!!.data!!.profile.toString()

                str = if (str.isEmpty() || str == "null") {
                    "Guru tidak mengisi"
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY).toString()
                    } else {
                        Html.fromHtml(str).toString()
                    }
                }

                binding.tvDescGuru.text = str
                binding.tvNamaGuruProfile.text = nama_guru
                binding.tvRating.text = rating.toString()
                Picasso.get().load(networkConfig.body()!!.data!!.avatar.toString())
                    .into(binding.ivTeacherProfile)
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

    }
}