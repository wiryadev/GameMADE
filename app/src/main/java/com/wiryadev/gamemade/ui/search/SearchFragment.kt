package com.wiryadev.gamemade.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.FragmentSearchBinding
import com.wiryadev.gamemade.ui.detail.DetailFragment
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

        gameAdapter = GameAdapter()

        gameAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt(DetailFragment.ARGS, it)
            }
            findNavController().navigate(R.id.action_navigation_search_to_detail_fragment, bundle)
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
                    binding?.progressBar?.visible()
                    viewModel.setDebounceDuration(false)
                    lifecycleScope.launch {
                        search?.let { viewModel.queryChannel.send(it) }
                    }
                    return true
                }

                override fun onQueryTextChange(search: String?): Boolean {
                    binding?.progressBar?.visible()
                    viewModel.setDebounceDuration(true)
                    lifecycleScope.launch {
                        search?.let { viewModel.queryChannel.send(it) }
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
                    is Resource.Loading -> this?.progressBar?.visible()
                    is Resource.Success -> {
                        this?.progressBar?.gone()
                        gameAdapter.setData(it.data)
                    }
                    is Resource.Error -> {
                        this?.progressBar?.gone()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

}