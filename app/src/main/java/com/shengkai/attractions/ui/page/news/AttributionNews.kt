package com.shengkai.attractions.ui.page.news

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shengkai.attractions.R
import com.shengkai.attractions.databinding.FragmentAttributionNewsBinding
import com.shengkai.attractions.ui.controller.MainActivity

class AttributionNews : Fragment() {

    private lateinit var binding: FragmentAttributionNewsBinding
    private lateinit var viewModel: AttributionNewsViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var newsUrl: String
    private var isFirstLoad : Boolean = true

    companion object {
        const val TAG = "AttributionNews"
        const val ATTRIBUTION_NEWS_KEY = "NEWS_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mainActivity = context
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_attribution_news, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.ivBack.setOnClickListener {
            mainActivity.popBackStack()
        }

        //獲取 ViewModel
        viewModel = ViewModelProvider(this)[AttributionNewsViewModel::class.java]

        newsUrl = arguments?.getString(ATTRIBUTION_NEWS_KEY) ?: ""

        setUpWebView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadNewsWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            // 啟用 AppCache
            cacheMode = WebSettings.LOAD_DEFAULT
            // 啟用文件訪問權限
            allowFileAccess = true
            // 啟用 DOM
            domStorageEnabled = true
            // 啟用數據庫
            databaseEnabled = true
        }


        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {

            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isFirstLoad = false
                binding.progressBar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }
    }

    private fun loadNewsWebView() {
        if (newsUrl.isNotEmpty()) {
            if(isFirstLoad){
                binding.webView.loadUrl(newsUrl)
            }

        }
    }
}