package io.silv.workout.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.databinding.FragmentHomeBinding
import io.silv.workout.ui.util.BaseFragment

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        binding.flStart.setOnClickListener {
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToExerciseFragment()
            )
        }

        binding.flBMI.setOnClickListener {
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToFragmentBmi()
            )
        }

        binding.flHistory.setOnClickListener {

        }
    }
}