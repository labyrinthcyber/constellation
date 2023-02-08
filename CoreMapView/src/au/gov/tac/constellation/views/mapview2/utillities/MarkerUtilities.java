/*
 * Copyright 2010-2022 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.tac.constellation.views.mapview2.utillities;

/**
 *
 * @author altair1673
 */
public class MarkerUtilities {

    private MarkerUtilities() {

    }

    /**
     * Convert longitude to x coordinate
     *
     * @param longitude - the longitude in degrees
     * @param minLong - the left most longitude on map
     * @param mapWidth - width of map
     * @param lonDelta - different between right most longitude and left most
     * longitude
     * @return x coordinate
     */
    public static double longToX(double longitude, double minLong, double mapWidth, double lonDelta) {
        return (longitude - minLong) * (mapWidth / lonDelta);
    }

    /**
     * Convert latitude to y coordinate
     *
     * @param lattitude - latitude in degrees
     * @param mapWidth
     * @param mapHeight
     * @return y coordinate
     */
    public static double latToY(double lattitude, double mapWidth, double mapHeight) {
        lattitude = lattitude * (Math.PI / 180);
        double y = Math.log(Math.tan((Math.PI / 4) + (lattitude / 2)));
        y = (mapHeight / 2) - (mapWidth * y / (2 * Math.PI));

        return y;
    }

    /**
     * Convert x coordinate to longitude
     *
     * @param x - x coordinate
     * @param minLong - left most longitude
     * @param mapWidth
     * @param lonDelta - difference between right and left most longitude
     * @return - longitude
     */
    public static double XToLong(double x, double minLong, double mapWidth, double lonDelta) {
        double longitude = (x / (mapWidth / lonDelta)) + minLong;
        return longitude;
    }

    /**
     * Convert y coordinate to latitude
     *
     * @param y
     * @param mapWidth
     * @param mapHeight
     * @return latitude
     */
    public static double YToLat(double y, double mapWidth, double mapHeight) {

        y = ((-y + (mapHeight / 2)) * (2 * Math.PI)) / mapWidth;
        y = (Math.atan(Math.exp(y)) - (Math.PI / 4)) * 2;
        double lattitude = y / (Math.PI / 180);
        return lattitude;
    }

}
