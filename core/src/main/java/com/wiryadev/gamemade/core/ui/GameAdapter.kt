package com.wiryadev.gamemade.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.gamemade.core.databinding.ItemGameBinding
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.Constant.Companion.CORNER_RADIUS
import com.wiryadev.gamemade.core.utils.Constant.Companion.NONE
import com.wiryadev.gamemade.core.utils.Constant.Companion.TBA

class GameAdapter : RecyclerView.Adapter<GameAdapter.RecyclerViewHolder>() {

    private var listData = ArrayList<Game>()

    fun setData(newListData: List<Game>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemGameBinding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(itemGameBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class RecyclerViewHolder(private val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Game) {
            with(binding) {
                ivPoster.load(data.bgImage) {
                    transformations(RoundedCornersTransformation(CORNER_RADIUS))
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
}