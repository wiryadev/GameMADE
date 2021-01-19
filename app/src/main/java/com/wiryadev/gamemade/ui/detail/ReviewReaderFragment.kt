package com.wiryadev.gamemade.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wiryadev.gamemade.databinding.FragmentReviewReaderBinding

class ReviewReaderFragment : BottomSheetDialogFragment() {

    companion object {
        const val REVIEW_URL = "REVIEW_URL"
    }

    private var _binding: FragmentReviewReaderBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReviewReaderBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString(REVIEW_URL)

        if (url != null) binding?.reviewReader?.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}