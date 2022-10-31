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
package au.gov.asd.tac.constellation.views.mapview2.markers;

import au.gov.asd.tac.constellation.views.mapview2.MapViewTopComponent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.SVGPath;

/**
 *
 * @author altair1673
 */
public abstract class AbstractMarker {
    protected int markerID = 0;
    protected List<Integer> idList = new ArrayList();
    protected boolean isSelected = false;

    protected int xOffset;
    protected int yOffset;

    protected MapViewTopComponent parentComponent;

    public AbstractMarker(MapViewTopComponent parentComponent, int markerID, int nodeId, int xOffset, int yOffset) {
        this.markerID = markerID;
        this.parentComponent = parentComponent;
        idList.add(nodeId);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getWeight() {
        return idList.size();
    }

    public void addNodeID(int id) {
        idList.add(id);
    }

    public List<Integer> getIdList() {
        return idList;
    }

    protected double longToX(double longitude, double minLong, double mapWidth, double lonDelta) {
        return (longitude - minLong) * (mapWidth / lonDelta);
    }

    protected double latToY(double lattitude, double mapWidth, double mapHeight) {
        lattitude = lattitude * (Math.PI / 180);
        double y = Math.log(Math.tan((Math.PI / 4) + (lattitude / 2)));
        y = (mapHeight / 2) - (mapWidth * y / (2 * Math.PI));

        return y;
    }

    public void setMarkerPosition(double mapWidth, double mapHeight) {

    }

    public SVGPath getMarker() {
        return null;
    }

    public int getMarkerId() {
        return markerID;
    }
}