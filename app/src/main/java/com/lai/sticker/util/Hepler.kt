package com.lai.sticker.util

import androidx.activity.OnBackPressedCallback
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.lai.sticker.R


object FragmentHelp {

    fun restoreFragment(activity: AppCompatActivity, tag: String?): Fragment? {
        val fm: FragmentManager = activity.supportFragmentManager
        return fm.findFragmentByTag(tag)
    }

    fun restoreFragment(fm: FragmentManager, tag: String?): Fragment? {
        return fm.findFragmentByTag(tag)
    }

    fun showOrHideFragment(activity: AppCompatActivity, fragment: Fragment) {
        val fm: FragmentManager = activity.supportFragmentManager
        if (fragment.isAdded) {
            if (fragment.isHidden) {
                fm.beginTransaction()
                    .show(fragment).commitAllowingStateLoss()
            } else {
                fm.beginTransaction()
                    .hide(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun showOrHideFragment(fm: FragmentManager, fragment: Fragment) {
        if (fragment.isAdded) {
            if (fragment.isHidden) {
                fm.beginTransaction()
                    .show(fragment).commitAllowingStateLoss()
            } else {
                fm.beginTransaction()
                    .hide(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun hideFragment(
        fm: FragmentManager, fragment: Fragment, @AnimatorRes @AnimRes enter: Int = 0,
        @AnimatorRes @AnimRes exit: Int = 0
    ) {
        if (fragment.isAdded) {
            if (!fragment.isHidden) {
                fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .hide(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun showFragment(
        fm: FragmentManager, fragment: Fragment, @AnimatorRes @AnimRes enter: Int = 0,
        @AnimatorRes @AnimRes exit: Int = 0
    ) {
        if (fragment.isAdded) {
            if (fragment.isHidden) {
                fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .show(fragment).commitAllowingStateLoss()
            }
            try {
                fm.executePendingTransactions()
            } catch (var4: IllegalStateException) {
            }
        }
    }

    fun initFragment(
        activity: AppCompatActivity,
        fragment: Fragment,
        containerId: Int,
        tag: String?,
        isShow: Boolean
    ) {
        val fm: FragmentManager = activity.supportFragmentManager
        if (isShow) {
            fm.beginTransaction()
                .add(containerId, fragment, tag).commitAllowingStateLoss()
        } else {
            fm.beginTransaction()
                .add(containerId, fragment, tag)
                .hide(fragment).commitAllowingStateLoss()
        }
        try {
            fm.executePendingTransactions()
        } catch (var9: java.lang.IllegalStateException) {
        }
    }

    fun initFragment(
        fm: FragmentManager,
        fragment: Fragment,
        containerId: Int,
        tag: String?,
        isShow: Boolean
    ) {
        if (isShow) {
            fm.beginTransaction()
                .add(containerId, fragment, tag).commitAllowingStateLoss()
        } else {
            fm.beginTransaction()
                .add(containerId, fragment, tag)
                .hide(fragment).commitAllowingStateLoss()
        }
        try {
            fm.executePendingTransactions()
        } catch (var9: java.lang.IllegalStateException) {
        }
    }

    fun isFragmentShowing(fragment: Fragment?): Boolean {
        return if (fragment == null) {
            false
        } else {
            fragment.isAdded && !fragment.isHidden
        }
    }

    fun addOnBackPressed(
        fragment: Fragment,
        owner: LifecycleOwner,
        onBackPressed: () -> Boolean
    ): OnBackPressedCallback {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isFragmentShowing(fragment)) {
                    isEnabled = false
                    fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                } else if (!onBackPressed()) {
                    isEnabled = false
                    fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
        fragment.requireActivity().onBackPressedDispatcher.addCallback(owner, callback)
        return callback
    }


}