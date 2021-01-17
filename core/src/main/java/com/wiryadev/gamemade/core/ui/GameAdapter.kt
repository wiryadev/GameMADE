package com.wiryadev.gamemade.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.gamemade.core.databinding.ItemGameBinding
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.Constant.Companion.CORNER_RADIUS

class GameAdapter : RecyclerView.Adapter<GameAdapter.RecyclerViewHolder>() {

    private var listData = ArrayList<Game>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(newListData: List<Game>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemGameBinding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(itemGameBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    class RecyclerViewHolder(private val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Game) {
            with(binding) {
                ivPoster.load(data.bgImage) {
                    transformations(RoundedCornersTransformation(CORNER_RADIUS))
                }

                tvTitle.text = data.title
                tvMetacritic.text = data.metacritic.toString()

                val releaseDate = "Released: ${data.released ?: ("TBA")}"
                tvRelease.text = releaseDate
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Game)
    }
}