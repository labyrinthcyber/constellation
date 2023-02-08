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

import au.gov.asd.tac.constellation.graph.GraphConstants;
import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.SpatialConcept;
import au.gov.asd.tac.constellation.graph.schema.type.SchemaVertexType;
import au.gov.asd.tac.constellation.utilities.datastructure.Tuple;
import au.gov.asd.tac.constellation.views.mapview.utilities.GraphElement;
import au.gov.asd.tac.constellation.views.mapview2.MapView;
import au.gov.asd.tac.constellation.views.mapview2.markers.AbstractMarker;
import au.gov.asd.tac.constellation.views.mapview2.markers.LineMarker;
import au.gov.asd.tac.constellation.views.mapview2.markers.PointMarker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author altair1673
 */
public class LocationPathsLayer extends AbstractPathsLayer {

    final double lineMarkerXOffset = 1;
    final double lineMarkerYOffset = 149;

    public LocationPathsLayer(MapView parent, int id, Map<String, AbstractMarker> queriedMarkers) {
        super(parent, id, queriedMarkers);
    }

    @Override
    public void setUp() {
        entityPaths.getChildren().clear();
        GraphReadMethods graph = parent.getCurrentGraph().getReadableGraph();
        final List<Integer> idList = new ArrayList<Integer>();

        // For every queried markers add all its connected neighbours to the idList
        for (Object value : queriedMarkers.values()) {
            AbstractMarker m = (AbstractMarker) value;

            if (m instanceof PointMarker) {
                m.getConnectedNodeIdList().forEach(id -> idList.add(id));
            }
        }

        // Get the the lattiuide,longitude and type attribute ID
        final int lonID2 = SpatialConcept.VertexAttribute.LONGITUDE.get(graph);
        final int latID2 = SpatialConcept.VertexAttribute.LATITUDE.get(graph);
        final int vertexTypeAttributeId = AnalyticConcept.VertexAttribute.TYPE.get(graph);

        // For every connected vertex
        for (int i = 0; i < idList.size(); ++i) {

            final int vertexID = idList.get(i);

            // Get the schema vertex type
            final SchemaVertexType vertexType = graph.getObjectValue(vertexTypeAttributeId, vertexID);

            // If the type is of "location"
            if (vertexType != null && vertexType.isSubTypeOf(AnalyticConcept.VertexType.LOCATION)) {

                // Get the ceighbour count of the vertex
                final int neighbourCount = graph.getVertexNeighbourCount(vertexID);

                // Loop through its neighbours
                for (int neighbourPosition = 0; neighbourPosition < neighbourCount; neighbourPosition++) {
                    final int neighbourId = graph.getVertexNeighbour(vertexID, neighbourPosition);
                    final SchemaVertexType neighbourType = graph.getObjectValue(vertexTypeAttributeId, neighbourId);
                    if (neighbourType != null && neighbourType.isSubTypeOf(AnalyticConcept.VertexType.LOCATION)) {

                        // Get the linkID
                        final int neighbourLinkId = graph.getLink(vertexID, neighbourId);

                        // See which way the transaction is going
                        final int outgoingDirection = vertexID < neighbourId ? GraphConstants.UPHILL : GraphConstants.DOWNHILL;

                        // Get the count of outgoing transactions
                        final int linkOutgoingTransactionCount = graph.getLinkTransactionCount(neighbourLinkId, outgoingDirection);

                        // For every one of those transacttions
                        for (int j = 0; j < linkOutgoingTransactionCount; ++j) {

                            // Get coordinates of both vertexes int transaction
                            final float sourceLat = graph.getObjectValue(latID2, vertexID);
                            final float sourceLon = graph.getObjectValue(lonID2, vertexID);

                            final float destLat = graph.getObjectValue(latID2, neighbourId);
                            final float destLon = graph.getObjectValue(lonID2, neighbourId);

                            String coordinateKey = (double) sourceLat + "," + (double) sourceLon + "," + (double) destLat + "," + (double) destLon;

                            // Draw line beteeen the two vertices
                            LineMarker l = new LineMarker(parent, parent.getNewMarkerID(), vertexID, (float) sourceLat, (float) sourceLon, (float) destLat, (float) destLon, lineMarkerXOffset, lineMarkerYOffset);
                            if (!parent.getAllMarkers().keySet().contains(coordinateKey)) {
                                //parent.addMarkerToHashMap(coordinateKey, l);

                                l.setMarkerPosition(MapView.mapWidth, MapView.mapHeight);
                                entityPaths.getChildren().add(l.getMarker());
                            } else {
                                //parent.getAllMarkers().get(coordinateKey).addNodeID(vertexID);
                            }
                        }
                    }
                }
            }
        }
    }

}
