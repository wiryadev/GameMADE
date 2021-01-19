package com.wiryadev.gamemade.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.Constant.Companion.CORNER_RADIUS
import com.wiryadev.gamemade.core.utils.Constant.Companion.TBA
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    companion object {
        const val ARGS = "args"
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    private lateinit var game: Game

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ARGS)

        if (id != null) {
            viewModel.getDetail(id)
            observeData()
            observeFavorite(id)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding?.contentDetail?.nestedScroll?.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    binding?.floatingActionButton?.hide()
                } else {
                    binding?.floatingActionButton?.show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        lifecycleScope.launch {
            with(binding) {
                viewModel.detail.observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is Resource.Loading -> this?.progressBar?.visible()
                        is Resource.Success -> {
                            this?.progressBar?.gone()
                            val detailGame = result.data
                            if (detailGame != null) {
                                game = detailGame
                                showDetail(game)
                                this?.contentDetail?.tvAbout?.visible()
                                this?.floatingActionButton?.visible()
                            }
                        }
                        is Resource.Error -> {
                            this?.progressBar?.gone()
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
            }
        }
    }

    private fun observeFavorite(id: Int) {
        viewModel.checkFavorite(id).observe(viewLifecycleOwner, {
            if (it > 0) {
                isFavorite = true
                setButtonState(isFavorite)
            } else {
                isFavorite = false
                setButtonState(isFavorite)
            }
        })
    }

    private fun setButtonState(state: Boolean) {
        with (binding?.floatingActionButton) {
            if (state) {
                this?.apply {
                    setImageResource(R.drawable.ic_round_star_24)
                    setOnClickListener { viewModel.deleteGameFromLibrary(game) }
                }
            } else {
                this?.apply {
                    setImageResource(R.drawable.ic_round_star_border_24)
                    setOnClickListener { viewModel.insertGameToLibrary(game) }
                }
            }
        }
    }

    private fun showDetail(game: Game) {
        with(binding?.contentDetail) {
            this?.ivDetailPoster?.load(game.bgImage) {
                transformations(RoundedCornersTransformation(bottomLeft = CORNER_RADIUS, bottomRight = CORNER_RADIUS))
            }

            val gameSeries = "Game Series: ${game.gameSeriesCount}"
            this?.tvDetailSeries?.text = gameSeries

            this?.tvDetailTitle?.text = game.title
            this?.tvDetailDesc?.text = game.description

            this?.tvDetailMetacritic?.visible()
            this?.tvDetailMetacritic?.text = game.metacritic.toString()

            val releaseDate = "Released: ${game.released ?: TBA}"
            this?.tvDetailRelease?.text = releaseDate
        }
    }

}