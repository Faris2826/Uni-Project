package view.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Module;

/**
 * The ReserveModulesPane allows students to select backup modules 
 * in case their primary selections are full.
 * DISTINCTION: Proper resizing with Priority.ALWAYS
 */
public class ReserveModulesPane extends BorderPane {

    private ListView<Module> lvUnselectedModules, lvReservedModules;
    private Button btnAddReserve, btnRemoveReserve, btnConfirmReserve;

    public ReserveModulesPane() {
        // --- Styling ---
        this.setPadding(new Insets(20));

        // --- 1. Selection Area (Center) ---
        lvUnselectedModules = new ListView<>();
        lvReservedModules = new ListView<>();

        // Interaction Buttons
        btnAddReserve = new Button("Add Reserve >>");
        btnAddReserve.setMinWidth(120);

        btnRemoveReserve = new Button("<< Remove Reserve");
        btnRemoveReserve.setMinWidth(120);

        VBox vbButtons = new VBox(10, btnAddReserve, btnRemoveReserve);
        vbButtons.setAlignment(Pos.CENTER);

        // Create containers for lists with labels
        VBox vbAvailable = new VBox(5, new Label("Available Optional Modules:"), lvUnselectedModules);
        VBox vbReserved = new VBox(5, new Label("Reserved Modules:"), lvReservedModules);

        // Layout the two lists with buttons in the middle
        HBox hbSelection = new HBox(15, 
            vbAvailable,
            vbButtons,
            vbReserved
        );

        // Allow lists AND their containers to expand to fill window
        HBox.setHgrow(vbAvailable, Priority.ALWAYS);
        HBox.setHgrow(vbReserved, Priority.ALWAYS);
        VBox.setVgrow(lvUnselectedModules, Priority.ALWAYS);
        VBox.setVgrow(lvReservedModules, Priority.ALWAYS);

        // --- 2. Bottom Controls ---
        btnConfirmReserve = new Button("Confirm Reserves");
        
        HBox hbBottom = new HBox(btnConfirmReserve);
        hbBottom.setAlignment(Pos.CENTER);
        hbBottom.setPadding(new Insets(20, 0, 0, 0));

        // --- Assembly ---
        this.setCenter(hbSelection);
        this.setBottom(hbBottom);
    }

    // --- Accessor Methods (Strict MVC) ---

    public ListView<Module> getUnselectedListView() {
        return lvUnselectedModules;
    }

    public ListView<Module> getReservedListView() {
        return lvReservedModules;
    }

    public Button getAddReserveBtn() {
        return btnAddReserve;
    }

    public Button getRemoveReserveBtn() {
        return btnRemoveReserve;
    }

    public Button getConfirmReserveBtn() {
        return btnConfirmReserve;
    }

    /**
     * Helper to clear lists when starting a new profile or resetting.
     */
    public void clearPane() {
        lvUnselectedModules.getItems().clear();
        lvReservedModules.getItems().clear();
    }
}
