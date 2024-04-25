package com.assetsense.tagbuilder.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagBuilderPage {

	public void loadTagBuilderPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildTaskPage());
	}

	private DockLayoutPanel buildTaskPage() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		dpanel.setSize("100%", "100%");

		dpanel.addNorth(buildNavbar(), 48);
		dpanel.addWest(buildLeftSidebar(), 200);
		
		dpanel.add(buildDetailsDashboard());

		return dpanel;
	}

	private HorizontalPanel buildNavbar() {
		HorizontalPanel navbar = new HorizontalPanel();
		navbar.setWidth("100%");

		navbar.setStyleName("navbar");

		return navbar;
	}

	// Start: Leftsidebar
	
	private VerticalPanel buildLeftSidebar() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("100%");
		
		VerticalPanel vpanel = new VerticalPanel();

		ScrollPanel spanel = new ScrollPanel();
		spanel.setSize("100%", "100%");
		spanel.getElement().getStyle().setPadding(5, Unit.PX);

		vpanel.add(buildSearchPanel());
		vpanel.add(buildAssetTree());
		
		spanel.add(vpanel);

		mainPanel.add(spanel);

		return mainPanel;
	}

	private HorizontalPanel buildSearchPanel() {
		HorizontalPanel searchPanel = new HorizontalPanel();
		searchPanel.setWidth("100%");
		searchPanel.setStyleName("searchPanel");

		Button btn = new Button("S");

		CheckBox cb = new CheckBox();
		Label label = new Label("Show Asset Hierarchy");

		searchPanel.add(btn);
		searchPanel.add(cb);
		searchPanel.add(label);

		return searchPanel;
	}

	private Tree buildAssetTree() {
		Tree tree = new Tree();
		tree.getElement().getStyle().setMarginTop(10, Unit.PX);

		// Parent Assets
		TreeItem asset1 = new TreeItem(new Label("Asset1(ECN)"));
		TreeItem asset2 = new TreeItem(new Label("Asset2(ECN)"));
		TreeItem asset3 = new TreeItem(new Label("Asset3(ECN)"));
		TreeItem asset4 = new TreeItem(new Label("Asset4(ECN)"));
		TreeItem asset5 = new TreeItem(new Label("Asset5(ECN)"));

		// Child Assets
		TreeItem casset1 = new TreeItem(new Label("Child Asset1(ECN)"));
		TreeItem casset2 = new TreeItem(new Label("Child Asset2(ECN)"));
		TreeItem casset3 = new TreeItem(new Label("Child Asset3(ECN)"));
		TreeItem casset4 = new TreeItem(new Label("Child Asset4(ECN)"));
		TreeItem casset5 = new TreeItem(new Label("Child Asset5(ECN)"));

		asset1.addItem(casset1);
		asset2.addItem(casset2);
		asset3.addItem(casset3);
		asset4.addItem(casset4);
		asset5.addItem(casset5);

		tree.addItem(asset1);
		tree.addItem(asset2);
		tree.addItem(asset3);
		tree.addItem(asset4);
		tree.addItem(asset5);

		return tree;
	}
	
	// End: LeftSidebar
	
	private VerticalPanel buildDetailsDashboard(){
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.setHeight("100%");
		vpanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		vpanel.getElement().getStyle().setMarginLeft(15, Unit.PX);
		
		vpanel.add(buildDetailsNavbar());
		
		vpanel.add(assetTable());
		
		return vpanel;
	}
	
	private HorizontalPanel buildDetailsNavbar(){
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setWidth("100%");
		
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.getElement().getStyle().setPadding(20, Unit.PX);
		
		Button editBtn = new Button("Edit");
		Button saveBtn = new Button("Save");
		
		saveBtn.getElement().getStyle().setMarginLeft(20, Unit.PX);
		
		hpanel.add(editBtn);
		hpanel.add(saveBtn);
		
		
		mainPanel.add(hpanel);
		
		return mainPanel;
	}

	private DockLayoutPanel buildTables(){
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		
		return dpanel;
	}
	
	private VerticalPanel assetTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setWidth("100%");
		hpanel.getElement().getStyle().setPadding(10, Unit.PX);
		hpanel.setStyleName("bg-blue");
		
		Label label = new Label("Asset Master");
		
		hpanel.add(label);
		
		
		FlexTable assetTable = new FlexTable();
		
		
		mainPanel.add(hpanel);
		
		return mainPanel;
	}
}
