package com.wiryadev.gamemade.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.utils.Constant
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            duration = DELAY_TRANSITION
        }

        exitTransition = MaterialElevationScale(false).apply {
            duration = DELAY_TRANSITION
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = DELAY_TRANSITION
        }

        gameAdapter = GameAdapter()

        gameAdapter.setOnItemClickListener {

            val request = NavDeepLinkRequest.Builder
                .fromUri(Uri.parse(Constant.DEEPLINK_DETAIL + it))
                .build()

            findNavController().navigate(request)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        with(binding?.rvGame) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = gameAdapter
            this?.setHasFixedSize(true)
        }

        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        with(binding) {
            viewModel.data.observe(viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading -> this?.progressBar?.visible()
                    is Resource.Success -> {
                        this?.progressBar?.gone()
                        gameAdapter.setData(it.data)
                    }
                    is Resource.Error -> {
                        this?.progressBar?.gone()
                        this?.viewError?.root?.visible()
                        this?.viewError?.tvError?.text = it.message ?: getString(R.string.default_error_message)
                    }
                }
            })
        }
    }

}