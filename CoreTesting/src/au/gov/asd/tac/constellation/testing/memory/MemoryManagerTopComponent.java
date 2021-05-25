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
package au.gov.asd.tac.constellation.testing.memory;

import au.gov.asd.tac.constellation.utilities.memory.MemoryManager;
import au.gov.asd.tac.constellation.utilities.memory.MemoryManager.ClassStats;
import au.gov.asd.tac.constellation.utilities.memory.MemoryManagerListener;
import au.gov.asd.tac.constellation.utilities.text.SeparatorConstants;
import java.util.Map;
import java.util.Map.Entry;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//au.gov.asd.tac.constellation.testing.memory//MemoryManager//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "MemoryManagerTopComponent",
        iconBase = "au/gov/asd/tac/constellation/testing/memory/resources/memory-manager.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
        mode = "rightSlidingSide",
        openAtStartup = false
)
@ActionID(
        category = "Window",
        id = "au.gov.asd.tac.constellation.testing.memory.MemoryManagerTopComponent"
)
@ActionReference(path = "Menu/Experimental/Views", position = 0)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MemoryManagerAction",
        preferredID = "MemoryManagerTopComponent"
)
@Messages({
    "CTL_MemoryManagerAction=Memory Manager",
    "CTL_MemoryManagerTopComponent=Memory Manager",
    "HINT_MemoryManagerTopComponent=Memory Manager"
})
public final class MemoryManagerTopComponent extends TopComponent implements MemoryManagerListener {

    public MemoryManagerTopComponent() {
        initComponents();
        setName(Bundle.CTL_MemoryManagerTopComponent());
        setToolTipText(Bundle.HINT_MemoryManagerTopComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        objectCountsTextArea = new javax.swing.JTextArea();

        objectCountsTextArea.setEditable(false);
        objectCountsTextArea.setColumns(20);
        objectCountsTextArea.setRows(5);
        jScrollPane1.setViewportView(objectCountsTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea objectCountsTextArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        MemoryManager.addMemoryManagerListener(this);
        updateObjectCounts();
    }

    @Override
    public void componentClosed() {
        MemoryManager.removeMemoryManagerListener(this);
    }

    void writeProperties(java.util.Properties p) {
        // Method intentionally left blank
    }

    void readProperties(java.util.Properties p) {
        // Method intentionally left blank
    }

    @Override
    public void newObject(Class<?> c) {
        updateObjectCounts();
    }

    @Override
    public void finalizeObject(Class<?> c) {
        updateObjectCounts();
    }

    private void updateObjectCounts() {

        StringBuilder result = new StringBuilder();

        Map<Class<?>, ClassStats> counts = MemoryManager.getObjectCounts();
        for (Entry<Class<?>, ClassStats> e : counts.entrySet()) {
            Class<?> c = e.getKey();
            ClassStats stats = e.getValue();
            result.append(c.getCanonicalName());
            result.append(": current = ");
            result.append(stats.getCurrentCount());
            result.append(", total = ");
            result.append(stats.getTotalCount());
            result.append(", max = ");
            result.append(stats.getMaxCount());
            result.append(SeparatorConstants.NEWLINE);
        }

        objectCountsTextArea.setText(result.toString());
    }
}
