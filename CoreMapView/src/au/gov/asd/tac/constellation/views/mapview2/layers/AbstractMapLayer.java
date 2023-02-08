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
package au.gov.asd.tac.constellation.views.mapview2.layers;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.views.mapview2.MapView;
import javafx.scene.Group;

/**
 *
 * @author altair1673
 */
public abstract class AbstractMapLayer {

    protected MapView parent;
    protected Graph currentGraph = null;
    protected boolean isShowing = false;

    protected int id;

    public AbstractMapLayer(MapView parent, int id) {
        this.parent = parent;
        currentGraph = parent.getCurrentGraph();
        this.id = id;
    }

    // Class to hold a location in lattitude and longitude and its corresponding x and y values
    protected class Location {

        public double lat;
        public double lon;

        public double x = 0;
        public double y = 0;

        public Location(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;

            x = lon;
            y = lat;

        }
    }

    public void setUp() {
    }

    public boolean getIsShowing() {
        return isShowing;
    }

    public int getId() {
        return id;
    }

    public void setIsShowing(boolean showing) {
        isShowing = showing;
    }

    public Group getLayer() {
        return null;
    }


    /*protected double longToX(double longitude, double minLong, double mapWidth, double lonDelta) {
        return (longitude - minLong) * (mapWidth / lonDelta);
    }

    protected double latToY(double lattitude, double mapWidth, double mapHeight) {
        lattitude = lattitude * (Math.PI / 180);
        double y = Math.log(Math.tan((Math.PI / 4) + (lattitude / 2)));
        y = (mapHeight / 2) - (mapWidth * y / (2 * Math.PI));

        return y;
    }*/

}
