package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restTimerDuration: Long = 5000
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseTimerDuration: Long = 30000
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()

    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(restTimerDuration, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = (restTimerDuration/1000).toInt() - restProgress
                binding?.textViewTimer?.text = ((restTimerDuration/1000).toInt() - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = (exerciseTimerDuration/1000).toInt() - exerciseProgress
                binding?.textViewTimerExercise?.text = ((exerciseTimerDuration/1000).toInt() - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    setupRestView()
                } else {
                    Toast.makeText(this@ExerciseActivity, "Congratulations !!!", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }

        }.start()
    }

    private fun setupRestView() {

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.textViewTitle?.visibility = View.VISIBLE
        binding?.textViewExercise?.visibility = View.INVISIBLE
        binding?.exerciseImageView?.visibility = View.INVISIBLE
        binding?.textViewExerciseUpcomingLabel?.visibility = View.VISIBLE
        binding?.textViewUpcomingExerciseName?.visibility = View.VISIBLE

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.textViewUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
        setRestProgressBar()
    }

    private fun setupExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.textViewTitle?.visibility = View.INVISIBLE
        binding?.textViewExercise?.visibility = View.VISIBLE
        binding?.exerciseImageView?.visibility = View.VISIBLE
        binding?.textViewExerciseUpcomingLabel?.visibility = View.INVISIBLE
        binding?.textViewUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.textViewExercise?.text = exerciseList?.get(currentExercisePosition)?.getName()
        binding?.exerciseImageView?.setImageResource(exerciseList!![currentExercisePosition].getImage())

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        setExerciseProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding = null
    }
}