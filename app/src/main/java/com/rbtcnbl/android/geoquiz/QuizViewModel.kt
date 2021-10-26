package com.rbtcnbl.android.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuiizViewModel"
private const val NUM_QUESTIONS = 6;

class QuizViewModel : ViewModel() {

    private var numOfCorrectAnswer = 0;
    var currentIndex = 0
    var isCheater = false

    private val questionBank = listOf(
        Question(R.string.question_russia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    //TODO после последнего вопроса проходит список заново и не выводит процент (упражнение на стр 107)
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
//        if(currentIndex == questionBank.size){
//                val messageResId = (numOfCorrectAnswer / NUM_QUESTIONS)*100
//                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
//
//            } else {
//                currentIndex = (currentIndex + 1) % questionBank.size
//
//            }
    }

    fun moveBack() {
        if (currentIndex != 0) {
            currentIndex = (currentIndex - 1) % questionBank.size
        } else {
            //игнорирует, иначе крашится
            //решить потом, что делать
        }

    }
}
