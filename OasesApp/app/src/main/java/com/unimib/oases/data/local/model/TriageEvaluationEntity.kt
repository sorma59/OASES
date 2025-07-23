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
    var visitId: String,

    // RED Code Symptoms
    @ColumnInfo(name = "unconsciousness") var unconsciousness: Boolean,
    @ColumnInfo(name = "active_convulsions") var activeConvulsions: Boolean,
    @ColumnInfo(name = "respiratory_distress") var respiratoryDistress: Boolean,
    @ColumnInfo(name = "heavy_bleeding") var heavyBleeding: Boolean,
    @ColumnInfo(name = "high_risk_trauma_burns") var highRiskTraumaBurns: Boolean,
    @ColumnInfo(name = "threatened_limb") var threatenedLimb: Boolean,
    @ColumnInfo(name = "poisoning_intoxication") var poisoningIntoxication: Boolean,
    @ColumnInfo(name = "snake_bite") var snakeBite: Boolean,
    @ColumnInfo(name = "aggressive_behavior") var aggressiveBehavior: Boolean,
    @ColumnInfo(name = "pregnancy_with_heavy_bleeding") var pregnancyWithHeavyBleeding: Boolean,
    @ColumnInfo(name = "pregnancy_with_severe_abdominal_pain") var pregnancyWithSevereAbdominalPain: Boolean,
    @ColumnInfo(name = "pregnancy_with_seizures") var pregnancyWithSeizures: Boolean,
    @ColumnInfo(name = "pregnancy_with_altered_mental_status") var pregnancyWithAlteredMentalStatus: Boolean,
    @ColumnInfo(name = "pregnancy_with_severe_headache") var pregnancyWithSevereHeadache: Boolean,
    @ColumnInfo(name = "pregnancy_with_visual_changes") var pregnancyWithVisualChanges: Boolean,
    @ColumnInfo(name = "pregnancy_with_sbp_high_dpb_high") var pregnancyWithSbpHighDpbHigh: Boolean,
    @ColumnInfo(name = "pregnancy_with_trauma") var pregnancyWithTrauma: Boolean,
    @ColumnInfo(name = "pregnancy_with_active_labor") var pregnancyWithActiveLabor: Boolean,

    // YELLOW Code Symptoms
    @ColumnInfo(name = "airway_swelling_mass") var airwaySwellingMass: Boolean,
    @ColumnInfo(name = "ongoing_bleeding") var ongoingBleeding: Boolean,
    @ColumnInfo(name = "severe_pallor") var severePallor: Boolean,
    @ColumnInfo(name = "ongoing_severe_vomiting_diarrhea") var ongoingSevereVomitingDiarrhea: Boolean,
    @ColumnInfo(name = "unable_to_feed_or_drink") var unableToFeedOrDrink: Boolean,
    @ColumnInfo(name = "recent_fainting") var recentFainting: Boolean,
    @ColumnInfo(name = "lethargy_confusion_agitation") var lethargyConfusionAgitation: Boolean,
    @ColumnInfo(name = "focal_neurologic_visual_deficit") var focalNeurologicVisualDeficit: Boolean,
    @ColumnInfo(name = "headache_with_stiff_neck") var headacheWithStiffNeck: Boolean,
    @ColumnInfo(name = "severe_pain") var severePain: Boolean,
    @ColumnInfo(name = "acute_testicular_scrotal_pain_priapism") var acuteTesticularScrotalPainPriapism: Boolean,
    @ColumnInfo(name = "unable_to_pass_urine") var unableToPassUrine: Boolean,
    @ColumnInfo(name = "acute_limb_deformity_open_fracture") var acuteLimbDeformityOpenFracture: Boolean,
    @ColumnInfo(name = "other_trauma_burns") var otherTraumaBurns: Boolean,
    @ColumnInfo(name = "sexual_assault") var sexualAssault: Boolean,
    @ColumnInfo(name = "animal_bite_needlestick_puncture") var animalBiteNeedlestickPuncture: Boolean,
    @ColumnInfo(name = "other_pregnancy_related_complaints") var otherPregnancyRelatedComplaints: Boolean,
    @ColumnInfo(name = "age_over_80_years") var ageOver80Years: Boolean,
    @ColumnInfo(name = "altered_vital_signs_spo2") var alteredVitalSignsSpo2: Boolean,
    @ColumnInfo(name = "altered_vital_signs_rr_low") var alteredVitalSignsRrLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_rr_high") var alteredVitalSignsRrHigh: Boolean,
    @ColumnInfo(name = "altered_vital_signs_hr_low") var alteredVitalSignsHrLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_hr_high") var alteredVitalSignsHrHigh: Boolean,
    @ColumnInfo(name = "altered_vital_signs_sbp_low") var alteredVitalSignsSbpLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_sbp_high") var alteredVitalSignsSbpHigh: Boolean,
    @ColumnInfo(name = "altered_vital_signs_temp_low") var alteredVitalSignsTempLow: Boolean,
    @ColumnInfo(name = "altered_vital_signs_temp_high") var alteredVitalSignsTempHigh: Boolean
)
