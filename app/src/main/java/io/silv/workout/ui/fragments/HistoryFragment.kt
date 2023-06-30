package io.silv.workout.ui.fragments

import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.databinding.FragmentHistoryBinding
import io.silv.workout.ui.util.BaseFragment

@AndroidEntryPoint
class HistoryFragment: BaseFragment<FragmentHistoryBinding>() {
    override fun getViewBinding(): FragmentHistoryBinding =
        FragmentHistoryBinding.inflate(layoutInflater)


    
}