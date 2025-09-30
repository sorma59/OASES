package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.ComplaintSummaryEntity
import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.SupportiveTherapyText
import com.unimib.oases.domain.model.symptom.Symptom

fun ComplaintSummary.toEntity(): ComplaintSummaryEntity {
    return ComplaintSummaryEntity(
        visitId = visitId,
        complaintId = complaintId,
        algorithmsQuestionsAndAnswers = algorithmsQuestionsAndAnswers,
        symptoms = symptoms.map{it.id},
        labelledTests = tests.toList(),
        immediateTreatments = immediateTreatments.map{it.text},
        supportiveTherapies = supportiveTherapies.map{it.text},
        additionalTests = additionalTests
    )
}

fun ComplaintSummaryEntity.toDomain(): ComplaintSummary {
    return ComplaintSummary(
        visitId = visitId,
        complaintId = complaintId,
        algorithmsQuestionsAndAnswers = algorithmsQuestionsAndAnswers,
        symptoms = symptoms.map { Symptom.symptoms[it] ?: error("Symptom not found") }.toSet(),
        tests = labelledTests.toSet(),
        immediateTreatments = immediateTreatments.map{ImmediateTreatment(it)}.toSet(),
        supportiveTherapies = supportiveTherapies.map{SupportiveTherapyText(it)}.toSet(),
        additionalTests = additionalTests,
    )
}