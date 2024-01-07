package com.iisi.statistics;

public class DistrictStatistics {
    private int numberOfPatrols;
    private int numberOfInterventions;
    private int numberOfFirings;
    private int numberOfNeutralizedPatrols;
    private int numberOfSolvedInterventions;
    private int numberOfSolvedFirings;
    private int numberOfPatrolsComingFromOtherDistricts;

    public DistrictStatistics() {
        reset();
    }

    public void incrementNumberOfPatrols() {
        this.numberOfPatrols++;
    }

    public void incrementNumberOfInterventions() {
        this.numberOfInterventions++;
    }

    public void incrementNumberOfFirings() {
        this.numberOfFirings++;
    }

    public void incrementNumberOfNeutralizedPatrols() {
        this.numberOfNeutralizedPatrols++;
    }

    public void incrementNumberOfSolvedInterventions() {
        this.numberOfSolvedInterventions++;
    }

    public void incrementNumberOfSolvedFirings() {
        this.numberOfSolvedInterventions++;
        this.numberOfSolvedFirings++;
    }

    public void incrementNumberOfPatrolsComingFromOtherDistricts() {
        this.numberOfPatrolsComingFromOtherDistricts++;
    }

    public int getNumberOfPatrols() {
        return numberOfPatrols;
    }

    public int getNumberOfInterventions() {
        return numberOfInterventions;
    }

    public int getNumberOfFirings() {
        return numberOfFirings;
    }

    public int getNumberOfNeutralizedPatrols() {
        return numberOfNeutralizedPatrols;
    }

    public int getNumberOfSolvedInterventions() {
        return numberOfSolvedInterventions;
    }

    public int getNumberOfSolvedFirings() {
        return numberOfSolvedFirings;
    }

    public int getNumberOfPatrolsComingFromOtherDistricts() {
        return numberOfPatrolsComingFromOtherDistricts;
    }

    public void reset() {
        this.numberOfPatrols = 0;
        this.numberOfInterventions = 0;
        this.numberOfFirings = 0;
        this.numberOfNeutralizedPatrols = 0;
        this.numberOfSolvedInterventions = 0;
        this.numberOfSolvedFirings = 0;
        this.numberOfPatrolsComingFromOtherDistricts = 0;
    }

    public double calculateDangerCoefficient() {
        // Calculating danger coefficient based on interventions, firings,
        // solved issues, neutralized patrols, and patrols coming from different districts
        return (0.3 * numberOfInterventions +
                0.4 * numberOfFirings -
                0.2 * numberOfSolvedInterventions -
                0.1 * numberOfSolvedFirings +
                0.4 * numberOfNeutralizedPatrols +
                0.3 * numberOfPatrolsComingFromOtherDistricts) / (numberOfPatrols + 1);
    }
}
