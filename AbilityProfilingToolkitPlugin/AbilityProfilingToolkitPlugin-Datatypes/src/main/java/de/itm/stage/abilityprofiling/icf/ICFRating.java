package de.itm.stage.abilityprofiling.icf;


import java.util.Arrays;
import java.util.LinkedList;

public class ICFRating {

    //Names of the categories

    public final static String EXTENT_OF_IMPAIRMENT = "EXTENT OF IMPAIRMENT";
    public final static String NATURE_OF_IMPAIRMENT = "NATURE OF IMPAIRMENT";
    public final static String LOCATION_OF_IMPAIRMENT = "LOCATION OF IMPAIRMENT";

    public final static String PERFORMANCE = "PERFORMANCE";
    public final static String CAPACITY_WITHOUT_ASSISTANCE = "CAPACITY (w/o ASSISTANCE)";
    public final static String CAPACITY_WITH_ASSISTANCE = "CAPACITY (w/ ASSISTANCE)";
    public final static String PERFORMANCE_WITHOUT_ASSISTANCE = "PERFORMANCE (w/o ASSISTANCE)";

    public final static String BARRIER_OR_FACILITATOR= "BARRIER OR FACILITATOR";


    public final static String[] BODY_FUNCTIONS_TITLES = new String[]{EXTENT_OF_IMPAIRMENT};
    public final static String[] BODY_STRUCTURE_TITLES = new String[]{EXTENT_OF_IMPAIRMENT,NATURE_OF_IMPAIRMENT,LOCATION_OF_IMPAIRMENT};
    public final static String[] DOMAIN_TITLES = new String[]{PERFORMANCE,CAPACITY_WITHOUT_ASSISTANCE,CAPACITY_WITH_ASSISTANCE,PERFORMANCE_WITHOUT_ASSISTANCE};
    public final static String[] ENVIRONMENTAL_TITLES = new String[]{BARRIER_OR_FACILITATOR};

    //Body Functions

    public final static String[] NO_IMPAIRMENT = new String[]{"NO_IMPAIRMENT", ".0"};
    public final static String[] MILD_IMPAIRMENT = new String[]{"MILD_IMPAIRMENT", ".1"};
    public final static String[] MODERATE_IMPAIRMENT = new String[]{"MODERATE_IMPAIRMENT", ".2"};
    public final static String[] SEVERE_IMPAIRMENT = new String[]{"SEVERE_IMPAIRMENT", ".3"};
    public final static String[] COMPLETE_IMPAIRMENT = new String[]{"COMPLETE_IMPAIRMENT", ".4"};

    public final static String[] NOT_SPECIFIED = new String[]{"NOT_SPECIFIED", ".8"};
    public final static String[] NOT_APPLICABLE = new String[]{"NOT_APPLICABLE", ".9"};

    public final static String[] NOT_SPECIFIED_IN = new String[]{"NOT_SPECIFIED", "8"};
    public final static String[] NOT_APPLICABLE_IN = new String[]{"NOT_APPLICABLE", "9"};

    //Body Structure
    public final static String[] NO_CHANGE_IN_STRUCTURE = new String[]{"NOT_SPECIFIED", "0"};
    public final static String[] TOTAL_ABSENCE = new String[]{"TOTAL_ABSENCE", "1"};
    public final static String[] PARTIAL_ABSENSE = new String[]{"PARTIAL_ABSENSE", "2"};
    public final static String[] ADDITIONAL_PART = new String[]{"ADDITIONAL_PART", "3"};
    public final static String[] ABERRANT_DIMENSIONS = new String[]{"ABERRANT_DIMENSIONS", "4"};
    public final static String[] DISCONTINUITY = new String[]{"DISCONTINUITY", "5"};
    public final static String[] DEVIATING_POSITION = new String[]{"DEVIATING_POSITION", "6"};
    public final static String[] QUALITATIVE_CHANGES_IN_STRUCTURE__INCLUDING_ACCUMULATION_OF_FLUID = new String[]{"QUALITATIVE_CHANGES_IN_STRUCTURE__INCLUDING_ACCUMULATION_OF_FLUID", "7"};

    public final static String[] MORE_THAN_ONE_REGION = new String[]{"MORE_THAN_ONE_REGION", "0"};
    public final static String[] RIGHT = new String[]{"RIGHT", "1"};
    public final static String[] LEFT = new String[]{"LEFT", "2"};
    public final static String[] BOTH_SIDES = new String[]{"BOTH_SIDES", "3"};
    public final static String[] FRONT = new String[]{"FRONT", "4"};
    public final static String[] BACK = new String[]{"BACK", "5"};
    public final static String[] PROXIMAL = new String[]{"PROXIMAL", "6"};
    public final static String[] DISTAL = new String[]{"DISTAL", "7"};

    //Domain (Activities and Participation)
    public final static String[] NO_DIFFICULTY = new String[]{"NO_DIFFICULTY", "0"};
    public final static String[] MILD_DIFFICULTY = new String[]{"MILD_DIFFICULTY", "1"};
    public final static String[] MODERATE_DIFFICULTY = new String[]{"MODERATE_DIFFICULTY", "2"};
    public final static String[] SEVERE_DIFFICULTY = new String[]{"SEVERE_DIFFICULTY", "3"};
    public final static String[] COMPLETE_DIFFICULTY = new String[]{"COMPLETE_DIFFICULTY", "4"};
    public final static String[] BLANK = new String[]{"BLANK", "_"};


    //Environmental Factors
    public final static String[] NO_BARRIER = new String[]{"NO_BARRIER", ".0"};
    public final static String[] MILD_BARRIER = new String[]{"MILD_BARRIER", ".1"};
    public final static String[] MODERATE_BARRIER = new String[]{"MODERATE_BARRIER", ".2"};
    public final static String[] SEVERE_BARRIER = new String[]{"SEVERE_BARRIER", ".3"};
    public final static String[] COMPLETE_BARRIER = new String[]{"COMPLETE_BARRIER", ".4"};

    public final static String[] NOT_SPECIFIED_ENVIRONMENT = new String[]{"NOT_SPECIFIED_ENVIRONMENT_BARRIER", "+8"};

    public final static String[] NO_FACILITATOR = new String[]{"NO_FACILITATOR", "+0"};
    public final static String[] MILD_FACILITATOR = new String[]{"MILD_FACILITATOR", "+1"};
    public final static String[] MODERATE_FACILITATOR = new String[]{"MODERATE_FACILITATOR", "+2"};
    public final static String[] SEVERE_FACILITATOR = new String[]{"SEVERE_FACILITATOR", "+3"};
    public final static String[] COMPLETE_FACILITATOR = new String[]{"COMPLETE_FACILITATOR", "+4"};

    public final static String[] NOT_SPECIFIED_ENVIRONMENT_FACILITATOR = new String[]{"NOT_SPECIFIED_ENVIRONMENT_FACILITATOR", "+8"};
    public final static String[] NOT_APPLICABLE_ENVIRONMENT = new String[]{"NOT_APPLICABLE_ENVIRONMENT", ".9"};



    // Sets
    public final static LinkedList<String[]> ALL_RATINGS = new LinkedList<String[]>(Arrays.asList(
            NO_IMPAIRMENT, MILD_IMPAIRMENT, MODERATE_IMPAIRMENT, SEVERE_IMPAIRMENT, COMPLETE_IMPAIRMENT, NOT_SPECIFIED, NOT_APPLICABLE,
            NO_BARRIER, MILD_BARRIER, MODERATE_BARRIER, SEVERE_BARRIER, COMPLETE_BARRIER, NO_FACILITATOR, MILD_FACILITATOR,
            MODERATE_FACILITATOR, SEVERE_FACILITATOR, COMPLETE_FACILITATOR, NOT_SPECIFIED_ENVIRONMENT, NOT_APPLICABLE_ENVIRONMENT));

    public static final LinkedList<String[]> FUNCTIONS_RATING_SET = new LinkedList<String[]>(Arrays.asList(NO_IMPAIRMENT, MILD_IMPAIRMENT,
            MODERATE_IMPAIRMENT, SEVERE_IMPAIRMENT, COMPLETE_IMPAIRMENT, NOT_SPECIFIED, NOT_APPLICABLE));

    public static final LinkedList<String[]> STRUCTURES_RATING_SET_A = new LinkedList<String[]>(Arrays.asList(NO_IMPAIRMENT, MILD_IMPAIRMENT,
            MODERATE_IMPAIRMENT, SEVERE_IMPAIRMENT, COMPLETE_IMPAIRMENT, NOT_SPECIFIED, NOT_APPLICABLE));

    public static final LinkedList<String[]> STRUCTURES_RATING_SET_B = new LinkedList<String[]>(Arrays.asList(NO_CHANGE_IN_STRUCTURE,
            TOTAL_ABSENCE, PARTIAL_ABSENSE, ADDITIONAL_PART, ABERRANT_DIMENSIONS, DISCONTINUITY, DEVIATING_POSITION,
            QUALITATIVE_CHANGES_IN_STRUCTURE__INCLUDING_ACCUMULATION_OF_FLUID, NOT_SPECIFIED_IN, NOT_APPLICABLE_IN));

    public static final LinkedList<String[]> STRUCTURES_RATING_SET_C = new LinkedList<String[]>(Arrays.asList(MORE_THAN_ONE_REGION, RIGHT, LEFT,
            BOTH_SIDES, FRONT, BACK, PROXIMAL, DISTAL, NOT_SPECIFIED_IN, NOT_APPLICABLE_IN));

    public static final LinkedList<String[]> ENVIRONMENT_RATING_SET = new LinkedList<String[]>(Arrays.asList(NO_BARRIER,
            MILD_BARRIER, MODERATE_BARRIER, SEVERE_BARRIER, COMPLETE_BARRIER,NOT_SPECIFIED_ENVIRONMENT, NOT_APPLICABLE_ENVIRONMENT, NO_FACILITATOR, MILD_FACILITATOR, MODERATE_FACILITATOR,
            SEVERE_FACILITATOR, COMPLETE_FACILITATOR, NOT_SPECIFIED_ENVIRONMENT_FACILITATOR,NOT_APPLICABLE_ENVIRONMENT));

    public static final LinkedList<String[]> DOMAIN_RATING_SET = new LinkedList<String[]>(Arrays.asList(NO_DIFFICULTY,
            MILD_DIFFICULTY, MODERATE_DIFFICULTY, SEVERE_DIFFICULTY,COMPLETE_DIFFICULTY,NOT_SPECIFIED_IN,NOT_APPLICABLE_IN, BLANK));
}
