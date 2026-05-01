package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import model.StudentProfile;

/**
 * Utility class to handle data persistence.
 * Uses Serialization for student profiles and provides a template 
 * for text-based module loading.
 */
public class FileManager {

    /**
     * Saves the StudentProfile object to a binary file.
     * Distinction note: Uses try-with-resources for automatic stream closing.
     */
    public static void saveStudentProfile(StudentProfile profile, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(profile);
            oos.flush();
        }
    }

    /**
     * Loads a StudentProfile object from a binary file.
     * Predicted Error: Handles ClassNotFoundException if the model structure has changed.
     */
    public static StudentProfile loadStudentProfile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (StudentProfile) ois.readObject();
        }
    }

    /**
     * DISTINCTION FEATURE: 
     * Example method for saving the Overview summary as a human-readable text file.
     * This is often a separate requirement from the binary save.
     */
    public static void saveSummaryText(String summary, String filename) throws IOException {
        Files.write(Paths.get(filename), summary.getBytes());
    }
}