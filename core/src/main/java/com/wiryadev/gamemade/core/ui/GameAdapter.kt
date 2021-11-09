package com.wiryadev.gamemade.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.gamemade.core.R
import com.wiryadev.gamemade.core.databinding.ItemGameBinding
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.Constant.Companion.CORNER_RADIUS
import com.wiryadev.gamemade.core.utils.Constant.Companion.NONE
import com.wiryadev.gamemade.core.utils.Constant.Companion.TBA

class GameAdapter : PagingDataAdapter<Game, GameAdapter.RecyclerViewHolder>(DIFF_CALLBACK) {

    private var listData = ArrayList<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemGameBinding =
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(itemGameBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = listData.size

    inner class RecyclerViewHolder(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
        fun bind(data: Game) {
            with(binding) {
                ivPoster.load(data.bgImage) {
                    transformations(RoundedCornersTransformation(CORNER_RADIUS))
                    placeholder(R.drawable.ic_paceholder)
                    error(R.drawable.ic_error)
                }

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