/*
 * Copyright 2010-2021 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.views.notes.state;

import au.gov.asd.tac.constellation.plugins.reporting.PluginReport;
import au.gov.asd.tac.constellation.plugins.reporting.PluginReportListener;
import au.gov.asd.tac.constellation.views.notes.utilities.MarkdownTree;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Holds the information for a note in the Notes View.
 *
 * @author sol695510
 */
public class NotesViewEntry implements PluginReportListener {

    private int id = -99;
    private static final Logger LOGGER = Logger.getLogger(NotesViewEntry.class.getName());

    private final String dateTime;
    private String noteTitle;
    private String noteContent;

    private String nodeColour = "#942483";
    private final boolean userCreated;
    private Boolean graphAttribute;
    private List<Integer> nodesSelected;
    private List<Integer> transactionsSelected;
    private List<String> tags = new ArrayList<>();
    private boolean editMode;
    private boolean isShowing = true;

    private TextFlow contentTextFlow;

    private final BooleanProperty calcChangeHeight = new SimpleBooleanProperty(true);

    public NotesViewEntry(final String dateTime, final String noteTitle, final String noteContent, final boolean userCreated, final boolean graphAttribute, final String nodeColour) {
        this.dateTime = dateTime;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;

        if (nodeColour != null) {
            this.nodeColour = nodeColour;
        }
        this.userCreated = userCreated;
        this.graphAttribute = graphAttribute;
        this.editMode = false;
        if (userCreated) {
            this.nodesSelected = new ArrayList<>();
            this.transactionsSelected = new ArrayList<>();
        }

    }

    public TextFlow getContentTextFlow() {
        return contentTextFlow;
    }

    public void setContentTextFlow(TextFlow contentTextFlow) {
        this.contentTextFlow = contentTextFlow;
    }


    public String getDateTime() {
        return dateTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public boolean isUserCreated() {
        return userCreated;
    }

    public Boolean isGraphAttribute() {
        return graphAttribute;
    }

    public void setGraphAttribute(final boolean graphAttribute) {
        this.graphAttribute = graphAttribute;
    }

    public List<Integer> getNodesSelected() {
        return nodesSelected;
    }

    public List<Integer> getTransactionsSelected() {
        return transactionsSelected;
    }

    public void setNodesSelected(final List<Integer> nodesSelected) {
        this.nodesSelected = nodesSelected;
    }

    public void setTransactionsSelected(final List<Integer> transactionsSelected) {
        this.transactionsSelected = transactionsSelected;
    }

    public void setNoteTitle(final String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(final String noteContent) {
        this.noteContent = noteContent;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getNodeColour() {
        return nodeColour;
    }

    public void setNodeColour(final String nodeColour) {
        this.nodeColour = nodeColour;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(final boolean editMode) {
        this.editMode = editMode;
    }


    public void setShowing(final boolean showing) {
        isShowing = showing;
    }

    public boolean getShowing() {
        return isShowing;
    }

    public int getID() {
        return id;
    }

    public void setID(final int id) {
        this.id = id;

    }

    public void refreshTextFlow() {
        final MarkdownTree md = new MarkdownTree(noteTitle + "\n\n" + noteContent);
        md.parse();
        final TextFlow t = md.getRenderedText();
        contentTextFlow.getChildren().clear();
        LOGGER.log(Level.SEVERE, "Cleared text flow");
        /*for (int i = 0; i < t.getChildren().size(); i++) {
            if (t.getChildren().get(i) instanceof Text) {
                Text tx = (Text) t.getChildren().get(i);
                LOGGER.log(Level.SEVERE, "re-adding text: " + tx.getText());
            }
            contentTextFlow.getChildren().add(t.getChildren().get(i));
        }*/
        contentTextFlow = t;
        contentTextFlow.autosize();
    }

    public BooleanProperty getCalcChangeHeight() {
        return calcChangeHeight;
    }


    @Override
    public void pluginReportChanged(final PluginReport pluginReport) {
        this.noteContent = pluginReport.getMessage();
    }

    @Override
    public void addedChildReport(final PluginReport parentReport, final PluginReport childReport) {
        // Intentionally left blank. Ignoring child plugin reports.
    }
}
