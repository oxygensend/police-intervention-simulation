package com.iisi.agents;

import com.iisi.City;
import com.iisi.statistics.DistrictStatistics;
import com.iisi.utils.Point;

import java.util.*;

public class District {

    public final int id;

    public final Districts name;

    /*
     * List of board indices that make up the boundaries of the district
     */
    public final List<Point> boundaries;

    /*
     * List of board indices that make up the district
     */
    public final List<Point> allPointsInDistrict;

    public final int initialNumberOfPatrols;

    public final DistrictStatistics statistics = new DistrictStatistics();
    private ThreatLevel threatLevel;
    private final List<Point> positionsTaken = new ArrayList<>();
    private double dangerCoefficient = 0;
    private int numberOfPatrols;

    public District(int id, Districts name, List<Point> boundaries, ThreatLevel threatLevel, int initialNumberOfPatrols) {
        this.id = id;
        this.name = name;
        this.boundaries = boundaries;
        this.allPointsInDistrict = fillBoardWithDistrictPoints();
        this.threatLevel = threatLevel;
        this.initialNumberOfPatrols = initialNumberOfPatrols;
        this.numberOfPatrols = initialNumberOfPatrols;

        this.statistics.setNumberOfPatrols(initialNumberOfPatrols);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return id == district.id && initialNumberOfPatrols == district.initialNumberOfPatrols && name == district.name && Objects.equals(boundaries, district.boundaries) && Objects.equals(allPointsInDistrict, district.allPointsInDistrict) && threatLevel == district.threatLevel && Objects.equals(positionsTaken, district.positionsTaken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, boundaries, allPointsInDistrict, initialNumberOfPatrols, threatLevel, positionsTaken);
    }

    private List<Point> fillBoardWithDistrictPoints() {
        List<Point> points = new ArrayList<>();
        var board = City.instance().grid;
        int horizontalMax = boundaries.stream().map(Point::x).max(Integer::compareTo).orElseThrow();
        int horizontalMin = boundaries.stream().map(Point::x).min(Integer::compareTo).orElseThrow();
        int verticalMax = boundaries.stream().map(Point::y).max(Integer::compareTo).orElseThrow();
        int verticalMin = boundaries.stream().map(Point::y).min(Integer::compareTo).orElseThrow();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                if (i >= horizontalMin && i <= horizontalMax && j >= verticalMin && j <= verticalMax) {
                    points.add(new Point(i, j));
                    City.instance().grid[i][j] = id;
                }
            }
        }

        return points;
    }

    public Point getRandomPositionInDistrict() {
        var temp = new Random().nextInt(allPointsInDistrict.size());
        var patrolPosition = allPointsInDistrict.get(temp);
        while (positionsTaken.contains(patrolPosition)) {
            temp = new Random().nextInt(allPointsInDistrict.size());
            patrolPosition = allPointsInDistrict.get(temp);
        }
        positionsTaken.add(patrolPosition);

        return patrolPosition;
    }

    public void removeTakenPosition(Point position) {
        positionsTaken.remove(position);
    }

    public ThreatLevel getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(ThreatLevel threatLevel) {
        this.threatLevel = threatLevel;
    }

    public void setNewThreatLevel() {
        if (name == Districts.DISTRICT_1) {
            this.threatLevel = ThreatLevel.MEDIUM;
        } else if (name == Districts.DISTRICT_2) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_3) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_4) {
            this.threatLevel = ThreatLevel.HIGH;
        } else if (name == Districts.DISTRICT_5) {
            this.threatLevel = ThreatLevel.MEDIUM;
        } else if (name == Districts.DISTRICT_6) {
            this.threatLevel = ThreatLevel.MEDIUM;
        } else if (name == Districts.DISTRICT_7) {
            this.threatLevel = ThreatLevel.MEDIUM;
        } else if (name == Districts.DISTRICT_8) {
            this.threatLevel = ThreatLevel.LOW;
        }

    }

    public void setNewThreatLevel2() {
        if (name == Districts.DISTRICT_1) {
            this.threatLevel = ThreatLevel.MEDIUM;
        } else if (name == Districts.DISTRICT_2) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_3) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_4) {
            this.threatLevel = ThreatLevel.HIGH;
        } else if (name == Districts.DISTRICT_5) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_6) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_7) {
            this.threatLevel = ThreatLevel.MEDIUM;
        } else if (name == Districts.DISTRICT_8) {
            this.threatLevel = ThreatLevel.HIGH;
        }

    }

    public void setNewThreatLevel3() {
        if (name == Districts.DISTRICT_1) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_2) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_3) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_4) {
            this.threatLevel = ThreatLevel.HIGH;
        } else if (name == Districts.DISTRICT_5) {
            this.threatLevel = ThreatLevel.HIGH;
        } else if (name == Districts.DISTRICT_6) {
            this.threatLevel = ThreatLevel.HIGH;
        } else if (name == Districts.DISTRICT_7) {
            this.threatLevel = ThreatLevel.LOW;
        } else if (name == Districts.DISTRICT_8) {
            this.threatLevel = ThreatLevel.MEDIUM;
        }

    }


    public Point findTheNearestPointFromDifferentDistrict(Point point) {
        return allPointsInDistrict.stream()
                                  .min(Comparator.comparingDouble(p -> Point.calculateDistance(p, point)))
                                  .orElse(null);

    }

    public double calculateDangerCoefficient() {
        return statistics.calculateDangerCoefficient();
    }

    public double getDangerCoefficient() {
        return dangerCoefficient;
    }

    public void setDangerCoefficient(double dangerCoefficient) {
        this.dangerCoefficient = dangerCoefficient;
    }

    public int getNumberOfPatrols() {
        return numberOfPatrols;
    }

    public void setNumberOfPatrols(int numberOfPatrols) {
        this.numberOfPatrols = numberOfPatrols;
    }

    public DistrictStatistics getStatistics() {
        return statistics;
    }

    public static enum ThreatLevel {
        LOW(1, 0, 0.1),
        MEDIUM(2, 0.1, 0.2),
        HIGH(3, 0.2, 1);

        public final int level;
        public final double low;
        public final double high;

        ThreatLevel(int level, double low, double high) {
            this.level = level;
            this.low = low;
            this.high = high;
        }

        public static ThreatLevel from(int value) {
            return Arrays.stream(values())
                         .filter(v -> v.level == value)
                         .findFirst()
                         .orElseThrow();
        }

    }

    public enum Districts {
        DISTRICT_1,
        DISTRICT_2,
        DISTRICT_3,
        DISTRICT_4,
        DISTRICT_5,
        DISTRICT_6,
        DISTRICT_7,
        DISTRICT_8,
    }


}
