package org.openmrs.module.pihcore.deploy.bundle.core;

import org.openmrs.GlobalProperty;
import org.openmrs.module.coreapps.CoreAppsConstants;
import org.openmrs.module.emr.EmrConstants;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.htmlformentry.HtmlFormEntryConstants;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.namephonetics.NamePhoneticsConstants;
import org.openmrs.module.pihcore.config.Config;
import org.openmrs.module.pihcore.config.ConfigDescriptor;
import org.openmrs.module.pihcore.deploy.bundle.core.concept.AllergyConcepts;
import org.openmrs.module.pihcore.deploy.bundle.core.concept.CommonConcepts;
import org.openmrs.module.registrationcore.RegistrationCoreConstants;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.visitdocumentsui.VisitDocumentsConstants;
import org.openmrs.ui.framework.UiFrameworkConstants;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GlobalPropertiesBundle extends AbstractMetadataBundle {

    @Autowired
    private Config config;

    private static final String DOUBLE_METAPHONE_ALTERNATE_NAME = "Double Metaphone Alternate";

    public static final class Concepts { // TODO: Confirm all below are in Hum_Metadata package

        public static final String LIBERIA_DIAGNOSIS_SET_OF_SETS = "ed97232b-1a09-4260-b06c-d193107c32a7";
        public static final String HAITI_DIAGNOSIS_SET_OF_SETS = "8fcd0b0c-f977-4a66-a1b5-ad7ce68e6770";

        public static final String PAYMENT_AMOUNT = "5d1bc5de-6a35-4195-8631-7322941fe528";
        public static final String PAYMENT_REASON = "36ba7721-fae0-4da4-aef2-7e476cc04bdf";
        public static final String PAYMENT_RECEIPT_NUMBER = "20438dc7-c5b4-4d9c-8480-e888f4795123";
        public static final String PAYMENT_CONSTRUCT = "7a6330f1-9503-465c-8d63-82e1ad914b47";

    }

    public static final class Forms {
        public static final String ADMISSION = "43acf930-eb1b-11e2-91e2-0800200c9a66";  // TODO: Install in bundle
        public static final String TRANSFER_WITHIN_HOSPITAL = "d068bc80-fb95-11e2-b778-0800200c9a66";  // TODO: Install in bundle
        public static final String EXIT_FROM_INPATIENT = "e0a26c20-fba6-11e2-b778-0800200c9a66";  // TODO: Install in bundle
    }

    public static final String DEFAULT_DATE_FORMAT = "dd MMM yyyy";
    public static final String DEFAULT_TIME_FORMAT = "h:mm aa";
    public static final String DEFAULT_DATETIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;

    @Override
    public void install() throws Exception {

        Map<String, String> properties = new LinkedHashMap<String, String>();

        // OpenMRS Core
        properties.put(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8");
        properties.put(OpenmrsConstants.GP_PASSWORD_REQUIRES_DIGIT, "false");
        properties.put(OpenmrsConstants.GP_PASSWORD_REQUIRES_NON_DIGIT, "false");
        properties.put(OpenmrsConstants.GP_PASSWORD_REQUIRES_UPPER_AND_LOWER_CASE, "false");
        //properties.put(OpenmrsConstants.GP_CASE_SENSITIVE_DATABASE_STRING_COMPARISON, "false");


        // Html Form Entry
        properties.put(HtmlFormEntryConstants.GP_DATE_FORMAT, DEFAULT_DATE_FORMAT);
        properties.put(HtmlFormEntryConstants.GP_TIME_FORMAT, DEFAULT_TIME_FORMAT);
        properties.put(HtmlFormEntryConstants.GP_SHOW_DATE_FORMAT, "false");
        properties.put(HtmlFormEntryConstants.GP_UNKNOWN_CONCEPT, CommonConcepts.Concepts.UNKNOWN);

        // Reporting
        properties.put(ReportingConstants.GLOBAL_PROPERTY_TEST_PATIENTS_COHORT_DEFINITION, "");

        // UI Framework
        properties.put(UiFrameworkConstants.GP_FORMATTER_DATE_FORMAT, DEFAULT_DATE_FORMAT);
        properties.put(UiFrameworkConstants.GP_FORMATTER_DATETIME_FORMAT, DEFAULT_DATETIME_FORMAT);

        // EMR API: most global properties have been moved to metadata mappings, but evidently not this one
        if (config.getCountry().equals(ConfigDescriptor.Country.HAITI)) {
            properties.put(EmrApiConstants.GP_DIAGNOSIS_SET_OF_SETS, Concepts.HAITI_DIAGNOSIS_SET_OF_SETS);
        }
        else if (config.getCountry().equals(ConfigDescriptor.Country.LIBERIA)) {
            properties.put(EmrApiConstants.GP_DIAGNOSIS_SET_OF_SETS, Concepts.LIBERIA_DIAGNOSIS_SET_OF_SETS);
        }


        // REST
        // These do not use constants from the rest module due to the omod dependency when provided in maven.
        // These are used to increase the number of results that rest web services returns (for the appointment scheduling module)
        properties.put("webservices.rest.maxResultsAbsolute", "1000");
        properties.put("webservices.rest.maxResultsDefault", "500");

        // EMR
        properties.put(EmrConstants.PAYMENT_AMOUNT_CONCEPT, Concepts.PAYMENT_AMOUNT);
        properties.put(EmrConstants.PAYMENT_REASON_CONCEPT, Concepts.PAYMENT_REASON);
        properties.put(EmrConstants.PAYMENT_RECEIPT_NUMBER_CONCEPT, Concepts.PAYMENT_RECEIPT_NUMBER);
        properties.put(EmrConstants.PAYMENT_CONSTRUCT_CONCEPT, Concepts.PAYMENT_CONSTRUCT);

        // Core Apps (force the user to hit enter when searching--no "autocomplete")
        properties.put(CoreAppsConstants.GP_SEARCH_DELAY_SHORT, "99999999");
        properties.put(CoreAppsConstants.GP_SEARCH_DELAY_LONG, "99999999");

        // Registration Core
        properties.put(RegistrationCoreConstants.GP_PATIENT_NAME_SEARCH, "registrationcore.ExistingPatientNameSearch");
        properties.put(RegistrationCoreConstants.GP_FAST_SIMILAR_PATIENT_SEARCH_ALGORITHM, "pihcore.PihPatientSearchAlgorithm");
        properties.put(RegistrationCoreConstants.GP_PRECISE_SIMILAR_PATIENT_SEARCH_ALGORITHM, "registrationcore.BasicExactPatientSearchAlgorithm");

        // Name Phonetics
        properties.put(NamePhoneticsConstants.GIVEN_NAME_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);
        properties.put(NamePhoneticsConstants.MIDDLE_NAME_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);
        properties.put(NamePhoneticsConstants.FAMILY_NAME_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);
        properties.put(NamePhoneticsConstants.FAMILY_NAME2_GLOBAL_PROPERTY, DOUBLE_METAPHONE_ALTERNATE_NAME);

        // Allergies
        properties.put("allergy.concept.unknown", CommonConcepts.Concepts.UNKNOWN);
        properties.put("allergy.concept.otherNonCoded", CommonConcepts.Concepts.OTHER_NON_CODED);
        properties.put("allergy.concept.severity.mild", AllergyConcepts.Concepts.ALLERGY_SEVERITY_MILD);
        properties.put("allergy.concept.severity.moderate", AllergyConcepts.Concepts.ALLERGY_SEVERITY_MODERATE);
        properties.put("allergy.concept.severity.severe", AllergyConcepts.Concepts.ALLERGY_SEVERITY_SEVERE);
        properties.put("allergy.concept.allergen.food", AllergyConcepts.Concepts.ALLERGENS_FOOD_SET);
        properties.put("allergy.concept.allergen.drug", AllergyConcepts.Concepts.ALLERGENS_DRUG_SET);
        properties.put("allergy.concept.allergen.environment", AllergyConcepts.Concepts.ALLERGENS_ENVIRONMENT_SET);
        properties.put("allergy.concept.reactions", AllergyConcepts.Concepts.ALLERGY_REACTIONS_SET);

        // Visit Document Module
        properties.put(VisitDocumentsConstants.GP_ALLOW_NO_CAPTION, "true");
        properties.put(VisitDocumentsConstants.GP_WEBCAM_ALLOWED, "false");
        properties.put(VisitDocumentsConstants.GP_MAX_STORAGE_FILE_SIZE, "5.0");
        properties.put(VisitDocumentsConstants.GP_MAX_UPLOAD_FILE_SIZE, "5.0");
        properties.put(VisitDocumentsConstants.GP_ENCOUNTER_TYPE_UUID, "");


        setGlobalProperties(properties);

        // EMR API global properties are now set up via Metadata Mappings
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_UNKNOWN_LOCATION), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_AT_FACILITY_VISIT_TYPE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_CLINICIAN_ENCOUNTER_ROLE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_ORDERING_PROVIDER_ENCOUNTER_ROLE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_CHECK_IN_CLERK_ENCOUNTER_ROLE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_CHECK_IN_ENCOUNTER_TYPE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_EXIT_FROM_INPATIENT_ENCOUNTER_TYPE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_ADMISSION_ENCOUNTER_TYPE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_TRANSFER_WITHIN_HOSPITAL_ENCOUNTER_TYPE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_VISIT_NOTE_ENCOUNTER_TYPE), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_ADMISSION_FORM), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_TRANSFER_WITHIN_HOSPITAL_FORM), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_EXIT_FROM_INPATIENT_FORM), "replaced by metadata mapping");
        uninstall(possible(GlobalProperty.class, EmrApiConstants.GP_EXTRA_PATIENT_IDENTIFIER_TYPES), "replaced by metadata mapping");

    }
}
