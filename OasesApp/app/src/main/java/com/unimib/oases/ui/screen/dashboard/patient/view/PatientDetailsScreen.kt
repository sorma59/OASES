package com.unimib.oases.ui.screen.dashboard.patient.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix

@Composable
fun PatientDetailsScreen(
    appViewModel: AppViewModel
) {

    val viewModel: PatientDetailsViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ){

        state.patient?.let {
            PatientDetails(it)
//          EditButton(
//              onClick = { viewModel.onEvent(PatientDetailsEvent.OnEdit) },
//              contentDescription = "Edit patient details"
//          ) // Top Right
        }
            ?: Box(Modifier.fillMaxSize()) {
                RetryButton(
                    error = "Failed to load patient data",
                    onClick = { viewModel.onEvent(PatientDetailsEvent.OnRetryPatientDetails) },
                )
            }

        MainComplaintsDetails(state, viewModel)
    }
}

@Composable
fun MainComplaintsDetails(
    state: PatientDetailsState,
    viewModel: PatientDetailsViewModel
) {
    state.currentVisit?.let {
        state.currentVisitRelatedError?.let {
            RetryButton(
                error = it,
                onClick = { viewModel.onEvent(PatientDetailsEvent.OnRetryCurrentVisitRelated) },
            )
        } ?: Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            TitleText("Main complaints summaries", fontSize = 22)
            if (state.mainComplaintsSummaries.isEmpty())
                CenteredText("No main complaints summaries")
            else {
                for (complaint in state.mainComplaintsSummaries) {
                    MainComplaintSummary(complaint)
                    HorizontalDivider()
                }
            }
        }
    } ?: CenteredText("No current visit")
}

@Composable
fun MainComplaintSummary(complaint: ComplaintSummary) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Spacer(Modifier.height(8.dp))

        val title = ComplaintId.complaints[complaint.complaintId]!!.label
        TitleText(title, fontSize = 20)

        Spacer(Modifier.height(8.dp))

        TitleText("Answers", fontSize = 16)

        for (algorithmQuestionsAndAnswer in complaint.algorithmsQuestionsAndAnswers) {
            for (questionAndAnswer in algorithmQuestionsAndAnswer){
                Text(
                    text = questionAndAnswer.question,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = questionAndAnswer.answer,
                    fontSize = 14.sp
                )
            }
        }

        HorizontalDivider()

        TitleText("Suggested immediate treatments", fontSize = 16)

        for (immediateTreatment in complaint.immediateTreatments) {
            Text(
                text = immediateTreatment.text,
                fontSize = 14.sp
            )
        }


        HorizontalDivider()

        TitleText("Symptoms", fontSize = 16)

        if (complaint.symptoms.isNotEmpty()){
            for (symptom in complaint.symptoms) {
                Text(
                    text = symptom.label,
                    fontSize = 14.sp
                )
            }
        } else {
            Text(
                text = "None",
                fontSize = 14.sp
            )
        }

        HorizontalDivider()

        TitleText("Selected tests", fontSize = 16)

        if (complaint.tests.isNotEmpty() || complaint.additionalTests.isNotBlank()){
            for (test in complaint.tests) {
                Text(
                    text = test.label,
                    fontSize = 14.sp
                )
            }
        } else {
            Text(
                text = "None",
                fontSize = 14.sp
            )
        }


        if (complaint.additionalTests.isNotBlank()){
            TitleText("Additional tests", fontSize = 16)

            Text(
                text = complaint.additionalTests,
                fontSize = 14.sp
            )
        }


        HorizontalDivider()

        TitleText("Suggested supportive therapies", fontSize = 16)

        if (complaint.supportiveTherapies.isNotEmpty()){
            for (supportiveTherapy in complaint.supportiveTherapies) {
                Text(
                    text = supportiveTherapy.text,
                    fontSize = 14.sp
                )
            }
        } else {
            Text(
                text = "None",
                fontSize = 14.sp
            )
        }

    }
}

@Composable
private fun PatientDetails(patient: Patient) {
    Column {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = patient.name,
                fontSize = 24.sp,
            )

            Text(
                text = patient.sex.displayName,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val age = getAgeWithSuffix(patient.ageInMonths)

            Text(
                text = age,
                fontSize = 16.sp
            )

            Text(
                text = patient.birthDate,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        val location = listOf(
            patient.village,
            patient.parish,
            patient.subCounty,
            patient.district
        ).joinToString()

        Text(
            text = patient.contact,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = location,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(Modifier.height(6.dp))

        HorizontalDivider()

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Next of kin"
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = patient.nextOfKin,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.MedicalServices,
                contentDescription = "Next of kin"
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = patient.status.displayValue,
                fontSize = 14.sp
            )
        }
    }
}