package com.rbtcnbl.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val IS_CHEATING = "cheating"
private const val REQUEST_CODE_CHEAT = 0
private const val NUM_QUESTIONS = 6;


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView


    private var numOfCorrectAnswer: Int = 0;
    private val massCheckQuestion: MutableList<Int> = ArrayList()

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        val isCheating = savedInstanceState?.getBoolean(IS_CHEATING) ?: false
        quizViewModel.isCheater = isCheating

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        cheatButton.setOnClickListener { view: View ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            trueButton.isEnabled = false
            falseButton.isEnabled = false

            if (quizViewModel.currentIndex == NUM_QUESTIONS - 1) {
                sumOfCorrectAnswer()
            }
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            trueButton.isEnabled = false
            falseButton.isEnabled = false

            if (quizViewModel.currentIndex == NUM_QUESTIONS - 1) {
                sumOfCorrectAnswer()
            }
        }


        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveBack()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()


    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putBoolean(IS_CHEATING, quizViewModel.isCheater)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        checkActualQuestion()
    }

    //TODO ослабить проверку на "читерство" и разрешать отвечать на другие вопросы
    private fun checkAnswer(userAnswer: Boolean) {
        val currentQuestion = quizViewModel.currentQuestionText
        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messageResId = 0
        when {
            quizViewModel.isCheater -> {
                messageResId = R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                messageResId = R.string.correct_toast
                numOfCorrectAnswer++
            }
            else -> {
                messageResId = R.string.incorrect_toast
            }

        }
        massCheckQuestion.add(currentQuestion)
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()


    }

    private fun sumOfCorrectAnswer() {
        val correctAnswer = (numOfCorrectAnswer.toFloat() / NUM_QUESTIONS) * 100
        Toast.makeText(this, "$correctAnswer%", Toast.LENGTH_SHORT).show()
    }

    private fun checkActualQuestion() {
        when {
            massCheckQuestion.contains(quizViewModel.currentQuestionText) -> {
                trueButton.isEnabled = false
                falseButton.isEnabled = false
            }
            else -> {
                trueButton.isEnabled = true
                falseButton.isEnabled = true
            }
        }
    }
}
