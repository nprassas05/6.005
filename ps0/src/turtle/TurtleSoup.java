/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        final int numSides = 4;
        drawRegularPolygon(turtle, numSides, sideLength);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        assert sides > 2 : "sides = " + sides + ": not > 2";
        
        /* Divide the total degrees by the number of sides.  
         * A three-sided polygon (triangle) is 180 degrees.  Each
         * additional side adds another 180 degrees to the total
         * interior degree of the polygon. */
        double totalInsideDegree = (sides - 2) * 180.0;
        return totalInsideDegree / sides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        assert 0.0 < angle && angle < 180.0 : "Error: requires 0 < angle < 180";
        double unroundedAnswer = 360 / (180 - angle);
        return (int) (Math.round(unroundedAnswer));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double insideAngle = calculateRegularPolygonAngle(sides);
        
        for (int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(180.0 - insideAngle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        if (!(0 <= currentHeading && currentHeading < 360)) {
            throw new RuntimeException("Exception: currentHeading must be in range [0, 360)");
        }
        
        int xDiff = targetX - currentX;
        int yDiff = targetY - currentY;
        double turnAngle = Math.toDegrees(Math.atan2(xDiff,  yDiff)) - currentHeading;
        return turnAngle < 0 ? turnAngle + 360 : turnAngle;
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        assert(xCoords.size() == yCoords.size());
        int numPoints = xCoords.size();
        List<Double> headingsList = new ArrayList<>();
        double currentHeading = 0.0;
        
        for (int i = 0; i < numPoints - 1; i++) {
            int currX = xCoords.get(i);
            int currY = yCoords.get(i);
            int targetX = xCoords.get(i + 1);
            int targetY = yCoords.get(i + 1);
            double turnAngle = calculateHeadingToPoint(currentHeading, currX, currY, 
                             targetX, targetY);
            headingsList.add(turnAngle);
            currentHeading += turnAngle;
            
        }
        
        return headingsList;
    }
    
    public static double dist(double x1, double y1, double x2, double y2) {
        double xDiffSquared = (x1 - x2) * (x1 - x2);
        double yDiffSquared = (y1 - y2) * (y1 - y2);
        return Math.sqrt(xDiffSquared + yDiffSquared);
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        double fullCircle = 360.0;
        double intervalDegrees = fullCircle / 15;
        double degreesTurned = 0;
        
        for (; degreesTurned < fullCircle; degreesTurned += intervalDegrees) {
            drawRegularPolygon(turtle, 6, 25);
            turtle.turn(intervalDegrees);
        }
        
        for (int i = 0; i < 4; i++) {
            drawRegularPolygon(turtle, 4, 80);
            turtle.turn(90);
        }
        
        turtle.draw();
    }
    
    public static void drawPersonalArt2(Turtle turtle) {
        List<Integer> xCoords = Arrays.asList(0, 0, 0, 0);
        List<Integer> yCoords = Arrays.asList(0, 160, -160, 0);
        List<Integer> forwardAmounts = Arrays.asList(160, 320);
        
        List<Double> headings = calculateHeadings(xCoords, yCoords);
        
        /* Draw main art piece first at origin, then above and below the origin */
        drawPersonalArt(turtle);
        for (int i = 0; i < headings.size() - 1; i++) {
            turtle.turn(headings.get(i));
            turtle.forward(forwardAmounts.get(i));
            drawPersonalArt(turtle);
        }
        
        /* Go back to the origin */
        turtle.turn(headings.get(headings.size() - 1));
        turtle.forward(160);
        
        
        /* Draw main art piece to the sides of the origin */
        xCoords = Arrays.asList(0, 160, -160);
        yCoords = Arrays.asList(0, 0, 0);
        headings = calculateHeadings(xCoords, yCoords);
        
        for (int i = 0; i < headings.size(); i++) {
            turtle.turn(headings.get(i));
            turtle.forward(forwardAmounts.get(i));
            drawPersonalArt(turtle);
        }
        
    }
    
    public static void drawPersonalArt3(Turtle turtle) {
        List<Integer> xCoords = Arrays.asList(0, 0, 0, 0);
        List<Integer> yCoords = Arrays.asList(0, 1, -1, 0);
        List<Integer> forwardAmounts = Arrays.asList(160, 320);
        
        List<Double> headings = calculateHeadings(xCoords, yCoords);
        
        /* Draw main art piece first at origin, then above and below the origin */
        drawPersonalArt(turtle);
        for (int j = 0; j < 2; j++) {
            turtle.turn(j == 0 ? 90 : 180);
            turtle.forward(forwardAmounts.get(j));
            drawPersonalArt(turtle);
        }
        
        turtle.turn(180);
        turtle.forward(160);
        turtle.turn(270);
        
        
        for (int i = 0; i < 2; i++) {
            turtle.turn(headings.get(i));
            turtle.forward(forwardAmounts.get(i));
            drawPersonalArt(turtle);
            
            /* Draw main art piece to the sides of the origin */
            List<Integer> xCoordsNested = Arrays.asList(0, 1, -1, 0);
            List<Integer> yCoordsNested = Arrays.asList(0, 0, 0, 0);
            List<Double> headingsNested = calculateHeadings(xCoordsNested, yCoordsNested);
            
            for (int j = 0; j < 2; j++) {
                turtle.turn(headingsNested.get(j));
                turtle.forward(forwardAmounts.get(j));
                drawPersonalArt(turtle);
            }
            
            turtle.turn(headingsNested.get(headingsNested.size() - 1));
            turtle.forward(160);
            turtle.turn(270.0);
        }
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();
        drawPersonalArt3(turtle);
    }
}