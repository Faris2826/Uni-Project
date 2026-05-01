package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a Degree Course containing multiple modules.
 */
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String courseName;
    private Collection<Module> modules;

    public Course(String courseName) {
        this.courseName = courseName;
        this.modules = new ArrayList<>();
    }

    public void addModuleToCourse(Module m) {
        modules.add(m);
    }

    public String getCourseName() {
        return courseName;
    }

    public Collection<Module> getAllModulesOnCourse() {
        return modules;
    }

    @Override
    public String toString() {
        return courseName;
    }
}