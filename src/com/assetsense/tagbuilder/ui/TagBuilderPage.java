package com.assetsense.tagbuilder.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;
import com.assetsense.tagbuilder.dto.AssetDTO;
import com.assetsense.tagbuilder.dto.ObservationDTO;
import com.assetsense.tagbuilder.service.AssetService;
import com.assetsense.tagbuilder.service.AssetServiceAsync;
import com.assetsense.tagbuilder.service.LookupService;
import com.assetsense.tagbuilder.service.LookupServiceAsync;
import com.assetsense.tagbuilder.utils.JsUtil;
import com.assetsense.tagbuilder.utils.TypeConverter;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Window;
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

	private final AssetServiceAsync assetService = GWT.create(AssetService.class);
	private final LookupServiceAsync lookupService = GWT.create(LookupService.class);
	// private final TagServiceAsync tagService = GWT.create(TagService.class);

	private final TypeConverter typeConverter = new TypeConverter();

	private final JsUtil jsUtil = new JsUtil();

	private FlexTable assetTable;
	private FlexTable obsTable;
	private FlexTable tagTable;

	private FlexTable selectedTable = null;
	private int selectedRow;
	private FlexTable editableTable = null;
	private int editableRow;
	private AssetDTO selectedAsset = null;

	private int selectedAssetRow = 0;

	private TextBox supplierNameField;
	private ListBox supplierField;

	private Map<Integer, List<ObservationDTO>> rowMap = new HashMap<Integer, List<ObservationDTO>>();

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

		jsUtil.sendMessageToServer("{\"request\":\"elements\", \"id\":\"\"}", new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// Handle failure
			}

			@Override
			public void onSuccess(String result) {
				// renderTree(getElements(result), tree);
				getAssets(result, tree);
			}
		});

		return tree;
	}

	private void renderTree(List<AssetDTO> assets, Tree tree) {
		for (final AssetDTO asset : assets) {
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

	private void renderChildren(TreeItem parentItem, List<AssetDTO> children) {
		for (final AssetDTO child : children) {
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

	private void updateTables(String assetId) {
		assetService.getAssetById(assetId, new AsyncCallback<AssetDTO>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(AssetDTO asset) {
				selectedAsset = asset;
				int row = getTableLastRow(assetTable);
				assetTable.setText(row, 0,
						(asset.getEcn() != null && asset.getEcn().trim().length() > 0) ? asset.getEcn() : "NULL");
				assetTable.setText(row, 1, asset.getName());
				assetTable.setWidget(row, 2, getModelField(asset));
				assetTable.setText(row, 3, asset.getLocation() != null ? asset.getLocation() : "NULL");

				setCursorPointer(assetTable, row);

				rowMap.put(row, asset.getObservations());
				updateObsAndTagTable(asset.getObservations(), asset.getName());

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

	private void updateObsAndTagTable(final List<ObservationDTO> observations, String assetName) {
		resetTable(obsTable);
		resetTable(tagTable);

		assetService.getAssetByName(assetName, new AsyncCallback<AssetDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(AssetDTO asset) {
				selectedAsset = asset;
				int row = getTableLastRow(obsTable);
				for (ObservationDTO observation : observations) {
					String description = observation.getDescription();
					obsTable.setText(row, 0, observation.getCode() != null && observation.getCode().length() > 0 ? observation.getCode() : "NULL");
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

		});
	}

	private int getTableLastRow(FlexTable table) {
		return table.getRowCount();
	}

	private void setCursorPointer(FlexTable table, int row) {
		table.getRowFormatter().getElement(row).getStyle().setProperty("cursor", "pointer");
	}

	private HorizontalPanel getModelField(final AssetDTO asset) {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setStyleName("removeBorder");

		final ListBox modelItems = new ListBox();
		modelItems.getElement().getStyle().setBackgroundColor("white");
		modelItems.setWidth("100%");

		lookupService.getLookupByCategory("110", new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<Lookup> lookups) {
				for (Lookup lookup : lookups) {
					modelItems.addItem(lookup.getName());
				}
			}

		});

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

				final VerticalPanel vpanel = new VerticalPanel();
				vpanel.setWidth("100%");

				final HorizontalPanel h1panel = new HorizontalPanel();
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

				final TextBox modelField = new TextBox();
				modelField.setStyleName("inputFieldStyle");

				final ListBox assetTypeField = new ListBox();
				assetTypeField.setStyleName("inputFieldStyle");

				grid.setText(0, 0, "Model #:");
				grid.setWidget(0, 1, modelField);

				lookupService.getLookupByCategory("100", new AsyncCallback<List<Lookup>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<Lookup> assetTypes) {
						for (Lookup assetType : assetTypes) {
							assetTypeField.addItem(assetType.getName());
						}
						grid.setText(1, 0, "Asset Type:");
						grid.setWidget(1, 1, assetTypeField);

						toggleSupplierField(grid, true);

						Button saveBtn = new Button("Save");
						grid.setWidget(3, 1, saveBtn);

						saveBtn.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {

								lookupService.getLookupByName(assetTypeField.getSelectedValue(),
										new AsyncCallback<Lookup>() {

											@Override
											public void onFailure(Throwable caught) {
												Window.alert("failure1");
											}

											@Override
											public void onSuccess(Lookup assetType) {
												asset.setAssettype(assetType);
												lookupService.getLookupByName(supplierField.getSelectedValue(),
														new AsyncCallback<Lookup>() {

															@Override
															public void onFailure(Throwable caught) {
																Window.alert("failure2");

															}

															@Override
															public void onSuccess(Lookup supplier) {
																asset.setSupplierName(supplier);
																Lookup model = new Lookup();
																model.setCategoryId("110");
																model.setName(modelField.getValue());
																lookupService.saveLookup(model,
																		new AsyncCallback<Void>() {

																			@Override
																			public void onFailure(Throwable caught) {
																				Window.alert("failure3");

																			}

																			@Override
																			public void onSuccess(Void result) {
																				modelItems
																						.addItem(modelField.getValue());
																				dialogBox.hide();

																				Asset assetDAO = typeConverter
																						.convertToAsset(asset);
																				assetService.saveAsset(assetDAO,
																						new AsyncCallback<Void>() {

																							@Override
																							public void onFailure(
																									Throwable caught) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub

																							}

																							@Override
																							public void onSuccess(
																									Void result) {

																							}

																						});
																			}

																		});

															}

														});

											}

										});
							}

						});

						grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "right");

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

			}

		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("removeBorder");
		buttonPanel.getElement().getStyle().setMarginLeft(2, Unit.PX);
		buttonPanel.setWidth("auto");

		buttonPanel.add(addButton);
		buttonPanel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);

		horizontalPanel.add(modelItems);
		horizontalPanel.add(buttonPanel);

		horizontalPanel.setCellWidth(modelItems, "100%");

		return horizontalPanel;
	}

	private void toggleSupplierField(final Grid grid, Boolean isBegin) {
		if (isBegin) {
			supplierField = new ListBox();
			supplierField.setStyleName("inputFieldStyle");

			final Button addSupplierBtn = new Button("+");

			addSupplierBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					toggleSupplierField(grid, false);
				}

			});
			addSupplierBtn.getElement().getStyle().setMarginLeft(5, Unit.PX);

			lookupService.getLookupByCategory("101", new AsyncCallback<List<Lookup>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(List<Lookup> suppliers) {
					for (Lookup supplier : suppliers) {
						supplierField.addItem(supplier.getName());
					}

					grid.setText(2, 0, "Supplier Master:");
					grid.setWidget(2, 1, supplierField);
					grid.setWidget(2, 2, addSupplierBtn);
				}

			});

		} else {
			supplierNameField = new TextBox();
			supplierNameField.setStyleName("inputFieldStyle");

			Button backBtn = new Button("X");
			backBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					toggleSupplierField(grid, true);
				}

			});

			backBtn.getElement().getStyle().setMarginLeft(5, Unit.PX);

			grid.setText(2, 0, "Supplier Name:");
			grid.setWidget(2, 1, supplierNameField);
			grid.setWidget(2, 2, backBtn);
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
		} else {
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
			updateObsAndTagTable(rowMap.get(row), assetTable.getText(row, 1));
			selectedAssetRow = row;
		} // drag and drop completed
	}

	private void convertRowToEditable() {
		if (selectedTable != null) {
			final int row = selectedRow;
			final FlexTable table = selectedTable;

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
				final TextBox codeField = new TextBox();
				codeField.setText(table.getText(row, 0));

				final TextBox descriptionField = new TextBox();
				descriptionField.setText(table.getText(row, 1));

				final ListBox inputTypeField = new ListBox();
				inputTypeField.addItem("<Select>");
				inputTypeField.getElement().getStyle().setProperty("width", "100%");

				inputTypeField.addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						String inputType = inputTypeField.getSelectedValue();
						if (inputType.equals("condition")) {
							showDialogBox(true);
						} else if (inputType.equals("Numeric Value")) {
							showDialogBox(false);
						}
					}

				});

				lookupService.getLookupByCategory("102", new AsyncCallback<List<Lookup>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<Lookup> inputTypes) {
						for (Lookup inputType : inputTypes) {
							inputTypeField.addItem(inputType.getName());
						}

						table.setWidget(row, 0, codeField);
						table.setWidget(row, 1, descriptionField);
						table.setWidget(row, 2, inputTypeField);
					}

				});
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

	private void showDialogBox(Boolean isConidition) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setSize("100%", "100%");
		dialogBox.setAnimationEnabled(true);

		dialogBox.addStyleName("dialogBoxStyle");

		final VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		final HorizontalPanel h1panel = new HorizontalPanel();
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

		final Grid grid = new Grid(5, 4);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("padding", "20px");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		final ListBox measurementField = new ListBox();
		measurementField.setStyleName("inputFieldStyle");

		final ListBox unitsField = new ListBox();
		unitsField.setStyleName("inputFieldStyle");

		final TextBox lowerLimit = new TextBox();
		lowerLimit.setStyleName("inputFieldStyle");

		final TextBox upperLimit = new TextBox();
		upperLimit.setStyleName("inputFieldStyle");

		final Button saveBtn = new Button("Save");

		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}

		});

		lookupService.getMeasurements(new AsyncCallback<List<Measurement>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<Measurement> measurements) {
				for (Measurement measurement : measurements) {
					measurementField.addItem(measurement.getName());
				}
				measurementField.addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						unitsField.clear();
						lookupService.getLookupByMeasurementName(measurementField.getSelectedValue(),
								new AsyncCallback<List<Lookup>>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(List<Lookup> lookups) {
										for (Lookup lookup : lookups) {
											unitsField.addItem(lookup.getName());
										}
									}

								});
					}

				});

				grid.setText(0, 0, "Measurement");
				grid.setWidget(0, 1, measurementField);
				grid.setWidget(0, 2, unitsField);

				grid.setText(1, 0, "Lower Limit");
				grid.setWidget(1, 1, lowerLimit);

				grid.setText(2, 0, "Upper Limit");
				grid.setWidget(2, 1, upperLimit);

				grid.setWidget(3, 1, saveBtn);

				vpanel.add(h1panel);
				vpanel.add(grid);

				dialogBox.add(vpanel);
				dialogBox.center();
			}

		});
	}

	private void revertFieldsToNormalState() {
		final int row = editableRow;
		final FlexTable table = editableTable;

		if (table == assetTable) {
			final String ecn = ((TextBox) table.getWidget(row, 0)).getText();
			final String location = ((TextBox) table.getWidget(row, 3)).getText();
			final String assetName = ((TextBox) table.getWidget(row, 1)).getText();

			table.setText(row, 0, ecn.trim().length() < 1 ? "NULL" : ecn.trim());
			table.setText(row, 1, assetName.trim().length() < 1 ? "NULL" : assetName.trim());
			table.setText(row, 3, location.trim().length() < 1 ? "NULL" : location.trim());

			selectedAsset.setEcn(ecn);
			selectedAsset.setLocation(location);
			assetService.saveAsset(typeConverter.convertToAsset(selectedAsset), new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Void result) {
				}

			});

		} else if (table == obsTable) {

			String code = ((TextBox) table.getWidget(row, 0)).getText();
			String description = ((TextBox) table.getWidget(row, 1)).getText();
			String inputType = ((ListBox) table.getWidget(row, 2)).getSelectedValue();

			table.setText(row, 0, code);
			table.setText(row, 1, description);
			table.setText(row, 2, inputType);

			selectedAsset.getObservations().get(2 - row).setCode(code);
			selectedAsset.getObservations().get(2 - row).setDescription(description);

			assetService.saveAsset(typeConverter.convertToAsset(selectedAsset), new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Void result) {
				}

			});

		} else if (table == tagTable) {
			String assetName = ((TextBox) table.getWidget(row, 0)).getText();
			String code = ((TextBox) table.getWidget(row, 1)).getText();
			String tag = ((TextBox) table.getWidget(row, 2)).getText();

			table.setText(row, 0, assetName);
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

	private void getAssets(String data, final Tree tree) {

		if (data != null) {
			JavaScriptObject jsArray = JsonUtils.safeEval(data);

			List<Asset> assets = getAssetHierrarchy(jsArray);

			assetService.saveAssets(assets, new AsyncCallback<List<AssetDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(List<AssetDTO> assetDTOs) {
					renderTree(assetDTOs, tree);
					// Window.alert(assetDTOs.size() + "");
				}

			});
		}
	}

	private List<Asset> getAssetHierrarchy(JavaScriptObject assetArray) {
		List<Asset> assets = new ArrayList<>();

		for (int i = 0; i < jsUtil.getArrayLength(assetArray); i++) {
			JavaScriptObject assetObject = jsUtil.getArrayElement(assetArray, i);

			Asset asset = new Asset();
			String name = jsUtil.getValueAsString(assetObject, "Name");
			String id = jsUtil.getValueAsString(assetObject, "ID");

			asset.setId(id);
			asset.setName(name);

			List<Observation> observations = new ArrayList<>();

			JavaScriptObject observationArray = jsUtil.getObjectProperty(assetObject, "Attributes");

			for (int j = 0; j < jsUtil.getArrayLength(observationArray); j++) {
				JavaScriptObject observationObject = jsUtil.getArrayElement(observationArray, j);
				Observation observation = new Observation();
				observation.setDescription(jsUtil.getValueAsString(observationObject, "Description"));
				observations.add(observation);
			}

			asset.setObservations(observations);

			JavaScriptObject childAssetsArray = jsUtil.getObjectProperty(assetObject, "Elements");
			if (childAssetsArray != null && jsUtil.isArray(childAssetsArray)) {
				List<Asset> childAssets = getAssetHierrarchy(childAssetsArray);
				asset.setChildAssets(childAssets);
			}

			assets.add(asset);
		}
		return assets;
	}

}
