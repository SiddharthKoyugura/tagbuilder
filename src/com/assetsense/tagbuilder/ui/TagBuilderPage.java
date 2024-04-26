package com.assetsense.tagbuilder.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagBuilderPage {

	private FlexTable assetTable;
	private FlexTable obsTable;
	private FlexTable tagTable;

	private FlexTable selectedTable = null;
	private int selectedRow;
	private FlexTable editableTable = null;
	private int editableRow;

	public void loadTagBuilderPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildTaskPage());
	}
	
	private SplitLayoutPanel buildTaskPage() {
		SplitLayoutPanel spanel = new SplitLayoutPanel();
		spanel.setSize("100%", "100%");
		
		spanel.addNorth(buildNavbar(), 48);
		spanel.addWest(buildLeftSidebar(), 200);
		
		spanel.add(buildDetailsDashboard());
		
		return spanel;
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

	private VerticalPanel buildDetailsDashboard() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.setHeight("100%");
		vpanel.getElement().getStyle().setBackgroundColor("#EEEEEE");

		ScrollPanel spanel = new ScrollPanel();
		spanel.setSize("100vw", "100vh");

		spanel.add(buildTables());

		vpanel.add(buildDetailsNavbar());
		vpanel.add(spanel);

		return vpanel;
	}

	private HorizontalPanel buildDetailsNavbar() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setWidth("100%");

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.getElement().getStyle().setProperty("padding", "20px 20px 0 20px");

		Button editBtn = new Button("Edit");
		Button saveBtn = new Button("Save");

		editBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				convertRowToEditable();
			}

		});

		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				revertFieldsToNormalState();
			}

		});

		saveBtn.getElement().getStyle().setMarginLeft(20, Unit.PX);

		hpanel.add(editBtn);
		hpanel.add(saveBtn);

		mainPanel.add(hpanel);

		return mainPanel;
	}

	private VerticalPanel buildTables() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.getElement().getStyle().setPadding(10, Unit.PX);

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setWidth("98%");

		hpanel.add(buildAssetTable());
		hpanel.add(buildObservationTable());

		mainPanel.add(hpanel);
		mainPanel.add(buildTagTable());

		return mainPanel;
	}

	private VerticalPanel buildAssetTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("300px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");

		assetTable = new FlexTable();
		assetTable.setWidth("100%");

		assetTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		assetTable.setStyleName("table-padding tableStyle");

		assetTable.setText(0, 0, "Assetmaster");

		assetTable.getFlexCellFormatter().setColSpan(0, 0, 4);
		assetTable.getRowFormatter().setStyleName(0, "bg-blue");
		assetTable.getRowFormatter().getElement(0).getStyle().setProperty("borderBottom", "1px solid black");

		assetTable.setText(1, 0, "ECN");
		assetTable.setText(1, 1, "Asset name");
		assetTable.setText(1, 2, "Model#");
		assetTable.setText(1, 3, "Location");

		assetTable.getRowFormatter().setStyleName(1, "bg-blue");
		assetTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		assetTable.setText(2, 0, "10171");
		assetTable.setText(2, 1, "10171");
		assetTable.setText(2, 2, "TAL116");
		assetTable.setText(2, 3, "Houston Plant");

		assetTable.getRowFormatter().getElement(2).getStyle().setProperty("cursor", "pointer");

		mainPanel.add(assetTable);

		setClickHandler(assetTable);

		return mainPanel;
	}

	private VerticalPanel buildObservationTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("76%");
		mainPanel.setHeight("300px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");
		mainPanel.getElement().getStyle().setMarginLeft(10, Unit.PX);

		obsTable = new FlexTable();
		obsTable.setWidth("100%");
		obsTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		obsTable.setStyleName("table-padding tableStyle");

		obsTable.setText(0, 0, "Observations");

		obsTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		obsTable.getRowFormatter().setStyleName(0, "bg-blue");
		obsTable.getRowFormatter().getElement(0).getStyle().setProperty("borderBottom", "1px solid black");

		obsTable.setText(1, 0, "Functional Category");
		obsTable.setText(1, 1, "Code");
		obsTable.setText(1, 2, "Description");

		obsTable.getRowFormatter().setStyleName(1, "bg-blue");
		obsTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		obsTable.setText(2, 0, "Mechanical");
		obsTable.setText(2, 1, "1");
		obsTable.setText(2, 2, "40HP Motor Temperature");

		obsTable.getRowFormatter().getElement(2).getStyle().setProperty("cursor", "pointer");

		obsTable.setText(3, 0, "Mechanical");
		obsTable.setText(3, 1, "1");
		obsTable.setText(3, 2, "40HP Motor Temperature");
		obsTable.getRowFormatter().getElement(3).getStyle().setProperty("cursor", "pointer");

		mainPanel.add(obsTable);

		setClickHandler(obsTable);

		return mainPanel;
	}

	private VerticalPanel buildTagTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("86%");
		mainPanel.setHeight("270px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");
		mainPanel.getElement().getStyle().setMarginTop(10, Unit.PX);

		tagTable = new FlexTable();
		tagTable.setWidth("100%");
		tagTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		tagTable.setStyleName("table-padding tableStyle");

		tagTable.setText(0, 0, "Tag Setup");

		tagTable.getFlexCellFormatter().setColSpan(0, 0, 4);
		tagTable.getRowFormatter().setStyleName(0, "bg-blue");
		tagTable.getRowFormatter().getElement(0).getStyle().setProperty("borderBottom", "1px solid black");

		tagTable.setText(1, 0, "Asset");
		tagTable.setText(1, 1, "Observation Code");
		tagTable.setText(1, 2, "Tag");

		tagTable.getRowFormatter().setStyleName(1, "bg-blue");
		tagTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		tagTable.setText(2, 0, "40HPMTR723");
		tagTable.setText(2, 1, "1");
		tagTable.setText(2, 2, "AS-40HPMTR723-1");

		tagTable.getRowFormatter().getElement(2).getStyle().setProperty("cursor", "pointer");

		tagTable.setText(3, 0, "40HPMTR723");
		tagTable.setText(3, 1, "2");
		tagTable.setText(3, 2, "AS-40HPMTR723-2");
		tagTable.getRowFormatter().getElement(3).getStyle().setProperty("cursor", "pointer");

		mainPanel.add(tagTable);

		setClickHandler(tagTable);

		return mainPanel;
	}

	private void setClickHandler(final FlexTable table) {
		table.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cell cell = table.getCellForEvent(event);
				if (cell != null) {
					final int row = cell.getRowIndex();
					if (row > 1) {
						if (editableTable != null) {
							if (editableTable == table) {
								if (editableRow != row) {
									revertFieldsToNormalState();
									handleSelection(table, row);
								}
							} else if (editableTable != table) {
								revertFieldsToNormalState();
								handleSelection(table, row);
							}
						} else if (selectedTable == table && selectedRow == row) {
							convertRowToEditable();
						}

						else {
							handleSelection(table, row);
						}
					}
				}
			}
		});
	}

	private void handleSelection(FlexTable table, int row) {
		if (selectedTable != null) {
			selectedTable.getRowFormatter().removeStyleName(selectedRow, "selectedRow");
			selectedTable = null;
		}
		table.getRowFormatter().addStyleName(row, "selectedRow");
		selectedTable = table;
		selectedRow = row;
	}

	private void convertRowToEditable() {
		if (selectedTable != null) {
			int row = selectedRow;
			FlexTable table = selectedTable;

			if (table == assetTable) {
				TextBox ecnField = new TextBox();
				ecnField.setText(table.getText(row, 0));

				TextBox assetField = new TextBox();
				assetField.setText(table.getText(row, 1));

				TextBox locationField = new TextBox();
				locationField.setText(table.getText(row, 3));

				table.setWidget(row, 0, ecnField);
				table.setWidget(row, 1, assetField);
				table.setWidget(row, 3, locationField);
			} else if (table == obsTable) {

				TextBox funcCatField = new TextBox();
				funcCatField.setText(table.getText(row, 0));

				TextBox codeField = new TextBox();
				codeField.setText(table.getText(row, 1));

				TextBox descriptionField = new TextBox();
				descriptionField.setText(table.getText(row, 2));

				table.setWidget(row, 0, funcCatField);
				table.setWidget(row, 1, codeField);
				table.setWidget(row, 2, descriptionField);
			} else if (table == tagTable) {
				TextBox assetField = new TextBox();
				assetField.setText(table.getText(row, 0));

				TextBox codeField = new TextBox();
				codeField.setText(table.getText(row, 1));

				TextBox tagField = new TextBox();
				tagField.setText(table.getText(row, 2));

				table.setWidget(row, 0, assetField);
				table.setWidget(row, 1, codeField);
				table.setWidget(row, 2, tagField);
			}

			editableTable = table;
			editableRow = row;
		}
	}

	private void revertFieldsToNormalState() {
		int row = editableRow;
		FlexTable table = editableTable;
		if (table == assetTable) {
			String ecn = ((TextBox) table.getWidget(row, 0)).getText();
			String asset = ((TextBox) table.getWidget(row, 1)).getText();
			String location = ((TextBox) table.getWidget(row, 3)).getText();

			table.setText(row, 0, ecn);
			table.setText(row, 1, asset);
			table.setText(row, 3, location);
		} else if (table == obsTable) {
			String funcCat = ((TextBox) table.getWidget(row, 0)).getText();
			String code = ((TextBox) table.getWidget(row, 1)).getText();
			String description = ((TextBox) table.getWidget(row, 2)).getText();

			table.setText(row, 0, funcCat);
			table.setText(row, 1, code);
			table.setText(row, 2, description);
		} else if (table == tagTable) {
			String asset = ((TextBox) table.getWidget(row, 0)).getText();
			String code = ((TextBox) table.getWidget(row, 1)).getText();
			String tag = ((TextBox) table.getWidget(row, 2)).getText();

			table.setText(row, 0, asset);
			table.setText(row, 1, code);
			table.setText(row, 2, tag);
		}

		editableTable = null;
	}
}
