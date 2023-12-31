package com.shengkai.attractions.ui.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.shengkai.attractions.R
import com.shengkai.attractions.base.iFragmentTransactionCallback
import com.shengkai.attractions.common.constant.FragmentAnim
import com.shengkai.attractions.common.dialog.LanguageSelectionDialog
import com.shengkai.attractions.common.dialog.LoadingDialog
import com.shengkai.attractions.data.local.ApplicationSp
import com.shengkai.attractions.data.remote.AttractionDetail
import com.shengkai.attractions.databinding.ActivityMainBinding
import com.shengkai.attractions.ui.detail.AttributionDetailAdapter
import com.shengkai.attractions.ui.news.AttributionNewsAdapter
import com.shengkai.attractions.ui.page.detail.AttributionDetailPage
import com.shengkai.attractions.ui.page.news.AttributionNewsPage
import com.shengkai.attractions.ui.page.web.WebViewBoardPage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * 主畫面(最新消息/遊憩景點/多國語系切換)
 */
class MainActivity : AppCompatActivity(), iFragmentTransactionCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var attractionNewsAdapter: AttributionNewsAdapter
    private lateinit var attractionDetailAdapter: AttributionDetailAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoadMoreLoading = false
    private var isFirstLoad: Boolean = true
    private var dialog = LoadingDialog()
    private var canKeepLoad: Boolean = true

    private var fragmentManager: FragmentManager = supportFragmentManager

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initNewsComponent()
        initAttractionsComponent()
        registerListener()
        bindObserver()
    }

    override fun onStart() {
        super.onStart()

        if (isFirstLoad) {
            viewModel.attractionInfoPage = 1
            isFirstLoad = false
            showLoadingDialog()
            viewModel.getAttributionNews(this)
            viewModel.getAttributionList(this)
        }
    }

    //-------------------------------------- init ----------------------------------------------//

    /**
     * 初始化
     */
    private fun init() {
        // 初始化 Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        //獲取 ViewModel
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setLanguageLocale()
    }

    /**
     * 綁定觀察者
     */
    @SuppressLint("SetTextI18n")
    private fun bindObserver() {
        //台北市最新消息
        viewModel.attributionNewsData.observe(this) {
            binding.tvNoNewsHint.visibility = if (it.data.isEmpty()) View.VISIBLE else View.GONE
            attractionNewsAdapter.setAttractionNews(
                if (it.data.size > 3) it.data.subList(0, 3) else it.data
            )
        }

        //台北市景點
        viewModel.attractionListData.observe(
            this
        ) {
            if(viewModel.isLockListWorking){
                binding.tvCount.text = "${viewModel.attractionInfoPage}/${it.total}"
                attractionDetailAdapter.addAttractionDetailData(it.data)

                isLoadMoreLoading = false
                canKeepLoad = it.data.isNotEmpty()

                if (canKeepLoad) {
                    viewModel.attractionInfoPage += 1
                }

                cancelLoadingDialog()

                viewModel.isLockListWorking = false
            }

        }
    }

    /**
     * 初始化最新消息組件
     */
    private fun initNewsComponent() {
        // 初始化 Adapter，並設置點擊監聽器
        attractionNewsAdapter = AttributionNewsAdapter { newsUrl: String ->
            jumpToAttributionNewsPage(newsUrl)
        }

        // 設置 RecyclerView 的 Adapter
        binding.rcyNews.adapter = attractionNewsAdapter

        // 設置 RecyclerView 的 LayoutManager（這裡使用 LinearLayoutManager）
        binding.rcyNews.layoutManager = LinearLayoutManager(this)
    }

    /**
     * 初始化遊憩景點組件
     */
    private fun initAttractionsComponent() {
        // 初始化 Adapter，並設置點擊監聽器
        attractionDetailAdapter = AttributionDetailAdapter { detail: AttractionDetail ->
            jumpToAttributionDetailPage(detail)
        }

        // 設置 RecyclerView 的 Adapter
        binding.rcyAttribution.adapter = attractionDetailAdapter

        // 設置 RecyclerView 的 LayoutManager
        layoutManager = LinearLayoutManager(this)
        binding.rcyAttribution.layoutManager = layoutManager
    }

    /**
     * 註冊監聽者(滾動加載監聽、語言選擇)
     */
    private fun registerListener() {
        binding.ivLanguage.setOnClickListener {
            showLanguageSelectDialog()
        }

        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
        { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                // 接近底部，執行你的操作
                if (!isLoadMoreLoading && canKeepLoad) {
                    showLoadingDialog()
                    isLoadMoreLoading = true
                    viewModel.getAttributionList(this)
                }
            }
        })
    }

    //-------------------------------------- Action------- --------------------------------------//

    /**
     * 跳轉最新消息
     */
    private fun jumpToAttributionNewsPage(newsUrl: String) {
        addFragment(WebViewBoardPage().apply {
            arguments = Bundle().apply {
                putString(WebViewBoardPage.WEB_BOARD_URL_KEY, newsUrl)
                putString(
                    WebViewBoardPage.WEB_BOARD_TITLE_KEY,
                    binding.tvLatestNewsTag.text.toString()
                )
            }
        })
    }

    /**
     * 跳轉景點細節
     */
    private fun jumpToAttributionDetailPage(detail: AttractionDetail) {
        addFragment(AttributionDetailPage().apply {
            arguments = Bundle().apply {
                putString(AttributionDetailPage.ATTRIBUTION_DETAIL_KEY, Gson().toJson(detail))
            }
        })
    }

    /**
     * 顯示語言選擇 dialog
     */
    private fun showLanguageSelectDialog() {
        val languageDialog = LanguageSelectionDialog {
            setLanguageLocale()
            showLoadingDialog()
            viewModel.attractionInfoPage = 1
            attractionDetailAdapter.clearDetailData()
            //attractionNewsAdapter.clearAttractionNesData()

            viewModel.getAttributionNews(this)
            viewModel.getAttributionList(this)
        }

        languageDialog.show(fragmentManager, "show")
    }

    /**
     * 設置 Application 語言
     */
    private fun setLanguageLocale() {
        val localSp = ApplicationSp(this)

        if (localSp.getString(ApplicationSp.CURRENT_LANGUAGE_SIGN).isEmpty()) {
            localSp.putString(ApplicationSp.CURRENT_LANGUAGE_SIGN, "zh-tw")
        }

        val language = localSp.getString(ApplicationSp.CURRENT_LANGUAGE_SIGN)

        val locale: Locale = if (language.contains("-")) {
            val (lang, country) = language.split("-")
            Locale(lang, country)
        } else {
            Locale(language, "")
        }

        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        resetAllComponentLanguage()
    }

    /**
     * 重製各組件對應的語言
     */
    private fun resetAllComponentLanguage() {
        binding.tvTitle.text = getString(R.string.txt_travel_taipei)
        binding.tvCountTitle.text = getString(R.string.txt_taipei_attribution)
        binding.tvLatestNewsTag.text = getString(R.string.txt_latest_news)
        binding.tvAttributionTag.text = getString(R.string.txt_travel_attribution)
        binding.tvNoNewsHint.text = getString(R.string.txt_no_news_error_hint)
    }

    /**
     * 顯示 loading
     */
    private fun showLoadingDialog() {
        dialog.show(fragmentManager, "show")
    }

    /**
     * 關閉 loading
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun cancelLoadingDialog() {
        GlobalScope.launch {
            delay(1500)
            withContext(Dispatchers.Main) {
                // 在主線程執行你的操作
                dialog.cancel()
            }
        }
    }

    //---------------------------------- Fragment Handle ----------------------------------------//

    override fun addFragment(fragment: Fragment) {
        this.addFragment(fragment, R.id.fragmentContainer, FragmentAnim.SLIDE)
    }

    @SuppressLint("CommitTransaction")
    override fun addFragment(fragment: Fragment, container: Int, anim: FragmentAnim) {
        val fragmentTransaction = fragmentManager.beginTransaction()

        when (anim) {
            FragmentAnim.SLIDE ->
                fragmentTransaction.setCustomAnimations(
                    R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit
                )

            FragmentAnim.SLIDE_UP ->
                fragmentTransaction.setCustomAnimations(
                    R.animator.slide_fragment_in,
                    R.animator.slide_fragment_out
                )

            FragmentAnim.FADE ->
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }

        this.fragmentManager.beginTransaction()
            .replace(container, fragment, "main")
            .addToBackStack("main_interface")
            .commitAllowingStateLoss()
    }

    override fun popBackStack() {
        this.fragmentManager.popBackStack();
    }

    override fun popAllBackStack() {
        val count = fragmentManager.backStackEntryCount
        (0..count).forEach {
            val backStackId = fragmentManager.getBackStackEntryAt(it).id
            fragmentManager.popBackStack(backStackId, 1)
        }
    }

}