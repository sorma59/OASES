package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.components.util.CenteredTextInBox
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import com.unimib.oases.util.DateAndTimeUtils
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix

@Composable
fun PatientItem(
    patientWithVisitInfo: PatientWithVisitInfo?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    errorText: String = "No patient info",
    isLoading: Boolean = false
){

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .height(100.dp)
            .padding(vertical = 2.dp),
        colors = CardDefaults
            .cardColors()
            .copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){

        if (isLoading)
            CenteredTextInBox(
                text = "Loading...",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

        else if (patientWithVisitInfo == null)
            CenteredTextInBox(
                text = errorText,
                color = MaterialTheme.colorScheme.errorContainer
            )

        else{

            val ageString = getAgeWithSuffix(patientWithVisitInfo.patient.ageInMonths)

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TitleText(patientWithVisitInfo.patient.name + ", " + ageString, color = MaterialTheme.colorScheme.onPrimaryContainer)

                    RoomAndCodeText(patientWithVisitInfo.visit)
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ){
                    Text(patientWithVisitInfo.patient.publicId, color = MaterialTheme.colorScheme.onPrimaryContainer)

                    Text(
                        text = patientWithVisitInfo.visit.patientStatus.displayValue,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PatientItemPreview(){
    PatientItem(
        patientWithVisitInfo = PatientWithVisitInfo(
            patient = Patient(
                id = "1",
                name = "John Doe",
                ageInMonths = 40 * 12,
                publicId = "P12345",
                birthDate = "",
                sex = Sex.MALE,
                village = "",
                parish = "",
                subCounty = "",
                district = "",
                nextOfKin = "",
                contact = ""
            ),
            visit = Visit(
                "3",
                patientId = "1",
                patientStatus = PatientStatus.WAITING_FOR_VISIT,
                triageCode = TriageCode.RED,
                roomName = "Emergency Room",
                arrivalTime = DateAndTimeUtils.getCurrentTime()
            )
        )
    )
}