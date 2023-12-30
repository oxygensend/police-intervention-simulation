package com.iisi.agents;

import com.iisi.City;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
                .filter(x -> x instanceof PolicePatrol)
                .map(x -> (PolicePatrol) x)
                .collect(Collectors.toList());
        incidents = allEntities.stream()
                .filter(x -> x instanceof Incident)
                .map(x -> (Incident) x)
                .collect(Collectors.toList());

        for (Incident incident : incidents) {
            if ((incident).getPatrolsReaching() == null && (incident).getPatrolsSolving() == null) {
                PolicePatrol availablePatrol;
                    availablePatrol = City.instance().findNearestAvailablePolicePatrol(incident);
                    if (availablePatrol != null) {
                        availablePatrol.takeTask();
                        incident.setPatrolsReaching(availablePatrol);
                        LOGGER.info("Patrol {} is going to incident at {}", availablePatrol.id, incident.position);
                        break;
                    }
            }
        }
    }
}
