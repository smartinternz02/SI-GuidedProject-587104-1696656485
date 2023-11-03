package com.project.newsapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.core.abstraction.BaseActivityBinding
import com.project.core.pref.EncryptedPreferences
import com.project.core.util.exception.Failure
import com.project.core.util.extension.dp
import com.project.core.util.pagination.RecyclerViewPaginator
import com.project.newsapp.R
import com.project.newsapp.databinding.ActivityMainBinding
import com.project.newsapp.ui.articles.ArticlesActivity
import com.project.newsapp.ui.main.adapter.SourceAdapter
import com.project.newsapp.ui.search.SearchActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityBinding<ActivityMainBinding>(),
    Observer<MainViewModel.MainUiState> {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    private val sourceAdapter: SourceAdapter by lazy { SourceAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    lateinit var currCategory: String

    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null

    private lateinit var categories : List<String>

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = { ActivityMainBinding.inflate(layoutInflater) }

    override fun setupView() {
        initiateData()
        initViewModel()
        setAction()
        setAdapter()
        setPagination()
        setTabAction()
        setTabItems()
    }

    override fun onChanged(state: MainViewModel.MainUiState?) {
        when (state) {
            is MainViewModel.MainUiState.SourcesLoaded -> {
                stopLoading()
                if (state.data.isEmpty() && paginator?.isFirstGet == true) {
                    showEmptyData()
                }else {
                    hideEmptyData()
                    sourceAdapter.appendList(state.data)
                }
            }
            is MainViewModel.MainUiState.InitialLoading -> {
                initialLoading()
            }
            is MainViewModel.MainUiState.PagingLoading -> {
                pagingLoading()
            }
            is MainViewModel.MainUiState.FailedLoaded -> {
                stopLoading()
                handleFailure(state.failure)
            }

            else -> {}
        }
    }

    private fun initiateData() {
        currCategory = getString(R.string.general_category)

        categories= listOf(
            getString(R.string.general_category),
            getString(R.string.business_category),
            getString(R.string.entertainment_category),
            getString(R.string.health_category),
            getString(R.string.science_category),
            getString(R.string.sports_category),
            getString(R.string.technology_category)
        )
    }

    private fun initViewModel() {
        mainViewModel.uiState.observe(this, this)
        mainViewModel.getSourceByCategory(currCategory, 1)
    }

    private fun setAction() {
        with(binding) {
            svNews.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard(svNews)
                    if (query.toString().isNotEmpty()) {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        intent.putExtra("searchQuery", query!!)
                        startActivity(intent)
                        binding.svNews.setQuery("")
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

            svNews.setOnAdditionalButtonListener {
                showFilterDialog()
            }
        }
    }

    private fun setAdapter() {
        with(binding.rvNews) {
            adapter = sourceAdapter
            setHasFixedSize(true)

            sourceAdapter.setOnClickData {
                val intent = Intent(this@MainActivity, ArticlesActivity::class.java)
                intent.putExtra("sourceId", it.id)
                startActivity(intent)
            }
        }
    }

    private fun setPagination() {
        paginator = RecyclerViewPaginator(binding.rvNews.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            paginator?.isFirstGet = false
            mainViewModel.getSourceByCategory(currCategory, page)
        }
        paginator?.let { binding.rvNews.addOnScrollListener(it) }
    }

    private fun setTabAction() {
        onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabPosition = tab?.position?.let { categories[it] }
                currCategory = tabPosition ?: getString(R.string.general_category)

                paginator?.isFirstGet = true
                sourceAdapter.clearList()
                mainViewModel.getSourceByCategory(currCategory, 1)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }

        onTabSelectedListener?.let {
            binding.tabLayoutCategory.addOnTabSelectedListener(it)
        }
    }

    private fun setTabItems() {
        with(binding.tabLayoutCategory) {
            val tabList = mutableListOf<String>()
            categories.forEach {
                tabList.add(it)
            }
            // Add tabs
            addTitleOnlyTabs(tabList)

            // Set margins
            setTabsMargin(6.dp, 6.dp, 8.dp, 6.dp)
        }
    }

    private fun initialLoading() {
        binding.progressCircular.visibility = View.VISIBLE
        binding.rvNews.visibility = View.GONE
    }

    private fun pagingLoading() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.rvNews.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.GONE
    }

    private fun showEmptyData() {
        binding.emptyDataView.parent.visibility = View.VISIBLE
    }

    private fun hideEmptyData() {
        binding.emptyDataView.parent.visibility = View.GONE
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(this, failure.throwable.message, Toast.LENGTH_SHORT).show()
    }

    private fun showFilterDialog() {
        val filterOptions = arrayOf(getString(R.string.search_for_source), getString(R.string.search_for_article))
        var filter = encryptedPreferences.filter
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.search_for))
            .setSingleChoiceItems(filterOptions, if (filter == SEARCH_SOURCE) 0 else 1) { _, which ->
                when (which) {
                    0 -> {
                        filter = SEARCH_SOURCE
                    }
                    1 -> {
                        filter = SEARCH_ARTICLE
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.apply)) { dialog, _ ->
                // Apply button clicked
                encryptedPreferences.filter = filter
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        const val SEARCH_SOURCE = 0
        const val SEARCH_ARTICLE = 1
    }


}