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
import com.example.listr.databinding.FragmentNaughtyBinding
import kotlinx.coroutines.launch

class NaughtyFragment : Fragment() {
    private var _binding: FragmentNaughtyBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: ListrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNaughtyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.naughtyRecyclerView.layoutManager = LinearLayoutManager(context)

        // Observe naughty list
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.naughtyList.collect { items ->
                    binding.naughtyRecyclerView.adapter = ListrAdapter(items)
                }
            }
        }

        // Add button click listener
        binding.addNaughtyBtn.setOnClickListener {
            val name = binding.naughtyPersonInput.text.toString()
            if (name.isNotBlank()) {
                viewModel.addToNaughtyList(name)
                binding.naughtyPersonInput.text.clear()
            }
        }

        // Random name button
        binding.randomNaughtyBtn.setOnClickListener {
            viewModel.addRandomToNaughtyList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}