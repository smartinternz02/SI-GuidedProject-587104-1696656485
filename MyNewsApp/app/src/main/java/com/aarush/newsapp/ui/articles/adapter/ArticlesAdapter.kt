package com.aarush.newsapp.ui.articles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aarush.core.abstraction.BaseViewHolder
import com.aarush.core.abstraction.PagingRecyclerViewAdapter
import com.aarush.core.model.model.Article
import com.aarush.core.util.extension.glide
import com.aarush.newsapp.databinding.ItemArticlesBinding

class ArticlesAdapter : PagingRecyclerViewAdapter<ArticlesAdapter.ViewHolder, Article>() {

    private var onClick: ((Article) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> ViewHolder
        get() = { inflater, viewGroup, boolean ->
            ViewHolder(ItemArticlesBinding.inflate(inflater, viewGroup, boolean))
        }

    inner class ViewHolder(itemView: ItemArticlesBinding): BaseViewHolder<Article, ItemArticlesBinding>(itemView) {
        override fun bind(data: Article) {
            with(binding) {
                tvTitle.text = data.title
                tvDesc.text = data.description
                imgArticle.glide(itemView, data.image)

                root.setOnClickListener {
                    onClick?.invoke(data)
                }
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    fun setOnClickData(onClick: (data: Article) -> Unit) {
        this.onClick = onClick
    }
}