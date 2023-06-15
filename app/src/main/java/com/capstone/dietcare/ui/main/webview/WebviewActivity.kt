package com.capstone.dietcare.ui.main.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.dietcare.R
import com.capstone.dietcare.databinding.ActivityWebviewBinding

class WebviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "News About Health"
        val url = intent.getStringExtra("NEWS_LINK")

        if (url != null){
            binding.webView.loadUrl(url)
            binding.webView.settings.javaScriptEnabled = true
        }

    }
}