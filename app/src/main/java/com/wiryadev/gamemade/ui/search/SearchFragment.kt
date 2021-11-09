package com.wiryadev.gamemade.ui.search

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.wiryadev.gamemade.core.domain.model.Game
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.ui.GameLoadStateAdapter
import com.wiryadev.gamemade.core.utils.Constant
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding?.rvSearch) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.adapter = gameAdapter.withLoadStateFooter(
                GameLoadStateAdapter { gameAdapter.retry() }
            )
        }

        binding?.tvSearchGame?.run {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    lifecycleScope.launch {
                        s?.let { viewModel.queryChannel.value = it.toString() }
                    }
                }

            })

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    binding?.updateSearchResult(viewModel.accept)
                    true
                } else {
                    false
                }
            }
        }

        binding?.observeData(
            uiState = viewModel.state,
            suggestionData = viewModel.searchResult,
            pagingData = viewModel.pagingDataFlow,
            onScrollChanged = viewModel.accept,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentSearchBinding.updateSearchResult(
        onQueryChanged: (UiAction.Search) -> Unit,
    ) {
        tvSearchGame.text.trim().let {
            if (it.isNotEmpty()) {
                rvSearch.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun FragmentSearchBinding.observeData(
        uiState: StateFlow<UiState>,
        suggestionData: Flow<List<Game>>,
        pagingData: Flow<PagingData<Game>>,
        onScrollChanged: (UiAction.Scroll) -> Unit,
    ) {
        viewError.btnRetry.setOnClickListener { gameAdapter.retry() }

        rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        val isNotLoading = gameAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for the paging source changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            flow = isNotLoading,
            flow2 = hasNotScrolledForCurrentSearch,
            transform = Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    suggestionData.collectLatest { item ->
                        if (item.isNotEmpty()) {
                            val searchSuggestion = item.map { it.title }
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.select_dialog_item,
                                searchSuggestion
                            )
                            adapter.notifyDataSetChanged()
                            binding?.tvSearchGame?.setAdapter(adapter)
                        }
                    }
                }
                launch {
                    pagingData.collectLatest(
                        gameAdapter::submitData
                    )
                }

                launch {
                    shouldScrollToTop.collect { shouldScroll ->
                        if (shouldScroll) rvSearch.scrollToPosition(0)
                    }
                }

                launch {
                    gameAdapter.loadStateFlow.collect { loadState ->
                        val isListEmpty = loadState.refresh is LoadState.NotLoading
                                && gameAdapter.itemCount == 0
                        // Only show the list if refresh succeeds.
                        rvSearch.isVisible = !isListEmpty
                        // Show loading spinner during initial load or refresh.
                        progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                        // Show the retry state if initial load or refresh fails.
                        viewError.root.isVisible = loadState.source.refresh is LoadState.Error

                        // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                        val errorState = loadState.source.append as? LoadState.Error
                            ?: loadState.source.prepend as? LoadState.Error
                            ?: loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error
                            ?: loadState.source.refresh as? LoadState.Error

                        errorState?.let {
                            viewError.tvError.text = it.error.localizedMessage
                        }
                    }
                }
            }
        }
    }

}