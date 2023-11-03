package com.project.newsapp.ui.articles

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.core.abstraction.BaseActivityBinding
import com.project.core.util.exception.Failure
import com.project.core.util.pagination.RecyclerViewPaginator
import com.project.newsapp.R
import com.project.newsapp.databinding.ActivityArticlesBinding
import com.project.newsapp.ui.articles.adapter.ArticlesAdapter
import com.project.newsapp.ui.detail.DetailNewsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticlesActivity : BaseActivityBinding<ActivityArticlesBinding>(), Observer<ArticlesViewModel.ArticlesUiState> {

    @Inject
    lateinit var articlesViewModel: ArticlesViewModel

    private val articlesAdapter: ArticlesAdapter by lazy { ArticlesAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    private lateinit var sourceId: String

    override val bindingInflater: (LayoutInflater) -> ActivityArticlesBinding
        get() = { ActivityArticlesBinding.inflate(layoutInflater) }

    override fun setupView() {
        getDataIntent()
        initViewModel()
        setHeader()
        setAdapter()
        setPagination()

    }

    override fun onChanged(state: ArticlesViewModel.ArticlesUiState?) {
        when(state) {
            is ArticlesViewModel.ArticlesUiState.ArticlesLoaded -> {
                stopLoading()
                if (state.data.isEmpty() && paginator?.isFirstGet == true) {
                    showEmptyData()
                }else {
                    hideEmptyData()
                    articlesAdapter.appendList(state.data)
                }
            }
            is ArticlesViewModel.ArticlesUiState.InitialLoading -> {
                initialLoading()
            }
            is ArticlesViewModel.ArticlesUiState.PagingLoading -> {
                pagingLoading()
            }
            is ArticlesViewModel.ArticlesUiState.FailedLoaded -> {
                stopLoading()
                handleFailure(state.failure)
            }
            else -> {}
        }
    }

    private fun getDataIntent() {
        sourceId = intent.getStringExtra("sourceId") ?: ""
    }

    private fun initViewModel() {
        articlesViewModel.uiState.observe(this, this)
        articlesViewModel.getArticles(sourceId, 1)
    }

    private fun setHeader() {
        with(binding) {
            headerDetail.tvHeader.text = getString(R.string.article)
            headerDetail.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun setAdapter() {
        with(binding.rvNews) {
            adapter = articlesAdapter
            setHasFixedSize(true)

            articlesAdapter.setOnClickData {
                val intent = Intent(this@ArticlesActivity, DetailNewsActivity::class.java)
                intent.putExtra("sourceNews", it.source)
                intent.putExtra("urlNews", it.url)
                startActivity(intent)
            }
        }
    }

    private fun setPagination() {
        paginator = RecyclerViewPaginator(binding.rvNews.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            paginator?.isFirstGet = false
            articlesViewModel.getArticles(sourceId, page)
        }
        paginator?.let { binding.rvNews.addOnScrollListener(it) }
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
}