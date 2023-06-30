package io.silv.workout.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.R
import io.silv.workout.databinding.FragmentHistoryBinding
import io.silv.workout.ui.adapters.HistoryAdapter
import io.silv.workout.ui.util.BaseFragment
import io.silv.workout.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment: BaseFragment<FragmentHistoryBinding>() {
    override fun getViewBinding(): FragmentHistoryBinding =
        FragmentHistoryBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm by viewModels<HistoryViewModel>()

        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding.toolbarHistory)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }

        binding.toolbarHistory.setNavigationOnClickListener {
            findNavController().popBackStack()
        }



        val historyAdapter = HistoryAdapter { id ->
            showAlertDialog(
                onConfirm = {
                    vm.deleteExerciseById(id)
                    Toast.makeText(
                        context,
                        "item has been deleted",
                        Toast.LENGTH_LONG
                    ).show()
                },
                onCancel = {}
            )
        }

        binding.rvItemsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemsList.adapter = historyAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            vm.exerciseHistory.collect { exerciseHistory ->
                if (exerciseHistory.isEmpty()) {
                    binding.rvItemsList.visibility = View.GONE
                    binding.tvNoneComplete.visibility = View.VISIBLE
                } else {
                    historyAdapter.differ.submitList(exerciseHistory)
                    binding.rvItemsList.visibility = View.VISIBLE
                    binding.tvNoneComplete.visibility = View.GONE
                }
            }
        }
    }

    private fun showAlertDialog(onConfirm:() -> Unit, onCancel: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete History")
            .setPositiveButton("Confirm") { dialogInter, _ ->
                onConfirm()
                dialogInter.dismiss()
            }
            .setNegativeButton("Cancel"){ dialog,_ ->
                onCancel()
                dialog.dismiss()
            }
            .setMessage("previous history can not be recovered after " +
                    "being deleted are you sure you wish to continue")
            .setIcon(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
            )
            .create()
            .show()
    }
}