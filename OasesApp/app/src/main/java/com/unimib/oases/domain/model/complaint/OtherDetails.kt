package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.OtherDetailQuestionsIds.ABCDE
import com.unimib.oases.domain.model.symptom.Symptom

object OtherDetails: ComplaintDetails {

    val airwayNotOpenImmediateTreatment = ImmediateTreatment(
        """
        A – Airway abnormal
        - If the patient is unconscious and no trauma is suspected, perform head tilt chin lift. If trauma is possible, use jaw thrust and protect the cervical spine.
        - Remove visible obstruction and clear secretions by suctioning if possible.
        - Insert an oropharyngeal or nasopharyngeal airway.
        - If choking is suspected, perform age-appropriate maneuvers (abdominal thrusts, back blows, chest thrusts).
        - If anaphylaxis is suspected, give IM adrenaline 0.01 mg/kg (maximum 0.5 mg in adults), repeat after 5–15 minutes as needed.
        - If airway cannot be secured or the patient deteriorates, prepare for intubation or tracheostomy.
        """.trimIndent()
    )

    val breathingNotAdequateImmediateTreatment = ImmediateTreatment(
        """
        B – Breathing abnormal
        - If breathing is absent or inadequate, start ventilation with bag and mask and provide oxygen if available.
        - If the patient has respiratory distress, give oxygen via nasal cannulas (1–5 L/min) or facemask (up to 10 L/min). Target SpO2 ≥ 90%
        - If severe wheeze, give salbutamol with inhaler + spacer (2 puffs up to 10 puffs) or nebulized (2.5 mg in children and 5 mg in adults in 2-4 ml saline). May be repeated every 20 minutes in the first hour
        - If anaphylaxis is suspected, give IM adrenaline 0.01 mg/kg (maximum 0.5 mg in adults), repeat after 5–15 minutes as needed.
        - If tension pneumothorax is suspected, consider emergency decompression.
        """.trimIndent()
    )

    val circulationNotAdequateImmediateTreatment = ImmediateTreatment(
        """
        C – Circulation abnormal
        - Apply direct pressure to any external bleeding.
        - Establish intravenous or intraosseous access and start IV fluid resuscitation (Ringer's lactate or Normal Saline 0.9%) 20 ml/kg boluses (up to 1 L in adults) in 30 min according to response (repeat up to 2 times).
        - Consider need of blood transfusion if suspected severe anemia or severe bleeding.
        - Consider internal bleeding in trauma or abdominal pain. If signs of severe shock persist despite initial fluids, or if surgical cause is suspected, arrange urgent transfer to the operating theater.
        - Bind pelvic fractures and splint femur fractures.
        """.trimIndent()
    )

    val disabilityAbnormalImmediateTreatment = ImmediateTreatment(
        """
        D – Disability abnormal
        - If the patient is not fully alert, ensure airway protection (head-tilt/chin lift or jaw thrust and oropharyngeal cannula if no gag reflex) and place in recovery position if no trauma is suspected.
        - Check blood glucose. Treat hypoglycemia (< 2.5 mmol/L, and < 3.0 mmol/L in severely malnourished children) immediately with IV glucose.
        - Immobilize the cervical spine if concern for trauma.
        - If seizures are ongoing, give diazepam (adults: 5-10 mg slow IV; children 0.2 mg/kg slow IV; rectal diazepam 0.5 mg/kg if no IV access)
        - If there are signs of raised intracranial pressure or focal deficit, keep the head elevated if safe and arrange urgent transfer.
        - Consider intubation or tracheostomy if GCS persistently < 8.
        """.trimIndent()
    )

    val exposureProblemImmediateTreatment = ImmediateTreatment(
        """
        E – Exposure abnormal
        - Fully expose the patient to identify hidden injuries while maintaining privacy and preventing hypothermia.
        - Perform log-roll to evaluate spinal injuries in trauma.
        - Remove wet clothing and keep the patient warm.
        - Treat burns according to severity and cover with clean dressings.
        - If anaphylaxis is suspected, give IM adrenaline 0.01 mg/kg (maximum 0.5 mg in adults), repeat after 5–15 minutes as needed.
        - If snakebite is suspected, immobilize the limb and avoid cutting, suction and tight turniquette.
        """.trimIndent()
    )

    override val questions = listOf(
        OtherABCDEQuestion,
        OtherOtherSymptoms,
        OtherDurationQuestion,
        OtherCourseQuestion,
        OtherOtherHighRiskSymptoms,
    )

    data object OtherABCDEQuestion: ComplementaryChoicesQuestion, ComplaintQuestionWithImmediateTreatments, MultipleChoiceComplaintQuestion {
        override val id = ABCDE
        override val question = "Evaluate ABCDE"
        override val type = QuestionType.MultipleChoice
        override val complementaryChoices = listOf(
            setOf(
                Symptom.AirwayNotOpen,
                Symptom.BreathingNotAdequate,
                Symptom.CirculationNotAdequate,
                Symptom.DisabilityAbnormal,
                Symptom.ExposureProblem,
            ),
            setOf(
                Symptom.ABCDEUnremarkable,
            )
        )
        override val isRequired = true
        override val optionsAndTreatments = mapOf(
            Symptom.AirwayNotOpen.id to airwayNotOpenImmediateTreatment,
            Symptom.BreathingNotAdequate.id to breathingNotAdequateImmediateTreatment,
            Symptom.CirculationNotAdequate.id to circulationNotAdequateImmediateTreatment,
            Symptom.DisabilityAbnormal.id to disabilityAbnormalImmediateTreatment,
            Symptom.ExposureProblem.id to exposureProblemImmediateTreatment,
        )
    }

    data object OtherOtherSymptoms: OtherSymptomsQuestion {
        override val id = OtherDetailQuestionsIds.OTHER_SYMPTOMS
        override val isRequired = false
        override val options = listOf(
            Symptom.FeverAbove38Degrees,
            Symptom.FatigueOrGeneralizedWeakness,
            Symptom.NightSweats,
            Symptom.WeightLoss,
            Symptom.Malnutrition,
            Symptom.InabilityToBreastfeedOrDrink,
            Symptom.Shock,
            Symptom.Cough,
            Symptom.Dyspnea,
            Symptom.ChestPain,
            Symptom.IrregularPulseOrIrregularHeartSounds,
            Symptom.RecentFainting,
            Symptom.PeripheralEdemaOrJugularVenousDistension,
            Symptom.Vomiting,
            Symptom.Diarrhea,
            Symptom.AbdominalPain,
            Symptom.GastrointestinalBleeding,
            Symptom.Jaundice,
            Symptom.GenitourinarySymptoms,
            Symptom.VaginalBleeding,
            Symptom.AlteredMentalStatus,
            Symptom.Convulsions,
            Symptom.Headache,
            Symptom.FocalNeurologicalDeficit,
            Symptom.Hypoglycemia,
            Symptom.MoodDisorder,
            Symptom.SeverePallor,
            Symptom.PetechialRashOrPurpura,
            Symptom.SkinRash,
            Symptom.Wound,
            Symptom.AcuteLimbDeformityOrOpenFracture,
            Symptom.LumbarPain,
            Symptom.ArticularPainOrSwelling,
            Symptom.SwellingOrPainOrWarmthOrRednessOfOneLeg,
            Symptom.EyeSymptoms,
            Symptom.ENTSymptoms,
            Symptom.OralOrDentalSymptoms,
        )
    }

    data object OtherOtherHighRiskSymptoms: OtherHighRiskSymptomsQuestion {
        override val id = OtherDetailQuestionsIds.OTHER_HIGH_RISK_SYMPTOMS
        override val isRequired = false
        override val options: List<Symptom> = listOf(
            Symptom.CurrentPregnancy,
            Symptom.HivPositive,
            Symptom.CloseContactWithKnownTuberculosisPatient,
            Symptom.HighRiskTrauma,
            Symptom.MajorBurns,
            Symptom.SexualAssault,
            Symptom.SnakeBite,
            Symptom.AnimalBiteOrNeedlestickPuncture,
            Symptom.InsectBiteOrSting,
            Symptom.SuspectOfDrugOrToxinIngestion,
            Symptom.SuspectOfAlcoholUseOrWithdrawal,
            Symptom.SmokingOrExposedToSmoke,
        )
    }

    data object OtherDurationQuestion: SingleChoiceComplaintQuestion{
        override val id = OtherDetailQuestionsIds.DURATION
        override val question = "How long did the patient experience the symptoms for?"
        override val isRequired = true
        override val options: List<Symptom> = listOf(
            Symptom.OtherSymptomsInTheLastOneToSevenDays,
            Symptom.OtherSymptomsInTheLastEightToFourteenDays,
            Symptom.OtherSymptomsInTheLastFifteenToThirtyDays,
            Symptom.OtherSymptomsInTheLastThirtyPlusDays,
        )
    }

    data object OtherCourseQuestion: SingleChoiceComplaintQuestion{
        override val id = OtherDetailQuestionsIds.COURSE
        override val question = "How was the course of the symptoms?"
        override val isRequired = true
        override val options: List<Symptom> = listOf(
            Symptom.OtherAcuteCourse,
            Symptom.OtherProgressiveCourse,
            Symptom.OtherRecurrentCourse,
        )
    }
}

object OtherDetailQuestionsIds {
    const val ABCDE = "abcde"
    const val OTHER_SYMPTOMS = "other_other_symptoms"
    const val OTHER_HIGH_RISK_SYMPTOMS = "other_other_high_risk_symptoms"
    const val DURATION = "other_duration"
    const val COURSE = "other_course"
}