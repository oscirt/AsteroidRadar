package com.example.asteroidradar.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.asteroidradar.adapters.AsteroidsListAdapter
import com.example.asteroidradar.viewmodel.AsteroidsViewModel
import com.example.asteroidradar.adapters.AsteroidItemClickListener
import com.example.asteroidradar.R
import com.example.asteroidradar.adapters.DefaultClickListener
import com.example.asteroidradar.databinding.FragmentListBinding
import com.example.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AsteroidsListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private val viewModel: AsteroidsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.asteroidsList.adapter = AsteroidsListAdapter(AsteroidItemClickListener {
            viewModel.setCurrentAsteroid(it)
            findNavController()
                .navigate(R.id.action_asteroidsListFragment_to_asteroidDetailsFragment)
        })

        lifecycle.coroutineScope.launch {
            viewModel.allAsteroids().collect { list ->
                (binding.asteroidsList.adapter as AsteroidsListAdapter).submitList(list.map {
                    Asteroid(
                        it.id,
                        it.name,
                        it.magnitude,
                        it.isHazardous,
                        it.estimatedDiameter,
                        it.date,
                        it.close_date,
                        it.kps,
                        it.ams
                    )
                })
            }
        }

        return binding.root
    }
}