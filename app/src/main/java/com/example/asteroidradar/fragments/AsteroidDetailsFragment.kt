package com.example.asteroidradar.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.asteroidradar.viewmodel.AsteroidsViewModel
import com.example.asteroidradar.R
import com.example.asteroidradar.adapters.DefaultClickListener
import com.example.asteroidradar.databinding.FragmentDetailsBinding

class AsteroidDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: AsteroidsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_details, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.asteroid = viewModel.currentAsteroid.value

        binding.clickListener = DefaultClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.about_magnitude_help_text)
                .setPositiveButton("ok") { _, _ -> }
                .create()
                .show()
        }

        return binding.root
    }
}