package com.ahmety.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmety.pokedex.databinding.ItemLoadingStateBinding

class MainLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MainLoadStateAdapter.MainLoadStateViewHolder>() {
    inner class MainLoadStateViewHolder(
        private val binding: ItemLoadingStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    txtError.text = loadState.error.localizedMessage
                }
                progressbar.isVisible = (loadState is LoadState.Loading)
                btnRetry.isVisible = (loadState is LoadState.Error)
                txtError.isVisible = (loadState is LoadState.Error)
                imgError.isVisible = (loadState is LoadState.Error)

                btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MainLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = MainLoadStateViewHolder(
        ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
}
