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
package au.gov.asd.tac.constellation.views.mapview2.plugins;

import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.StoreGraph;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.SpatialConcept;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.views.mapview2.MapView;
import au.gov.asd.tac.constellation.views.mapview2.MapViewPane;
import au.gov.asd.tac.constellation.views.mapview2.MapViewTopComponent;
import au.gov.asd.tac.constellation.views.mapview2.markers.AbstractMarker;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mockito.Mockito;
import org.testfx.api.FxToolkit;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author altair1673
 */
public class ExtractCoordsFromGraphPluginNGTest {

    private static final Logger LOGGER = Logger.getLogger(ExtractCoordsFromGraphPluginNGTest.class.getName());

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (!FxToolkit.isFXApplicationThreadRunning()) {
            FxToolkit.registerPrimaryStage();
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        try {
            FxToolkit.cleanupStages();
        } catch (TimeoutException ex) {
            LOGGER.log(Level.WARNING, "FxToolkit timed out trying to cleanup stages", ex);
        }
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {

    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of read method, of class ExtractCoordsFromGraphPlugin.
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");

        final MapViewTopComponent component = Mockito.mock(MapViewTopComponent.class);
        final MapViewPane mapViewPane = Mockito.spy(new MapViewPane(component));
        final MapView mapView = Mockito.spy(new MapView(mapViewPane));

        Mockito.doReturn(mapViewPane).when(component).getMapViewPane();
        Mockito.doNothing().when(mapView).clearQueriedMarkers();
        Mockito.when(mapViewPane.getMap()).thenReturn(mapView);

        Mockito.doCallRealMethod().when(component).addMarker(Mockito.anyString(), Mockito.any(AbstractMarker.class));
        Mockito.doCallRealMethod().when(mapView).addMarkerToHashMap(Mockito.anyString(), Mockito.any(AbstractMarker.class));
        Mockito.when(mapView.getAllMarkers()).thenCallRealMethod();

        final GraphWriteMethods graph = Mockito.spy(new StoreGraph());

        final int vertexCount = 1;
        final int vertexID = 1;

        Mockito.doReturn(vertexCount).when(graph).getVertexCount();
        Mockito.doReturn(vertexID).when(graph).getVertex(0);

        final int lonID = SpatialConcept.VertexAttribute.LATITUDE.ensure(graph);
        final int latID = SpatialConcept.VertexAttribute.LONGITUDE.ensure(graph);

        Mockito.doReturn(Float.parseFloat("100")).when(graph).getObjectValue(lonID, vertexID);
        Mockito.doReturn(Float.parseFloat("100")).when(graph).getObjectValue(latID, vertexID);
        Mockito.doReturn("#000000").when(graph).getStringValue(Mockito.anyInt(), Mockito.anyInt());

        final PluginInteraction interaction = Mockito.mock(PluginInteraction.class);
        final PluginParameters parameters = Mockito.mock(PluginParameters.class);

        final ExtractCoordsFromGraphPlugin instance = new ExtractCoordsFromGraphPlugin(component);
        instance.read(graph, interaction, parameters);

        final String exp = "100.0,100.0";

        final Set<String> markerKeys = component.getMapViewPane().getMap().getAllMarkers().keySet();

        assertEquals(markerKeys.contains(exp), true);
    }

}
