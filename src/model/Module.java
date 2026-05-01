package model;

import java.io.Serializable;

/**
 * Represents a university module.
 */
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    private String moduleCode;
    private String moduleName;
    private int credits;
    private boolean mandatory;
    private String delivery; // e.g., "Term 1", "Term 2", or "All Year"

    public Module(String moduleCode, String moduleName, int credits, boolean mandatory) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.credits = credits;
        this.mandatory = mandatory;
        this.delivery = "All Year"; // Default delivery
    }

    public Module(String moduleCode, String moduleName, int credits, boolean mandatory, String delivery) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.credits = credits;
        this.mandatory = mandatory;
        this.delivery = delivery;
    }

    // --- Getters ---
    public String getModuleCode() { return moduleCode; }
    public String getModuleName() { return moduleName; }
    public int getCredits() { return credits; }
    public boolean isMandatory() { return mandatory; }
    public String getDelivery() { return delivery; }

    /**
     * The toString method is what the ListView displays by default.
     * Distinction formatting: "Code: Name (Credits credits)"
     */
    @Override
    public String toString() {
        return moduleCode + " : " + moduleName + " (" + credits + " credits)";
    }
}