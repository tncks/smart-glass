package com.smart.app.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

@Suppress("unused")
fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
    val ft: FragmentTransaction = fragmentManager.beginTransaction()
    ft.detach(fragment).commitNowAllowingStateLoss()
    ft.attach(fragment).commitAllowingStateLoss()
}