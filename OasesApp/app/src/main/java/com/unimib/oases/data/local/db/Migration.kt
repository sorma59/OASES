package com.unimib.oases.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unimib.oases.data.local.TableNames

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // --- PREPARATION ---
        // It's safest to disable foreign keys while altering schemas.
        db.execSQL("PRAGMA foreign_keys=OFF")

        // --- STEP 1: Add the new columns to the EXISTING Visit table ---
        // We add them one by one. `IF NOT EXISTS` is a safety measure.
        db.execSQL("ALTER TABLE ${TableNames.VISIT} ADD COLUMN patient_status TEXT NOT NULL DEFAULT 'DISMISSED'")
        db.execSQL("ALTER TABLE ${TableNames.VISIT} ADD COLUMN room_name TEXT") // Nullable by default
        db.execSQL("ALTER TABLE ${TableNames.VISIT} ADD COLUMN arrival_time TEXT NOT NULL DEFAULT ''")

        // --- STEP 2: Create a temporary Patient table with the final, clean schema ---
        db.execSQL("""
            CREATE TABLE patient_new (
                id TEXT NOT NULL PRIMARY KEY,
                public_id TEXT NOT NULL,
                name TEXT NOT NULL,
                birth_date TEXT NOT NULL,
                sex TEXT NOT NULL,
                village TEXT NOT NULL,
                parish TEXT NOT NULL,
                sub_county TEXT NOT NULL,
                district TEXT NOT NULL,
                next_of_kin TEXT NOT NULL,
                contact TEXT NOT NULL,
                image BLOB
            )
        """)

        // --- STEP 3: Copy only the patient-specific data to the new patient table ---
        db.execSQL("""
            INSERT INTO patient_new (id, public_id, name, birth_date, sex, village, parish, sub_county, district, next_of_kin, contact, image)
            SELECT id, public_id, name, birth_date, sex, village, parish, sub_county, district, next_of_kin, contact, image FROM ${TableNames.PATIENT}
        """)

        // --- STEP 4: Populate the new columns in the Visit table using data from the OLD Patient table ---
        // This query updates each visit by looking up the corresponding patient in the OLD patient table.
        // We assume each patient had one "active" visit's data stored on them.
        db.execSQL("""
            UPDATE ${TableNames.VISIT}
            SET
                patient_status = (SELECT CASE status
                       WHEN 'Waiting for triage' THEN 'WAITING_FOR_TRIAGE'
                       WHEN 'Waiting for visit' THEN 'WAITING_FOR_VISIT'
                       WHEN 'Waiting for test results' THEN 'WAITING_FOR_TEST_RESULTS'
                       WHEN 'Hospitalized' THEN 'HOSPITALIZED'
                       WHEN 'Dismissed' THEN 'DISMISSED'
                       ELSE 'DISMISSED'
                   END FROM ${TableNames.PATIENT} WHERE ${TableNames.PATIENT}.id = ${TableNames.VISIT}.patient_id),
                room_name = (SELECT room FROM ${TableNames.PATIENT} WHERE ${TableNames.PATIENT}.id = ${TableNames.VISIT}.patient_id),
                arrival_time = (SELECT SUBSTR(arrival_time, 12) FROM ${TableNames.PATIENT} WHERE ${TableNames.PATIENT}.id = ${TableNames.VISIT}.patient_id)
        """)

        // --- STEP 5: Clean up ---
        // Drop the original, outdated patient table
        db.execSQL("DROP TABLE ${TableNames.PATIENT}")

        // --- STEP 6: Rename the new patient table to its original name ---
        db.execSQL("ALTER TABLE patient_new RENAME TO ${TableNames.PATIENT}")

        // --- FINALIZATION ---
        // Re-enable foreign key constraints.
        db.execSQL("PRAGMA foreign_keys=ON")
    }
}

val MIGRATION_Disease_Refactor: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys=OFF")

        // 1. Create the new table with the updated schema
        // Note: Enums map to TEXT, Booleans map to INTEGER (0/1) in SQLite
        db.execSQL("""
            CREATE TABLE disease_new (
                name TEXT NOT NULL PRIMARY KEY,
                sex_specificity TEXT NOT NULL,
                age_specificity TEXT NOT NULL,
                `group` TEXT NOT NULL,
                allows_free_text INTEGER NOT NULL DEFAULT 0
            )
        """)

        // 2. Transfer data and Map strings to Enum Names
        // We use CASE to translate 'Male only' -> 'MALE_ONLY' etc.
        db.execSQL("""
            INSERT INTO disease_new (name, sex_specificity, age_specificity, `group`, allows_free_text)
            SELECT 
                name,
                CASE  
                    WHEN sex_specificity IN ('Male', 'MALE') THEN 'MALE'
                    WHEN sex_specificity IN ('Female', 'FEMALE') THEN 'FEMALE'
                    ELSE 'ALL' 
                END,
                CASE 
                    WHEN age_specificity IN ('Adults', 'ADULTS') THEN 'ADULTS'
                    WHEN age_specificity IN ('Children', 'CHILDREN') THEN 'CHILDREN'
                    ELSE 'ALL'
                END,
                CASE 
                    WHEN name = 'Allergies' THEN 'ALLERGIES'
                    WHEN name = 'Current medications' THEN 'MEDICATIONS'
                    WHEN name = 'Vaccinations' THEN 'VACCINATIONS'
                    WHEN name IN ('HIV/AIDS', 'Tuberculosis', 'STIs (e.g., syphilis, urethritis, cervicitis/PID)', 'Malaria (recurrent/severe)', 'Pneumonia (recurrent/severe)', 'Congenital infections (e.g., CMV, toxoplasmosis, syphilis)') THEN 'INFECTIOUS'
                    WHEN name IN ('Sickle cell disease or trait', 'Other chronic anaemia', 'Bleeding disorder/coagulopathy') THEN 'HAEMATOLOGICAL'
                    WHEN name IN ('Peptic ulcer disease/Gastritis/GERD', 'Liver disease (e.g., HBV, HCV, cirrhosis)', 'Liver disease (e.g., HBV, HCV, congenital)/jaundice') THEN 'GASTROINTESTINAL'
                    WHEN name IN ('Heart disease (e.g., congenital, rheumatic, ischemic, arrhythmic)', 'Hypertension', 'Stroke or TIA', 'Heart disease (e.g., congenital/rheumatic)') THEN 'CARDIOVASCULAR'
                    WHEN name IN ('Asthma or chronic lung disease', 'Asthma') THEN 'LUNG'
                    WHEN name IN ('Chronic kidney disease', 'Kidney disease (e.g., congenital, CKD)') THEN 'KIDNEY'
                    WHEN name IN ('Diabetes mellitus', 'Thyroid dysfunction (e.g., goitre, hypothyroidism)') THEN 'ENDOCRINE'
                    WHEN name IN ('Epilepsy/seizure disorder', 'Depression or other mental health disorders', 'Substance use disorder (e.g., alcohol, drugs)', 'Neurodevelopmental delay or cerebral palsy') THEN 'NEUROPSYCHIATRIC'
                    WHEN name IN ('Malnutrition', 'Prematurity/low birth weight', 'Genetic syndrome', 'Cancer', 'Other (specify)') THEN 'MEDICAL_CONDITIONS'
                    WHEN name IN ('Previous major surgeries', 'Previous major trauma/fractures', 'Previous transfusions') THEN 'MEDICAL_EVENTS'
                    WHEN name IN ('Current pregnancy', 'Currently lactating', 'Prior pregnancies', 'Previous C-section', 'Previous obstetric complications', 'Menopause') THEN 'OBSTETRIC_HISTORY'
                    ELSE 'ALLERGIES' 
                END,
                CASE
                    WHEN name IN ('Allergies', 'Current medications', 'Vaccinations') THEN 1
                    ELSE 0
                END FROM ${TableNames.DISEASE}
        """)

        // 3. Swap tables
        db.execSQL("DROP TABLE ${TableNames.DISEASE}")
        db.execSQL("ALTER TABLE disease_new RENAME TO ${TableNames.DISEASE}")

        db.execSQL("ALTER TABLE ${TableNames.PATIENT_DISEASE} ADD COLUMN free_text_value TEXT NOT NULL DEFAULT ''")

        db.execSQL("INSERT INTO ${TableNames.DISEASE} (name, sex_specificity, age_specificity, `group`, allows_free_text) VALUES ('Previous hospitalizations', 'ALL', 'ALL', 'MEDICAL_EVENTS', 0)")

        db.execSQL("PRAGMA foreign_keys=ON")
    }
}