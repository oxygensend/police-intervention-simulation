package com.iisi.statistics;

public record HistoricSimulationStatistics(
        int iteration,
        int numberOfPatrols,
        int numberOfNeutralizedPatrols,
        int numberOfPatrolsPatrolling,
        int numberOfPatrolsTransferringToIncident,
        int numberOfPatrolsTransferringToFiring,
        int numberOfPatrolsIntervening,
        int numberOfPatrolsFiring,
        int numberOfInterventions,
        int numberOfSolvedInterventions,
        int numberOfFirings,
        int numberOfSolvedFirings
) {

}
