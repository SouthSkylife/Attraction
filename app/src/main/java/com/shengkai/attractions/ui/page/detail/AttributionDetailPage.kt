package com.shengkai.attractions.ui.page.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shengkai.attractions.R
import com.shengkai.attractions.data.AttractionDetail
import com.shengkai.attractions.data.Image
import com.shengkai.attractions.databinding.FragmentAttributionDetailBinding
import com.shengkai.attractions.ui.controller.MainActivity
import com.shengkai.attractions.ui.page.news.AttributionNewsPage
import com.shengkai.attractions.ui.page.web.WebViewBoardPage

/**
 *
 */
class AttributionDetailPage : Fragment() {

    private lateinit var binding: FragmentAttributionDetailBinding
    private lateinit var viewModel: AttributionDetailViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var attributionJson: String
    private var isFirstLoad: Boolean = true

    companion object {
        const val TAG = "AttributionDetail"
        const val ATTRIBUTION_DETAIL_KEY = "ATTRIBUTION_DETAIL_KEY"
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_attribution_detail,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            ivBack.setOnClickListener {
                mainActivity.popBackStack()
            }
        }

        //獲取 ViewModel
        viewModel = ViewModelProvider(this)[AttributionDetailViewModel::class.java]

        attributionJson = arguments?.getString(ATTRIBUTION_DETAIL_KEY) ?: ""
    }

    override fun onStart() {
        super.onStart()
        loadAttributionDetail()
    }

    private fun loadAttributionDetail() {
        if (attributionJson.isNotEmpty()) {
            val type = object : TypeToken<AttractionDetail>() {}.type
            val detail: AttractionDetail =
                Gson().fromJson(attributionJson, type)

            binding.apply {
                tvTitle.text = detail.name
                tvDetailOpen.text = detail.openTime
                tvDetailAddress.text = detail.address
                tvDetailPhone.text = detail.tel
                tvDetailUrl.text = detail.url
                tvDetailContent.text = detail.introduction
                tvDetailUrl.setOnClickListener {
                    jumpToReferWebPage(detail.url, detail.name)
                }
            }

            insertAttributionImgList(detail.images)
        }
    }

    private fun jumpToReferWebPage(referUrl: String, attributionTitle: String) {
        mainActivity.addFragment(WebViewBoardPage().apply {
            arguments = Bundle().apply {
                putString(WebViewBoardPage.WEB_BOARD_URL_KEY, referUrl)
                putString(WebViewBoardPage.WEB_BOARD_TITLE_KEY, attributionTitle)
            }
        })
    }

    private fun insertAttributionImgList(photos : List<Image>){
        val imageList = ArrayList<SlideModel>()

        photos.forEach{
            imageList.add(SlideModel(it.src, ScaleTypes.CENTER_CROP))
        }

        binding.imageSlider.setImageList(imageList)
    }
}