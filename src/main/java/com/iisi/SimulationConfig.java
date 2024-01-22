package com.iisi;

import com.iisi.agents.District;
import com.iisi.utils.Point;

import java.util.List;
import java.util.Map;

public class SimulationConfig {

    public static final int GRID_HEIGHT = 100;
    public static final int GRID_WIDTH = 100;

    public static final int SIMULATION_DURATION = 20000;
    public static final int SHIFT_DURATION = 1000;
    public static final int NUMBER_OF_PATROLS = 20;
    public static final int BASE_THREAT_LEVEL = 1;
    public static final int BASE_PATROL_SPEED = 1;
    public static final int INTERVENTION_PATROL_SPEED = 5;
    public static final int MIN_INTERVENTION_DURATION = 3;
    public static final int MAX_INTERVENTION_DURATION = 10;
    public static final int MIN_FIRING_STRENGTH = 20;
    public static final int MAX_FIRING_STRENGTH = 90;
    public static final int STATISTICS_INTERVAL = 600;

    public static final Map<District.ThreatLevel, Double> PROBABILITY_OF_INCIDENT_BY_THREAT_LEVEL = Map.of(
            District.ThreatLevel.LOW, 0.01,
            District.ThreatLevel.MEDIUM, 0.02,
            District.ThreatLevel.HIGH, 0.04
    );

    public static final Map<District.Districts, List<Point>> DISTRICT_BOUNDARIES_CONFIG = Map.of(
        District.Districts.DISTRICT_1, List.of(
            new Point(0, 0),
            new Point(0, 24),
            new Point(49, 24),
            new Point(49, 0)
        ),
        District.Districts.DISTRICT_2, List.of(
            new Point(0, 25),
            new Point(0, 49),
            new Point(49, 49),
            new Point(49, 25)
        ),
        District.Districts.DISTRICT_3, List.of(
            new Point(0, 50),
            new Point(0, 74),
            new Point(49, 74),
            new Point(49, 50)
        ),
        District.Districts.DISTRICT_4, List.of(
            new Point(0, 75),
            new Point(0, 99),
            new Point(49, 99),
            new Point(49, 75)
        ),
        District.Districts.DISTRICT_5, List.of(
            new Point(50, 0),
            new Point(50, 24),
            new Point(99, 24),
            new Point(99, 0)
        ),
        District.Districts.DISTRICT_6, List.of(
            new Point(50, 25),
            new Point(50, 49),
            new Point(99, 49),
            new Point(99, 25)
        ),
        District.Districts.DISTRICT_7, List.of(
            new Point(50, 50),
            new Point(50, 74),
            new Point(99, 74),
            new Point(99, 50)
        ),
        District.Districts.DISTRICT_8, List.of(
            new Point(50, 75),
            new Point(50, 99),
            new Point(99, 99),
            new Point(99, 75)
        )
    );
    
    public static final Map<District.Districts, District.ThreatLevel> DISTRICTS_THREAT_LEVEL_CONFIG = Map.of(
            District.Districts.DISTRICT_1, District.ThreatLevel.LOW,
            District.Districts.DISTRICT_2, District.ThreatLevel.MEDIUM,
            District.Districts.DISTRICT_3, District.ThreatLevel.HIGH,
            District.Districts.DISTRICT_4, District.ThreatLevel.LOW,
            District.Districts.DISTRICT_5, District.ThreatLevel.LOW,
            District.Districts.DISTRICT_6, District.ThreatLevel.MEDIUM,
            District.Districts.DISTRICT_7, District.ThreatLevel.HIGH,
            District.Districts.DISTRICT_8, District.ThreatLevel.LOW
    );

    private SimulationConfig() {
    }

}
