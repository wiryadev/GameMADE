package com.wiryadev.gamemade.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.ViewSizeResolver
import com.wiryadev.gamemade.core.databinding.ItemGameBinding
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.Constant.Companion.NONE
import com.wiryadev.gamemade.core.utils.Constant.Companion.TBA

class GameAdapter : PagingDataAdapter<Game, GameAdapter.RecyclerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemGameBinding =
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(itemGameBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class RecyclerViewHolder(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Game) {
            with(binding) {
                val request = ImageRequest.Builder(this.root.context)
                    .size(ViewSizeResolver(ivPoster))
                    .build()
                val loader = this.root.context.imageLoader
                loader.enqueue(request)
                ivPoster.load(
                    uri = data.bgImage,
                    imageLoader = loader,
                )

                tvTitle.text = data.title

                val rating = data.metacritic ?: NONE
                tvMetacritic.text = rating.toString()

                val releaseDate = "Released: ${data.released ?: TBA}"
                tvRelease.text = releaseDate

                root.setOnClickListener {
                    onItemClickListener?.let { it(data.id.toString()) }
                }
            }
        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }

        }
    }
}