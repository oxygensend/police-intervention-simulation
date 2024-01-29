package com.iisi.statistics;

import com.iisi.City;
import com.iisi.agents.District;

public record HistoricDistrictStatistics(
        District.Districts districtName,
        int iteration,
        double dangerCoefficient,
        int numberOfPatrols,
        int numberOfInterventions,
        int numberOfFirings,
        int numberOfNeutralizedPatrols,
        int numberOfSolvedInterventions,
        int numberOfSolvedFirings,
        int numberOfPatrolsComingFromOtherDistricts,
        District.ThreatLevel threatLevel
) {

    public HistoricDistrictStatistics(District district) {
        this(district.name,
             City.instance().getSimulationDuration(),
             district.getDangerCoefficient(),
             district.getNumberOfPatrols(),
             district.getStatistics().getNumberOfInterventions(),
             district.getStatistics().getNumberOfFirings(),
             district.getStatistics().getNumberOfNeutralizedPatrols(),
             district.getStatistics().getNumberOfSolvedInterventions(),
             district.getStatistics().getNumberOfSolvedFirings(),
             district.getStatistics().getNumberOfPatrolsComingFromOtherDistricts(),
             district.getThreatLevel()
        );
    }

}
