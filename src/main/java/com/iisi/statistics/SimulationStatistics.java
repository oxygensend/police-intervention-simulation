package com.iisi.statistics;

public class SimulationStatistics {

    private static int NUMBER_OF_NEUTRALIZED_PATROLS = 0;
    private static int NUMBER_OF_INTERVENTIONS = 0;
    private static int NUMBER_OF_SOLVED_INTERVENTIONS = 0;
    private static int NUMBER_OF_FIRINGS = 0;
    private static int NUMBER_OF_SOLVED_FIRINGS = 0;

    public static void incrementNumberOfNeutralizedPatrols() {
        NUMBER_OF_NEUTRALIZED_PATROLS++;
    }

    public static void incrementNumberOfInterventions() {
        NUMBER_OF_INTERVENTIONS++;
    }


    public static void incrementNumberOfSolvedInterventions() {
        NUMBER_OF_SOLVED_INTERVENTIONS++;
    }

    public static void incrementNumberOfFirings() {
        NUMBER_OF_FIRINGS++;
        NUMBER_OF_INTERVENTIONS--;
    }

    public static void incrementNumberOfSolvedFirings() {
        NUMBER_OF_SOLVED_FIRINGS++;
    }

    public static int getNumberOfInterventions() {
        return NUMBER_OF_INTERVENTIONS;
    }

    public static int getNumberOfSolvedInterventions() {
        return NUMBER_OF_SOLVED_INTERVENTIONS;
    }

    public static int getNumberOfFirings() {
        return NUMBER_OF_FIRINGS;
    }

    public static int getNumberOfSolvedFirings() {
        return NUMBER_OF_SOLVED_FIRINGS;
    }

    public static int getNumberOfNeutralizedPatrols() {
        return NUMBER_OF_NEUTRALIZED_PATROLS;
    }

    public static void reset() {
        NUMBER_OF_INTERVENTIONS = 0;
        NUMBER_OF_SOLVED_INTERVENTIONS = 0;
        NUMBER_OF_FIRINGS = 0;
        NUMBER_OF_SOLVED_FIRINGS = 0;
        NUMBER_OF_NEUTRALIZED_PATROLS = 0;
    }


}