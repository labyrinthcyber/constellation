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

import au.gov.asd.tac.constellation.views.mapview2.MapView;
import au.gov.asd.tac.constellation.views.mapview2.markers.AbstractMarker;
import au.gov.asd.tac.constellation.views.mapview2.markers.PointMarker;
import au.gov.tac.constellation.views.mapview2.utillities.IntersectionNode;
import au.gov.tac.constellation.views.mapview2.utillities.Vec3;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author altair1673
 */
public class ThiessenPolygonsLayer extends AbstractMapLayer {

    private final Group layer;
    private final Group debugLayer;

    private ImageView imageView;
    private Image img;

    private int nodeID = 1;

    private static final Logger LOGGER = Logger.getLogger("ThiessenPolygons");

    private Map<String, AbstractMarker> markers;

    private final Map<String, IntersectionNode> intersectionMap = new HashMap<String, IntersectionNode>();
    private Map<Line, ArrayList<IntersectionNode>> lineMap = new HashMap<Line, ArrayList<IntersectionNode>>();

    private final Map<Integer, PointMarker> nodesOnScreen = new HashMap<Integer, PointMarker>();
    private Map<String, Line> bisectorLines = new HashMap<String, Line>();

    private Map<String, Line> finalBisectorLines = new HashMap<String, Line>();

    private final Line top;
    private final Line left;
    private final Line right;
    private final Line bottom;

    private String topID = "-1,-2";
    private String bottomID = "-3,-4";
    private String leftID = "-5,-6";
    private String rightID = "-7,-8";

    public ThiessenPolygonsLayer(MapView parent, int id, Map<String, AbstractMarker> markers) {
        super(parent, id);
        layer = new Group();
        debugLayer = new Group();


        top = new Line();
        top.setStartX(0);
        top.setStartY(0);
        top.setEndX(MapView.mapWidth);
        top.setEndY(0);
        top.setStroke(Color.RED);
        //top.setStrokeWidth(5);
        layer.getChildren().add(top);

        left = new Line();
        left.setStartX(0);
        left.setStartY(0);
        left.setEndX(0);
        left.setEndY(MapView.mapHeight);
        left.setStroke(Color.RED);
        //left.setStrokeWidth(5);
        layer.getChildren().add(left);

        right = new Line();
        right.setStartX(MapView.mapWidth);
        right.setStartY(0);
        right.setEndX(MapView.mapWidth);
        right.setEndY(MapView.mapHeight);
        right.setStroke(Color.RED);
        //right.setStrokeWidth(5);
        layer.getChildren().add(right);

        bottom = new Line();
        bottom.setStartX(0);
        bottom.setStartY(MapView.mapHeight);
        bottom.setEndX(MapView.mapWidth);
        bottom.setEndY(MapView.mapHeight);
        bottom.setStroke(Color.RED);
        //bottom.setStrokeWidth(5);
        layer.getChildren().add(bottom);

        this.markers = markers;

    }

    private void sortNodes(Map<String, AbstractMarker> markers) {
        nodesOnScreen.clear();

        for (AbstractMarker marker : markers.values()) {
            if (marker instanceof PointMarker) {
                PointMarker p = (PointMarker) marker;

                /*Rectangle r = new Rectangle();
                r.setWidth(5);
                r.setHeight(5);

                r.setX(p.getX() - 97);
                r.setY(p.getY() + 93);
                r.setFill(Color.RED);
                layer.getChildren().addAll(r);*/

                nodesOnScreen.put(nodeID++, p);
            }
        }

    }

    private void calculateBisectors() {
        bisectorLines.clear();
        Set<String> calculatedPairs = new HashSet<>();
        for (Integer id : nodesOnScreen.keySet()) {
            for (Integer id2 : nodesOnScreen.keySet()) {
                if (id.intValue() != id2.intValue()) {
                    String idPair = id + "," + id2;
                    String idPair2 = id2 + "," + id;

                    if (!calculatedPairs.contains(idPair) && !calculatedPairs.contains(idPair2)) {
                        calculatedPairs.add(idPair);
                        calculatedPairs.add(idPair2);

                        PointMarker node1 = nodesOnScreen.get(id);
                        PointMarker node2 = nodesOnScreen.get(id2);

                        Vec3 coords1 = new Vec3(node1.getX() - 97, node1.getY() + 93);
                        Vec3 coords2 = new Vec3(node2.getX() - 97, node2.getY() + 93);

                        Vec3 midPoint = new Vec3((coords1.x + coords2.x) / 2, (coords1.y + coords2.y) / 2);

                        /*Circle c = new Circle();
                        c.setRadius(5);

                        c.setCenterX(midPoint.x);
                        c.setCenterY(midPoint.y);
                        c.setFill(Color.BLUE);
                        layer.getChildren().addAll(c);*/


                        Vec3 slope = new Vec3((coords2.y - coords1.y), (coords2.x - coords1.x));

                        double reciprocal = -1 * (slope.y / slope.x);

                        double b = midPoint.y - (reciprocal * midPoint.x);

                        Vec3 lineStart = new Vec3(0, b);
                        Vec3 lineEnd = new Vec3(-b / reciprocal, 0);

                        double distance = Vec3.getDistance(lineStart, lineEnd);

                        Vec3 directVect = new Vec3((lineEnd.x - lineStart.x) / distance, (lineEnd.y - lineStart.y) / distance);

                        lineStart.x = midPoint.x + (directVect.x * 1000);
                        lineStart.y = midPoint.y + (directVect.y * 1000);

                        lineEnd.x = midPoint.x - (directVect.x * 1000);
                        lineEnd.y = midPoint.y - (directVect.y * 1000);

                        Line line = new Line();
                        line.setStartX(lineStart.x);
                        line.setStartY(lineStart.y);

                        line.setEndX(lineEnd.x);
                        line.setEndY(lineEnd.y);
                        
                        line.setStroke(Color.RED);
                        bisectorLines.put(idPair, line);
                    }
                }
            }
        }
    }

    private void shortenBisectorLines() {
        for (String key : bisectorLines.keySet()) {
            Integer id1 = Integer.parseInt(key.split(",")[0]);
            Integer id2 = Integer.parseInt(key.split(",")[1]);

            Line bisector = bisectorLines.get(key);

            Vec3 start = new Vec3(bisector.getStartX(), bisector.getStartY());
            Vec3 end = new Vec3(bisector.getEndX(), bisector.getEndY());

            double distance = Vec3.getDistance(start, end);
            Vec3 dirVect = new Vec3((end.x - start.x) / distance, (end.y - start.y) / distance);

            Vec3[] shortLine = {null, null};

            int index = 0;

            Vec3 marker1 = new Vec3(nodesOnScreen.get(id1).getX() - 97, nodesOnScreen.get(id1).getY() + 93);
            Vec3 marker2 = new Vec3(nodesOnScreen.get(id2).getX() - 97, nodesOnScreen.get(id2).getY() + 93);
            for (int i = 0; i < distance; ++i) {

                if (start.x > MapView.mapWidth + 5 || start.x < -5 || start.y < -2 || start.y > MapView.mapHeight + 2) {
                    start.x += dirVect.x;
                    start.y += dirVect.y;
                    continue;
                }

                Integer shortestDistanceID = null;
                for (Integer id : nodesOnScreen.keySet()) {
                    if (id.intValue() != id1.intValue() && id.intValue() != id2.intValue()) {
                        Vec3 markerPos = new Vec3(nodesOnScreen.get(id).getX() - 97, nodesOnScreen.get(id).getY() + 93);

                        if (Vec3.getDistance(start, markerPos) < Vec3.getDistance(start, marker1) && Vec3.getDistance(start, markerPos) < Vec3.getDistance(start, marker2)) {
                            shortestDistanceID = id;
                            break;
                        }
                    }

                }

                if (shortestDistanceID == null && index == 0) {
                    shortLine[index] = new Vec3(start.x, start.y);
                    ++index;
                } else if (shortestDistanceID != null && index == 1) {
                    start.x += dirVect.x;
                    start.y += dirVect.y;
                    shortLine[0].x -= dirVect.x;
                    shortLine[0].y -= dirVect.y;
                    shortLine[index] = new Vec3(start.x, start.y);

                    break;
                }

                start.x += dirVect.x;
                start.y += dirVect.y;


            }


            if (shortLine[1] == null && shortLine[0] != null) {
                //LOGGER.log(Level.SEVERE, "Line end is null");
                shortLine[1] = end;
            }



            if (shortLine[0] != null && shortLine[1] != null) {
                Line l = new Line();
                l.setStartX(shortLine[0].x);
                l.setStartY(shortLine[0].y);

                l.setEndX(shortLine[1].x);
                l.setEndY(shortLine[1].y);

                finalBisectorLines.put(key, l);
                lineMap.put(finalBisectorLines.get(key), new ArrayList<IntersectionNode>());

            }

        }
        //finalBisectorLines.add(top);
        //finalBisectorLines.add(bottom);
        //finalBisectorLines.add(left);
        //finalBisectorLines.add(right);

        finalBisectorLines.put(topID, top);
        finalBisectorLines.put(bottomID, bottom);
        finalBisectorLines.put(leftID, left);
        finalBisectorLines.put(rightID, right);

        lineMap.put(finalBisectorLines.get(topID), new ArrayList<IntersectionNode>());
        lineMap.put(finalBisectorLines.get(bottomID), new ArrayList<IntersectionNode>());
        lineMap.put(finalBisectorLines.get(leftID), new ArrayList<IntersectionNode>());
        lineMap.put(finalBisectorLines.get(rightID), new ArrayList<IntersectionNode>());

    }

    private void calculateIntersectionCircles() {
        boolean first = true;
        int duplicateIntersectionPoint = 0;
        //Map<String, String> bisectPairs = new HashMap<String, String>();
        Set<String> intersections = new HashSet<String>();
        for (String bisectID1 : finalBisectorLines.keySet()) {
            boolean validIntersection = false;
            Line bisect1 = finalBisectorLines.get(bisectID1);
            String intersectionPoint;

            if (first) {
                bisect1.setStroke(Color.PURPLE);
                first = false;
            }

            for (String bisectID2 : finalBisectorLines.keySet()) {

                Line bisect2 = finalBisectorLines.get(bisectID2);

                if ((bisectID1.equals(topID) && bisectID2.equals(bottomID)) || (bisectID1.equals(bottomID) && bisectID2.equals(topID))) {
                    continue;
                }
                if ((bisectID1.equals(leftID) && bisectID2.equals(rightID)) || (bisectID1.equals(rightID) && bisectID2.equals(leftID))) {
                    continue;
                }

                if (!bisectID1.equals(bisectID2) && doesIntersect(bisect1, bisect2)) {
                    validIntersection = false;
                    Vec3 slope = new Vec3((bisect1.getEndY() - bisect1.getStartY()), (bisect1.getEndX() - bisect1.getStartX()));
                    // y = mx + b
                    double m1 = slope.x / slope.y;

                    double b1 = bisect1.getStartY() - (m1 * bisect1.getStartX());

                    Vec3 slope2 = new Vec3((bisect2.getEndY() - bisect2.getStartY()), (bisect2.getEndX() - bisect2.getStartX()));

                    double m2 = slope2.x / slope2.y;

                    double b2 = bisect2.getStartY() - (m2 * bisect2.getStartX());

                    final double x = (b2 - b1) / (m1 - m2);

                    final double y = m1 * x + b1;

                    final double roundedX = Math.round(x * 10000) / 10000;
                    final double roundedY = Math.round(y * 10000) / 10000;


                    intersectionPoint = roundedX + "," + roundedY;


                    if (intersectionPoint.equals("NaN,NaN")) {
                        LOGGER.log(Level.SEVERE, "Intersection point is nan");
                        continue;
                    }

                    if (!(bisect1.contains(x, y) && bisect2.contains(x, y))) {
                        continue;
                    }

                    validIntersection = true;

                    LOGGER.log(Level.SEVERE, "Intersection point is: " + intersectionPoint);

                    if (!intersectionMap.containsKey(intersectionPoint)) {
                        IntersectionNode intersectionNode = new IntersectionNode(roundedX, roundedY);
                        intersectionNode.addContainedPoint(x, y);
                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID1.split(",")[0]));
                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID1.split(",")[1]));

                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID2.split(",")[0]));
                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID2.split(",")[1]));


                        intersectionMap.put(intersectionPoint, intersectionNode);
                    } else {
                        IntersectionNode intersectionNode = intersectionMap.get(intersectionPoint);
                        intersectionNode.addContainedPoint(x, y);
                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID1.split(",")[0]));
                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID1.split(",")[1]));

                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID2.split(",")[0]));
                        intersectionNode.addRelevantMarker(Integer.parseInt(bisectID2.split(",")[1]));

                    }

                    if (!intersections.contains(intersectionPoint)) {
                        intersections.add(intersectionPoint);
                        Circle c = new Circle();

                        c.setRadius(5);
                        c.setCenterX(roundedX);
                        c.setCenterY(roundedY);

                        c.setFill(Color.GREEN);
                        c.setOpacity(0.5);

                        c.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                printGraph(intersectionMap.get(roundedX + "," + roundedY));
                            }

                        });

                        layer.getChildren().add(c);
                    }

                    if ((bisect2.getEndY() - bisect2.getStartY()) / (bisect2.getEndX() - bisect2.getEndY()) == 0) {
                        LOGGER.log(Level.SEVERE, "Dealing with horizontal line");
                    }

                    if (!lineMap.get(bisect2).contains(intersectionMap.get(intersectionPoint))) {
                        lineMap.get(bisect2).add(intersectionMap.get(intersectionPoint));
                    } else {
                        LOGGER.log(Level.SEVERE, "Duplicate: " + (++duplicateIntersectionPoint));
                    }

                    for (int i = 0; i < lineMap.get(bisect2).size(); ++i) {
                        lineMap.get(bisect2).get(i).addConnectedPoint(intersectionMap.get(intersectionPoint));
                        }
                    }


            }

        }


    }

    private void addCornerIntersectionNodes() {
        IntersectionNode topLeft = new IntersectionNode(0, 0);
        IntersectionNode topRight = new IntersectionNode(MapView.mapWidth, 0);
        IntersectionNode bottomLeft = new IntersectionNode(0, MapView.mapHeight);
        IntersectionNode bottomRight = new IntersectionNode(MapView.mapWidth, MapView.mapHeight);

        intersectionMap.put("-1,-1", topLeft);
        intersectionMap.put("-2,-2", topRight);
        intersectionMap.put("-3,-3", bottomLeft);
        intersectionMap.put("-4,-4", bottomRight);

    }

    private boolean contains(Line line, double x, double y) {
        double maxX = line.getStartX() > line.getEndX() ? line.getStartX() : line.getEndX();
        double minX = line.getStartX() < line.getEndX() ? line.getStartX() : line.getEndX();

        double maxY = line.getStartY() > line.getEndY() ? line.getStartY() : line.getEndY();
        double minY = line.getStartY() < line.getEndY() ? line.getStartY() : line.getEndY();

        if (x < minX || x > maxX) {
            return false;
        }

        if (y > maxY || y < minY) {
            return false;
        }

        Vec3 slope = new Vec3((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX()));
        // y = mx + b
        double m = slope.x / slope.y;

        double b = line.getStartY() - (m * line.getStartX());

        return (m * x + b) == y;

    }

    /*private void connectIntersections()
    {
        for(IntersectionNode n : intersectionMap.values())
        {
            for(Line l : finalBisectorLines.values())
            {
                if(l.co)
            }
        }
    }*/

    private Set<String> visited = new HashSet<String>();

    private void printGraph(IntersectionNode node) {
        visited.clear();
        debugLayer.getChildren().clear();
        if (visited.contains(node.getKey())) {
            return;
        }

        visited.add(node.getKey());

        Circle c = new Circle();

        c.setRadius(5);
        c.setCenterX(node.getX());
        c.setCenterY(node.getY());

        /*for (Line l : finalBisectorLines.values()) {
            if (l.contains(node.getX(), node.getY())) {
                //l.setStroke(Color.CORAL);
            }
        }*/

        c.setFill(Color.BLACK);

        debugLayer.getChildren().add(c);


        for (IntersectionNode connected : node.getConnectedPoints()) {

            Circle c2 = new Circle();

            c2.setRadius(5);
            c2.setCenterX(connected.getX());
            c2.setCenterY(connected.getY());
            c2.setMouseTransparent(true);
            c2.setFill(Color.DARKGOLDENROD);
            //c2.setOpacity(0.25);
            debugLayer.getChildren().add(c2);

            //printGraph(connected);
        }

    }

    private void showRelatedMarkers() {
        for (IntersectionNode node : intersectionMap.values()) {
            for (int i = 0; i < node.getRelevantMarkers().size(); ++i) {
                if (nodesOnScreen.containsKey(node.getRelevantMarkers().get(i))) {
                    AbstractMarker m = nodesOnScreen.get(node.getRelevantMarkers().get(i));

                    if (m instanceof PointMarker) {
                        PointMarker p = (PointMarker) m;

                        double startX = node.getX();
                        double startY = node.getY();

                        double endX = p.getX() - 97;
                        double endY = p.getY() + 93;

                        Line l = new Line();

                        l.setStartX(startX);
                        l.setStartY(startY);
                        l.setEndX(endX);
                        l.setEndY(endY);

                        l.setStroke(Color.DARKMAGENTA);

                        layer.getChildren().add(l);
                    }
                }
            }
        }
    }

    private boolean doesIntersect(Line l1, Line l2) {

        return l1.intersects(l2.getBoundsInLocal()) || l2.contains(l1.getStartX(), l1.getStartY()) || l2.contains(l1.getEndX(), l1.getEndY());

    }

    @Override
    public void setUp() {
        layer.getChildren().clear();

        sortNodes(markers);
        calculateBisectors();
        shortenBisectorLines();

        /*finalBisectorLines.put("-1,-2", top);
        finalBisectorLines.put("-3,-4", bottom);
        finalBisectorLines.put("-5,-6", left);
        finalBisectorLines.put("-7,-8", right);*/

        calculateIntersectionCircles();
        addCornerIntersectionNodes();
        //intersectionMap.remove(intersectionMap.entrySet().iterator().next().getValue().getKey());
        Map.Entry<String, IntersectionNode> entry = intersectionMap.entrySet().iterator().next();
        //printGraph(entry.getValue());
        //showRelatedMarkers();

        for (Line line : finalBisectorLines.values()) {
            layer.getChildren().add(line);
        }

        Line line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(201);
        line.setEndY(200);
        line.setStroke(Color.RED);
        layer.getChildren().add(line);
        layer.getChildren().add(debugLayer);

        LOGGER.log(Level.SEVERE, "Intersection Map Count: " + intersectionMap.size());

        /*Rectangle border = new Rectangle();
        border.setX(0);
        border.setY(0);
        border.setWidth(MapView.mapWidth);
        border.setHeight(MapView.mapHeight);
        border.setStroke(Color.GREEN);
        border.setStrokeWidth(10);
        border.setOpacity(0);
        layer.getChildren().add(border);*/

        //layer.getChildren().addAll(imageView);
    }

    @Override
    public Group getLayer() {
        return layer;
    }
}