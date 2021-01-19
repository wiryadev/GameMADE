package com.wiryadev.gamemade.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.wiryadev.gamemade.core.ui.GameAdapter
import com.wiryadev.gamemade.core.utils.Constant
import com.wiryadev.gamemade.core.utils.Constant.Companion.DELAY_TRANSITION
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.di.FavoriteModuleDependencies
import com.wiryadev.gamemade.favorite.databinding.FragmentLibraryBinding
import com.wiryadev.gamemade.ui.detail.DetailFragment
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
            .context(requireContext())
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    requireContext(),
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

        enterTransition = MaterialFadeThrough().apply {
            duration = DELAY_TRANSITION
        }

        gameAdapter = GameAdapter()

        gameAdapter.setOnItemClickListener {
            exitTransition = MaterialElevationScale(false).apply {
                duration = DELAY_TRANSITION
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = DELAY_TRANSITION
            }
            val bundle = Bundle().apply {
                putInt(DetailFragment.ARGS, it)
            }
//            findNavController().navigate(R.id.action_navigation_home_to_detail_fragment, bundle)
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

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

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
                gameAdapter.setData(it)
                if (it.isNotEmpty()) this?.progressBar?.gone() else this?.progressBar?.visible()
            })
        }
    }

}