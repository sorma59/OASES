package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(
    tableName = TableNames.TRIAGE_EVALUATION,
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TriageEvaluationEntity(

    @PrimaryKey
    @ColumnInfo(name = "visit_id")
    val visitId: String,

    // RED Code Symptoms
    @ColumnInfo(name = "unconsciousness") val unconsciousness: Boolean,
    @ColumnInfo(name = "active_convulsions") val activeConvulsions: Boolean,
    @ColumnInfo(name = "respiratory_distress") val respiratoryDistress: Boolean,
    @ColumnInfo(name = "heavy_bleeding") val heavyBleeding: Boolean,
    @ColumnInfo(name = "high_risk_trauma_burns") val highRiskTraumaBurns: Boolean,
    @ColumnInfo(name = "threatened_limb") val threatenedLimb: Boolean,
    @ColumnInfo(name = "poisoning_intoxication") val poisoningIntoxication: Boolean,
    @ColumnInfo(name = "snake_bite") val snakeBite: Boolean,
    @ColumnInfo(name = "aggressive_behavior") val aggressiveBehavior: Boolean,
    @ColumnInfo(name = "pregnancy_with_heavy_bleeding") val pregnancyWithHeavyBleeding: Boolean,
    @ColumnInfo(name = "pregnancy_with_severe_abdominal_pain") val pregnancyWithSevereAbdominalPain: Boolean,
    @ColumnInfo(name = "pregnancy_with_seizures") val pregnancyWithSeizures: Boolean,
    @ColumnInfo(name = "pregnancy_with_altered_mental_status") val pregnancyWithAlteredMentalStatus: Boolean,
    @ColumnInfo(name = "pregnancy_with_severe_headache") val pregnancyWithSevereHeadache: Boolean,
    @ColumnInfo(name = "pregnancy_with_visual_changes") val pregnancyWithVisualChanges: Boolean,
    @ColumnInfo(name = "pregnancy_with_sbp_high_dpb_high") val pregnancyWithSbpHighDpbHigh: Boolean,
    @ColumnInfo(name = "pregnancy_with_trauma") val pregnancyWithTrauma: Boolean,
    @ColumnInfo(name = "pregnancy_with_active_labor") val pregnancyWithActiveLabor: Boolean,

    // YELLOW Code Symptoms
    @ColumnInfo(name = "airway_swelling_mass") val airwaySwellingMass: Boolean,
    @ColumnInfo(name = "ongoing_bleeding") val ongoingBleeding: Boolean,
    @ColumnInfo(name = "severe_pallor") val severePallor: Boolean,
    @ColumnInfo(name = "ongoing_severe_vomiting_diarrhea") val ongoingSevereVomitingDiarrhea: Boolean,
    @ColumnInfo(name = "unable_to_feed_or_drink") val unableToFeedOrDrink: Boolean,
    @ColumnInfo(name = "recent_fainting") val recentFainting: Boolean,
    @ColumnInfo(name = "lethargy_confusion_agitation") val lethargyConfusionAgitation: Boolean,
    @ColumnInfo(name = "focal_neurologic_visual_deficit") val focalNeurologicVisualDeficit: Boolean,
    @ColumnInfo(name = "headache_with_stiff_neck") val headacheWithStiffNeck: Boolean,
    @ColumnInfo(name = "severe_pain") val severePain: Boolean,
    @ColumnInfo(name = "acute_testicular_scrotal_pain_priapism") val acuteTesticularScrotalPainPriapism: Boolean,
    @ColumnInfo(name = "unable_to_pass_urine") val unableToPassUrine: Boolean,
    @ColumnInfo(name = "acute_limb_deformity_open_fracture") val acuteLimbDeformityOpenFracture: Boolean,
    @ColumnInfo(name = "other_trauma_burns") val otherTraumaBurns: Boolean,
    @ColumnInfo(name = "sexual_assault") val sexualAssault: Boolean,
    @ColumnInfo(name = "animal_bite_needlestick_puncture") val animalBiteNeedlestickPuncture: Boolean,
    @ColumnInfo(name = "other_pregnancy_related_complaints") val otherPregnancyRelatedComplaints: Boolean,
    @ColumnInfo(name = "age_over_80_years") val ageOver80Years: Boolean,
    @ColumnInfo(name = "altered_vital_signs_spo2") val alteredVitalSignsSpo2: Boolean,
    @ColumnInfo(name = "altered_vital_signs_rr_low") val alteredVitalSignsRrLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_rr_high") val alteredVitalSignsRrHigh: Boolean,
    @ColumnInfo(name = "altered_vital_signs_hr_low") val alteredVitalSignsHrLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_hr_high") val alteredVitalSignsHrHigh: Boolean,
    @ColumnInfo(name = "altered_vital_signs_sbp_low") val alteredVitalSignsSbpLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_sbp_high") val alteredVitalSignsSbpHigh: Boolean,
    @ColumnInfo(name = "altered_vital_signs_temp_low") val alteredVitalSignsTempLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_temp_high") val alteredVitalSignsTempHigh: Boolean
)
