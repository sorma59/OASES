package com.unimib.oases.ui.screen.patient_registration.info


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.DateSelector
import com.unimib.oases.ui.components.util.FadeOverlay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun PatientInfoScreen(
    name: String,
    onNameChanged: (String) -> Unit,
    age: String,
    onAgeChanged: (String) -> Unit,
    sex: String,
    onSexChanged: (String) -> Unit,
    village: String,
    onVillageChanged: (String) -> Unit,
    parish: String,
    onParishChanged: (String) -> Unit,
    subCountry: String,
    onSubCountryChanged: (String) -> Unit,
    district: String,
    onDistrictChanged: (String) -> Unit,
    nextOfKin: String,
    onNextOfKinChanged: (String) -> Unit,
    contact: String,
    onContactChanged: (String) -> Unit,
    date: String,
    onDateChanged: (String) -> Unit,
    time: String,
    onTimeChanged: (String) -> Unit,

) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedLabelOutlinedTextField(name, onNameChanged, "Name", Modifier.fillMaxWidth())

            AnimatedLabelOutlinedTextField(
                age,
                onAgeChanged,
                "Age",
                Modifier.fillMaxWidth(),
                isNumeric = true
            )

            SexDropdown(sex, onSexChanged, Modifier.fillMaxWidth())

            AnimatedLabelOutlinedTextField(
                village,
                onVillageChanged,
                "Village",
                Modifier.fillMaxWidth()
            )

            AnimatedLabelOutlinedTextField(
                parish,
                onParishChanged,
                "Parish",
                Modifier.fillMaxWidth()
            )

            AnimatedLabelOutlinedTextField(
                subCountry,
                onSubCountryChanged,
                "Sub Country",
                Modifier.fillMaxWidth()
            )

            AnimatedLabelOutlinedTextField(
                district,
                onDistrictChanged,
                "District",
                Modifier.fillMaxWidth()
            )

            AnimatedLabelOutlinedTextField(
                nextOfKin,
                onNextOfKinChanged,
                "Next of Kin",
                Modifier.fillMaxWidth()
            )

            AnimatedLabelOutlinedTextField(
                contact,
                onContactChanged,
                "Contact",
                Modifier.fillMaxWidth()
            )

            DateSelector(date, onDateChanged, Modifier.fillMaxWidth(), context)

            CurrentTimeDisplay(onTimeChanged, Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(30.dp))
        }

        FadeOverlay(modifier = Modifier.align(Alignment.BottomCenter))
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexDropdown(
    selectedSex: String,
    onSexSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val sexOptions = listOf("Maschio", "Femmina")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        AnimatedLabelOutlinedTextField(
            value = selectedSex,
            onValueChange = { /* Non permettere la modifica diretta */ },
            labelText = "Sesso",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            anchorModifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sexOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onSexSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CurrentTimeDisplay(
    onTimeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var time by remember { mutableStateOf("") }
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("Europe/Rome") }

    LaunchedEffect(key1 = Unit) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"))
        time = timeFormat.format(calendar.time)
        onTimeChanged(time)
    }

    AnimatedLabelOutlinedTextField(
        value = time,
        onValueChange = { /* Non permettere la modifica diretta */ },
        labelText = "Time",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Filled.Timer, contentDescription = "Ora attuale")
        }
    )
}