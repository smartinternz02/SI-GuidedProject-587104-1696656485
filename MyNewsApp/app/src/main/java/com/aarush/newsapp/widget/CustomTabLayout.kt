package com.aarush.newsapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CustomTabLayout: TabLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    fun addTitleOnlyTabs(titles: List<String>) {
        titles.forEach { title ->
            addTab(newTab().setText(title.replaceFirstChar (Char::uppercase)))
        }
    }

    fun setTabsMargin(left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until tabCount) {
            val tab = (getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as MarginLayoutParams
            params.setMargins(left, top, right, bottom)
            tab.requestLayout()
        }
    }

    fun setIndicatorForViewPager(pager: ViewPager2) {
        TabLayoutMediator(this, pager) { tab, _ ->
            with(tab.view) {
                isFocusable = false
                isClickable = false
                isEnabled = false
            }
            tab.select()
        }.attach()
    }
}