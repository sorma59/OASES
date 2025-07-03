package com.unimib.oases.domain.model

data class Disease(
    val name: String,
    val sexSpecificity: String,
    val ageSpecificity: String
)

enum class SexSpecificity(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    ALL("All");

    // Optional: Function to get enum from display name (useful for UI)
    companion object {
        fun fromSexSpecificityDisplayName(displayName: String): SexSpecificity {
            return entries.find { it.displayName == displayName } ?: ALL
        }

        // Optional: Function to get enum from stored name (robust)
        fun fromStoredName(storedName: String): SexSpecificity? {
            return try {
                valueOf(storedName)
            } catch (e: IllegalArgumentException) {
                null // Handle cases where the stored name might be invalid
            }
        }
    }
}

enum class AgeSpecificity(val displayName: String) {
    CHILDREN("Children"),
    ADULTS("Adults"),
    ALL("All");

    // Optional: Function to get enum from display name (useful for UI)
    companion object {
        fun fromAgeSpecificityDisplayName(displayName: String): AgeSpecificity {
            return AgeSpecificity.entries.find { it.displayName == displayName } ?: ALL
        }

        // Optional: Function to get enum from stored name (robust)
        fun fromStoredName(storedName: String): AgeSpecificity? {
            return try {
                AgeSpecificity.valueOf(storedName)
            } catch (e: IllegalArgumentException) {
                null // Handle cases where the stored name might be invalid
            }
        }
    }
}