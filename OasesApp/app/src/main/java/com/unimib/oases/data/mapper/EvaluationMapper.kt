package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.EvaluationEntity
import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.symptom.symptomsById

fun Evaluation.toEntity(): EvaluationEntity {
    return EvaluationEntity(
        visitId = visitId,
        complaintId = complaintId.id,
        algorithmsQuestionsAndAnswers = algorithmsQuestionsAndAnswers,
        symptomIds = symptoms.map { it.id },
        suggestedTests = suggestedTests.toList(),
        labelledTests = requestedTests.toList(),
        immediateTreatments = immediateTreatments.toList(),
        supportiveTherapies = supportiveTherapies.toList(),
        additionalTests = additionalTestsText,
        treeAnswers = treeAnswers,
        detailQuestionAnswers = detailQuestionAnswers,
    )
}

fun EvaluationEntity.toDomain(): Evaluation {
    return Evaluation(
        visitId = visitId,
        complaintId = ComplaintId.complaints[complaintId] ?: error("Complaint id $complaintId not found"),
        algorithmsQuestionsAndAnswers = algorithmsQuestionsAndAnswers,
        symptoms = symptomIds.map { symptomsById[it] ?: error("Symptom not found $it") }.toSet(),
        suggestedTests = suggestedTests.toSet(),
        requestedTests = labelledTests.toSet(),
        immediateTreatments = immediateTreatments.toSet(),
        supportiveTherapies = supportiveTherapies.toSet(),
        additionalTestsText = additionalTests,
        treeAnswers = treeAnswers,
        detailQuestionAnswers = detailQuestionAnswers,
    )
}

fun List<Evaluation>.toEntities(): List<EvaluationEntity> {
    return this.map { it.toEntity() }
}