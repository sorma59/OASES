package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Fever
import com.unimib.oases.domain.model.symptom.Symptom

data class TherapyEvaluationParameters(
    val symptoms: Set<Symptom>,
    val findings: Set<Finding>,
    val ageInMonths: Int?,
    val sbp: Int?,
    val dbp: Int?
) {
    val age: Int?
        get() = ageInMonths?.div(12)
}

sealed interface DefinitiveTherapy {

    val description: TherapyText
    val predicate: (TherapyEvaluationParameters) -> Boolean

    sealed interface HighRiskPatientHospitalization: DefinitiveTherapy {

    }

    data object BacterialInfection: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This patient might have a bacterial infection due to presence of blood in the stools.
            Consider giving antibiotics in the suspect of Shigella
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Symptom.DiarrheaBloodyStools in symptoms
        }
    }

    data object CholeraInfection: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This patient might have Cholera infection due to the presence of acute watery diarrhea with abundant rice-water stools and known Cholera outbreak in the region.
            Consider giving antibiotics for Cholera: 
            - Adults: doxycycline 300 mg po single dose of erythromycin 500 mg po qid x 3 days
            - Children doxycycline 2-4 mg/kg single dose or erythromycin 12.5 mg/kg po qid x 3 days
            The most important part of treatment involves rehydration since Cholera can lead to abundant fluid loss. 
            Need case isolation and reporting.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Symptom.DiarrheaBloodyStools in symptoms
            && Symptom.CholeraOutbreak in symptoms
        }
    }

    data object EntamoebaHistolyticaInfection: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with Entamoeba histolytica infection. Treat with antibiotics:
            - Adults: metronidazole 750 mg tid for 5 days (10 days if severe)
            - Children: metronidazole 10 mg/kg tid for 5 days (10 days if severe)
            
            Entamoeba infection treatment should also be considered if no response after 2 lines of antibiotics for Shigella
            Entamoeba can spread via the bloodstream and cause amoebic abscesses in the liver, lung, brain or intestine.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.EntamoebaHistolyticaPositive in findings
        }
    }

    data object GiardiaInfection: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with Giardia infection. Treat with antibiotics:
            - Adults: metronidazole 250 mg po tid for 5 days
            - Children: metronidazole 5 mg/kg po tid for 5 days

            Patients with Giardia usually present with chronic diarrhea and malabsorption.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.GiardiaPositive in findings
        }
    }

    data object CryptosporidiumInfection: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with Cryptosporidium infection. 
            If HIV status is not known, test for HIV. 
            If HIV is positive, start ART.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.CryptosporidiumPositive in findings
        }
    }

    data object IntestinalWorms: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with intestinal worms. Treat with anthelmintics:
            - Adults and children > 2 years: albendazole 400 mg single dose
            - Children < 2 years: albendazole 200 mg single dose.
            - If strongyloides infection: albendazole 400 mg bid for 7 days
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.IntestinalWormsPositive in findings
        }
    }

    data class Malaria(val complaintId: ComplaintId): DefinitiveTherapy {
        override val description = TherapyText(
            buildString {
                appendLine("This is a patient with malaria.")

                if (complaintId == ComplaintId.SEIZURES_OR_COMA) {
                    appendLine("NB: if more than 2 convulsions in 24 hours, impaired consciousness or hypoglycemia, treat as severe malaria")
                }

                appendLine("Start malaria treatment:")
                appendLine("- uncomplicated malaria: artemether/lumefantrine (A/L, Coartem) 20/120 mg bid for 3 days --> 1 tab (< 15 kg) OR 2 tab (15-24 kg) OR 3 tab (25-34 Kg) OR 4 tab (> 34 Kg) twice a day (on day 1, give at 0hr then at 8hr, on days 2 and 3, give every 12 hr)")
                appendLine("- recurrent uncomplicated malaria (new episode < 28 days from treatment with A/L): Dihydroartemisinin/Piperaquine (DHA/PPQ, D-Artepp, P-alaxin) 40/320 mg od for 3 days --> ...")
                appendLine("- severe malaria/inability to take oral drugs: artesunate IV/IM for at least 24 hr --> ...")
            }
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.MalariaRapidDiagnosticTestPositive in findings
            || Finding.BloodSmearPositiveForMalaria in findings
        }
    }

    data object ToxicMegacolonOrIntestinalObstruction: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This patient might have toxic megacolon or intestinal obstruction.
            Hospitalize the patient and immediately ask for surgical consultation.
            Give IV fluids for dehydration and pass a nasogastric tube.
            Consider initiation of broad-spectrum antibiotics (eg. ceftriaxone + metronidazole).
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.ToxicMegacolonOrIntestinalObstruction in findings
        }
    }

    data class HIVInfection(val complaintId: ComplaintId): DefinitiveTherapy {
        override val description = TherapyText(
            buildString {
                appendLine("This is a patient with HIV infection.")

                if (complaintId == ComplaintId.SEIZURES_OR_COMA) {
                    appendLine("If HIV status was not known, consider hospitalization for ART initiation, but before ART initiation test and treat for cryptococcal meningitis.")
                    appendLine("In advanced HIV (CD4 < 100) with subacute fever, headache, focal deficits, seizures, confusion, also consider toxoplasmosis.")
                    appendLine("Consider referral for CT scan or start empiric treatment with trimethoprim-sulfamethoxazole (TMP-SMX 5 mg/kg TMP component IV/PO q12h) for 4-6 weeks after resolution of symptoms, followed by suppressive therapy with TMP-SMX 160mg/800 mg PO bid until CD4 > 200 for 6 months.")
                } else {
                    appendLine("If HIV status was not known, consider hospitalization for ART initiation.")
                }
            }
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.HIVTestPositive in findings
        }
    }

    data object HemolyticUremicSyndrome: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This patient might have hemolytic uremic syndrome (HUS). 
            Consider hospitalization, urinalysis and renal function tests.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.LowPlateletCount in findings
            || Finding.LowHemoglobin in findings
        }
    }

    data object LowHemoglobin: DefinitiveTherapy {
        override val description = TherapyText(
            """
            Consider transfusion if:
            - children: Hb < 5 g/dl --> 10 mL/kg of pRBC or 20 ml/kg WB administered over 2–4 hours
            - adults: Hb < 7 g/dl --> 1 unit (500 ml) of of pRBC or WB
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.LowHemoglobin in findings
        }
    }

    data object DiarrheaHighRiskPatientHospitalization: HighRiskPatientHospitalization {
        override val description = TherapyText(
            """
            This is a high-risk patient in which hospitalization should always be considered for treatment with IV fluids.
            Reassess patient frequently (every 30-60 minutes) to re-classify dehydration and treatment plan.
            As soon as signs of dehydration have disappeared, start fluid maintenance therapy, alternating ORS and water (to avoid hypernatraemia) as much as the patient wants.
            If IV fluids are needed, start fluid maintenance with NS + 5% glucose
            - 100 ml/kg for each Kg from 1 to 10 Kg (1 L)
            - 50 ml/kg for each Kg from 11 to 20 Kg (500 ml)
            - 20 ml/kg for each Kg from 21 to 40 Kg (400 ml)
            NB: If >40 Kg = give 2 L/24 hr
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            symptoms.any {
                it in setOf(
                    Symptom.DiarrheaBloodyStools,
                    Symptom.DiarrheaInTheLastThirtyPlusDays,
                    Symptom.DiarrheaShockOrSevereDehydration,
                    Symptom.DiarrheaMalnourishment,
                    Symptom.Convulsions,
                    Symptom.CholeraOutbreak,
                    Symptom.HivPositive
                )
            }
        }
    }

    data object SeverePneumonia: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with severe pneumonia due to presence of severe respiratory distress. 
            The patient needs hospitalization for IV antibiotics treatment with:
            - Children < 5 yo: Ampicillin 50 mg/kg IV/IM q6h OR benzylpenicillin 50,000 U/kg (2M UI in adults) IV/IM q6h PLUS gentamicin 7.5 mg/kg IV/IM od for ≥5 days
            - Adults: benzylpenicillin 2M UI for ≥5 days
            - If failure of first-line therapy or very severe disease: ceftriaxone 80 mg/kg IV/IM od (adults 2g od) for 7 days
            - If no improvement in 48 h and suspect Staph or empyema/lung abscess: replace ampicillin/benzylpenicillin with cloxacillin 50 mg/kg IV/IM q6h (adults 2g q6h) × 7 days, then switch to oral cloxacillin qid × 3 wks.
            - If aspiration/anaerobes suspected: add metronidazole 7.5 mg/kg q6h (adults 500 mg IV q6h)
            - If HIV+ admit and treat for 10 days as severe pneumonia. If PCP suspicion (>12 months old or clinical/radiological suspicion): add IV/oral cotrimoxazole (TMP 8 mg/kg + SMX 40 mg/kg) q8h × 3 wks and prednisone 2 mg/kg/day divided in 3 doses × 5 days.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->

            val severeRespiratoryDistress = Symptom.DyspneaSevereRespiratoryDistress in symptoms
            val pneumoniaEvidence =
                Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia in findings

            val crackles = Symptom.CracklesAtChestAuscultation in symptoms
            val fever = symptoms.any { it is Fever }

            severeRespiratoryDistress && (pneumoniaEvidence || (crackles && fever))
        }
    }

    data object NonSeverePneumonia: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with pneumonia without high-risk features (no severe respiratory/danger signs/shock).
            The patient can be treated at home with oral antibiotics:
            - children: Amoxicillin 40 mg/kg BID x 5 days
            - adults: Amoxicillin 1 g TID × 5 days
            - If no improvement in 48 h and suspect atypical pathogens (especially in adults): add doxycycline 2 mg/kg q12h (adults 100mg q12h) × 5 days or erythromycin 10–15 mg/kg q6h (adults 500 mg q6h) × 5 days
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->

            val absentSevereRespiratoryDistress = Symptom.DyspneaSevereRespiratoryDistress !in symptoms
            val pneumoniaEvidence = Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia in findings

            val crackles = Symptom.CracklesAtChestAuscultation in symptoms
            val fever = symptoms.any { it is Fever }

            absentSevereRespiratoryDistress && (pneumoniaEvidence || (crackles && fever))
        }
    }

    data object PleuralEffusion: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with pleural effusion. 
            Treat according to suspected underlying cause:
            - if bilateral and associated to pulmonary edema / signs of fluid overload (peripheral edema, JVD): drainage only if large and compromising breathing. Give diuretics: furosemide (oral or IV for faster effect). In adults: furosemide 20–60 mg IV initially, increase as needed by 20-40 mg every 6-8 hours (max 500 mg/day); in children: furosemide 2 mg/kg IV initially, increase by 1–2 mg/kg every 6-8 hours if needed, max 6 mg/kg. Monitor I&O, weight, edema/JVD, HR/RR, RFTs/electrolytes. Also suggest fluid & salt restriction.
            - if unilateral: drain unless very small and send for analysis; repeat if fluid re-accumulates and compromises breathing. If bloody (hemothorax) or purulent (empyema) fluid: insert chest tube and and leave until no longer draining. Also treat for pneumonia / TB based on suspected underlying cause. With empyema use cloxacillin 50 mg/kg IV/IM q6h (adults 2g q6h) × 7 days, then switch to oral cloxacillin qid × 3 wks.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.ChestXRaySuggestiveOfPleuralEffusion in findings
        }
    }

    data object Pneumothorax: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is patient with pneumothorax.
            Drainage is indicated in patients who are clinically unstable, or in those who are stable but have a large pneumothorax (defined radiographically as a visible rim of air >2 cm between the lung margin and chest wall at the level of the hilum), especially if they are symptomatic or have underlying lung disease.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.ChestXRaySuggestiveOfPneumothorax in findings
        }
    }

    data object PresumptiveOrConfirmedTuberculosis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with presumptive/confirmed TB. 
            Hospitalize (isolated room) and start TB treatment if diagnosis confirmed.
            If drug-susceptible: HRZE × 2 months, then HR × 4 months.
            If latent TB: H x 6 months. Doses:
            - isoniazid (H) 10 mg/kg (5 mg/kg in adults, max 300 mg)
            - rifampicin (R) 15 mg/kg (10 mg/kg adults, max 600 mg)
            - pyrazinamide (Z) 35 mg/kg (20 mg/kg adults, max 2500 mg)
            - ethambutol (E) 20 mg/kg
            - add vitamin B6 25 mg/day with isoniazid where risk of neuropathy.
            All drugs are given once a day.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.ChestXRaySuggestiveOfTuberculosis in findings
            || Finding.TuberculosisGeneXpertOrMicroscopyPositive in findings
        }
    }

    data object PulmonaryEdema: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with pulmonary edema.
             Give diuretics: furosemide (oral or IV for faster effect).
            - adults: furosemide 20–60 mg IV initially, increase by 20-40 mg every 6 to 8 hours (max 500 mg/day).
            - children: furosemide 2 mg/kg IV initially, increase by 1–2 mg/kg every 6 to 8 hours if needed (max 6 mg/kg)
            - Monitor I&O, weight, edema/JVD, HR/RR, RFTs/electrolytes. Also suggest fluid & salt restriction.
            - In patients with heart failure, especially if concomitant hypertension, also start therapy with low dose Captopril 12.5-25mg tid, increased over 2-4 weeks up to a max of 50 mg tid.
            - If heart failure cause is not known, send for echocardiography to check for valvular heart diseases or other reversible causes of heart failure
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.ChestXRaySuggestiveOfPulmonaryEdema in findings
        }
    }

    data object PericardialEffusionOrPericardialTamponade: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with pericardial effusion / tamponade.
            If abundant fluid, consider performing pericardial tapping; also test for TB.
            If suspected pericarditis, start ibuprofene 600 mg every 8 hours.
            """.trimMargin()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponade in findings
        }
    }

    data object MyocardialInfarction: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with myocardial infarction.
            - Consider transferring the patient to higher-care level
            - Give immediately aspirin 225 mg to chew then continue aspirin 75 mg od
            - Start atenolol 25-100 mg od if no hypotension or bradycardia
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.EchoCardiographySuggestiveOfMyocardialInfarction in findings
        }
    }

    data object AtrialFibrillation: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with atrial fibrillation.
            If no bradycardia/hypotension, start atenolol 25-50 mg od up to 100 mg od to decrease heart rate.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.EchoCardiographySuggestiveOfAtrialFibrillation in findings
        }
    }

    data object DeepVeinThrombosisOrSuspectedPulmonaryEmbolism: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with deep vein thrombosis / suspected pulmonary embolism.
            - Consider transferring the patient to higher-care level
            - Treat with enoxaparin 1 mg/kg bid for 3 months
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.VenousUltraSoundSuggestiveOfDeepVeinThrombosis in findings
        }
    }

    data object SuspectedSevereAcuteAsthma: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected severe acute asthma (recurrent wheeze / cough, difficulty in breathing with chest tightness, no fever, good response to bronchodilators, worse at night/early morning or with exertion).
            Consider hospitalization for treatment with:
            - inhaled salbutamol: inhaler+spacer (2 puffs, up to 6 puffs if ≤5 y and up to 10 puffs if >5 y) OR nebulized (at 6–9 L/min; 2.5 mg in children / 5 mg in adults in 2–4 ml saline) up to 6 times/day
            - If salbutamol not effective: IV magnesium sulfate (MgSO4 50%) 50 mg/kg (maximum 2g) by slow infusion over 20 minutes
            - systemic steroids: prednisolone 1 mg/kg (max 60 mg/day) PO ×3–5 days OR hydrocortisone 4 mg/kg IV q6h (100 mg IV q6h in adults) x3-5 days
            """.trimIndent()
        )

        override val predicate = { params: TherapyEvaluationParameters ->
            params.age?.let {
                it >= 2
                && Symptom.DyspneaWheezing in params.symptoms
                && Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia !in params.findings
                && Symptom.DyspneaSevereRespiratoryDistress in params.symptoms
            } ?: false
        }
    }

    data object SuspectedAcuteAsthma: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected acute asthma (recurrent wheeze / cough, difficulty in breathing with chest tightness, no fever, good response to bronchodilators, worse at night/early morning or with exertion) without high-risk features.
            Discharge with:
            - salbutamol inhaler (+ spacer in children) to be used as a rescue therapy in case of respiratory distress: 2 puffs (up to 6 puffs if ≤5 y and up to 10 puffs if >5 y) up to 6 times/day
            - systemic steroids: prednisolone 1 mg/kg (max 60 mg/day) PO ×3–5 days
            - inhaled corticosteroids maintenance therapy if available
            """.trimIndent()
        )

        override val predicate = { params: TherapyEvaluationParameters ->
            params.age?.let {
                it >= 2
                && Symptom.DyspneaWheezing in params.symptoms
                && Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia !in params.findings
                && Symptom.DyspneaSevereRespiratoryDistress !in params.symptoms
            } ?: false
        }
    }

    data object SuspectedChronicObstructivePulmonaryDiseaseExacerbation: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected COPD exacerbation (exacerbation of chronic dyspnea, cough, sputum production, wheezing and tight chest; with exposure to tobacco or indoor air pollution; often triggered by RTI).
            - inhaled salbutamol: inhaler (2 puffs, up to 10 puffs) OR nebulized (at 6–9 L/min; 5 mg in 2–4 ml saline) up to 6 times/day
            - If salbutamol not effective: IV magnesium sulfate (MgSO4 50%) 50mg/kg (maximum 2g) by slow infusion over 20 minutes
            - systemic steroids: prednisolone 1 mg/kg (max 60 mg/day) PO ×3–5 days OR hydrocortisone 100 mg IV q6h x3-5 days
            - if purulent sputum, fever or suspicious infection also add antibiotics: amoxicillin 500 mg tid for 5 days 
            """.trimIndent()
        )

        override val predicate = { params: TherapyEvaluationParameters ->
            params.age?.let {
                it > 18
                && Symptom.DyspneaWheezing in params.symptoms
                && Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia !in params.findings
                && Symptom.SmokingOrExposedToSmoke in params.symptoms
            } ?: false
        }
    }

    data object SuspectedBronchiolitis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected bronchiolitis (mainly children < 1 year, usually associated to URTI symptoms).
            Only supportive care is required.
            Hospitalize if the patient presents with severe respiratory failure requiring oxygen therapy or if the patient presents with lethargy, convulsions or inability to breastfeed / drink.
            NB: bronchodilators and steroids not effective—avoid. Do not give antibiotics unless evidence of pneumonia.
            """.trimIndent()
        )

        override val predicate = { params: TherapyEvaluationParameters ->
            params.age?.let {
                it < 2
                && Symptom.DyspneaWheezing in params.symptoms
                && Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia !in params.findings
            } ?: false
        }
    }

    data object SuspectedPertussis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected pertussis (URTI followed by paroxysmal cough + whoop/vomiting ± apnea/cyanosis; in older patients can present as chronic cough).
            Admit if  < 6 months or severe disease. 
            Treat all with:
            - oral erythromycin 12.5 mg/kg qid × 10 days OR azithromycin 10 mg/kg (max 500 mg) od × 5 days
            - give DTP booster/vaccination
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Symptom.DyspneaParoxysmalCoughWithWhoopsOrCentralCyanosisOrVomiting in symptoms
            && Symptom.Unvaccinated in symptoms
        }
    }

    data object SickleCellDiseaseAndSuspectedAcuteChestSyndrome: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with sickle cell disease and suspected acute chest syndrome (fever, chest pain, difficulty in breathing, low SpO2, cough, wheezing, new infiltrates at CXR).
            Hospitalize the patient and treat with:
            - antibiotics: ceftriaxone 50–100 mg/kg IV (max 2 gr IV) once daily for 7-10 days + azitromycin 10 mg/kg (max 500 mg) PO on day one, then 5 mg/kg (max 250 mg) PO for 4 days
            - pain medications: morphine 0.05–0.1 mg/kg IV (2.5-5 mg IV in adults) every 2–4 hours, titrate to effect
            - supportive therapy with oxygen, salbutamol, transfusions, incentive spirometry
            - after recovery, consider hydroxyurea 20 mg/kg
            NB: avoid fluid overload!
            """.trimIndent()
        )

        override val predicate = { (symptoms): TherapyEvaluationParameters ->
            Symptom.SickleCellDisease in symptoms
        }
    }

    data object StridorAndNoForeignBodyObstruction: DefinitiveTherapy {
        override val description = TherapyText(
            """
            In patients presenting with stridor and no foreign body obstruction, consider the following differential diagnosis:
            - Viral croup (< 2 y; barky cough/hoarse voice, fever ± distress; often associated to measles or other viral infections): supportive if mild; if severe → dexamethasone 0.6 mg/kg or prednisone 1 mg/kg + nebulized adrenaline 2 ml (repeat hourly if needed).
            - Diphtheria (unvaccinated children; grey pharyngeal membrane, bull neck; can cause muscle paralysis and myocarditis): antitoxin 40,000 IU IM/IV + procaine benzylpenicillin 50,000 IU/kg IM od (max 1.2 MIU) × 10 days.
            - Epiglottitis (sore throat, fever, difficult in swallowing with drooling of saliva): ceftriaxone 80 mg/kg od × 5 days + supportive care.
            """.trimIndent()
        )

        override val predicate = { (symptoms): TherapyEvaluationParameters ->
            Symptom.DyspneaAirwayObstruction in symptoms
        }
    }

    data object DyspneaHighRiskPatientHospitalization: HighRiskPatientHospitalization {
        override val description = TherapyText(
            """
            This is a high-risk patient in which hospitalization should always be considered.
            Reassess patient frequently and provide supportive therapy with:
            - oxygen to keep SpO2 > 90%
            - paracetamol 1 gr PO/IV/PR (children: 15 mg/kg or 7.5mg/kg if <10 kg) if fever/pain (every 4-6 hr)
            - inhaled salbutamol if wheeze: inhaler+spacer (2 puffs, up to 6 puffs if ≤5 y and up to 10 puffs if >5 y) OR nebulized (at 6–9 L/min; 2.5 mg in children / 5 mg in adults in 2–4 ml saline); may be repeated as needed up to 6 times/day
            - suction thick secretions
            - encourage oral fluids uptake and feeding. If the patient is unable to drink/breastfeed, start IV fluid maintenance with NS + 5% glucose:
            - 100 ml/kg for each Kg from 1 to 10 Kg (1 L)
            - 50 ml/kg for each Kg from 11 to 20 Kg (500 ml)
            - 20 ml/kg for each Kg from 21 to 40 Kg (400 ml)
            NB: If >40 Kg = give 2 L/24 hr
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            symptoms.any {
                it in setOf(
                    Symptom.DyspneaSevereRespiratoryDistress,
                    Symptom.InabilityToBreastfeedOrDrink,
                    Symptom.Convulsions,
                    Symptom.Lethargy,
                    Symptom.Shock,
                    Symptom.DyspneaAirwayObstruction
                )
            }
        }
    }

    data object SuspectedOrConfirmedBacterialMeningitis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected / confirmed bacterial meningitis.
            Start antibiotics immediately (do not delay for LP if raised ICP signs).
            - < 1 month: Ampicillin 50–100 mg/kg (BID if < 7 days; TID if ≥ 7 days) + gentamicin 2.5 mg/kg BID × 3 wks; or ceftriaxone 50 mg/kg q12h (< 7 d) / 75 mg/kg q12h (≥7 d) + ampicillin × 3 wks.
            - > 1 month & adults: ceftriaxone 50 mg/kg q12h (adults 2 g BID) OR 100 mg/kg OD × 7–10 d
            - Dexamethasone 0.15 mg/kg q6h × 2–4 d, ideally 10–20 min before or with first antibiotic dose can be added

            NB: If suspected viral etiology instead (eg. clear CSF with lymphocytic pleocytosis): acyclovir 10 mg/kg IV every 8 hours for 14–21 days in adults, and 20 mg/kg IV every 8 hours for 21 days in children
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Symptom.FeverAbove38Degrees in symptoms
            && Symptom.StiffNeck in symptoms
            || Symptom.HeadacheOrCervicalPain in symptoms
            || Symptom.BulgingFontanelle in symptoms
            || Symptom.PetechialRashOrPurpura in symptoms
            || Symptom.ResponsiveToPainLevelOfConsciousness in symptoms
            || Symptom.ResponsiveToVoiceLevelOfConsciousness in symptoms
            || Symptom.UnresponsiveLevelOfConsciousness in symptoms
            || Finding.CerebrospinalFluidSuggestiveOfBacterialInfection in findings
        }
    }

    data object TuberculosisMeningitis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with TB meningitis. Hospitalize (isolated room) and
            - start TB treatment
            - add dexamethasone 0.6 mg/kg/day for 2–3 wks, then taper over 2–3 wks.
            If drug-susceptible: HRZE × 2 months, then HR × 10 months. Doses:
            - isoniazid (H) 10 mg/kg (5 mg/kg in adults, max 300 mg)
            - rifampicin (R) 15 mg/kg (10 mg/kg adults, max 600 mg)
            - pyrazinamide (Z) 35 mg/kg (20 mg/kg adults, max 2500 mg)
            - ethambutol (E) 20 mg/kg
            - add vitamin B6 25 mg/day with isoniazid where risk of neuropathy.
            All drugs are given once a day.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.TuberculosisGeneXpertOrMicroscopyPositive in findings
            || Finding.CerebrospinalFluidSuggestiveOfTuberculosisMeningitis in findings
        }
    }

    data object CryptoCoccalMeningitis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with cryptococcal meningitis. 
            Treat with:
            - Induction (first 10 weeks): Fluconazole 12 mg/kg/day PO (max 1200 mg/day) × 10 weeks.
            - Consolidation (next 8 weeks): Fluconazole 6–12 mg/kg/day PO (max 800 mg/day) × 8 weeks.
            - Maintenance (secondary prophylaxis): Fluconazole 6 mg/kg/day PO (max 200 mg/day) until immune recovery per HIV program.
            - ART timing: start or re-start ART 4–6 weeks after beginning antifungal therapy.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.CerebrospinalFluidSuggestiveOfCryptococcalMeningitis in findings
        }
    }

    data object Neurosyphilis: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with neurosyphilis. 
            Treat with:
            - benzylpenicillin 4 million IU IV every 4 hours for 10-14 days (preferred)
            - followed by Benzathine penicillin 2.4 million IU IM weekly for 3 weeks
            - treat partner(s), abstain from sex during treatment and 10 days after
            - as an alternative, if penicillin allergy, ceftriaxone 2 g IV or IM daily for 10–14 days may be considered, but penicillin desensitization is preferred if possible
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Finding.CerebrospinalFluidSuggestiveOfNeurosyphilis in findings
        }
    }

    data object Epilepsy: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with epilepsy.
            - If not on therapy, start antiepileptic drugs if ≥2 seizures in last year on different days. Refer to epilepsy clinic for follow-up.
            - If already on antiepileptic drugs and compliant: increase dose and refer to epilepsy clinic.

            Antiepileptic drugs doses:
            - Carbamazepine: start 50–100 mg BID, ↑ by 50 mg q1–2 wks to 400–1440 mg/day. Children 5 mg/kg/day, ↑ by 10–20 mg/kg/day.
            - Phenobarbital: start 1 mg/kg (max 60 mg) OD evening; may ↑ to 2 mg/kg (max 120 mg) after 2 wks; up to 3 mg/kg/day. Children: start 2 mg/kg/day, then 3–6 mg/kg/day as needed.
            - Phenytoin: start 150–200 mg/day (1–2 doses), maintenance 200–400 mg/day. Children start 3–4 mg/kg/day, maintenance 3–8 mg/kg/day (max 300 mg/day), ↑ by 25–30 mg q2 wks.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Symptom.EpilepsyOrHistoryOfRecurrentUnprovokedSeizures in symptoms
        }
    }

    data object SuspectedEclampsiaForPregnancyBeyondTwentyWeeksAndSeizures: DefinitiveTherapy {
        override val description = TherapyText(
            """
            Suspect eclampsia in patients with seizures after 20 weeks of gestation, associated with hypertension (BP > 140/90) and proteinuria.
            Other finding include low PLTs, high RFT/LFT, edema. Always exclude malaria in pregnancy.
            Eclampsia is a medical emergency!
            - Call for OBGYN: deliver the baby by the safest and fastest means available (within 6-12 hours).
            - MgSO4: 4 g IV loading dose over 20 min (draw 8 mL of MgSO4 50% 5 g/10 ml and add 12 mL of water for injection/NS, then 5 g IM in each buttock (10 mL of MgSO4 50% solution, undiluted) with 1 mL of 2% lidocaine in the same syringe. After this continue with 5 g IM (10 mL of MgSO4 50% solution) every 4 hours in alternate buttocks for 24 hours from the time of loading dose or after the last convulsion, whichever comes first.
            - Hydralazine: 5–10 mg IV bolus for BP >160/110 mmHg every 30 minutes until diastolic is BP is down to &lt;100 mmHg
            - Nifedipine retard: 20 mg 12 hourly until delivery after controlling BP with hydralazine
            """.trimIndent()
        )

        override val predicate = { params: TherapyEvaluationParameters ->
            Symptom.SeizuresOrComaPregnancyBeyondTwentyWeeks in params.symptoms
            && (Finding.UrineAnalysisWithProteinuria in params.findings
                || params.sbp?.let { it >= 140 } ?: false
                || params.dbp?.let { it >= 90 } ?: false
            )
        }
    }

    data object HeadTrauma: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with head trauma. 
            Immobilize spine, limit fluids to 2/3 of usual maintenance, elevate head 30°, avoid sedation, keep SBP > 90 mmHg.
            Consider CT referral if not improving.
            If open head injury: start ceftriaxone 100 mg/kg (adults 2 g) IV.
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            Symptom.HeadInjury in symptoms
            || Finding.SkullXRaySuggestiveOfSkullFracture in findings
        }
    }

    data object PossibleAlcoholAbuse: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with possible alcohol abuse:
            - Check/treat hypoglycemia & hypothermia, encourage nutrition
            - Give thiamine 100 mg IV/IM that can be continue for 3-5 days od (if discharged, switch to 100 mg PO for 3-5 days od)
            - For withdrawal (tremor, agitation, tachycardia, hypertension, hallucinations, delirium): diazepam 10–20 mg IV q10 min until appropriate sedation is achieved; if not effective consider phenobarbital 100–200 mg
            """.trimIndent()
        )

        override val predicate = { (symptoms): TherapyEvaluationParameters ->
            Symptom.SuspectOfAlcoholUseOrWithdrawal in symptoms
        }
    }

    data object PossibleStrokeOrPossibleIntracranialHemorrhage: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with possible stroke/intracranial hemorrhage.
            Consider referring for CT if hemorrhagic stroke suspected.
            Do ECG (rule out AF), echo (cardiac thrombus) when available.
            """.trimIndent()
        )

        override val predicate = { (symptoms): TherapyEvaluationParameters ->
            Symptom.FocalNeurologicalDeficit in symptoms
        }
    }

    data object SuspectedPoisoningOrSuspectedIntoxication: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with suspected poisoning/intoxication.
            - Remove contamination: take off contaminated clothes; wash skin/hair with soap and water; irrigate eyes if exposed.
            - Supportive treatment: position airway, suction as needed, O₂ to keep SpO₂ > 90%, IV access, fluids if hypotensive, BDZ if seizures/agitation. Check RBS (treat hypoglycemia), temperature, continuous SpO₂/HR/BP monitoring.
            - Decontamination (if ingestion): Activated charcoal 0.5 g/kg (max 50 g) within 4 hours only if GCS ≥ 9 or airway protected (intubated).

            Suspect organophosphate poisoning (fertilizers/pesticides ingestion) with typical cholinergic signs: bronchospasm/bronchorrhea, lacrimation, urination, diarrhea, vomiting, salivation, sweating, weakness, fasciculations, confusion, agitation, coma, respiratory depression. Usually bradycardia/hypotension/myosis (but tachycardia/hypertension/mydriasis can occur). Treat with:
            - Atropine boluses: initial dose 2–4 mg IV (0.02–0.05 mg/kg IV in children), double the dose every 3–5 min until endpoints achieved (clear lungs with no secretions and improved SpO2, HR ≥ 80 bpm, SBP ≥ 90 mmHg)
            - Atropine maintenance infusion (after endpoints reached): 0.05 mg/kg/hour, titrate down slowly over 24 hours to keep lungs dry and vitals stable. If atropine toxicity (agitation, delirium, fever, absent bowel sounds): stop infusion for 60 min, then restart at a lower rate.
            - Pralidoxime if muscle weakness, fasciculations, respiratory failure, or severe OP poisoning: adults = loading with 30 mg/kg IV over 30 min (max 2 g), then 8 mg/kg/hour infusion; children = loading with 25–50 mg/kg IV over 30 min (max 2 g), then 10–20 mg/kg/hour infusion.
            - Salbutamol (inhaled/nebulized) if bronchospasm
            """.trimIndent()
        )

        override val predicate = { (symptoms): TherapyEvaluationParameters ->
            Symptom.SuspectOfDrugOrToxinIngestion in symptoms
        }
    }

    data object RenalFailureAndPossibleUremicEncephalopathy: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with renal failure and possibile uremic encephalopathy.
            Supportive care is paramount, including careful management of fluid and electrolyte balance, correction of acidosis, and avoidance of nephrotoxic medications.
            Consider referral for dyalisis.
            """.trimIndent()
        )

        override val predicate = { (_, findings): TherapyEvaluationParameters ->
            Finding.CreatinineOrUreaElevated in findings
        }
    }

    data object LiverFailureAndPossibleHepaticEncephalopathy: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with liver failure and possible hepatic encephalopathy.
            - Start lactulose administered orally at an initial dose of 25–30 mL two to three times daily and titrated to achieve 2–3 soft bowel movements per day. In patients unable to take oral medications, lactulose enemas may be used.
            - For those who do not respond adequately to lactulose, rifaximin (550 mg orally twice daily) can be added
            - Correct precipitating factors—such as infection, gastrointestinal bleeding, electrolyte disturbances, dehydration, and constipation        
            """.trimIndent()
        )

        override val predicate = { (_, findings): TherapyEvaluationParameters ->
            Finding.ASTOrALTOrBilirubinElevated in findings
        }
    }

    data object ElectrolytesAlterations: DefinitiveTherapy {
        override val description = TherapyText(
            """
            This is a patient with electrolytes alterations. Treat based on the altered electrolyte:
            - Hyponatremia (serum Na < 120 mEq/L or symptomatic): in patients with seizures, somnolence, or cardiorespiratory distress, initiate emergency therapy with 3% sodium chloride. Administer a 100–150 mL IV bolus over 10–20 minutes, repeat up to 2–3 times as needed to increase serum sodium by 4–6 mEq/L within 1–2 hours. Do not exceed 10 mEq/L increase in 24 hours. In less severe patients, correct with isotonic saline (0.9% NaCl).
            - Hypernatremia (serum Na > 160 mEq/L or symptomatic): correct with hypotonic fluids (e.g., 0.45% saline or 5% dextrose in water) at a rate to lower serum sodium by no more than 10–12 mEq/L in 24 hours.
            - Hypocalcemia (serum Ca < 7.5 mg/dL or symptomatic): give IV calcium gluconate 1-3 gr (10-30 mL of 10% solution over 10 minutes; children 0.5 ml/kg), repeat as needed. Associated with coexisting hypokalemia and hypomagnesemia.
            - Hypomagnesemia (serum Mg < 1.2 mg/dL or symptomatic): IV magnesium sulfate (1–2 g over 30–60 minutes), repeat as needed. Associated with coexisting hypokalemia and hypocalcemia.
            """.trimIndent()
        )

        override val predicate = { (_, findings): TherapyEvaluationParameters ->
            Finding.AlteredElectrolytes in findings
        }
    }

    data object SeizuresOrComaHighRiskPatientHospitalization: HighRiskPatientHospitalization {
        override val description = TherapyText(
            """
            This is a high-risk patient in which hospitalization should always be considered.
            If the patient neurological status is not improving after 24 hours (persistent altered consciousness, focal neurological signs, seizures refractory to therapy), consider referral for CT scan to exclude brain hemorrhage, cerebral abscess, brain tumor, toxoplasmosis, neurocysticercosis.
            Reassess patient frequently and provide supportive therapy with
            - protect airways if GCS < 8 and give oxygen to keep SpO2 > 90%
            - evaluate need of antiepileptic maintenance therapy with phenobarbital
            - paracetamol 1 gr PO/IV/PR (children: 15 mg/kg or 7.5mg/kg if < 10 kg) if fever/pain (every 4-6 hr)
            - encourage oral fluids uptake and feeding. If the patient is unable to drink/breastfeed, start IV fluid maintenance with NS + 5% glucose:
            - 100 ml/kg for each Kg from 1 to 10 Kg (1 L)
            - 50 ml/kg for each Kg from 11 to 20 Kg (500 ml)
            - 20 ml/kg for each Kg from 21 to 40 Kg (400 ml)
            NB: If >40 Kg = give 2 L/24 hr
            """.trimIndent()
        )

        override val predicate = { (symptoms, findings): TherapyEvaluationParameters ->
            symptoms.any {
                it in setOf(
                    Symptom.InabilityToBreastfeedOrDrink,
                    Symptom.StatusEpilepticus,
                    Symptom.Shock,
                    Symptom.UnequalPupils,
                    Symptom.Opisthotonus,
                    Symptom.FocalNeurologicalDeficit,
                    Symptom.ResponsiveToVoiceLevelOfConsciousness,
                    Symptom.ResponsiveToPainLevelOfConsciousness,
                    Symptom.UnresponsiveLevelOfConsciousness,
                    Symptom.SeizuresOrComaPregnancyBeyondTwentyWeeks,
                    Symptom.PetechialRashOrPurpura,
                    Symptom.StiffNeck,
                    Symptom.BulgingFontanelle,
                    Symptom.HeadInjury,
                    Symptom.PoisoningIntoxication,
                    Symptom.CurrentPregnancy,
                    Symptom.SickleCellDisease,
                    Symptom.HivPositive
                )
            } || findings.any {
                it in setOf(
                    Finding.HIVTestPositive,
                    Finding.TuberculosisGeneXpertOrMicroscopyPositive,
                    Finding.CerebrospinalFluidSuggestiveOfBacterialInfection,
                    Finding.CerebrospinalFluidSuggestiveOfTuberculosisMeningitis,
                    Finding.CerebrospinalFluidSuggestiveOfCryptococcalMeningitis,
                    Finding.CerebrospinalFluidSuggestiveOfNeurosyphilis,
                    Finding.SkullXRaySuggestiveOfSkullFracture
                )
            }
        }
    }
}