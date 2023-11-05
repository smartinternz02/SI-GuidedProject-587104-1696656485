package com.aarush.core.abstraction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<Holder: RecyclerView.ViewHolder> : RecyclerView.Adapter<Holder>() {

    protected abstract val holderInflater: (LayoutInflater, ViewGroup, Boolean, Int) -> Holder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return holderInflater.invoke(LayoutInflater.from(parent.context), parent, false, viewType)
    }

}