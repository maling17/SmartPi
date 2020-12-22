package com.example.smartpi.view.pembelian

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.databinding.ActivityCreditCardBinding

class CreditCardActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreditCardBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val url = intent.getStringExtra("url").toString()

        binding.webviewXendit.webViewClient = WebViewClient()
        binding.webviewXendit.loadUrl(url)
        val webSettings = binding.webviewXendit.settings
        webSettings.javaScriptEnabled = true
        binding.ivBackPembayaran.setOnClickListener { finish() }

    }

    override fun onBackPressed() {
        if (binding.webviewXendit.canGoBack()) {
            binding.webviewXendit.goBack()
        } else {
            super.onBackPressed()
        }
    }
}