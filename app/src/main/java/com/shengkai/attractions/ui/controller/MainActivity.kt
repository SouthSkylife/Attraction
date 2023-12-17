package com.shengkai.attractions.ui.controller

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shengkai.attractions.R
import com.shengkai.attractions.base.iFragmentTransactionCallback
import com.shengkai.attractions.common.FragmentAnim
import com.shengkai.attractions.data.AttractionDetail
import com.shengkai.attractions.data.AttractionInfoModel
import com.shengkai.attractions.databinding.ActivityMainBinding
import com.shengkai.attractions.repo.AttributionInfoRepo
import com.shengkai.attractions.ui.detail.AttributionDetailAdapter
import com.shengkai.attractions.ui.news.AttributionNewsAdapter
import com.shengkai.attractions.util.GeneralUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), iFragmentTransactionCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var attractionNewsAdapter: AttributionNewsAdapter
    private lateinit var attractionDetailAdapter: AttributionDetailAdapter
    private val containerId = 0

    private var fragmentManager: FragmentManager = supportFragmentManager

    companion object {
        const val TAG = "MainActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initNewsComponent()
        initDetailComponent()
        bindObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAttributionNews()
        viewModel.getAttributionList()
    }

    private fun init() {
        // 初始化 Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        //獲取 ViewModel
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun bindObserver() {
        //台北市最新消息
        viewModel.attributionNewsData.observe(this) {
            attractionNewsAdapter.setAttractionList(it.data)
        }

        //台北市景點
        viewModel.attractionInfoData.observe(
            this
        ) {
            binding.tvAttributionCount.text =
                "台北市景點 ${viewModel.attractionInfoPage}/${it.total}"
            attractionDetailAdapter.setAttractionDetailList(it.data)
        }
    }

    /**
     * 初始化最新消息組件
     */
    private fun initNewsComponent() {
        // 初始化 Adapter，並設置點擊監聽器
        attractionNewsAdapter = AttributionNewsAdapter { newsUrl: String ->
            println("最新消息網址${newsUrl}")
        }

        // 設置 RecyclerView 的 Adapter
        binding.rcyNews.adapter = attractionNewsAdapter

        // 設置 RecyclerView 的 LayoutManager（這裡使用 LinearLayoutManager）
        binding.rcyNews.layoutManager = LinearLayoutManager(this)
    }

    /**
     * 初始化最新消息組件
     */
    private fun initDetailComponent() {
        // 初始化 Adapter，並設置點擊監聽器
        attractionDetailAdapter = AttributionDetailAdapter { detail: AttractionDetail ->
            println("景觀網址${detail.url}")
        }

        // 設置 RecyclerView 的 Adapter
        binding.rcyAttribution.adapter = attractionDetailAdapter

        // 設置 RecyclerView 的 LayoutManager（這裡使用 LinearLayoutManager）
        binding.rcyAttribution.layoutManager = LinearLayoutManager(this)
    }

    //--------------------- Fragment Handle -----------------------------------//

    override fun addFragment(fragment: Fragment) {
        this.addFragment(fragment, containerId, FragmentAnim.SLIDE)
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