package hw1.c;

import java.awt.*;

public class ImmutableTrajectory
{
    private final Point[] data;

    public ImmutableTrajectory(Point[] data) { this.data = data; }

    public Point[] getValues()
    {
        //Return deep copy of array
        Point[] copy = new Point[data.length];
        for(int i = 0; i < data.length; i++){ copy[i] = new Point(data[i].x, data[i].y); }
        return copy;
    }

    public Point getValue(int index)
    {
        return new Point(data[index].x, data[index].y); //Makes deep copy of point reference
    }

}