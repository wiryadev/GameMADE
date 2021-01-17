package com.wiryadev.gamemade.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wiryadev.gamemade.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    companion object {
        const val ARGS = "args"
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

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

        Log.d(ARGS, "onViewCreated: $id")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}