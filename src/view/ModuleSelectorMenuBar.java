package view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

/**
 * The MenuBar for the application.
 * Provides file management (Load/Save/Exit) and help information.
 */
public class ModuleSelectorMenuBar extends MenuBar {

    // File Menu Items
    private MenuItem loadItem;
    private MenuItem saveItem;
    private MenuItem exitItem;

    // Help Menu Items
    private MenuItem aboutItem;

    public ModuleSelectorMenuBar() {
        // --- File Menu ---
        Menu fileMenu = new Menu("_File"); // Use underscore for Alt+F mnemonic

        loadItem = new MenuItem("_Load Student Data");
        loadItem.setAccelerator(KeyCombination.keyCombination("ShortCut+L"));

        saveItem = new MenuItem("_Save Student Data");
        saveItem.setAccelerator(KeyCombination.keyCombination("ShortCut+S"));

        exitItem = new MenuItem("E_xit");
        exitItem.setAccelerator(KeyCombination.keyCombination("ShortCut+X"));

        // Add items to File menu with a separator before Exit for better UX
        fileMenu.getItems().addAll(loadItem, saveItem, new SeparatorMenuItem(), exitItem);

        // --- Help Menu ---
        Menu helpMenu = new Menu("_Help");

        aboutItem = new MenuItem("_About");
        aboutItem.setAccelerator(KeyCombination.keyCombination("ShortCut+A"));

        helpMenu.getItems().add(aboutItem);

        // Add menus to the MenuBar
        this.getMenus().addAll(fileMenu, helpMenu);
    }

    // --- Getters for the Controller to attach EventHandlers ---

    public MenuItem getLoadItem() {
        return loadItem;
    }

    public MenuItem getSaveItem() {
        return saveItem;
    }

    public MenuItem getExitItem() {
        return exitItem;
    }

    public MenuItem getAboutItem() {
        return aboutItem;
    }
}