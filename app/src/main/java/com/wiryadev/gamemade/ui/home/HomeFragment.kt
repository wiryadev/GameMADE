package com.wiryadev.gamemade.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.data.Resource
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.FragmentHomeBinding
import com.wiryadev.gamemade.ui.detail.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameAdapter = GameAdapter()

        gameAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt(DetailFragment.ARGS, it)
            }
            findNavController().navigate(R.id.action_navigation_home_to_detailFragment, bundle)
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
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

}