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
package au.gov.asd.tac.constellation.views.mapview2.overlays;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author altair1673
 */
public class ToolsOverlay extends AbstractOverlay {

    final private BorderPane overlayPane;

    final private GridPane gridPane;

    private boolean drawingEnabled = false;
    private boolean measureEnabled = false;

    private int height = 75;
    private int width = 150;

    private final String[] units = {"km", "nmi", "mi"};
    private int unitSelected = 0;
    public ToolsOverlay() {
        super();

        overlayPane = new BorderPane();
        gridPane = new GridPane();

        overlayPane.setCenter(gridPane);

        Label measureText = new Label("Measure");
        measureText.setTextFill(Color.WHITE);

        Label measureToggleText = new Label("Disabled");
        measureToggleText.setTextFill(Color.WHITE);
        measureToggleText.setBackground(Background.fill(Color.BLACK));

        measureToggleText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                measureEnabled = !measureEnabled;

                if (measureEnabled) {
                    measureToggleText.setText("Enabled");
                } else {
                    measureToggleText.setText("Disabled");
                }

                event.consume();
            }
        });

        Label measureUnitText = new Label(units[unitSelected]);
        measureUnitText.setTextFill(Color.WHITE);
        measureUnitText.setBackground(Background.fill(Color.BLACK));

        measureUnitText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ++unitSelected;

                if (unitSelected > units.length - 1) {
                    unitSelected = 0;
                }
                measureUnitText.setText(units[unitSelected]);
                event.consume();
            }
        });

        Label drawLabelText = new Label("Draw");
        drawLabelText.setTextFill(Color.WHITE);

        Label drawToggleText = new Label("Disabled");
        drawToggleText.setTextFill(Color.WHITE);
        drawToggleText.setBackground(Background.fill(Color.BLACK));

        drawToggleText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drawingEnabled = !drawingEnabled;

                if (drawingEnabled) {
                    drawToggleText.setText("Enabled");
                } else {
                    drawToggleText.setText("Disabled");
                }

                event.consume();
            }
        });

        Label drawSymbol = new Label("+");
        drawSymbol.setTextFill(Color.WHITE);
        drawSymbol.setBackground(Background.fill(Color.BLACK));

        gridPane.add(measureText, 0, 0);
        gridPane.add(measureToggleText, 1, 0);
        gridPane.add(measureUnitText, 2, 0);

        gridPane.add(drawLabelText, 0, 1);
        gridPane.add(drawToggleText, 1, 1);
        gridPane.add(drawSymbol, 2, 1);

        gridPane.setPadding(new Insets(0, 0, 0, 10));

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        overlayPane.setPrefHeight(height);
        overlayPane.setPrefWidth(width);
        overlayPane.setMinWidth(width);
        overlayPane.setMaxWidth(width);
        overlayPane.setMinHeight(height);
        overlayPane.setMaxHeight(height);

        overlayPane.setBackground(Background.fill(new Color(0.224, 0.239, 0.278, 1.0)));

        overlayPane.setTranslateX(415);
        overlayPane.setTranslateY(-550);

    }

    public BorderPane getOverlayPane() {
        return overlayPane;
    }
}
