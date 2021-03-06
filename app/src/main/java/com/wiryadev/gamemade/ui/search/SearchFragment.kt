package com.wiryadev.gamemade.ui.search

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.utils.Constant
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    private val viewModel: SearchViewModel by viewModels()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding?.rvSearch) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = gameAdapter
            this?.setHasFixedSize(true)
        }

        binding?.svGame?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(search: String?): Boolean {
                    handleLoadingView()

                    viewModel.setDebounceDuration(false)
                    lifecycleScope.launch {
                        search?.let { viewModel.queryChannel.value = it }
                    }
                    return true
                }

                override fun onQueryTextChange(search: String?): Boolean {
                    handleLoadingView()

                    viewModel.setDebounceDuration(true)
                    lifecycleScope.launch {
                        search?.let { viewModel.queryChannel.value = it }
                    }
                    return true
                }

            })
        }

        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        with(binding) {
            viewModel.searchResult.observe(viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading -> handleLoadingView()
                    is Resource.Success -> {
                        this?.progressBar?.gone()
                        this?.rvSearch?.visible()
                        gameAdapter.setData(it.data)
                    }
                    is Resource.Error -> {
                        this?.progressBar?.gone()
                        this?.rvSearch?.gone()
                        this?.viewError?.root?.visible()
                        this?.viewError?.tvError?.text = it.message
                    }
                }
            })
        }
    }

    private fun handleLoadingView() {
        binding?.progressBar?.visible()
        binding?.viewError?.root?.gone()
        binding?.rvSearch?.gone()
    }

}