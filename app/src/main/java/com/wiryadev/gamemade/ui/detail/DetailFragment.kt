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
import com.wiryadev.gamemade.core.data.Resource
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = arguments?.getInt(ARGS)

        if (id != null) {
            viewModel.getDetail(id)
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

        observeData()

        Log.d(ARGS, "onViewCreated: $id")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        lifecycleScope.launch {
            with(binding) {
                viewModel.detail.observe(viewLifecycleOwner, {
                    when (it) {
                        is Resource.Loading -> this?.progressBar?.visible()
                        is Resource.Success -> {
                            this?.progressBar?.gone()
                            Log.d(ARGS, "observeData: Success with data $it")
                            Log.d(ARGS, "observeData: Success with data ${it.data}")
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

}