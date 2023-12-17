package com.shengkai.attractions.base

import androidx.fragment.app.Fragment
import com.shengkai.attractions.common.constant.FragmentAnim

interface iFragmentTransactionCallback {

    fun addFragment(fragment: Fragment)

    fun addFragment(fragment: Fragment, container: Int, anim: FragmentAnim)

    fun popBackStack()

    fun popAllBackStack()

}