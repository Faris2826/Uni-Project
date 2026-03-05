package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;


public class ModuleSelectorRootPane extends BorderPane {

	private CreateStudentProfilePane cspp;
	private ModuleSelectorMenuBar msmb;
	private TabPane tp;
	
	public ModuleSelectorRootPane() {
		//create tab pane and disable tabs from being closed
		tp = new TabPane();
		tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		//create panes
		cspp = new CreateStudentProfilePane();
		
		//create tabs with panes added
		Tab t1 = new Tab("Create Profile", cspp);
		
		//add tabs to tab pane
		tp.getTabs().addAll(t1);
		
		//create menu bar
		msmb = new ModuleSelectorMenuBar();
		
		//add menu bar and tab pane to this root pane
		this.setTop(msmb);
		this.setCenter(tp);
		
	}

	//methods allowing sub-containers to be accessed by the controller.
	public CreateStudentProfilePane getCreateStudentProfilePane() {
		return cspp;
	}
	
	public ModuleSelectorMenuBar getModuleSelectorMenuBar() {
		return msmb;
	}
	
	//method to allow the controller to change tabs
	public void changeTab(int index) {
		tp.getSelectionModel().select(index);
	}
}
