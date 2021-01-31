package com.wiryadev.gamemade.favorite

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.utils.Constant.Companion.DEEPLINK_DETAIL
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.di.FavoriteModuleDependencies
import com.wiryadev.gamemade.favorite.databinding.FragmentLibraryBinding
import dagger.hilt.android.EntryPointAccessors
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
            .context(requireActivity().applicationContext)
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
            adapter = gameAdapter
            setHasFixedSize(true)
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
                this?.progressBar?.gone()
                gameAdapter.setData(it)
                if (it.isNotEmpty()) {
                    this?.viewEmpty?.root?.gone()
                } else {
                    this?.viewEmpty?.root?.visible()
                    this?.viewEmpty?.tvError?.text = getString(R.string.empty_message)
                }
            })
        }
    }

}