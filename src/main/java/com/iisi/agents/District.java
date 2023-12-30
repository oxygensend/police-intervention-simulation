package com.iisi.agents;

import com.iisi.City;
import com.iisi.utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class District {
    private ThreatLevel threatLevel;

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

    public District(int id, Districts name, List<Point> boundaries, ThreatLevel threatLevel, int initialNumberOfPatrols) {
        this.id = id;
        this.name = name;
        this.boundaries = boundaries;
        this.allPointsInDistrict = fillBoardWithDistrictPoints();
        this.threatLevel = threatLevel;
        this.initialNumberOfPatrols = initialNumberOfPatrols;
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


    public ThreatLevel getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(ThreatLevel threatLevel) {
        this.threatLevel = threatLevel;
    }

    public static enum ThreatLevel {
        LOW(1),
        MEDIUM(2),
        HIGH(3);

        public final int level;

        ThreatLevel(int level) {
            this.level = level;
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
        DISTRICT_4
    }

}
