package com.wiryadev.gamemade.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.domain.model.Game
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
        }

        Log.d(ARGS, "onViewCreated: $id")
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
                                this?.floatingActionButton?.visible()
                                observeFavorite(game.id)
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
        Log.d("Fav", "setButtonState: called observer")
        viewModel.checkFavorite(id).observe(viewLifecycleOwner, {
            Log.d("Fav", "setButtonState: $it")
            if (it > 0) {
                Log.d("Fav", "setButtonState: $it")
                isFavorite = true
                setButtonState(isFavorite)
            } else {
                Log.d("Fav", "setButtonState: $it")
                isFavorite = false
                setButtonState(isFavorite)
            }
        })
    }

    private fun setButtonState(state: Boolean) {
        Log.d("Fav", "setButtonState: called state")
        with (binding?.floatingActionButton) {
            if (state) {
                this?.apply {
                    setImageResource(R.drawable.ic_round_star_24)
                    setOnClickListener { viewModel.deleteGameFromLibrary(game) }
                }
                Log.d("Fav", "setButtonState: called $state")
            } else {
                this?.apply {
                    setImageResource(R.drawable.ic_round_star_border_24)
                    setOnClickListener { viewModel.insertGameToLibrary(game) }
                    Log.d("Fav", "setButtonState: called $state")
                }
            }
        }
    }

//    private fun setButtonState(state: Boolean) {
//        Log.d("Fav", "setButtonState: called state")
//        if (state) {
//            binding?.floatingActionButton?.apply {
//                setImageResource(R.drawable.ic_round_star_24)
//                setOnClickListener { viewModel.insertGameToLibrary(game) }
//                Log.d("Fav", "setButtonState: called $state")
//            }
//        } else {
//            binding?.floatingActionButton?.apply {
//                setImageResource(R.drawable.ic_round_star_border_24)
//                setOnClickListener { viewModel.deleteGameFromLibrary(game) }
//                Log.d("Fav", "setButtonState: called $state")
//            }
//        }
//
//    }

}