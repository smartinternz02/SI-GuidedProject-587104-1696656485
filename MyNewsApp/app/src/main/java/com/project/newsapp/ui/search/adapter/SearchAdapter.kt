package com.project.newsapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.core.abstraction.PagingRecyclerViewAdapter
import com.project.core.model.model.Article
import com.project.core.model.model.Source
import com.project.core.util.extension.glide
import com.project.newsapp.databinding.ItemArticlesBinding
import com.project.newsapp.databinding.ItemSourceBinding
import com.project.newsapp.ui.main.MainActivity.Companion.SEARCH_ARTICLE
import com.project.newsapp.ui.main.MainActivity.Companion.SEARCH_SOURCE

class SearchAdapter : PagingRecyclerViewAdapter<RecyclerView.ViewHolder, Any>() {

    private var onClick: ((Any, Int) -> Unit)? = null
    private var viewType = SEARCH_SOURCE

    fun setTypeAdapter(type: Int) {
        viewType = type
        notifyDataSetChanged()
    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> RecyclerView.ViewHolder
        get() = { inflater, viewGroup, boolean ->
            when (viewType) {
                SEARCH_SOURCE -> {
                    SourceViewHolder(ItemSourceBinding.inflate(inflater, viewGroup, boolean))
                }
                SEARCH_ARTICLE -> {
                    ArticleViewHolder(ItemArticlesBinding.inflate(inflater, viewGroup, boolean))
                }
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

    inner class SourceViewHolder(private val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Source) {
            with(binding) {
                tvSource.text = data.name
                tvDesc.text = data.description

                root.setOnClickListener {
                    onClick?.invoke(data, viewType)
                }
            }
        }
    }

    inner class ArticleViewHolder(private val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Article) {
            with(binding) {
                tvTitle.text = data.title
                tvDesc.text = data.description
                imgArticle.glide(itemView, data.image)

                root.setOnClickListener {
                    onClick?.invoke(data, viewType)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SourceViewHolder -> holder.bind(listData[position] as Source)
            is ArticleViewHolder -> holder.bind(listData[position] as Article)
        }
    }

    fun setOnClickData(onClick: (Any, Int) -> Unit) {
        this.onClick = onClick
    }

}