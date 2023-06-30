package io.silv.workout.ui.fragments

import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.silv.workout.R
import io.silv.workout.data.types.Exercise
import io.silv.workout.databinding.DialogCustomBackConfirmationBinding
import io.silv.workout.databinding.FragmentExerciseBinding
import io.silv.workout.ui.adapters.ExerciseStatusAdapter
import io.silv.workout.ui.util.BaseFragment
import io.silv.workout.ui.viewmodels.ExerciseEvent
import io.silv.workout.ui.viewmodels.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ExerciseFragment: BaseFragment<FragmentExerciseBinding>() {

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    override fun getViewBinding(): FragmentExerciseBinding =
        FragmentExerciseBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customDialogForBackButton(requireContext())
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm by viewModels<ExerciseViewModel>()

        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                speakOut("rest for ten seconds")
            }
        }

        (activity as AppCompatActivity?)?.apply {
            setSupportActionBar(binding.toolbarExercise)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarExercise.setNavigationOnClickListener {
            customDialogForBackButton(requireContext())
        }

        binding.rvExerciseStatus.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = ExerciseStatusAdapter()
        binding.rvExerciseStatus.adapter = adapter
        adapter.differ.submitList(vm.exercises.value)

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                vm.exercises.collect { exercises ->
                    adapter.differ.submitList(exercises)
                }
            }
            launch {
                collectEvents(vm)
            }
        }
        vm.startRestTimer()
    }

    private suspend fun collectEvents(vm: ExerciseViewModel) {
        vm.eventChannel.collect { event ->
            when (event) {
                is ExerciseEvent.ExerciseStart -> {
                    playBellSound()
                    binding.tvTimerExersice.text = ExerciseViewModel.initialExerciseTime.toString()
                    speakOut(event.exercise.exerciseName)
                    binding.layoutNextExercise.visibility = View.INVISIBLE
                    binding.ivExercise.visibility = View.VISIBLE
                    binding.flProgressBar.visibility = View.GONE
                    binding.flExerciseView.visibility = View.VISIBLE
                    setViews(binding.flExerciseView)
                    binding.tvTitle.text = event.exercise.exerciseName
                    binding.ivExercise.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            event.exercise.image
                        )
                    )
                    vm.exerciseTimer.collect { time ->
                        binding.progressBarExercise.progress = time
                        binding.tvTimerExersice.text = time.toString()
                    }
                    if (!event.isLast) {
                        vm.startRestTimer()
                    } else {
                        findNavController().navigate(
                            ExerciseFragmentDirections.actionExerciseFragmentToFinishFragment()
                        )
                    }
                }

                is ExerciseEvent.RestStart -> {
                    binding.tvTimer.text = ExerciseViewModel.initialRestTime.toString()
                    speakOut("rest for ten seconds")
                    binding.tvTitle.text = "GET READY FOR"
                    binding.layoutNextExercise.visibility = View.VISIBLE
                    binding.tvNextExercise.text = event.nextExercise.exerciseName
                    binding.ivExercise.visibility = View.INVISIBLE

                    binding.flProgressBar.visibility = View.VISIBLE
                    binding.flExerciseView.visibility = View.GONE
                    setViews(binding.flProgressBar)
                    vm.restTimer.collect { time ->
                        binding.progressBar.progress = time
                        binding.tvTimer.text = time.toString()
                    }
                    vm.startExerciseTimer()
                }
            }
        }
    }

    private fun playBellSound() = CoroutineScope(Dispatchers.Main).launch {
        runCatching {
            val sound = Uri.parse("android.resource://io.silv.workout/" + R.raw.boxing_start)

            player = MediaPlayer.create(requireContext(), sound).apply {
                isLooping = false
                start()
            }
        }
            .onFailure {
                Toast.makeText(
                    context,
                    it.localizedMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    private fun speakOut(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    private fun setViews(view: View){
        val constraintLayout: ConstraintLayout = binding.root
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            binding.tvTitle.id,
            ConstraintSet.BOTTOM,
            view.id,
            ConstraintSet.TOP,
            5
        )
        constraintSet.applyTo(constraintLayout)
    }


    private fun customDialogForBackButton(context: Context) {
        val customDialog = Dialog(context)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.apply {
            setCancelable(false)
            show()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        dialogBinding.btnYes.setOnClickListener {
            customDialog.dismiss()
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
        player?.stop()
    }
}