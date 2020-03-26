package net.axay.blueutils.geometry;

import java.util.ArrayList;
import java.util.List;

public class Circle {

    public static List<Location2D> getCircle(int radius) {
        return getCircle(0, 0, radius);
    }

    @SuppressWarnings("DuplicatedCode")
    public static List<Location2D> getCircle(int x_loc, int y_loc, int radius) {

        List<Location2D> locationList = new ArrayList<>();

        while (radius >= 0) {

            int d = -radius;
            int x = radius;
            int y = 0;

            while (y <= x) {

                locationList.add(new Location2D(x_loc + x, y_loc + y));
                locationList.add(new Location2D(x_loc + x, y_loc - y));
                locationList.add(new Location2D(x_loc - x, y_loc + y));
                locationList.add(new Location2D(x_loc - x, y_loc - y));
                locationList.add(new Location2D(x_loc + y, y_loc + x));
                locationList.add(new Location2D(x_loc + y, y_loc - x));
                locationList.add(new Location2D(x_loc - y, y_loc + x));
                locationList.add(new Location2D(x_loc - y, y_loc - x));

                d = d + 2*y + 1;
                y = y + 1;

                if (d > 0) {
                    d = d - 2*x + 2;
                    x = x - 1;
                }

            }

            radius--;

        }

        return locationList;

    }

}
