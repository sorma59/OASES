@file:JvmName("PatientCardKt")

package com.unimib.oases.ui.components.card

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.components.patients.RoomAndCodeText
import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import com.unimib.oases.ui.theme.OasesTheme
import com.unimib.oases.util.DateTimeFormatter
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.math.roundToInt

@Composable
fun PatientCard(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    patientWithVisitInfo: PatientWithVisitInfo,
    onCardClick: (PatientAndVisitIds) -> Unit,
) {
    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if (isRevealed) {
            offset.animateTo(contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    val ageString = getAgeWithSuffix(ageInMonths = patientWithVisitInfo.patient.ageInMonths)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .height(IntrinsicSize.Min)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                }
                .background(color = MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            actions()
        }
        Card(
            onClick = { onCardClick(patientWithVisitInfo.getIds()) },
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(0f, contextMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                },
            colors = CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.primary),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 0.dp, top = 0.dp, bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    RoomAndCodeText(patientWithVisitInfo.visit)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(  modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom){
                    Text(
                        text = patientWithVisitInfo.patient.name + ", ",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = ageString,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {

                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){
                        Text(
                            text = patientWithVisitInfo.patient.publicId,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = patientWithVisitInfo.visit.patientStatus.displayValue,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }

                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(end = 10.dp), horizontalArrangement = Arrangement.End){
                    Text(
                        text = patientWithVisitInfo.visit.arrivalTime.format(DateTimeFormatter.hoursAndMinutesFormatter),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Normal,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun PatientCardPreview() {

    OasesTheme {
        PatientCard(
            isRevealed = false,
            actions = {},
            modifier = Modifier,
            onExpanded = {},
            onCollapsed = {},
            patientWithVisitInfo = PatientWithVisitInfo(
                patient = Patient(
                    id = "1",
                    name = "John Doe",
                    ageInMonths = 45,
                    publicId = "P12345",
                    birthDate = "",
                    sex = Sex.FEMALE,
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
                    arrivalTime = LocalTime.now()
                )
            ),
            onCardClick = {}
        )
    }
}


