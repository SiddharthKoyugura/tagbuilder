package com.assetsense.tagbuilder.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.utils.JsUtil;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagBuilderPage {
	private final JsUtil jsUtil = new JsUtil();

	private FlexTable assetTable;
	private FlexTable obsTable;
	private FlexTable tagTable;

	private FlexTable selectedTable = null;
	private int selectedRow;
	private FlexTable editableTable = null;
	private int editableRow;

	private int selectedAssetRow = 0;

	private Map<Integer, JavaScriptObject> rowMap = new HashMap<Integer, JavaScriptObject>();

	public void loadTagBuilderPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildTaskPage());
	}

	private SplitLayoutPanel buildTaskPage() {
		SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
		splitLayoutPanel.setSize("100%", "100%");

		splitLayoutPanel.addStyleName("mySplitLayoutPanel");

		splitLayoutPanel.addNorth(buildNavbar(), 48);
		splitLayoutPanel.addWest(buildLeftSidebar(), 200);

		splitLayoutPanel.add(buildDetailsDashboard());

		return splitLayoutPanel;
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

		TextBox searchField = new TextBox();

		searchField.getElement().getStyle().setBackgroundColor("white");
		searchField.getElement().setPropertyString("placeholder", "Search an Asset");

		searchPanel.add(searchField);

		return searchPanel;
	}

	private Tree buildAssetTree() {
		final Tree tree = new Tree();
		tree.getElement().getStyle().setMarginTop(10, Unit.PX);

		// Parent Assets
		// TreeItem asset1 = new TreeItem(new Label("Asset1(ECN)"));
		// TreeItem asset2 = new TreeItem(new Label("Asset2(ECN)"));
		// TreeItem asset3 = new TreeItem(new Label("Asset3(ECN)"));
		// TreeItem asset4 = new TreeItem(new Label("Asset4(ECN)"));
		// TreeItem asset5 = new TreeItem(new Label("Asset5(ECN)"));
		//
		// // Child Assets
		// TreeItem casset1 = new TreeItem(new Label("Child Asset1(ECN)"));
		// TreeItem casset2 = new TreeItem(new Label("Child Asset2(ECN)"));
		// TreeItem casset3 = new TreeItem(new Label("Child Asset3(ECN)"));
		// TreeItem casset4 = new TreeItem(new Label("Child Asset4(ECN)"));
		// TreeItem casset5 = new TreeItem(new Label("Child Asset5(ECN)"));
		//
		// asset1.addItem(casset1);
		// asset2.addItem(casset2);
		// asset3.addItem(casset3);
		// asset4.addItem(casset4);
		// asset5.addItem(casset5);
		//
		// tree.addItem(asset1);
		// tree.addItem(asset2);
		// tree.addItem(asset3);
		// tree.addItem(asset4);
		// tree.addItem(asset5);

		jsUtil.sendMessageToServer("{\"request\":\"elements\", \"id\":\"\"}", new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// Handle failure
			}

			@Override
			public void onSuccess(String result) {
				renderTree(getElements(result), tree);
			}
		});

		return tree;
	}

	private void renderTree(List<Asset> assets, Tree tree) {
		for (final Asset asset : assets) {
			Label label = new Label(asset.getName());
			TreeItem item = new TreeItem(label);

			label.getElement().setDraggable("true");

			label.addDragStartHandler(new DragStartHandler() {

				@Override
				public void onDragStart(DragStartEvent event) {
					event.setData("elementId", asset.getId());
				}

			});

			if (!asset.getChildAssets().isEmpty()) {
				renderChildren(item, asset.getChildAssets());
			}
			tree.addItem(item);
		}
	}

	private void renderChildren(TreeItem parentItem, List<Asset> children) {
		for (final Asset child : children) {
			Label label = new Label(child.getName());
			TreeItem childItem = new TreeItem(label);

			label.getElement().setDraggable("true");

			label.addDragStartHandler(new DragStartHandler() {

				@Override
				public void onDragStart(DragStartEvent event) {
					event.setData("elementId", child.getId());
				}

			});

			if (!child.getChildAssets().isEmpty()) {
				renderChildren(childItem, child.getChildAssets());
			}
			parentItem.addItem(childItem);
		}
	}

	private void updateTables(String elementId) {
		jsUtil.sendMessageToServer("{\"request\":\"element\", \"id\":\"" + elementId + "\"}",
				new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						// Handle failure
					}

					@Override
					public void onSuccess(String data) {
						int row = getTableLastRow(assetTable);

						JavaScriptObject dataArray = JsonUtils.safeEval(data);
						JavaScriptObject elementObject = jsUtil.getArrayElement(dataArray, 0);

						assetTable.setText(row, 0, "NULL");
						assetTable.setText(row, 1, jsUtil.getValueAsString(elementObject, "Name"));
						assetTable.setWidget(row, 2, getModelField());
						assetTable.setText(row, 3, "NULL");

						setCursorPointer(assetTable, row);

						JavaScriptObject attributeArray = jsUtil.getObjectProperty(elementObject, "Attributes");
						updateObsAndTagTable(attributeArray);
						rowMap.put(row, attributeArray);
						
						if (selectedTable == assetTable) {
							selectedTable.getRowFormatter().removeStyleName(selectedAssetRow, "selectedRow");
						}
						
						selectedAssetRow = row;

						selectedTable = assetTable;
						selectedRow = row;

						resetTableStates();
					}
				});

	}

	private void updateObsAndTagTable(JavaScriptObject attributeArray) {
		resetTable(obsTable);
		resetTable(tagTable);
		int row = getTableLastRow(obsTable);
		if (attributeArray != null && jsUtil.isArray(attributeArray)) {
			for (int i = 0; i < jsUtil.getArrayLength(attributeArray); i++) {
				JavaScriptObject attribute = jsUtil.getArrayElement(attributeArray, i);
				String description = jsUtil.getValueAsString(attribute, "Description");
				obsTable.setText(row, 0, "NULL");
				obsTable.setText(row, 1, description.trim().length() > 0 ? description.trim() : "NULL");
				obsTable.setText(row, 2, "NULL");
				setCursorPointer(obsTable, row);

				tagTable.setText(row, 0, "NULL");
				tagTable.setText(row, 1, "NULL");
				tagTable.setText(row, 2, "NULL");
				setCursorPointer(tagTable, row);

				row++;
			}
		}
	}

	private int getTableLastRow(FlexTable table) {
		return table.getRowCount();
	}

	private void setCursorPointer(FlexTable table, int row) {
		table.getRowFormatter().getElement(row).getStyle().setProperty("cursor", "pointer");
	}

	private HorizontalPanel getModelField() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setStyleName("removeBorder");

		ListBox listBox = new ListBox();
		listBox.getElement().getStyle().setBackgroundColor("white");
		listBox.setWidth("100%");

		final Button addButton = new Button("+");
		addButton.getElement().getStyle().setProperty("cursor", "pointer");

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// showDialogBox();
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setSize("100%", "100%");
				dialogBox.setAnimationEnabled(true);

				dialogBox.addStyleName("dialogBoxStyle");

				VerticalPanel vpanel = new VerticalPanel();
				vpanel.setWidth("100%");

				HorizontalPanel h1panel = new HorizontalPanel();
				h1panel.setWidth("100%");
				h1panel.setHeight("40px");
				h1panel.getElement().getStyle().setBackgroundColor("#5C9ED4");
				h1panel.getElement().getStyle().setPadding(5, Unit.PX);

				Button closeBtn = new Button("X");

				closeBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}

				});

				h1panel.add(closeBtn);

				h1panel.setCellHorizontalAlignment(closeBtn, HasHorizontalAlignment.ALIGN_RIGHT);

				final Grid grid = new Grid(5, 3);
				grid.setCellPadding(10);
				grid.getElement().getStyle().setProperty("padding", "20px");
				grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
				grid.setWidth("100%");

				TextBox nameField = new TextBox();
				nameField.setStyleName("inputFieldStyle");

				TextBox modelField = new TextBox();
				modelField.setStyleName("inputFieldStyle");

				ListBox assetTypeField = new ListBox();
				assetTypeField.setStyleName("inputFieldStyle");

				grid.setText(0, 0, "Model Name:");
				grid.setWidget(0, 1, nameField);

				grid.setText(1, 0, "Model #:");
				grid.setWidget(1, 1, modelField);

				grid.setText(2, 0, "Asset Type:");
				grid.setWidget(2, 1, assetTypeField);

				toggleSupplierField(grid, true);

				Button saveBtn = new Button("Save");
				grid.setWidget(4, 1, saveBtn);

				grid.getCellFormatter().getElement(4, 1).getStyle().setProperty("textAlign", "right");

				vpanel.add(h1panel);
				vpanel.add(grid);

				dialogBox.add(vpanel);

				dialogBox.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					@Override
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = addButton.getAbsoluteLeft();
						int top = addButton.getAbsoluteTop() + addButton.getOffsetHeight();
						dialogBox.setPopupPosition(left - 50, top);
					}
				});
			}

		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("removeBorder");
		buttonPanel.getElement().getStyle().setMarginLeft(2, Unit.PX);
		buttonPanel.setWidth("auto");

		buttonPanel.add(addButton);
		buttonPanel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);

		horizontalPanel.add(listBox);
		horizontalPanel.add(buttonPanel);

		horizontalPanel.setCellWidth(listBox, "100%");

		return horizontalPanel;
	}

	private void toggleSupplierField(final Grid grid, Boolean isBegin) {
		if (isBegin) {
			ListBox supplierField = new ListBox();
			supplierField.setStyleName("inputFieldStyle");

			Button addSupplierBtn = new Button("+");

			addSupplierBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					toggleSupplierField(grid, false);
				}

			});
			addSupplierBtn.getElement().getStyle().setMarginLeft(5, Unit.PX);

			grid.setText(3, 0, "Supplier Master:");
			grid.setWidget(3, 1, supplierField);
			grid.setWidget(3, 2, addSupplierBtn);
		} else {
			TextBox supplierNameField = new TextBox();
			supplierNameField.setStyleName("inputFieldStyle");

			Button backBtn = new Button("X");
			backBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					toggleSupplierField(grid, true);
				}

			});

			backBtn.getElement().getStyle().setMarginLeft(5, Unit.PX);

			grid.setText(3, 0, "Supplier Name:");
			grid.setWidget(3, 1, supplierNameField);
			grid.setWidget(3, 2, backBtn);
		}
	}

	// End: LeftSidebar

	private ScrollPanel buildDetailsDashboard() {
		ScrollPanel mainPanel = new ScrollPanel();
		mainPanel.setSize("100%", "100%");

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.setHeight("100%");
		vpanel.getElement().getStyle().setBackgroundColor("#EEEEEE");

		ScrollPanel spanel = new ScrollPanel();
		spanel.setSize("100vw", "100vh");

		spanel.add(buildTables());

		vpanel.add(buildDetailsNavbar());
		vpanel.add(spanel);

		mainPanel.add(vpanel);

		return mainPanel;
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

		mainPanel.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				event.preventDefault();
			}

		}, DragOverEvent.getType());

		mainPanel.addDomHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				String elementId = event.getData("elementId");
				updateTables(elementId);
			}

		}, DropEvent.getType());

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

		// assetTable.setText(2, 0, "NULL");
		// assetTable.setText(2, 1, "Hey");
		// assetTable.setWidget(2, 2, getModelField());
		// assetTable.setText(2, 3, "NULL");

		// assetTable.getRowFormatter().getElement(2).getStyle().setProperty("cursor",
		// "pointer");

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

		obsTable.setText(1, 0, "Code");
		obsTable.setText(1, 1, "Description");
		obsTable.setText(1, 2, "Input Type");

		obsTable.getRowFormatter().setStyleName(1, "bg-blue");
		obsTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		// obsTable.setText(2, 0, "Mechanical");
		// obsTable.setText(2, 1, "1");
		// obsTable.setText(2, 2, "40HP Motor Temperature");
		//
		// obsTable.getRowFormatter().getElement(2).getStyle().setProperty("cursor",
		// "pointer");
		//
		// obsTable.setText(3, 0, "Mechanical");
		// obsTable.setText(3, 1, "1");
		// obsTable.setText(3, 2, "40HP Motor Temperature");
		// obsTable.getRowFormatter().getElement(3).getStyle().setProperty("cursor",
		// "pointer");

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

		// tagTable.setText(2, 0, "40HPMTR723");
		// tagTable.setText(2, 1, "1");
		// tagTable.setText(2, 2, "AS-40HPMTR723-1");
		//
		// tagTable.getRowFormatter().getElement(2).getStyle().setProperty("cursor",
		// "pointer");
		//
		// tagTable.setText(3, 0, "40HPMTR723");
		// tagTable.setText(3, 1, "2");
		// tagTable.setText(3, 2, "AS-40HPMTR723-2");
		// tagTable.getRowFormatter().getElement(3).getStyle().setProperty("cursor",
		// "pointer");

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
					int col = cell.getCellIndex();
					if (table == assetTable && col == 2) {

					} else {
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
			}
		});
	}

	private void resetTableStates() {
		if (selectedTable == assetTable) {
			selectedTable.getRowFormatter().addStyleName(selectedRow, "selectedRow");
		}else{
			selectedTable = null;
			selectedRow = 0;
		}
		editableTable = null;
		editableRow = 0;
	}

	private void handleSelection(FlexTable table, int row) {
		if (selectedTable != null) {
			selectedTable.getRowFormatter().removeStyleName(selectedRow, "selectedRow");
			selectedTable = null;
		}
		table.getRowFormatter().addStyleName(row, "selectedRow");
		selectedTable = table;
		selectedRow = row;
		if (table == assetTable && selectedAssetRow != row) {
			updateObsAndTagTable(rowMap.get(row));
			selectedAssetRow = row;
		}
	}

	private void convertRowToEditable() {
		if (selectedTable != null) {
			int row = selectedRow;
			FlexTable table = selectedTable;

			if (table == assetTable) {
				TextBox ecnField = new TextBox();
				String ecn = table.getText(row, 0) == "NULL" ? "" : table.getText(row, 0);
				ecnField.setText(ecn);

				TextBox assetField = new TextBox();
				String asset = table.getText(row, 1) == "NULL" ? "" : table.getText(row, 1);
				assetField.setText(asset);

				TextBox locationField = new TextBox();
				String location = table.getText(row, 3) == "NULL" ? "" : table.getText(row, 1);
				locationField.setText(location);

				table.setWidget(row, 0, ecnField);
				table.setWidget(row, 1, assetField);
				table.setWidget(row, 3, locationField);
			} else if (table == obsTable) {
				TextBox codeField = new TextBox();
				codeField.setText(table.getText(row, 0));

				TextBox descriptionField = new TextBox();
				descriptionField.setText(table.getText(row, 1));

				ListBox inputTypeField = new ListBox();
				inputTypeField.getElement().getStyle().setProperty("width", "100%");
				inputTypeField.addItem("NULL");

				table.setWidget(row, 0, codeField);
				table.setWidget(row, 1, descriptionField);
				table.setWidget(row, 2, inputTypeField);
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

			table.setText(row, 0, ecn.trim().length() < 1 ? "NULL" : ecn.trim());
			table.setText(row, 1, asset.trim().length() < 1 ? "NULL" : asset.trim());
			table.setText(row, 3, location.trim().length() < 1 ? "NULL" : location.trim());
		} else if (table == obsTable) {
			String code = ((TextBox) table.getWidget(row, 0)).getText();
			String description = ((TextBox) table.getWidget(row, 1)).getText();
			String funcCat = ((ListBox) table.getWidget(row, 2)).getSelectedValue();

			table.setText(row, 0, code);
			table.setText(row, 1, description);
			table.setText(row, 2, funcCat);
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

	private void resetTable(FlexTable table) {
		int lastRowIndex = table.getRowCount() - 1;
		while (lastRowIndex >= 2) {
			table.removeRow(lastRowIndex);
			lastRowIndex--;
		}
	}

	private List<Asset> getElements(String data) {
		List<Asset> assets = new ArrayList<>();

		if (data != null) {
			JavaScriptObject jsArray = JsonUtils.safeEval(data);

			for (int i = 0; i < jsUtil.getArrayLength(jsArray); i++) {
				JavaScriptObject elementObject = jsUtil.getArrayElement(jsArray, i);

				Asset asset = new Asset();
				String name = jsUtil.getValueAsString(elementObject, "Name");
				String id = jsUtil.getValueAsString(elementObject, "ID");

				asset.setId(id);
				asset.setName(name);

				JavaScriptObject childElementsArray = jsUtil.getObjectProperty(elementObject, "Elements");
				if (childElementsArray != null && jsUtil.isArray(childElementsArray)) {
					for (int j = 0; j < jsUtil.getArrayLength(childElementsArray); j++) {
						JavaScriptObject childElObject = jsUtil.getArrayElement(childElementsArray, j);
						String childName = jsUtil.getValueAsString(childElObject, "Name");
						String childId = jsUtil.getValueAsString(childElObject, "ID");

						Asset childAsset = new Asset();
						childAsset.setName(childName);
						childAsset.setId(childId);

						asset.getChildAssets().add(childAsset);

					}
				}
				assets.add(asset);
			}
		}

		return assets;
	}

}
