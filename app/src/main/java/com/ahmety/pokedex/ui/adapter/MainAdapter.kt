package com.ahmety.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmety.pokedex.databinding.ItemMainBinding
import com.ahmety.pokedex.model.Pokemon

class MainAdapter(private val onClickItem: (Pokemon) -> Unit) : PagingDataAdapter<Pokemon, MainAdapter.MainViewHolder>(
    MainAdapterComparator
) {
    inner class MainViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: Pokemon, onClickItem: (Pokemon) -> Unit) {
            binding.apply {
                txtPokemonTitle.text = item.name
                root.setOnClickListener { onClickItem.invoke(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val mView = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(mView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClickItem) }
    }

    companion object {
        private val MainAdapterComparator = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }
    }
}