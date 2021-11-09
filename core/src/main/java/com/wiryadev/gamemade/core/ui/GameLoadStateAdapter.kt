package com.wiryadev.gamemade.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wiryadev.gamemade.core.R
import com.wiryadev.gamemade.core.databinding.ItemLoadStateBinding

class GameLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<GameLoadStateAdapter.GameLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: GameLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): GameLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_load_state, parent, false)
        val binding = ItemLoadStateBinding.bind(view)
        return GameLoadStateViewHolder(binding, retry)
    }

    inner class GameLoadStateViewHolder(
        private val binding: ItemLoadStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            binding.run {
                placeholder.root.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                tvError.isVisible = loadState is LoadState.Error

                if (loadState is LoadState.Error) {
                    tvError.text = loadState.error.localizedMessage
                }
            }
        }
    }
}