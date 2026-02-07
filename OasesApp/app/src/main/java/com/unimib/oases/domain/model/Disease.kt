package com.unimib.oases.domain.model

data class Disease(
    val name: String,
    val sexSpecificity: SexSpecificity,
    val ageSpecificity: AgeSpecificity,
    val group: PmhGroup,
    val entryType: DiseaseEntryType
)

enum class SexSpecificity(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    ALL("All")
}

enum class AgeSpecificity(val displayName: String) {
    CHILDREN("Children"),
    ADULTS("Adults"),
    ALL("All")
}

enum class PmhGroup(val displayName: String) {
    ALLERGIES("Allergies"),
    MEDICATIONS("Current medications"),
    VACCINATIONS("Vaccinations"),
    INFECTIOUS("Infectious Diseases"),
    HAEMATOLOGICAL("Haematological Diseases"),
    GASTROINTESTINAL("Gastrointestinal Diseases"),
    CARDIOVASCULAR("Cardiovascular Diseases"),
    LUNG("Lung Diseases"),
    KIDNEY("Kidney Diseases"),
    ENDOCRINE("Endocrine Diseases"),
    NEUROPSYCHIATRIC("Neuropsychiatric Diseases"),
    MEDICAL_CONDITIONS("Other significant medical conditions"),
    MEDICAL_EVENTS("Other significant past medical events"),
    OBSTETRIC_HISTORY("Obstetric history")
}

enum class DiseaseEntryType {
    SELECTION,
    FREE_TEXT
}