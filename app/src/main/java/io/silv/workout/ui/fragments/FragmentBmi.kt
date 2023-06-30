package io.silv.workout.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.databinding.FragmentBmiBinding
import io.silv.workout.ui.util.BaseFragment
import io.silv.workout.ui.viewmodels.BmiViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class FragmentBmi: BaseFragment<FragmentBmiBinding>() {

    override fun getViewBinding(): FragmentBmiBinding =
        FragmentBmiBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm by viewModels<BmiViewModel>()

        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding.toolbarBMI)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "BMI calculator"
        }

        binding.toolbarBMI.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

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

        binding.btnCalculateBMI.setOnClickListener {
            if (binding.rbMetricUnits.isChecked)
                vm.calculateBmi(
                    height = binding.etHeightCm.text.toString(),
                    weight = binding.etMetricWeight.text.toString()
                )
            else
                vm.calculateBmiUs(
                    heightIn = binding.etHeightin.text.toString(),
                    heightFt = binding.etHeightFt.text.toString(),
                    weight = binding.etUSWeight.text.toString()
                )
        }

        fun getBmiLabel(bmi: Double)  = if (bmi <= 15){
                 "very underweight"
            }else if (bmi > 15 && bmi <= 18.5){
                "underweight"
            }else if(bmi > 18.5 && bmi <= 25){
                "Normal"
            }else{
                "Overweight"
            }
        fun getBmiDescription(bmi: Double) =   if (bmi <= 15){
            "you need to eat more"
        }else if (bmi > 15 && bmi <= 18.5){
            "you need to eat more"
        }else if(bmi > 18.5 && bmi <= 25){
            "You are in good shape"
        }else{
            "you need to shed some pounds (If you are an athlete this calculation may be incorrect)"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                vm.errors.collect {
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            launch {
                vm.bmi.collect {
                    binding.llBMITexts.visibility = View.VISIBLE
                    binding.tvBMILabel.text = getBmiLabel(it)
                    binding.tvBMIText.text = getBmiDescription(it)
                    binding.tvBMINum.text = BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN).toString()
                }
            }
        }
    }
}