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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.EditButton
import com.unimib.oases.util.StringFormatHelper.formatDate
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix
import java.time.LocalDate

@Composable
fun PatientDetailsScreen() {

    val patient = Patient(
        name = "Matthias Muia",
        birthDate = "2003-01-24",
        ageInMonths = 22 * 12,
        sex = "Male",
        village = "Rho",
        parish = "Milan",
        subCounty = "Lombardy",
        district = "Italy",
        nextOfKin = "Lilian Maria Berggren",
        contact = "3701302979",
        status = PatientStatus.WAITING_FOR_TRIAGE.displayValue
    )

    Column(
        verticalArrangement = Arrangement.Bottom
    ){
        Box(
            modifier = Modifier.weight(1f)
        ){

            EditButton(
                onClick = {/*TODO*/},
                contentDescription = "Edit patient details"
            ) // Top Right
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)

            ) {
                TitleText(
                    text = "Patient Details",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = patient.name,
                        fontSize = 24.sp,
                    )

                    Text(
                        text = patient.sex,
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

                    val birthDate = formatDate(LocalDate.parse(patient.birthDate))

                    Text(
                        text = age,
                        fontSize = 16.sp
                    )

                    Text(
                        text = birthDate,
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
                        text = patient.status,
                        fontSize = 14.sp
                    )
                }
            }
        }

        BottomButtons(
            onCancel = {/*TODO*/},
            onConfirm = {/*TODO*/},
            cancelButtonText = "Back",
            confirmButtonText = "Next",
        )
    }
}

@Preview
@Composable
fun PatientDetailsScreenPreview() {
    PatientDetailsScreen()
}