package com.project.core.abstraction

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<in Data, out Type>(itemView: ViewBinding) : RecyclerView.ViewHolder(itemView.root) {

    @Suppress("UNCHECKED_CAST")
    private val _binding: Type = itemView as Type

    protected val binding: Type
        get() {
            try {
                return _binding
            } catch (throwable: Throwable) {
                throw TypeCastException(throwable.message.toString())
            }
        }

    open fun bind(data: Data){}
    open fun bindWithPosition(data:Data,position:Int){}

    object None
}