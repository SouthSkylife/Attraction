package com.shengkai.attractions.ui.page.web

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.shengkai.attractions.databinding.FragmentWebviewBoardBinding
import com.shengkai.attractions.ui.controller.MainActivity

/**
 * WebView Load Url 作用頁
 */
class WebViewBoardPage : Fragment() {

    private lateinit var binding: FragmentWebviewBoardBinding
    private lateinit var viewModel: WebViewBoardViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var boardUrl: String
    private lateinit var boardTitle: String
    private var isFirstLoad: Boolean = true

    companion object {
        const val TAG = "WebViewBoard"
        const val WEB_BOARD_URL_KEY = "WEB_BOARD_URL_KEY"
        const val WEB_BOARD_TITLE_KEY = "WEB_BOARD_TITLE_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mainActivity = context
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_webview_board, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.ivBack.setOnClickListener {
            mainActivity.popBackStack()
        }

        //獲取 ViewModel
        viewModel = ViewModelProvider(this)[WebViewBoardViewModel::class.java]

        boardUrl = arguments?.getString(WEB_BOARD_URL_KEY) ?: ""
        boardTitle = arguments?.getString(WEB_BOARD_TITLE_KEY) ?: ""
        binding.tvTitle.text = boardTitle

        setUpWebView()
    }

    override fun onStart() {
        super.onStart()
        loadNewsWebView()
    }

    /**
     * setUp init webView
     */
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

    /**
     * load webView
     */
    private fun loadNewsWebView() {
        if (boardUrl.isNotEmpty()) {
            if (isFirstLoad) {
                binding.webView.loadUrl(boardUrl)
            }

        }
    }
}