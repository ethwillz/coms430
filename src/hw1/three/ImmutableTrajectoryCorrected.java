package hw1.three;

import java.awt.*;
import java.util.Arrays;

/**
 * CHANGES:
 * 1/28 Made deep copy of Point[] for return of getValues
 *      Made deep copy of point when single index requested
 *
 * NOTES:
 */
public class ImmutableTrajectoryCorrected
{
    private final Point[] data;

    public ImmutableTrajectoryCorrected(Point[] data)
    {
        this.data = data;
    }

    public Point[] getValues()
    {
        return Arrays.stream(data).toArray(Point[]::new);
    }

    public Point getValue(int index)
    {
        return new Point(data[index].x, data[index].y);
    }

}