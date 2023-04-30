package com.sudo248.jogingu.ui.activities.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sudo248.jogingu.R
import com.sudo248.jogingu.databinding.FragmentHomeBinding
import com.sudo248.jogingu.util.gone
import com.sudo248.jogingu.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val adapter: HomeListRunAdapter by lazy {
        HomeListRunAdapter()
    }
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.home)
        viewModel.state.observe(viewLifecycleOwner) {
            it.onState(
                loading = {
                    binding.shimmer.apply {
                        visible()
                        startShimmer()
                    }
                    binding.homeNoRun.gone()
                    binding.homeRcv.gone()
                },
                error = {
                    binding.shimmer.apply {
                        hideShimmer()
                        gone()
                    }
                },
                success = {
                    binding.shimmer.apply {
                        hideShimmer()
                        gone()
                    }
                }
            )
        }
        viewModel.listRun.observe(viewLifecycleOwner) { runs ->
            runs?.let {
                if (it.isNotEmpty()) {
                    adapter.submitList(it.reversed())
                    binding.homeRcv.visible()
                } else {
                    binding.homeNoRun.visible()
                }
            }
        }
        binding.homeRcv.adapter = adapter
    }
}