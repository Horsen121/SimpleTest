package com.lifecontroller.simpletest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lifecontroller.simpletest.ui.theme.SimpleTestTheme

val screen = mutableStateOf("start")
var questions: List<Question> = emptyList()
var answers: ArrayList<String> = arrayListOf()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (screen.value) {
                        "start" -> {
                            Start()
                        }
                        "test" -> {
                            Test(context = applicationContext)
                        }
                        "result" -> {
                            Results()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Start() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { screen.value = "test" }) {
            Text(text = "Пройти тест")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun Test(
    context: Context
) {
    questions = DB.getInstance(context).dao.getQuestions()
    if (answers.size != questions.size)
        for (i in questions.indices)
            answers.add("")

    var question by remember {mutableStateOf(questions[0]) }
    var answer by remember { mutableStateOf("") }
    var page by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question.query,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = answer,
                onValueChange = {
                    answers[page] = it.lowercase()
                    answer = it.lowercase()
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                if (page > 0) { // .value
//                    answers[page] = answer

                    page -= 1
                    answer = answers[page]
                    question = questions[page]
                }
            }) {
                Text(text = "Back")
            }
            Button(onClick = {
                if (page < questions.size - 1) {
//                    answers[page] = answer

                    page += 1
                    answer = answers[page]
                    question = questions[page]
                }
            }) {
                Text(text = "Next")
            }

            if (page == questions.size - 1) {
                Button(onClick = { screen.value = "result" }) {
                    Text(text = "Проверить")
                }
            }
        }
    }
}

@Composable
fun Results() {
    var score = 0
    for (el in questions.indices) {
        if (questions[el].answer == answers[el])
            score++
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "$score / ${questions.size}",
            modifier = Modifier.offset(y = 100.dp)
        )

        Button(onClick = { screen.value = "start" }) {
            Text(text = "На главную")
        }
    }
}