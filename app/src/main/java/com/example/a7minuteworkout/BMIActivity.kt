package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minuteworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val IMPERIAL_UNITS_VIEW = "IMPERIAL_UNITS_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private var binding: ActivityBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.btnCalculateBmi?.setOnClickListener {
            if (validateUnits()) {
                if (currentVisibleView == METRIC_UNITS_VIEW) {
                    val heightValue: Float = binding?.etHeight?.text.toString().toFloat() / 100
                    val weightValue: Float = binding?.etWeight?.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    val feetValue: String = binding?.etFeet?.text.toString()
                    val inchValue: String = binding?.etInches?.text.toString()
                    val weightValue: Float = binding?.etWeight?.text.toString().toFloat()

                    val heightValue: Float = (feetValue.toFloat() * 12 + inchValue.toFloat())

                    val bmi = (weightValue / (heightValue * heightValue)) * 703
                    displayBMIResult(bmi)
                }
            } else {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rb_metric_unit) {
                makeVisibleMetricUnitsView()
            } else if (checkedId == R.id.rb_imperial_unit) {
                makeVisibleImperialUnitsView()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun validateUnits(): Boolean {
        var isValid = true

        if (binding?.etWeight?.text.toString().isEmpty()) {
            isValid = false
        }
        if (currentVisibleView == METRIC_UNITS_VIEW) {
            if (binding?.etHeight?.text.toString().isEmpty()) {
                isValid = false
            }
        } else {
            if (binding?.etFeet?.text.toString().isEmpty() || binding?.etInches?.text.toString()
                    .isEmpty()
            ) {
                isValid = false
            }
        }

        return isValid
    }

    private fun makeVisibleImperialUnitsView() {
        clearAllTextFields()
        currentVisibleView = IMPERIAL_UNITS_VIEW
        binding?.llImperialHeightInput?.visibility = View.VISIBLE
        binding?.tilHeight?.visibility = View.GONE
        binding?.tilWeight?.hint = "Enter Weight (lbs)"
        binding?.llDisplayBmiResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleMetricUnitsView() {
        clearAllTextFields()
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.llImperialHeightInput?.visibility = View.GONE
        binding?.tilHeight?.visibility = View.VISIBLE
        binding?.tilWeight?.hint = "Enter Weight (kg)"
        binding?.llDisplayBmiResult?.visibility = View.INVISIBLE
    }

    private fun clearAllTextFields() {
        binding?.etHeight?.text?.clear()
        binding?.etWeight?.text?.clear()
        binding?.etFeet?.text?.clear()
        binding?.etInches?.text?.clear()
        binding?.llDisplayBmiResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your better! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your better! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "Oops! You really need to take care of your better! Workout maybe!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "Oops! You really need to take care of your better! Workout maybe!"
        }

        binding?.llDisplayBmiResult?.visibility = View.VISIBLE
        binding?.tvYourBmiValue?.text =
            BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.tvYourBmiCategory?.text = bmiLabel
        binding?.tvYourBmiCategoryDescription?.text = bmiDescription
    }
}