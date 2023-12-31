package com.iisi.utils;

public record Point(int x, int y) {

    public static double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2));
    }
}
