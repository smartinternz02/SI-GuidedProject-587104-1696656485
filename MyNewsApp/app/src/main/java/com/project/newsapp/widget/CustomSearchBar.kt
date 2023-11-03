package com.project.newsapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.project.newsapp.R

class CustomSearchBar : LinearLayout {

    private lateinit var searchView: SearchView
    private lateinit var imageView: ImageView

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.searchview_custom, this)

        searchView = findViewById(R.id.searchBar)
        imageView = findViewById(R.id.btnSearchUtil)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomSearchBar)

        searchView.apply {
            queryHint = attributes.getString(R.styleable.CustomSearchBar_queryHint)
            queryHint = attributes.getString(R.styleable.CustomSearchBar_android_queryHint)
        }
        removeClearTextAnimation()

        val isUseAdditionalButton = attributes.getBoolean(R.styleable.CustomSearchBar_useAdditionalButton, false)
        val btnSrc = attributes.getDrawable(R.styleable.CustomSearchBar_additionalButtonSrc)
        if (isUseAdditionalButton) {
            imageView.visibility = View.VISIBLE
            setAdditionalButtonImage(btnSrc)
        } else {
            imageView.visibility = View.GONE
        }

        attributes.recycle()
    }

    private fun removeClearTextAnimation() {
        val closeBtn = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeBtn.background = null
    }

    private fun removeSearchIcon() {
        val icSearch = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        icSearch.layoutParams = LayoutParams(0, 0)
    }

    fun setOnSearchViewClickListener(listener: () -> Unit) {
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                listener()
                v.clearFocus()
            }
        }
        (this as LinearLayout).setOnClickListener { listener() }
    }

    fun setOnQueryChangeListener(listener: SearchView.OnQueryTextListener) {
        searchView.setOnQueryTextListener(listener)
    }

    fun setOnAdditionalButtonListener(onClick: () -> Unit) {
        imageView.setOnClickListener { onClick() }
    }

    fun setQuery(query: String){
        searchView.setQuery(query, false)
    }

    fun setQueryHint(hint: String) {
        searchView.queryHint = hint
    }

    fun getQuery(): String {
        return searchView.query.toString()
    }

    fun isUseSearchIcon(isUse: Boolean) {
        if (!isUse) {
            removeSearchIcon()
        }
    }

    fun isUsingAdditionalButton(isUsing: Boolean) {
        imageView.visibility = if (isUsing) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun setAdditionalButtonImage(source: Any?) {
        Glide.with(context)
            .load(source)
            .into(imageView)
    }
}