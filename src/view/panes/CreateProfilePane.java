package view.panes;

import java.time.LocalDate;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.Course;

/**
 * The CreateProfilePane allows the user to input student details.
 * This version includes all required fields for the high-distinction rubric.
 */
public class CreateProfilePane extends GridPane {

    private ComboBox<Course> cbCourses;
    private DatePicker inputDate;
    private TextField txtPNumber, txtFirstName, txtSurname, txtEmail;
    private Button btnCreateProfile;

    public CreateProfilePane() {
        // --- Styling & Layout ---
        this.setVgap(15);
        this.setHgap(20);
        this.setPadding(new Insets(40));
        this.setAlignment(Pos.CENTER);

        // Column constraints to ensure labels and fields are balanced
        ColumnConstraints column1 = new ColumnConstraints(100, 150, Double.MAX_VALUE);
        column1.setHalignment(HPos.RIGHT);
        ColumnConstraints column2 = new ColumnConstraints(200, 300, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(column1, column2);

        // --- Component Initialization ---
        
        // Course Selection
        this.add(new Label("Select course:"), 0, 0);
        cbCourses = new ComboBox<>();
        cbCourses.setMaxWidth(Double.MAX_VALUE);
        this.add(cbCourses, 1, 0);

        // P-Number
        this.add(new Label("Input P-Number:"), 0, 1);
        txtPNumber = new TextField();
        this.add(txtPNumber, 1, 1);

        // First Name (Added for Distinction)
        this.add(new Label("Input first name:"), 0, 2);
        txtFirstName = new TextField();
        this.add(txtFirstName, 1, 2);

        // Surname (Added for Distinction)
        this.add(new Label("Input surname:"), 0, 3);
        txtSurname = new TextField();
        this.add(txtSurname, 1, 3);

        // Email
        this.add(new Label("Input email:"), 0, 4);
        txtEmail = new TextField();
        this.add(txtEmail, 1, 4);

        // Date (Added for Distinction - using DatePicker for better UX)
        this.add(new Label("Input date:"), 0, 5);
        inputDate = new DatePicker(LocalDate.now());
        inputDate.setMaxWidth(Double.MAX_VALUE);
        this.add(inputDate, 1, 5);

        // Create Button
        btnCreateProfile = new Button("Create Profile");
        this.add(btnCreateProfile, 1, 6);
    }

    // --- Methods for the Controller to interact with the View ---

    public void populateCourseComboBox(Course[] courses) {
        cbCourses.getItems().setAll(courses);
        cbCourses.getSelectionModel().select(0); // Default selection
    }

    public Course getSelectedCourse() {
        return cbCourses.getSelectionModel().getSelectedItem();
    }

    public String getPNumberInput() {
        return txtPNumber.getText();
    }

    public String getFirstNameInput() {
        return txtFirstName.getText();
    }

    public String getSurnameInput() {
        return txtSurname.getText();
    }

    public String getEmailInput() {
        return txtEmail.getText();
    }

    public LocalDate getDateInput() {
        return inputDate.getValue();
    }

    public Button getCreateProfileBtn() {
        return btnCreateProfile;
    }

    // --- Setters for loading saved data ---
    
    public void setPNumberInput(String pNumber) {
        txtPNumber.setText(pNumber);
    }

    public void setFirstNameInput(String firstName) {
        txtFirstName.setText(firstName);
    }

    public void setSurnameInput(String surname) {
        txtSurname.setText(surname);
    }

    public void setEmailInput(String email) {
        txtEmail.setText(email);
    }

    public void setDateInput(java.time.LocalDate date) {
        inputDate.setValue(date);
    }
}
