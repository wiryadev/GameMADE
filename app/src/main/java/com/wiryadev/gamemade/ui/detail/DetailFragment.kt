package com.wiryadev.gamemade.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.transition.MaterialContainerTransform
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.utils.Constant.Companion.CORNER_RADIUS
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.core.utils.Constant.Companion.TBA
import com.wiryadev.gamemade.core.utils.Constant.Companion.NONE
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.themeColor
import com.wiryadev.gamemade.core.utils.toDp
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    private var isFavorite = false
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = DELAY_TRANSITION
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

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

        val stringId = args.stringId?.toInt()

        if (stringId != null) {
            viewModel.getDetail(stringId)
            observeData()
            observeFavorite(stringId)
        } else {
            binding?.viewError?.root?.visible()
            binding?.viewError?.tvError?.text = getString(R.string.invalid_id)
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
                            this?.viewError?.root?.gone()

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
                            this?.viewError?.root?.visible()
                            this?.viewError?.tvError?.text = result.message
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
        with(binding?.floatingActionButton) {
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
                transformations(
                    RoundedCornersTransformation(
                        bottomLeft = CORNER_RADIUS,
                        bottomRight = CORNER_RADIUS
                    )
                )
            }

            val gameSeries = "Game Series: ${game.gameSeriesCount}"
            this?.tvDetailSeries?.text = gameSeries

            this?.tvDetailTitle?.text = game.title
            this?.tvDetailDesc?.text = game.description

            this?.tvDetailMetacritic?.visible()
            this?.tvDetailMetacritic?.text = "${game.metacritic ?: NONE}"
            
            val releaseDate = "Released: ${game.released ?: TBA}"
            this?.tvDetailRelease?.text = releaseDate

            if (game.metacriticUrl.isNullOrEmpty()) {
                this?.btnReview?.gone()
                setConstraint()
                setMargin()
            } else {
                this?.btnReview?.visible()
                val bundle = Bundle().apply {
                    putString(ReviewReaderFragment.REVIEW_URL, game.metacriticUrl)
                }
                this?.btnReview?.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_detail_fragment_to_review_reader_fragment,
                        bundle
                    )
                }
            }
        }
    }

    // Make bottom constraint for tv_detail_desc
    private fun setConstraint() {
        val constraintLayout: ConstraintLayout? = binding?.contentDetail?.contentLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.tv_detail_desc,
            ConstraintSet.BOTTOM,
            R.id.content_layout,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(constraintLayout)
    }

    // set the marginBottom value
    private fun setMargin() {
        with(binding?.contentDetail) {
            val layoutParams = this?.tvDetailDesc?.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = 24.toDp(requireContext())
            this.tvDetailDesc.layoutParams = layoutParams
        }
    }

}