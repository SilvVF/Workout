package io.silv.workout.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.databinding.FragmentFinishBinding
import io.silv.workout.ui.util.BaseFragment
import io.silv.workout.ui.viewmodels.FinishViewModel

@AndroidEntryPoint
class FinishFragment: BaseFragment<FragmentFinishBinding>() {

    override fun getViewBinding(): FragmentFinishBinding =
        FragmentFinishBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm by viewModels<FinishViewModel>()

        vm.saveToExerciseSession()

        val navController = findNavController()

        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding.toolbarFinish)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        binding.toolbarFinish.setNavigationOnClickListener {
            // acts like the back button on the bottom
            navController.navigate(
                FinishFragmentDirections.actionFinishFragmentToHomeFragment()
            )
        }
        binding.btnFinish.setOnClickListener {
            navController.navigate(
                FinishFragmentDirections.actionFinishFragmentToHomeFragment()
            )
        }
    }
}