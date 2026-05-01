package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import view.panes.CreateProfilePane;
import view.panes.OverviewPane;
import view.panes.ReserveModulesPane;
import view.panes.SelectModulesPane;

/**
 * The Root Pane acts as the main container for the application.
 * It holds the MenuBar and the TabPane containing all sub-panes.
 */
public class ModuleSelectorRootPane extends BorderPane {

    private CreateProfilePane createProfilePane;
    private SelectModulesPane selectModulesPane;
    private ReserveModulesPane reserveModulesPane;
    private OverviewPane overviewPane;
    private ModuleSelectorMenuBar msmb;
    private TabPane tp;

    public ModuleSelectorRootPane() {
        // Initialize the TabPane and prevent tabs from being closed
        tp = new TabPane();
        tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        // Initialize sub-panes
        createProfilePane = new CreateProfilePane();
        selectModulesPane = new SelectModulesPane();
        reserveModulesPane = new ReserveModulesPane();
        overviewPane = new OverviewPane();

        // Initialize MenuBar
        msmb = new ModuleSelectorMenuBar();

        // Create Tabs and attach panes
        Tab t1 = new Tab("Create Profile", createProfilePane);
        Tab t2 = new Tab("Select Modules", selectModulesPane);
        Tab t3 = new Tab("Reserve Modules", reserveModulesPane);
        Tab t4 = new Tab("Overview", overviewPane);

        // Add tabs to the TabPane
        tp.getTabs().addAll(t1, t2, t3, t4);

        // Layout: MenuBar at top, TabPane in the center
        this.setTop(msmb);
        this.setCenter(tp);
    }

    // --- Getters for the Controller to access sub-panes ---

    public CreateProfilePane getCreateProfilePane() {
        return createProfilePane;
    }

    public SelectModulesPane getSelectModulesPane() {
        return selectModulesPane;
    }

    public ReserveModulesPane getReserveModulesPane() {
        return reserveModulesPane;
    }

    public OverviewPane getOverviewPane() {
        return overviewPane;
    }

    public ModuleSelectorMenuBar getModuleSelectorMenuBar() {
        return msmb;
    }

    public TabPane getTabPane() {
        return tp;
    }

    // --- Utility Methods for Navigation and Feedback ---

    /**
     * Changes the currently visible tab.
     * @param index 0: Create, 1: Select, 2: Reserve, 3: Overview
     */
    public void changeTab(int index) {
        tp.getSelectionModel().select(index);
    }

    /**
     * Standardized error dialog for the whole application.
     */
    public void displayAlert(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Module Selector Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}