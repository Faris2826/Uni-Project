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
 * The SelectModulesPane handles the complex task of choosing optional modules.
 * It features a split layout for clear distinction between blocks and selections.
 * DISTINCTION: Proper resizing with Priority.ALWAYS
 */
public class SelectModulesPane extends BorderPane {

    // ListViews for different module categories
    private ListView<Module> lvUnselectedModules, lvSelectedModules;
    private ListView<Module> lvBlock1Modules, lvBlock2Modules;
    
    // Control buttons
    private Button btnAdd, btnRemove, btnReset, btnSubmit;
    
    // Feedback label
    private Label lblTotalCredits;

    public SelectModulesPane() {
        // --- Styling ---
        this.setPadding(new Insets(20));

        // --- 1. Top/Left: Fixed Modules (Block 1 & 2) ---
        // These are usually 'read-only' in this view to show core modules
        lvBlock1Modules = new ListView<>();
        lvBlock1Modules.setPrefHeight(150);
        
        lvBlock2Modules = new ListView<>();
        lvBlock2Modules.setPrefHeight(150);

        VBox vbLeft = new VBox(10, 
            new Label("Mandatory Block 1 Modules (Read-only):"), lvBlock1Modules,
            new Label("Mandatory Block 2 Modules (Read-only):"), lvBlock2Modules
        );
        vbLeft.setPrefWidth(300);
        VBox.setVgrow(lvBlock1Modules, Priority.ALWAYS);
        VBox.setVgrow(lvBlock2Modules, Priority.ALWAYS);

        // --- 2. Center: The Selection Area ---
        lvUnselectedModules = new ListView<>();
        lvSelectedModules = new ListView<>();

        // Buttons for moving modules
        btnAdd = new Button("Add >>");
        btnAdd.setMinWidth(100);
        
        btnRemove = new Button("<< Remove");
        btnRemove.setMinWidth(100);

        VBox vbSelectionButtons = new VBox(10, btnAdd, btnRemove);
        vbSelectionButtons.setAlignment(Pos.CENTER);

        VBox vbUnselected = new VBox(5, new Label("Unselected Modules:"), lvUnselectedModules);
        VBox vbSelected = new VBox(5, new Label("Selected Modules:"), lvSelectedModules);

        HBox hbSelectionArea = new HBox(15, 
            vbUnselected,
            vbSelectionButtons,
            vbSelected
        );
        
        // Ensure the lists AND their containers grow to fill the center space
        HBox.setHgrow(vbUnselected, Priority.ALWAYS);
        HBox.setHgrow(vbSelected, Priority.ALWAYS);
        VBox.setVgrow(lvUnselectedModules, Priority.ALWAYS);
        VBox.setVgrow(lvSelectedModules, Priority.ALWAYS);
        hbSelectionArea.setPadding(new Insets(0, 0, 0, 20));

        // --- 3. Bottom: Controls & Credits ---
        lblTotalCredits = new Label("Current Credits: 0");
        lblTotalCredits.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        btnReset = new Button("Reset");
        btnSubmit = new Button("Submit");

        HBox hbBottom = new HBox(20, lblTotalCredits, btnReset, btnSubmit);
        hbBottom.setAlignment(Pos.CENTER);
        hbBottom.setPadding(new Insets(20, 0, 0, 0));

        // --- Assembly ---
        this.setLeft(vbLeft);
        this.setCenter(hbSelectionArea);
        this.setBottom(hbBottom);
    }

    // --- Accessor Methods (Strict MVC) ---

    public ListView<Module> getUnselectedListView() { return lvUnselectedModules; }
    public ListView<Module> getSelectedListView() { return lvSelectedModules; }
    public ListView<Module> getBlock1ListView() { return lvBlock1Modules; }
    public ListView<Module> getBlock2ListView() { return lvBlock2Modules; }

    public Button getAddBtn() { return btnAdd; }
    public Button getRemoveBtn() { return btnRemove; }
    public Button getResetBtn() { return btnReset; }
    public Button getSubmitBtn() { return btnSubmit; }

    /**
     * Updates the credit label text.
     */
    public void setTotalCredits(int credits) {
        lblTotalCredits.setText("Current Credits: " + credits);
    }
}
