package com.iisi;

import com.iisi.agents.District;
import com.iisi.agents.PolicePatrol;
import com.iisi.utils.ArrayUtils;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Simulation {
    private final static City CITY = City.instance();
    private final static Logger LOGGER = LoggerFactory.getLogger(Simulation.class);
    private List<Point> positionsTaken;


    public void run() {

        setUpPatrols();

    }


    private int[] calculateInitialPatrolPerDistrict() {

        int[] patrolPerDistrict = new int[SimulationConfig.DISTRICTS_CONFIG.size()];

        int sum;
        int j = 0;
        while (true) {
            sum = ArrayUtils.sumArray(patrolPerDistrict);
            if (sum == SimulationConfig.NUMBER_OF_PATROLS) {
                break;
            }
            patrolPerDistrict[j]++;
            j++;

            if (j == patrolPerDistrict.length) {
                j = 0;
            }
        }

        return patrolPerDistrict;

    }

    private void setUpPatrols() {
        int index = 1;
        int[] patrolPerDistrict = calculateInitialPatrolPerDistrict();


        for (var districtSet : SimulationConfig.DISTRICTS_CONFIG.entrySet()) {
            var district = new District(index, districtSet.getKey(), districtSet.getValue(),
                                        District.ThreatLevel.from(SimulationConfig.BASE_THREAT_LEVEL),
                                        patrolPerDistrict[index - 1]);

            LOGGER.info("District {} created with threatLevel {}", district.name, district.getThreatLevel());
            City.instance().addDistrict(district);

            positionsTaken = new ArrayList<>();
            for (int i = 0; i < district.initialNumberOfPatrols; i++) {
                var patrolPosition = getRandomPositionInDistrict(district);
                var patrol = new PolicePatrol(patrolPosition, district);
                City.instance().addAgent(patrol);
                LOGGER.info("Patrol {} created at the position {} in district {}", patrol.id, patrol.getPosition(), district.name);
            }

            index++;
        }
    }

    private Point getRandomPositionInDistrict(District district) {
        var temp = new Random().nextInt(district.allPointsInDistrict.size());
        var patrolPosition = district.allPointsInDistrict.get(temp);
        while (positionsTaken.contains(patrolPosition)) {
            temp = new Random().nextInt(district.allPointsInDistrict.size());
            patrolPosition = district.allPointsInDistrict.get(temp);
        }
        positionsTaken.add(patrolPosition);

        return patrolPosition;
    }

//    private static List<District> generateRandomDistricts() {
//        List<District> districts = new ArrayList<>();
//        List<District.Districts> districtNames = Arrays.asList(District.Districts.values());
//        Collections.shuffle(districtNames);
//
//        int index = 0;
//        int[] zones = {10, 20, 30, 40};
//        for (var districtSet : SimulationConfig.DISTRICTS_CONFIG.entrySet()) {
//            var district = new District(index, districtSet.getKey(), districtSet.getValue(),
//                                        District.ThreatLevel.from(SimulationConfig.BASE_THREAT_LEVEL),
//                                        4);
//            districts.add(district);
//
//            var GRID_SIZE = SimulationConfig.GRID_HEIGHT;
//            var random = new Random();
//            int startX = random.nextInt(GRID_SIZE);
//            int startY = random.nextInt(GRID_SIZE);
//
//            while (!isValid(startX, startY)) {
//                startX = random.nextInt(GRID_SIZE);
//                startY = random.nextInt(GRID_SIZE);
//            }
//
//            List<Point> districtCells = new ArrayList<>();
//            districtCells.add(new Point(startX, startY));
//            CITY.board[startX][startY] = district.id;
//
//            while (districtCells.size() < GRID_SIZE * GRID_SIZE) {
//                List<Point> newCells = new ArrayList<>();
//
//                for (Point cell : districtCells) {
//                    int x = cell.x();
//                    int y = cell.y();
//
//                    int[] dx = {-1, 1, 0, 0};
//                    int[] dy = {0, 0, -1, 1};
//
//                    for (int i = 0; i < 4; i++) {
//                        int newX = x + dx[i];
//                        int newY = y + dy[i];
//
//                        if (isValid(newX, newY)) {
//                            CITY.board[newX][newY] = district.id;
//                            newCells.add(new Point(newX, newY));
//                        }
//                    }
//                }
//
//                if (newCells.isEmpty()) {
//                    break;
//                }
//
//                districtCells.addAll(newCells);
//            }
//            index++;
//        }
//
//        int districtId = 1;
////        for (int x = 0; x < SimulationConfig.GRID_WIDTH; x++) {
////            for (int y = 0; y < SimulationConfig.GRID_HEIGHT; y++) {
////                if (CITY.board[x][y] == 0) {
////                    floodFill(x, y, districtId);
////                    districts.get(districtId - 1).boundaries.add(new Point(x, y));
////                    districtId++;
////                    if (districtId > districts.size()) {
////                        break;
////                    }
////                }
////            }
////        }
//
//        return districts;
//    }

//    private static boolean isValid(int x, int y) {
////        return x >= 0 && x < SimulationConfig.GRID_WIDTH && y >= 0 && y < SimulationConfig.GRID_HEIGHT && CITY.board[x][y] == 0;
////    }
////
////    // Function to perform flood fill algorithm for creating districts
////    private static void floodFill(int x, int y, int districtId) {
////        Stack<Point> stack = new Stack<>();
////        stack.push(new Point(x, y));
////
////        while (!stack.isEmpty()) {
////            Point point = stack.pop();
////            x = point.x();
////            y = point.y();
////
////            if (isValid(x, y)) {
////                CITY.board[x][y] = districtId;
////                stack.push(new Point(x + 1, y));
////                stack.push(new Point(x - 1, y));
////                stack.push(new Point(x, y + 1));
////                stack.push(new Point(x, y - 1));
////            }
////        }
////    }


}
