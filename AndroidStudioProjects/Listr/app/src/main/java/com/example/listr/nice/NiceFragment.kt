package com.example.listr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listr.databinding.FragmentNiceBinding
import kotlinx.coroutines.launch

class NiceFragment : Fragment() {
    private var _binding: FragmentNiceBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: ListrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.niceRecyclerView.layoutManager = LinearLayoutManager(context)

        // Observe nice list
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.niceList.collect { items ->
                    binding.niceRecyclerView.adapter = ListrAdapter(items)
                }
            }
        }

        // Add button click listener
        binding.addNiceBtn.setOnClickListener {
            val name = binding.nicePersonInput.text.toString()
            if (name.isNotBlank()) {
                viewModel.addToNiceList(name)
                binding.nicePersonInput.text.clear()
            }
        }

        // Random name button
        binding.randomNiceBtn.setOnClickListener {
            viewModel.addRandomToNiceList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}