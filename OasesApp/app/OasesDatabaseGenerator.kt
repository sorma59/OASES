import java.sql.DriverManager

fun main() {
    val dbName = "oases.db"
    val dbVersion = 1 // Update when db version changes
    val conn = DriverManager.getConnection("jdbc:sqlite:$dbName")
    val stmt = conn.createStatement()

    /*
        * const val DISEASE = "diseases"
        const val PATIENT_DISEASE = "patients_diseases"
        const val PATIENT = "patients"
        const val USER = "users"
        const val VISIT = "visits"
        const val VISIT_VITAL_SIGN = "visits_vital_signs"
        const val VITAL_SIGN = "vital_signs"

        enum class SexSpecificity(val displayName: String) {
            MALE("Male"),
            FEMALE("Female"),
            ALL("All")
        }

        enum class AgeSpecificity(val displayName: String) {
            CHILD("Child"),
            ADULT("Adult"),
            ALL("All")
        }
        *
        enum class NumericPrecision(val displayName: String){
            INTEGER("Whole number"),
            FLOAT("Decimal number")
        }
    */



    stmt.execute("DROP TABLE IF EXISTS vital_signs")
    stmt.execute("""
        CREATE TABLE vital_signs (
            name TEXT NOT NULL PRIMARY KEY,
            acronym TEXT NOT NULL,
            unit TEXT NOT NULL,
            precision TEXT NOT NULL
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS diseases")
    stmt.execute("""
        CREATE TABLE diseases (
            name TEXT NOT NULL PRIMARY KEY,
            sex_specificity TEXT NOT NULL,
            age_specificity TEXT NOT NULL
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS patients")
    stmt.execute("""
        CREATE TABLE patients (
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
            status TEXT NOT NULL,
            image BLOB DEFAULT NULL
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS visits")
    stmt.execute("""
        CREATE TABLE visits (
            id TEXT NOT NULL PRIMARY KEY,
            patient_id TEXT NOT NULL,
            triage_code TEXT NOT NULL,
            date TEXT NOT NULL,
            description TEXT NOT NULL,
            FOREIGN KEY(patient_id) REFERENCES patients(id) ON UPDATE NO ACTION ON DELETE CASCADE
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS visits_vital_signs")
    stmt.execute("""
        CREATE TABLE visits_vital_signs (
            visit_id TEXT NOT NULL,
            vital_sign_name TEXT NOT NULL,
            timestamp TEXT NOT NULL,
            value DOUBLE NOT NULL,
            PRIMARY KEY(visit_id, vital_sign_name, timestamp),
            FOREIGN KEY(visit_id) REFERENCES visits(id) ON UPDATE NO ACTION ON DELETE CASCADE,
            FOREIGN KEY(vital_sign_name) REFERENCES vital_signs(name) ON UPDATE NO ACTION ON DELETE CASCADE
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS patients_diseases")
    stmt.execute("""
        CREATE TABLE patients_diseases (
            patient_id TEXT NOT NULL,
            disease_name TEXT NOT NULL,
            is_diagnosed INTEGER NOT NULL,
            diagnosis_date TEXT NOT NULL,
            additional_info TEXT NOT NULL,
            PRIMARY KEY(patient_id, disease_name),
            FOREIGN KEY(patient_id) REFERENCES patients(id) ON UPDATE NO ACTION ON DELETE CASCADE,
            FOREIGN KEY(disease_name) REFERENCES diseases(name) ON UPDATE NO ACTION ON DELETE CASCADE
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS triage_evaluations")
    stmt.execute("""
        CREATE TABLE triage_evaluations (
            visit_id TEXT NOT NULL,
            red_symptom_ids TEXT NOT NULL,
            yellow_symptom_ids TEXT NOT NULL,
            
            PRIMARY KEY(visit_id),
            FOREIGN KEY(visit_id) REFERENCES visits(id) ON UPDATE NO ACTION ON DELETE CASCADE
        )
    """.trimIndent())

    stmt.execute("DROP TABLE IF EXISTS malnutrition_screenings")
    stmt.execute("""
        CREATE TABLE malnutrition_screenings (
            visit_id TEXT NOT NULL,
            weight_in_kg DOUBLE NOT NULL,
            height_in_cm DOUBLE NOT NULL,
            muac_in_cm DOUBLE NOT NULL,
            muac_category TEXT NOT NULL,
            bmi DOUBLE NOT NULL,
            
            PRIMARY KEY(visit_id),
            FOREIGN KEY(visit_id) REFERENCES visits(id) ON UPDATE NO ACTION ON DELETE CASCADE
        )  
    """.trimIndent())

    val vitalSigns = listOf(
        listOf("Systolic Blood Pressure", "SBP", "mmHg", "INTEGER"),
        listOf("Diastolic Blood Pressure", "DBP", "mmHg", "INTEGER"),
        listOf("Heart Rate", "HR", "bpm", "INTEGER"),
        listOf("Oxygen Saturation", "SpO2", "%", "INTEGER"),
        listOf("Respiratory Rate", "RR", "bpm", "INTEGER"),
        listOf("Temperature", "Temp", "°C", "FLOAT"),
        listOf("Rapid Blood Sugar", "RBS", "mmol/L", "FLOAT")
    )

    val insertVitalSignsStmt = conn.prepareStatement("INSERT INTO vital_signs (name, acronym, unit, precision) VALUES (?, ?, ?, ?)")

    for ((name, acronym, unit, precision) in vitalSigns) {

        insertVitalSignsStmt.setString(1, name)
        insertVitalSignsStmt.setString(2, acronym)
        insertVitalSignsStmt.setString(3, unit)
        insertVitalSignsStmt.setString(4, precision)
        insertVitalSignsStmt.executeUpdate()
        println("✔ Vital sign added: $name $acronym $unit $precision")
    }

    val diseases = listOf(
        Triple("HIV/AIDS", "ALL", "ALL"),
        Triple("Tuberculosis", "ALL", "ALL"),
        Triple("STIs (e.g., syphilis, urethritis, cervicitis/PID)", "ALL", "ADULTS"),
        Triple("Malaria (recurrent/severe)", "ALL", "ALL"),
        Triple("Pneumonia (recurrent/severe)", "ALL", "ALL"),

        Triple("Sickle cell disease or trait", "ALL", "ALL"),
        Triple("Other chronic anaemia", "ALL", "ALL"),
        Triple("Bleeding disorder/coagulopathy", "ALL", "ALL"),
        Triple("Peptic ulcer disease/Gastritis/GERD", "ALL", "ALL"),
        Triple("Liver disease (e.g., HBV, HCV, cirrhosis)", "ALL", "ADULTS"),

        Triple("Heart disease (e.g., congenital, rheumatic, ischemic, arrhythmic)", "ALL", "ADULTS"),
        Triple("Hypertension", "ALL", "ADULTS"),
        Triple("Asthma or chronic lung disease", "ALL", "ADULTS"),
        Triple("Chronic kidney disease", "ALL", "ADULTS"),
        Triple("Diabetes mellitus", "ALL", "ALL"),

        Triple("Thyroid dysfunction (e.g., goitre, hypothyroidism)", "ALL", "ALL"),
        Triple("Stroke or TIA", "ALL", "ADULTS"),
        Triple("Epilepsy/seizure disorder", "ALL", "ALL"),
        Triple("Depression or other mental health disorders", "ALL", "ADULTS"),
        Triple("Substance use disorder (e.g., alcohol, drugs)", "ALL", "ADULTS"),

        Triple("Cancer", "ALL", "ALL"),
        Triple("Other (specify)", "ALL", "ALL"),
        Triple("Previous major surgeries", "ALL", "ALL"),
        Triple("Previous major trauma/fractures", "ALL", "ALL"),
        Triple("Previous transfusions", "ALL", "ALL"),

        Triple("Allergies", "ALL", "ALL"),
        Triple("Current medications", "ALL", "ALL"),
        Triple("Vaccinations", "ALL", "ALL"),

        Triple("Current pregnancy", "FEMALE", "ADULTS"),
        Triple("Currently lactating", "FEMALE", "ADULTS"),
        Triple("Prior pregnancies", "FEMALE", "ADULTS"),
        Triple("Previous C-section", "FEMALE", "ADULTS"),
        Triple("Previous obstetric complications", "FEMALE", "ADULTS"),
        Triple("Menopause", "FEMALE", "ADULTS"),

        Triple("Congenital infections (e.g., CMV, toxoplasmosis, syphilis)", "ALL", "CHILDREN"),
        Triple("Liver disease (e.g., HBV, HCV, congenital)/jaundice", "ALL", "CHILDREN"),
        Triple("Heart disease (e.g., congenital/rheumatic)", "ALL", "CHILDREN"),
        Triple("Asthma", "ALL", "CHILDREN"),
        Triple("Kidney disease (e.g., congenital, CKD)", "ALL", "CHILDREN"),

        Triple("Neurodevelopmental delay or cerebral palsy", "ALL", "CHILDREN"),
        Triple("Genetic syndrome", "ALL", "CHILDREN"),
        Triple("Malnutrition", "ALL", "CHILDREN"),
        Triple("Prematurity/low birth weight", "ALL", "CHILDREN"),
    )

    val insertDiseasesStmt =
        conn.prepareStatement("INSERT INTO diseases (name, sex_specificity, age_specificity) VALUES (?, ?, ?)")

    for ((name, sexSpecificity, ageSpecificity) in diseases) {
        insertDiseasesStmt.setString(1, name)
        insertDiseasesStmt.setString(2, sexSpecificity)
        insertDiseasesStmt.setString(3, ageSpecificity)
        insertDiseasesStmt.executeUpdate()
        println("✔ Disease added: $name $sexSpecificity $ageSpecificity")
    }

    stmt.executeUpdate("PRAGMA oases_version = $dbVersion;")

    conn.close()
    println("✅ Database '$dbName' created with ${vitalSigns.size} vital signs and ${diseases.size} diseases.")
}