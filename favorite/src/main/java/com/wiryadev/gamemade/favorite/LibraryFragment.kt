package com.wiryadev.gamemade.favorite

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.ui.GameLoadStateAdapter
import com.wiryadev.gamemade.core.utils.Constant.Companion.DEEPLINK_DETAIL
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.di.FavoriteModuleDependencies
import com.wiryadev.gamemade.favorite.databinding.FragmentLibraryBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel: LibraryViewModel by viewModels { factory }

    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerFavoriteComponent.builder()
            .context(requireContext())
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    requireActivity().applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

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
                .fromUri(Uri.parse(DEEPLINK_DETAIL + it))
                .build()

            findNavController().navigate(request)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvLibrary?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gameAdapter.withLoadStateFooter(
                GameLoadStateAdapter { gameAdapter.retry() }
            )
            setHasFixedSize(true)
        }

        binding?.observeData(viewModel.pagingDataFlow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentLibraryBinding.observeData(pagingData: Flow<PagingData<Game>>) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    pagingData.collectLatest(
                        gameAdapter::submitData
                    )
                }

                launch {
                    gameAdapter.loadStateFlow.collect { loadState ->
                        val isListEmpty =
                            loadState.refresh is LoadState.NotLoading && gameAdapter.itemCount == 0
                        // show empty list
                        viewEmpty.root.isVisible = isListEmpty
                        // Only show the list if refresh succeeds.
                        rvLibrary.isVisible = !isListEmpty
                        // Show loading spinner during initial load or refresh.
                        progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                        // Show the retry state if initial load or refresh fails.
//                        viewEmpty.btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                        // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                        val errorState = loadState.source.append as? LoadState.Error
                            ?: loadState.source.prepend as? LoadState.Error
                            ?: loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error
                            ?: loadState.source.refresh as? LoadState.Error

                        errorState?.let {
                            viewEmpty.tvError.text = it.error.localizedMessage
                        }
                    }
                }
            }
        }
    }

}