package io.silv.workout.ui.fragments

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.databinding.FragmentBmiBinding
import io.silv.workout.ui.util.BaseFragment

@AndroidEntryPoint
class FragmentBmi: BaseFragment<FragmentBmiBinding>() {

    override fun getViewBinding(): FragmentBmiBinding =
        FragmentBmiBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rbMetricUnits.setOnClickListener {
            binding.llUsHeight.visibility = View.GONE
            binding.tilWeightLb.visibility = View.GONE

            binding.tilWeightKilos.visibility = View.VISIBLE
            binding.tlHeightCm.visibility = View.VISIBLE
        }

        binding.rbUsUnits.setOnClickListener {
            binding.tilWeightKilos.visibility = View.INVISIBLE
            binding.tlHeightCm.visibility = View.INVISIBLE

            binding.llUsHeight.visibility = View.VISIBLE
            binding.tilWeightLb.visibility = View.VISIBLE
        }
    }
}