package com.unimib.oases.ui.screen.medical_visit.disposition

import com.unimib.oases.domain.model.complaint.ComplaintId

data class DispositionState(
    val visitId: String,

    val complaintIds: Set<ComplaintId> = emptySet(),

    val dispositionChoice: DispositionChoice = DispositionChoice.NONE,

    val wardChoice: WardChoice = WardChoice.NONE,

    val prescribedTreatmentsText: String = "",
    val finalDiagnosisText: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,

    val closeVisitError: String? = null,
) {
    val dispositionTypeQuestion = DispositionTypeQuestion

    val wardQuestion = WardQuestion

    val homeTreatments = setOf(
        HomeTreatment.Diarrhea,
        HomeTreatment.Dyspnea,
        HomeTreatment.SeizuresOrComa,
        HomeTreatment.OtherComplaints,
    )

    val suggestedHomeTreatments: List<HomeTreatment>?
        get() = homeTreatments
            .takeIf { dispositionChoice == DispositionChoice.DISCHARGE }
            ?.filter { it.complaintId in complaintIds }


    private val isDispositionComplete: Boolean
        get() = when (dispositionChoice) {
            DispositionChoice.DISCHARGE -> true
            DispositionChoice.HOSPITALIZE -> wardChoice != WardChoice.NONE
            DispositionChoice.NONE -> false
        }

    val areFreeTextsVisible: Boolean
        get() = isDispositionComplete

    val isSubmitButtonVisible: Boolean
        get() = isDispositionComplete && finalDiagnosisText.isNotBlank()
}

enum class DispositionChoice(val label: String) {
    DISCHARGE("Discharge"),
    HOSPITALIZE("Hospitalize"),
    NONE(""),
}

enum class WardChoice(val label: String) {
    MEDICAL("Medical ward"),
    SURGICAL("Surgical ward"),
    CHILDREN("Children ward"),
    MATERNITY("Maternity ward"),
    HIGH_DEPENDENCY_UNIT("High-dependency unit"),
    NONE(""),
}

sealed class HomeTreatment(
    val complaintId: ComplaintId,
    val label: String,
) {
    data object Diarrhea: HomeTreatment(
        complaintId = ComplaintId.DIARRHEA,
        label = """
        For patients discharged home, counsel the patient/the mother on the 4 rules of home treatment:
        1. Give extra fluid to prevent dehydration: give extra fluid as much as the patient will take. Give frequent small sips from a cup. If the patient vomits, wait 10 minutes, then give more slowly. Continue giving extra fluid as well as ORS until the diarrhoea or other cause of dehydration stops.
        In addition to the usual fluid intake, give ORS (or ReSomal if the patient is malnourished) after each loose stool or episode of vomiting:
        - Child < 2 years: 50-100 ml
        - Child 2-5 years: 100-200 ml
        - From 5 years: 25 ml/kg in the first 4 hours
    
        2. Continue feeding: advice the mother to continue or increase breastfeeding. If child exclusively breastfed, give ORS or safe clean water in addition to breast milk. If child not exclusively breastfed, give one or more of: ORS, soup, rice-water, yoghurt, clean water.
    
        3. Zinc supplementation for children < 5 years
        - Child < 6 months: 10 mg once a day for 10 days
        - Child > 6 months: 20 mg once a day for 10 days
    
        4. When to return: advise to return immediately to the clinic if the patient becomes sicker, is unable to drink or breastfeed, drinks poorly, develops a fever or has blood in the stool. If the patient is still not
        improving, advise the patient to return for follow-up after 5 days.
        """.trimIndent()
    )

    data object Dyspnea: HomeTreatment(
        complaintId = ComplaintId.DYSPNEA,
        label = """
        For patients discharged home, counsel the patient/the mother:
        - encourage adequate oral hydration and feeding / breastfeeding to avoid hypoglycemia and dehydration
        - return if the patient becomes sicker, if breathing worsens, if presence of danger signs (lethargy, convulsions, inability to drink/breastfeed)
        - paracetamol 1 gr PO (children: 15 mg/kg or 7.5mg/kg if < 10 kg) if fever/pain (every 4-6 hr)
        - rescue inhaled salbutamol if wheeze: inhaler+spacer (2 puffs, up to 6 puffs if ≤5 y and up to 10 puffs if >5 y); may be repeated as needed up to 6 times/day
        - avoid antibiotics if cough or cold without difficulty in breathing/evidence of pneumonia (URTI): give warm sweet drinks for sore-throat and home remedies (honey, steam), clean the nostrils with normal saline
        """.trimIndent()
    )

    data object SeizuresOrComa: HomeTreatment(
        complaintId = ComplaintId.SEIZURES_OR_COMA,
        label = """
        For patients discharged home, counsel the patient/the mother:
        - encourage adequate oral hydration and feeding / breastfeeding to avoid hypoglycemia and dehydration
        - if suspected febrile convulsions (age 3 months-6 years with no other high-risk features): sponging and paracetamol 15 mg/kg or 7.5mg/kg if < 10 kg OR/PR if fever > 38°C (every 4-6 hr). Treat based on origin of suspected infection (eg. pneumonia, UTI)
        - return if the patient becomes sicker or if presence of danger signs (lethargy, repeated convulsions, inability to drink/breastfeed)
        - if known epilepsy, encourage compliance with therapy and reassessment at epilepsy clinic
        """.trimIndent()
    )

    data object OtherComplaints: HomeTreatment(
        complaintId = ComplaintId.OTHER,
        label = """
        For patients discharged home, counsel the patient/the mother:
        - encourage adequate oral hydration and feeding / breastfeeding to avoid hypoglycemia and dehydration
        - return if the patient becomes sicker or if presence of danger signs (lethargy, convulsions, inability to drink/breastfeed)
        - paracetamol 1 gr PO (children: 15 mg/kg or 7.5mg/kg if < 10 kg) if fever/pain (every 4-6 hr)
        """.trimIndent()
    )
}

data object DispositionTypeQuestion {
    val question: String = "What is your final disposition decision?"
    val options: Set<DispositionChoice> = setOf(
        DispositionChoice.HOSPITALIZE,
        DispositionChoice.DISCHARGE,
    )
}

data object WardQuestion {
    val question: String = "In which ward will the patient be hospitalized?"
    val options: Set<WardChoice> = WardChoice.entries.minus(WardChoice.NONE).toSet()
}