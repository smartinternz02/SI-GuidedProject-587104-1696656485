package com.aarush.core.abstraction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class PagingRecyclerViewAdapter<Holder: RecyclerView.ViewHolder, Data> : RecyclerView.Adapter<Holder>() {

    protected val listData = ArrayList<Data>()

    fun appendList(newData: List<Data>) {
        listData.addAll(newData)
        notifyDataSetChanged()

    }

    open fun clearList() {
        listData.clear()
        notifyDataSetChanged()
    }

    protected abstract val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> Holder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return holderInflater.invoke(LayoutInflater.from(parent.context), parent, false)
    }

    override fun getItemCount(): Int = listData.size

}