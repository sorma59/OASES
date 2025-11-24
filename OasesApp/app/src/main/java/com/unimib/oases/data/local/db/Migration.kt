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