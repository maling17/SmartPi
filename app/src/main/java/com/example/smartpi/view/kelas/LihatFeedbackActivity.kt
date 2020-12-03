package com.example.smartpi.view.kelas

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.databinding.ActivityLihatFeedbackBinding

class LihatFeedbackActivity : AppCompatActivity() {

    lateinit var binding: ActivityLihatFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatFeedbackBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var str: String = intent.getStringExtra("feedback").toString()
        str = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(str).toString()
        }
        binding.tvFeedback.text = str
        binding.ivBackDetailFeedback.setOnClickListener { finish() }
    }
}