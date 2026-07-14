package com.flowstudent.app.ui.screens.survey

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SurveyScreen(onSurveyComplete: (Map<String, String>) -> Unit) {
    var step by remember { mutableStateOf(1) }
    val answers = remember { mutableStateMapOf<String, String>() }

    val questions = listOf(
        Question("¿Para qué quieres usar Zent?", listOf("ahorrar", "controlar gastos", "curiosidad")),
        Question("¿Qué tan seguido gastas dinero?", listOf("diario", "semanal", "ocasional")),
        Question("¿Qué tan detallado quieres el control?", listOf("simple", "medio", "avanzado")),
        Question("¿Prefieres diseño:", listOf("minimalista", "colorido", "oscuro"))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (step <= questions.size) {
            val currentQuestion = questions[step - 1]
            Text(
                text = "Paso $step de ${questions.size}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentQuestion.text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            currentQuestion.options.forEach { option ->
                Button(
                    onClick = {
                        answers[currentQuestion.text] = option
                        if (step < questions.size) step++ else onSurveyComplete(answers)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = MaterialTheme.shapes.medium,
                    border = if (answers[currentQuestion.text] == option) 
                        androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) 
                        else null
                ) {
                    Text(
                        text = option.replaceFirstChar { it.uppercase() },
                        color = if (answers[currentQuestion.text] == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

data class Question(val text: String, val options: List<String>)
