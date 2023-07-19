package com.authguardian.mobileapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.authguardian.mobileapp.R
import com.authguardian.mobileapp.databinding.ItemMessageBinding

class DatabaseAdapter : ListAdapter<DatabaseAdapter.Item, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean = oldItem == newItem
    }
) {

    sealed class Item {
        data class DatabaseMessageItem(
            val text: String
        ) : Item()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_message -> DatabaseViewHolder(
                ItemMessageBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is DatabaseViewHolder -> holder.onBind(getItem(position) as Item.DatabaseMessageItem)
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Item.DatabaseMessageItem -> R.layout.item_message
    }

    inner class DatabaseViewHolder(private val viewBinding: ItemMessageBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(item: Item.DatabaseMessageItem) {
            viewBinding.txtMessage.text = item.text
        }
    }
}