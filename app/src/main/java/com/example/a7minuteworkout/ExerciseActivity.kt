package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding
import com.example.a7minuteworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restTimerDuration: Long = 5000
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseTimerDuration: Long = 1000 * 30
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var textToSpeechObject: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()
        textToSpeechObject = TextToSpeech(this, this)

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        setupRestView()
        setupExerciseStatusRecyclerView()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        customDialogForBackButton()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
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
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
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
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    private fun setupRestView() {

        try {
            val soundURI = Uri.parse("android.resource://com.example.a7minuteworkout/raw/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

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

        speakOut(exerciseList!![currentExercisePosition].getName())
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

        if (textToSpeechObject != null) {
            textToSpeechObject!!.stop()
            textToSpeechObject!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeechObject?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("TTS", "The language is not supported")
            } else {
                Log.d("TTS", "Initialization error")
            }
        }

    }

    private fun speakOut(text: String) {
        textToSpeechObject!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}