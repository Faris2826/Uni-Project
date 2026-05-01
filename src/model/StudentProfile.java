package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The main data carrier for the application.
 * Stores all student info and their module choices.
 */
public class StudentProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pNumber;
    private String firstName;
    private String surname;
    private String email;
    private LocalDate date;
    private Course course;
    private Set<Module> selectedModules;
    private Set<Module> reservedModules;

    public StudentProfile() {
        pNumber = "";
        firstName = "";
        surname = "";
        email = "";
        date = null;
        course = null;
        selectedModules = new HashSet<>();
        reservedModules = new HashSet<>();
    }

    // --- Getters and Setters ---
    public String getStudentPNo() { return pNumber; }
    public void setStudentPNo(String pNumber) { this.pNumber = pNumber; }

    public void setStudentName(String first, String last) {
        this.firstName = first;
        this.surname = last;
    }

    public String getFullName() { return firstName + " " + surname; }

    public String getStudentEmail() { return email; }
    public void setStudentEmail(String email) { this.email = email; }

    public LocalDate getStudentDate() { return date; }
    public void setStudentDate(LocalDate date) { this.date = date; }

    public Course getStudentCourse() { return course; }
    public void setStudentCourse(Course course) { this.course = course; }

    public Set<Module> getSelectedModules() { return selectedModules; }
    public Set<Module> getReservedModules() { return reservedModules; }

    /**
     * Formats the student's personal details for the Overview TextAreas.
     */
    public String getDetailsSummary() {
        return "Name: " + getFullName() + "\n" +
               "P-Number: " + pNumber + "\n" +
               "Email: " + email + "\n" +
               "Date: " + (date != null ? date.toString() : "N/A") + "\n" +
               "Course: " + (course != null ? course.getCourseName() : "N/A");
    }
}