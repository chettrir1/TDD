package com.example.tdd.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.tdd.R
import com.example.tdd.databinding.FragmentAddShoppingItemBinding
import com.example.tdd.other.Status
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddShoppingItemFragment @Inject constructor(private val glide: RequestManager) : Fragment() {

    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ShoppingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        subscribeToObservers()
        binding.btnAddShoppingItem.setOnClickListener {
            val name = binding.etShoppingItemName.text.toString()
            val amount = binding.etShoppingItemAmount.text.toString()
            val price = binding.etShoppingItemPrice.text.toString()
            viewModel.insertShoppingItem(name, amount, price)
//            findNavController().navigate(
//                R.id.action_addShoppingItemFragment_to_imagePickFragment
//            )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurrentUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObservers() {
        viewModel.currentImageUrl.observe(viewLifecycleOwner) {
            glide.load(it).into(binding.ivShoppingImage)
        }
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        Snackbar.make(binding.root, "Added Shopping Item!", Snackbar.LENGTH_LONG)
                            .show()
                        findNavController().popBackStack()
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "Unknown error!",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }
}