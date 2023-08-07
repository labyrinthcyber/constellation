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
package au.gov.asd.tac.constellation.views.analyticview.state;

import au.gov.asd.tac.constellation.views.analyticview.AnalyticConfigurationPane.SelectableAnalyticPlugin;
import au.gov.asd.tac.constellation.views.analyticview.questions.AnalyticQuestion;
import au.gov.asd.tac.constellation.views.analyticview.questions.AnalyticQuestionDescription;
import au.gov.asd.tac.constellation.views.analyticview.results.AnalyticResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all AnalyticQuestion currently active and current results in the Analytic View.
 *
 * @author cygnus_x-1
 */
public class AnalyticViewState {

    private int currentAnalyticQuestionIndex;
    private List<AnalyticQuestionDescription<?>> activeAnalyticQuestions;
    private List<List<SelectableAnalyticPlugin>> activeSelectablePlugins;
    private AnalyticResult<?> result;
    private boolean resultsVisible = false;
    private boolean categoriesVisible = false;
    private AnalyticQuestionDescription<?> currentQuestion;
    private AnalyticQuestion question;
    private String activeCategory = "Global";

    // boolean variables: color, hide, size

    public AnalyticViewState() {
       // this(0, new ArrayList<>(), new ArrayList<>(), null, null, null, null, null);
    }

    public AnalyticViewState(final AnalyticViewState state) {
        this.currentAnalyticQuestionIndex = state.getCurrentAnalyticQuestionIndex();
        this.activeAnalyticQuestions = new ArrayList<>(state.getActiveAnalyticQuestions());
        this.activeSelectablePlugins = new ArrayList<>(state.getActiveSelectablePlugins());
        this.result = state.getResult();
        this.resultsVisible = state.isResultsPaneVisible();
        this.categoriesVisible = state.isCategoriesPaneVisible();
        this.currentQuestion = state.getCurrentQuestion();
        this.question = state.getQuestion();
        this.activeCategory = state.getActiveCategory();
    }

//    public AnalyticViewState(final int currentQuestionIndex, final List<AnalyticQuestionDescription<?>> activeQuestions,
//            final List<List<SelectableAnalyticPlugin>> activePlugins, final AnalyticResult<?> result, final AnalyticQuestionDescription<?> currentQuestion,
//            final AnalyticQuestion question, final String activeCategory, final HashMap<String, Boolean> hashmap) {
//        this.currentAnalyticQuestionIndex = currentQuestionIndex;
//        this.activeAnalyticQuestions = activeQuestions;
//        this.activeSelectablePlugins = activePlugins;
//        this.result = result;
//        this.resultsVisible = hashmap.getOrDefault("resultsVisible", false);
//        this.categoriesVisible = hashmap.getOrDefault("categoriesVisible", false);
//        this.currentQuestion = currentQuestion;
//        this.question = question;
//        this.activeCategory = activeCategory;
//        this.hashmap = hashmap;
//    }

    public String getActiveCategory() {
        return activeCategory;
    }

    public void setActiveCategory(String activeCategory) {
        this.activeCategory = activeCategory;
    }

    public AnalyticQuestion getQuestion() {
        return question;
    }

    public void setQuestion(final AnalyticQuestion question) {
        this.question = question;
    }

    public int getCurrentAnalyticQuestionIndex() {
        return currentAnalyticQuestionIndex;
    }

    public void setCurrentAnalyticQuestionIndex(final int currentAnalyticQuestionIndex) {
        this.currentAnalyticQuestionIndex = currentAnalyticQuestionIndex;
    }

    public List<AnalyticQuestionDescription<?>> getActiveAnalyticQuestions() {
        return activeAnalyticQuestions;
    }

    public AnalyticResult<?> getResult() {
        return result;
    }

    public List<List<SelectableAnalyticPlugin>> getActiveSelectablePlugins() {
        return activeSelectablePlugins;
    }

    public void setActiveSelectablePlugins(final List<List<SelectableAnalyticPlugin>> activeSelectablePlugins) {
        this.activeSelectablePlugins = activeSelectablePlugins;
    }

    public void setActiveAnalyticQuestions(final List<AnalyticQuestionDescription<?>> activeAnalyticQuestions) {
        this.activeAnalyticQuestions = activeAnalyticQuestions;
    }

    public boolean isResultsPaneVisible() {
        return resultsVisible;
    }

    public void setResultsPaneVisible(final boolean resultsVisible) {
        this.resultsVisible = resultsVisible;
    }

    public boolean isCategoriesPaneVisible() {
        return categoriesVisible;
    }

    public void setCategoriesPaneVisible(final boolean categoriesVisible) {
        this.categoriesVisible = categoriesVisible;
    }

    /**
     * Get the question which is currently selected in this pane.
     *
     * @return the current {@link AnalyticQuestionDescription}.
     */
    public final AnalyticQuestionDescription<?> getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(final AnalyticQuestionDescription<?> currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
 

    /**
     * Update the results values and record whether the results pane is currently visible
     *
     * @param newResults
     */
    public void updateResults(final AnalyticResult<?> newResults) {
        result = newResults;
        resultsVisible = result != null;
    }
}
