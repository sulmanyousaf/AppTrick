package com.apptrick.app.ui.fragment.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apptrick.app.R
import com.apptrick.app.databinding.FragmentHomeBinding
import com.apptrick.app.ui.fragment.base.viewmodel.BaseViewModel
import com.apptrick.app.ui.fragment.base.viewmodel.observeLiveData
import com.apptrick.app.ui.fragment.base.viewmodel.progressDialog
import com.apptrick.app.ui.fragment.base.viewmodel.visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeFragmentVM>()
    private lateinit var mAdapter1: ListItem1Adapter
    private lateinit var mAdapter2: ListItem2Adapter

    private lateinit var progress: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        progress = progressDialog()

        observeLiveData(viewModel.progress) {
            progress.visibility(it)
        }

        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsList.collect { productsList ->
                    if (productsList.isNotEmpty()) {
                        if (productsList.size > 10) {
                            mAdapter1 = ListItem1Adapter(productsList.take(10))
                            binding.rv1.adapter = mAdapter1

                            mAdapter2 = ListItem2Adapter(productsList.drop(10))
                            binding.rv2.adapter = mAdapter2

                            binding.tvHeading2.visibility = View.VISIBLE
                            binding.rv2.visibility = View.VISIBLE
                        } else {
                            mAdapter1 = ListItem1Adapter(productsList)
                            binding.rv1.adapter = mAdapter1

                            binding.tvHeading2.visibility = View.GONE
                            binding.rv2.visibility = View.GONE
                        }
                    }
                }
            }

            launch {
                viewModel.baseEvents.collect { event ->
                    when (event) {
                        is BaseViewModel.BaseEvent.ShowErrorMessage -> showToast(event.msg)
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}