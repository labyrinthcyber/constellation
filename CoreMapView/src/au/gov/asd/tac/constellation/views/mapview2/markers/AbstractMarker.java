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

import au.gov.asd.tac.constellation.views.mapview2.MapView;
import au.gov.asd.tac.constellation.views.mapview2.MapViewTopComponent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

/**
 *
 * @author altair1673
 */
public abstract class AbstractMarker {

    // The svg path for the actual marker
    protected final SVGPath markerPath;
    protected int markerID = 0;

    // Id list contains ids of all nodes this marker represents
    protected List<Integer> idList = new ArrayList();
    protected boolean isSelected = false;

    private double x = 0;
    private double y = 0;
    protected double xOffset;
    protected double yOffset;

    protected MapView parent;

    public static enum MarkerType {
        POINT_MARKER,
        LINE_MARKER,
        POLYGON_MARKER,
        CLUSTER_MARKER
    }

    protected MarkerType type;

    public AbstractMarker(MapView parent, int markerID, int nodeId, double xOffset, double yOffset, MarkerType type) {
        this.markerID = markerID;
        this.parent = parent;
        idList.add(nodeId);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.type = type;

        markerPath = new SVGPath();
    }

    public int getWeight() {
        return idList.size();
    }

    public MarkerType getType() {
        return type;
    }

    public void addNodeID(int id) {
        idList.add(id);
    }

    public List<Integer> getConnectedNodeIdList() {
        return idList;
    }

    public void setMarkerPosition(double mapWidth, double mapHeight) {

    }

    public Shape getMarker() {
        return markerPath;
    }

    public int getMarkerId() {
        return markerID;
    }

    protected void setX(double x) {
        this.x = x;
    }

    protected void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


}
