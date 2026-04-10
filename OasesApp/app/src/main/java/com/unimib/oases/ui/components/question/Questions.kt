package com.unimib.oases.ui.components.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.complaint.ComplaintQuestion
import com.unimib.oases.domain.model.complaint.ComplaintQuestionWithImmediateTreatment
import com.unimib.oases.domain.model.complaint.Condition
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.MultipleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.SingleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.domain.model.complaint.binarytree.ManualNode
import com.unimib.oases.domain.model.complaint.binarytree.Tree
import com.unimib.oases.domain.model.complaint.getLabels
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.input.LabeledRadioButton
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.screen.medical_visit.evaluation.TreeSummary

@Composable
fun SupportiveTherapies(
    supportiveTherapies: List<TherapyText>?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        supportiveTherapies?.let { supportiveTherapies ->

            if (supportiveTherapies.isNotEmpty()){
                TitleText("Supportive therapies", fontSize = 18)
                HorizontalDivider(thickness = 0.8.dp)
                supportiveTherapies.forEach {
                    Text(it.text)
                    HorizontalDivider(thickness = 0.8.dp)
                }
            } else
                Text("No supportive therapies suggested")
        }
    }
}

@Composable
fun GenerateTestsButton(
    onClick: () -> Unit,
    visible: Boolean
) {
    if (visible){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClick
            ) {
                Text("Suggest tests and supportive therapies", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun Tests(
    conditions: List<Condition>,
    isChecked: (LabelledTest) -> Boolean,
    onCheckedChange: (LabelledTest) -> Unit,
    additionalTestsText: () -> String,
    onAdditionalTestsChanged: (String) -> Unit,
    readOnly: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        if (conditions.isNotEmpty()){
            TitleText("Suggested tests (flag if you do order)")
            conditions.forEach { condition ->
                Column{
                    TitleText(condition.label, fontSize = 18)
                    Column(verticalArrangement =  Arrangement.spacedBy(8.dp)){
                        condition.suggestedTests.forEach { labelledTest ->
                            LabeledCheckbox(
                                label = labelledTest.label,
                                checked = isChecked(labelledTest),
                                onCheckedChange = { onCheckedChange(labelledTest) },
                                readOnly = readOnly,
                            )
                        }
                    }
                }
            }
            AdditionalTests(additionalTestsText, onAdditionalTestsChanged, readOnly)
        }
    }
}

@Composable
fun AdditionalTests(
    text: () -> String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
) {
    Column{
        TitleText("Additional tests", fontSize = 18)

        OutlinedTextField(
            value = text(),
            onValueChange = onValueChange,
            label = { Text("Write here any additional test you have ordered") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
            enabled = readOnly.not()
        )
    }
}

@Composable
fun DetailsQuestions(
    detailsQuestions: List<ComplaintQuestion>,
    symptoms: Set<Symptom>,
    onSymptomSelected: (Symptom, ComplaintQuestion) -> Unit,
    readOnly: Boolean = false,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        detailsQuestions.forEach { question ->
            when(question){
                is MultipleChoiceComplaintQuestion -> {
                    MultipleChoiceQuestion(
                        question = question,
                        isChecked = {
                            symptoms.contains(it)
                        },
                        onCheckedChange = { onSymptomSelected(it, question) },
                        readOnly = readOnly,
                    )
                }

                is SingleChoiceComplaintQuestion -> {
                    SingleChoiceQuestion(
                        question = question,
                        isSelected = {
                            symptoms.contains(it)
                        },
                        onSelected = { onSymptomSelected(it, question) },
                        readOnly = readOnly,
                    )
                }
            }
            if (question is ComplaintQuestionWithImmediateTreatment)
                if (question.shouldShowTreatment(symptoms)) {
                    TitleText("Immediate Treatment")
                    Text(question.treatment.text)
                }
        }
    }
}

@Composable
fun ImmediateTreatmentQuestions(
    algorithms: List<Tree>,
    immediateTreatmentQuestions: List<TreeSummary>,
    immediateTreatments: List<ImmediateTreatment>,
    onNodeAnswer: (Boolean, ManualNode, Tree) -> Unit = {_, _, _ -> },
    readOnly: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )  {
        algorithms.forEachIndexed { index, algorithm ->
            if (index <= immediateTreatmentQuestions.size) // Somewhat defensive
                Column{
                    immediateTreatmentQuestions.elementAt(index).answers.forEach { (node, answer) ->
                        YesOrNoQuestion(
                            question = node.value,
                            onAnswer = {
                                onNodeAnswer(it, node, algorithm)
                            },
                            readOnly = readOnly,
                            answer = answer,

                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    if (immediateTreatments.isNotEmpty()){
                        immediateTreatments.elementAt(index).let {
                            TitleText("Immediate Treatment")
                            Text(it.text)
                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider(thickness = 0.8.dp)
                        }
                    }
                }
        }
    }
}

@Composable
fun YesOrNoQuestion(
    question: String,
    onAnswer: (Boolean) -> Unit,
    readOnly: Boolean,
    answer: Boolean?
){

    Column{
        Text(question)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Yes")

            RadioButton(
                selected = answer == true,
                onClick = {
                    if (answer != true) {
                        onAnswer(true)
                    }
                },
                enabled = readOnly.not()
            )

            Spacer(Modifier.width(8.dp))

            Text("No")

            RadioButton(
                selected = answer == false,
                onClick = {
                    if (answer != false) {
                        onAnswer(false)
                    }
                },
                enabled = readOnly.not()
            )
        }
    }
}

@Composable
fun SingleChoiceQuestion(
    question: SingleChoiceComplaintQuestion,
    isSelected: (Symptom) -> Boolean,
    onSelected: (Symptom) -> Unit,
    readOnly: Boolean = false,
){

    val labels = question.getLabels()

    Column{
        TitleText(question.question)

        question.options.forEachIndexed { index, option ->
            LabeledRadioButton(
                label = { Text(text = labels[index]) },
                selected = isSelected(option),
                onClick = { onSelected(option) },
                readOnly = readOnly,
            )
        }
    }
}

@Composable
fun MultipleChoiceQuestion(
    question: MultipleChoiceComplaintQuestion,
    isChecked: (Symptom) -> Boolean,
    onCheckedChange: (Symptom) -> Unit,
    readOnly: Boolean,
){
    Column{
        TitleText(question.question)

        question.options.forEach { symptom ->
            LabeledCheckbox(
                label = symptom.label,
                checked = isChecked(symptom),
                onCheckedChange = { onCheckedChange(symptom) },
                readOnly = readOnly,
            )
        }
    }
}