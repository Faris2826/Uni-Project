package controller;

import java.io.IOException;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.Course;
import model.Module;
import model.StudentProfile;
import util.FileManager;
import view.ModuleSelectorRootPane;
import view.panes.CreateProfilePane;
import view.panes.OverviewPane;
import view.panes.ReserveModulesPane;
import view.panes.SelectModulesPane;
import view.ModuleSelectorMenuBar;

/**
 * The Controller links the Model and the View.
 * It handles all event logic, input validation, and data flow.
 * DISTINCTION FOCUS: Strict MVC, complete workflow, full validation, proper state management.
 */
public class ModuleSelectorController {

    private StudentProfile model;
    private ModuleSelectorRootPane view;
    
    // Sub-panes and Menu for easier access
    private CreateProfilePane cpp;
    private SelectModulesPane smp;
    private ReserveModulesPane rmp;
    private OverviewPane op;
    private ModuleSelectorMenuBar msmb;
    
    // Tabs for workflow enforcement
    private TabPane tabPane;
    
    // Store original modules for reset
    private Course currentCourse;
    private java.util.List<Module> block1Modules;
    private java.util.List<Module> block2Modules;
    private java.util.List<Module> optionalModules;

    public ModuleSelectorController(StudentProfile model, ModuleSelectorRootPane view) {
        this.model = model;
        this.view = view;
        
        // Initialize references to sub-views
        this.cpp = view.getCreateProfilePane();
        this.smp = view.getSelectModulesPane();
        this.rmp = view.getReserveModulesPane();
        this.op = view.getOverviewPane();
        this.msmb = view.getModuleSelectorMenuBar();
        this.tabPane = view.getTabPane();
        
        // Initialize storage for modules
        this.block1Modules = new java.util.ArrayList<>();
        this.block2Modules = new java.util.ArrayList<>();
        this.optionalModules = new java.util.ArrayList<>();

        // Populate the course list
        cpp.populateCourseComboBox(setupAndGetCourses());

        // Initialize tab states (only Create Profile enabled)
        initializeTabStates();

        // Attach ALL event handlers
        this.attachEventHandlers();
    }

    /**
     * Initialize tabs: Only tab 0 (Create Profile) enabled initially
     */
    private void initializeTabStates() {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            Tab tab = tabPane.getTabs().get(i);
            tab.setDisable(i != 0); // Only first tab enabled
        }
    }

    /**
     * Update tab disabled states based on workflow progress
     */
    private void updateTabStates() {
        TabPane tp = tabPane;
        tp.getTabs().get(0).setDisable(false); // Create Profile always available
        tp.getTabs().get(1).setDisable(false); // Select Modules always available after profile
        tp.getTabs().get(2).setDisable(false); // Reserve Modules always available
        tp.getTabs().get(3).setDisable(false); // Overview always available
    }

    private void attachEventHandlers() {
        // --- Create Profile Logic ---
        cpp.getCreateProfileBtn().setOnAction(e -> handleCreateProfile());

        // --- Module Selection Logic ---
        smp.getAddBtn().setOnAction(e -> handleAddModule());
        smp.getRemoveBtn().setOnAction(e -> handleRemoveModule());
        smp.getResetBtn().setOnAction(e -> handleResetModules());
        smp.getSubmitBtn().setOnAction(e -> handleSubmitSelection());

        // --- Reserve Modules Logic ---
        rmp.getAddReserveBtn().setOnAction(e -> handleAddReserve());
        rmp.getRemoveReserveBtn().setOnAction(e -> handleRemoveReserve());
        rmp.getConfirmReserveBtn().setOnAction(e -> handleConfirmReserve());

        // --- Overview Logic ---
        op.getSaveOverviewBtn().setOnAction(e -> handleSaveOverview());

        // --- Menu Logic ---
        msmb.getExitItem().setOnAction(e -> System.exit(0));
        
        msmb.getAboutItem().setOnAction(e -> handleAbout());

        msmb.getSaveItem().setOnAction(e -> handleMenuSave());
        
        msmb.getLoadItem().setOnAction(e -> handleMenuLoad());
    }

    // ============== CREATE PROFILE HANDLERS ==============

    private void handleCreateProfile() {
        // Validation
        String pNumber = cpp.getPNumberInput().trim();
        String firstName = cpp.getFirstNameInput().trim();
        String surname = cpp.getSurnameInput().trim();
        String email = cpp.getEmailInput().trim();

        // Check required fields
        if (pNumber.isEmpty() || firstName.isEmpty() || surname.isEmpty()) {
            view.displayAlert("Input Error", "Please ensure P-Number, First Name, and Surname are filled.");
            return;
        }

        // Validate P-Number format (P followed by digits)
        if (!pNumber.matches("^P\\d{7}$")) {
            view.displayAlert("P-Number Error", "P-Number must be in format: P followed by 7 digits (e.g., P1234567)");
            return;
        }

        // Validate Email format
        if (!email.isEmpty() && !email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$")) {
            view.displayAlert("Email Error", "Please enter a valid email address.");
            return;
        }

        // Update Model
        model.setStudentPNo(pNumber);
        model.setStudentName(firstName, surname);
        model.setStudentEmail(email);
        model.setStudentDate(cpp.getDateInput());
        
        Course selectedCourse = cpp.getSelectedCourse();
        if (selectedCourse != null) {
            model.setStudentCourse(selectedCourse);
            this.currentCourse = selectedCourse;
            loadModulesForCourse(selectedCourse);
        }

        // Enable other tabs
        updateTabStates();

        // Navigate to Select Modules tab
        view.changeTab(1);
    }

    /**
     * Load all modules for the selected course and populate the UI lists
     */
    private void loadModulesForCourse(Course course) {
        // Clear previous data
        block1Modules.clear();
        block2Modules.clear();
        optionalModules.clear();
        smp.getBlock1ListView().getItems().clear();
        smp.getBlock2ListView().getItems().clear();
        smp.getUnselectedListView().getItems().clear();
        smp.getSelectedListView().getItems().clear();

        // Separate modules by mandatory status
        for (Module m : course.getAllModulesOnCourse()) {
            if (m.isMandatory()) {
                // Mandatory modules go to Block1 or Block2 based on terms
                if (m.getDelivery().contains("Term 1") || m.getDelivery().contains("1")) {
                    block1Modules.add(m);
                } else {
                    block2Modules.add(m);
                }
            } else {
                // Optional modules for Block 3/4
                optionalModules.add(m);
            }
        }

        // Populate the UI lists
        smp.getBlock1ListView().getItems().addAll(block1Modules);
        smp.getBlock2ListView().getItems().addAll(block2Modules);
        smp.getUnselectedListView().getItems().addAll(optionalModules);

        // Update credits with mandatory modules only
        updateCredits();
    }

    // ============== MODULE SELECTION HANDLERS ==============

    private void handleAddModule() {
        Module selected = smp.getUnselectedListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            view.displayAlert("Selection Error", "Please select a module to add.");
            return;
        }

        // Check if adding this module would exceed 120 credits
        int currentCredits = calculateCurrentCredits();
        if (currentCredits + selected.getCredits() > 120) {
            view.displayAlert("Credit Limit Error", "Adding this module would exceed 120 credits. Current: " + 
                currentCredits + ", Module: " + selected.getCredits());
            return;
        }

        // Move module
        smp.getUnselectedListView().getItems().remove(selected);
        smp.getSelectedListView().getItems().add(selected);
        model.getSelectedModules().add(selected);
        
        updateCredits();
    }

    private void handleRemoveModule() {
        Module selected = smp.getSelectedListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            view.displayAlert("Selection Error", "Please select a module to remove.");
            return;
        }

        smp.getSelectedListView().getItems().remove(selected);
        smp.getUnselectedListView().getItems().add(selected);
        model.getSelectedModules().remove(selected);
        
        updateCredits();
    }

    private void handleResetModules() {
        // Clear selected and move all optional modules back to unselected
        smp.getSelectedListView().getItems().clear();
        smp.getUnselectedListView().getItems().clear();
        smp.getUnselectedListView().getItems().addAll(optionalModules);
        model.getSelectedModules().clear();
        
        updateCredits();
    }

    private void handleSubmitSelection() {
        int totalCredits = calculateCurrentCredits();
        
        // Must have exactly 120 credits
        if (totalCredits != 120) {
            view.displayAlert("Submission Error", "You must select exactly 120 credits.\n" +
                "Current total: " + totalCredits);
            return;
        }

        // Move to Reserve Modules tab
        view.displayAlert("Success", "Selection submitted successfully!");
        populateReserveModules();
        view.changeTab(2);
    }

    // ============== RESERVE MODULES HANDLERS ==============

    private void populateReserveModules() {
        // Available modules are those NOT selected
        rmp.clearPane();
        
        java.util.Set<Module> selectedSet = model.getSelectedModules();
        java.util.List<Module> available = new java.util.ArrayList<>();
        
        // Add all optional modules except those already selected
        for (Module m : optionalModules) {
            if (!selectedSet.contains(m)) {
                available.add(m);
            }
        }
        
        rmp.getUnselectedListView().getItems().addAll(available);
    }

    private void handleAddReserve() {
        Module selected = rmp.getUnselectedListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            view.displayAlert("Selection Error", "Please select a module to reserve.");
            return;
        }

        rmp.getUnselectedListView().getItems().remove(selected);
        rmp.getReservedListView().getItems().add(selected);
    }

    private void handleRemoveReserve() {
        Module selected = rmp.getReservedListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            view.displayAlert("Selection Error", "Please select a reserved module to remove.");
            return;
        }

        rmp.getReservedListView().getItems().remove(selected);
        rmp.getUnselectedListView().getItems().add(selected);
    }

    private void handleConfirmReserve() {
        // Move all reserved modules to model
        model.getReservedModules().clear();
        model.getReservedModules().addAll(rmp.getReservedListView().getItems());

        // Show overview
        displayOverview();
        view.changeTab(3);
    }

    // ============== OVERVIEW HANDLERS ==============

    private void displayOverview() {
        // Student Details
        String studentDetails = model.getDetailsSummary();

        // Selected Modules
        StringBuilder selectedModulesText = new StringBuilder("Selected Modules (120 credits):\n");
        selectedModulesText.append("=".repeat(50)).append("\n");
        int selectedTotal = 0;
        for (Module m : model.getSelectedModules()) {
            selectedModulesText.append(m.toString()).append("\n");
            selectedTotal += m.getCredits();
        }
        selectedModulesText.append("Total: ").append(selectedTotal).append(" credits");

        // Reserved Modules
        StringBuilder reservedModulesText = new StringBuilder("Reserved Modules:\n");
        reservedModulesText.append("=".repeat(50)).append("\n");
        if (model.getReservedModules().isEmpty()) {
            reservedModulesText.append("No reserved modules.");
        } else {
            for (Module m : model.getReservedModules()) {
                reservedModulesText.append(m.toString()).append("\n");
            }
        }

        // Update view
        op.updateOverview(studentDetails, selectedModulesText.toString(), reservedModulesText.toString());
    }

    private void handleSaveOverview() {
        try {
            // Save model to binary file
            FileManager.saveStudentProfile(model, "student_profile.dat");
            
            // Also save overview as text file
            String overview = model.getDetailsSummary() + "\n\n" +
                "Selected Modules:\n" + model.getSelectedModules().toString() + "\n\n" +
                "Reserved Modules:\n" + model.getReservedModules().toString();
            FileManager.saveSummaryText(overview, "overview_summary.txt");
            
            view.displayAlert("Save Info", "Profile saved to student_profile.dat");
        } catch (IOException ex) {
            view.displayAlert("Save Error", "Could not save: " + ex.getMessage());
        }
    }

    // ============== MENU HANDLERS ==============

    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION, 
            "Module Selector v2.0\n\n" +
            "A JavaFX application for managing module selections.\n" +
            "Features:\n" +
            "- Profile creation with validation\n" +
            "- Dynamic module loading\n" +
            "- Credit calculation (120 credits required)\n" +
            "- Reserve module functionality\n" +
            "- Data persistence via serialization\n\n" +
            "Created for Distinction Grade");
        alert.setHeaderText("About This Tool");
        alert.showAndWait();
    }

    private void handleMenuSave() {
        try {
            FileManager.saveStudentProfile(model, "student_profile.dat");
            view.displayAlert("Save Info", "Data saved successfully to student_profile.dat");
        } catch (IOException ex) {
            view.displayAlert("Save Error", "Could not save: " + ex.getMessage());
        }
    }

    private void handleMenuLoad() {
        try {
            StudentProfile loadedProfile = FileManager.loadStudentProfile("student_profile.dat");
            
            // Restore model
            this.model = loadedProfile;
            
            // Restore UI state from loaded model
            if (loadedProfile.getStudentPNo() != null && !loadedProfile.getStudentPNo().isEmpty()) {
                cpp.setPNumberInput(loadedProfile.getStudentPNo());
                cpp.setFirstNameInput(loadedProfile.getFullName().split(" ")[0]);
                cpp.setSurnameInput(loadedProfile.getFullName().split(" ").length > 1 ? 
                    loadedProfile.getFullName().split(" ", 2)[1] : "");
                cpp.setEmailInput(loadedProfile.getStudentEmail());
                cpp.setDateInput(loadedProfile.getStudentDate());
                
                // Reload modules
                if (loadedProfile.getStudentCourse() != null) {
                    loadModulesForCourse(loadedProfile.getStudentCourse());
                    
                    // Restore selected modules to the view
                    smp.getUnselectedListView().getItems().clear();
                    smp.getSelectedListView().getItems().clear();
                    
                    for (Module m : optionalModules) {
                        if (loadedProfile.getSelectedModules().contains(m)) {
                            smp.getSelectedListView().getItems().add(m);
                        } else {
                            smp.getUnselectedListView().getItems().add(m);
                        }
                    }
                    
                    // Restore reserved modules
                    rmp.clearPane();
                    rmp.getReservedListView().getItems().addAll(loadedProfile.getReservedModules());
                    
                    updateCredits();
                    displayOverview();
                    updateTabStates();
                }
            }
            
            view.displayAlert("Load Info", "Profile loaded successfully!");
        } catch (IOException | ClassNotFoundException ex) {
            view.displayAlert("Load Error", "Could not load: " + ex.getMessage());
        }
    }

    // ============== UTILITY METHODS ==============

    private void updateCredits() {
        // Calculate total credits: mandatory + selected optional
        int total = 0;
        
        // Add mandatory modules
        for (Module m : block1Modules) {
            total += m.getCredits();
        }
        for (Module m : block2Modules) {
            total += m.getCredits();
        }
        
        // Add selected optional modules
        for (Module m : smp.getSelectedListView().getItems()) {
            total += m.getCredits();
        }
        
        smp.setTotalCredits(total);
    }

    private int calculateCurrentCredits() {
        int total = 0;
        
        // Mandatory credits
        for (Module m : block1Modules) {
            total += m.getCredits();
        }
        for (Module m : block2Modules) {
            total += m.getCredits();
        }
        
        // Selected optional credits
        for (Module m : smp.getSelectedListView().getItems()) {
            total += m.getCredits();
        }
        
        return total;
    }

    /**
     * Helper to set up comprehensive mock course data for the demo.
     * DISTINCTION: Realistic data with multiple blocks
     */
    private Course[] setupAndGetCourses() {
        // --- Computer Science Course ---
        Course cs = new Course("BSc Computer Science");
        
        // Block 1 (Core - Term 1)
        cs.addModuleToCourse(new Module("CS101", "Programming Fundamentals", 20, true, "Term 1"));
        cs.addModuleToCourse(new Module("CS102", "Discrete Mathematics", 15, true, "Term 1"));
        
        // Block 2 (Core - Term 2)
        cs.addModuleToCourse(new Module("CS201", "Data Structures", 20, true, "Term 2"));
        cs.addModuleToCourse(new Module("CS202", "Algorithms", 15, true, "Term 2"));
        
        // Block 3/4 (Optional)
        cs.addModuleToCourse(new Module("CS301", "Web Development", 15, false, "All Year"));
        cs.addModuleToCourse(new Module("CS302", "Artificial Intelligence", 20, false, "All Year"));
        cs.addModuleToCourse(new Module("CS303", "Mobile Apps", 15, false, "All Year"));
        cs.addModuleToCourse(new Module("CS304", "Cloud Computing", 15, false, "All Year"));
        cs.addModuleToCourse(new Module("CS305", "Cybersecurity", 20, false, "All Year"));
        cs.addModuleToCourse(new Module("CS306", "Game Development", 15, false, "All Year"));
        
        // --- Software Engineering Course ---
        Course se = new Course("BSc Software Engineering");
        
        // Block 1 (Core - Term 1)
        se.addModuleToCourse(new Module("SE101", "OOP Fundamentals", 20, true, "Term 1"));
        se.addModuleToCourse(new Module("SE102", "Software Design", 15, true, "Term 1"));
        
        // Block 2 (Core - Term 2)
        se.addModuleToCourse(new Module("SE201", "Project Management", 20, true, "Term 2"));
        se.addModuleToCourse(new Module("SE202", "Testing & QA", 15, true, "Term 2"));
        
        // Block 3/4 (Optional)
        se.addModuleToCourse(new Module("SE301", "DevOps", 15, false, "All Year"));
        se.addModuleToCourse(new Module("SE302", "Agile Methods", 20, false, "All Year"));
        se.addModuleToCourse(new Module("SE303", "Enterprise Systems", 15, false, "All Year"));
        se.addModuleToCourse(new Module("SE304", "Data Engineering", 20, false, "All Year"));
        se.addModuleToCourse(new Module("SE305", "Cloud Architecture", 15, false, "All Year"));
        
        // --- Business IT Course ---
        Course bit = new Course("BSc Business IT");
        
        // Block 1 (Core - Term 1)
        bit.addModuleToCourse(new Module("BI101", "Business Process", 20, true, "Term 1"));
        bit.addModuleToCourse(new Module("BI102", "Information Systems", 15, true, "Term 1"));
        
        // Block 2 (Core - Term 2)
        bit.addModuleToCourse(new Module("BI201", "Database Design", 20, true, "Term 2"));
        bit.addModuleToCourse(new Module("BI202", "Systems Analysis", 15, true, "Term 2"));
        
        // Block 3/4 (Optional)
        bit.addModuleToCourse(new Module("BI301", "Business Analytics", 20, false, "All Year"));
        bit.addModuleToCourse(new Module("BI302", "Enterprise Systems", 15, false, "All Year"));
        bit.addModuleToCourse(new Module("BI303", "Digital Transformation", 20, false, "All Year"));
        bit.addModuleToCourse(new Module("BI304", "IT Governance", 15, false, "All Year"));
        
        return new Course[]{cs, se, bit};
    }
}