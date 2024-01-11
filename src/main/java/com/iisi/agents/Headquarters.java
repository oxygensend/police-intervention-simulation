package com.iisi.agents;

import com.iisi.City;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Headquarters extends Agent {

    private List<PolicePatrol> patrols = new ArrayList<>();
    private List<Incident> incidents = new ArrayList<>();
    private int range = 1;
    private final static Logger LOGGER = LoggerFactory.getLogger(Headquarters.class);

    public Headquarters(Point position, District district) {
        super(position, district);
    }

    public void assignTasks() {
        var allEntities = City.instance().agentList;
        patrols = allEntities.stream()
                             .filter(x -> x instanceof PolicePatrol && x.isActive())
                             .map(x -> (PolicePatrol) x)
                             .collect(Collectors.toList());

        var compareByPriority = Comparator.comparing(Incident::getPriorityValue);
        var compareByCreatedAt = Comparator.comparing(Incident::getCreatedAt);
        incidents = allEntities.stream()
                               .filter(x -> x instanceof Incident && x.isActive())
                               .map(x -> (Incident) x)
                               .sorted(compareByPriority.thenComparing(compareByCreatedAt))
                               .collect(Collectors.toList());

        for (Incident incident : incidents) {
            if ((incident.getPatrolsReaching().isEmpty() && incident.getPatrolsSolving().isEmpty()) || incident.isFiring()) {
                PolicePatrol availablePatrol;
                availablePatrol = City.instance().findNearestAvailablePolicePatrol(incident);
                if (availablePatrol != null) {
                    if (incident.isFiring() && (incident.getPatrolsSolving().size() == 1 && incident.getPatrolsReaching().isEmpty())) {
                        availablePatrol.takeTask(incident);
                        incident.setPatrolsReaching(availablePatrol);
                        LOGGER.info("Support patrol {} is going to firing at {}", availablePatrol.id, incident.position);

                        if (availablePatrol.district != incident.district) {
                            incident.district.statistics.incrementNumberOfPatrolsComingFromOtherDistricts();
                        }
                        break;
                    } else if (incident.getPatrolsReaching().isEmpty()) {
                        availablePatrol.takeTask(incident);
                        incident.setPatrolsReaching(availablePatrol);
                        LOGGER.info("Patrol {} is going to incident at {}", availablePatrol.id, incident.position);

                        if (availablePatrol.district != incident.district) {
                            incident.district.statistics.incrementNumberOfPatrolsComingFromOtherDistricts();
                        }
                        break;
                    }

                }
            }
        }
    }

    public void newShift() {
        var districtAndPatrolsMap = City.instance().calculateDangerLevelsAndPatrolNumberToDistrict();

        for (var patrol :
                patrols
        ) {
            if (patrol.getState() == PolicePatrol.State.PATROLLING) {
                LOGGER.info("Patrol {} finished shift at {}. Removing...", patrol.id, City.instance().getSimulationDuration());
                patrol.deactivate();
            } else {
                patrol.removeAfterIntervention();
            }
        }

        for (var entrySet :
                districtAndPatrolsMap.entrySet()
        ) {
            for (int i = 0; i < entrySet.getValue(); i++) {
                var patrolPosition = entrySet.getKey().getRandomPositionInDistrict();
                var patrol = new PolicePatrol(patrolPosition, entrySet.getKey());
                City.instance().addAgent(patrol);
                LOGGER.info("Patrol {} created at the position {} in district {}", patrol.id, patrol.getPosition(), entrySet.getKey().name);
            }
        }

    }

}
