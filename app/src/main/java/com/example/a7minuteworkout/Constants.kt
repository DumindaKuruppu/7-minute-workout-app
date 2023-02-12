package com.example.a7minuteworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
        )
        exerciseList.add(jumpingJacks)

        val lunge = ExerciseModel(
            2,
            "Lunge",
            R.drawable.ic_lunge,
        )
        exerciseList.add(lunge)

        val plank = ExerciseModel(
            3,
            "Plank",
            R.drawable.ic_plank,
        )
        exerciseList.add(plank)

        val pushUp = ExerciseModel(
            4,
            "Push Up",
            R.drawable.ic_push_up,
        )
        exerciseList.add(pushUp)

        val pushUpAndRotation = ExerciseModel(
            5,
            "Push Up And Rotation",
            R.drawable.ic_push_up_and_rotation,
        )
        exerciseList.add(pushUpAndRotation)

        val sidePlank = ExerciseModel(
            6,
            "Side Plank",
            R.drawable.ic_side_plank,
        )
        exerciseList.add(sidePlank)

        val squat = ExerciseModel(
            7,
            "Squat",
            R.drawable.ic_squat,
        )
        exerciseList.add(squat)

        val tricepsDipOnChair = ExerciseModel(
            8,
            "Triceps Dip On Chair",
            R.drawable.ic_triceps_dip_on_chair,
        )
        exerciseList.add(tricepsDipOnChair)

        val wallSit = ExerciseModel(
            9,
            "Wall Sit",
            R.drawable.ic_wall_sit,
        )
        exerciseList.add(wallSit)

        val stepUpOntoChair = ExerciseModel(
            10,
            "Step Up Onto Chair",
            R.drawable.ic_step_up_onto_chair,
        )
        exerciseList.add(stepUpOntoChair)

        val abdominalCrunch = ExerciseModel(
            11,
            "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch,
        )
        exerciseList.add(abdominalCrunch)

        val highKneesRunningInPlace = ExerciseModel(
            12,
            "High Knees Running In Place",
            R.drawable.ic_high_knees_running_in_place,
        )
        exerciseList.add(highKneesRunningInPlace)

        return exerciseList
    }
}