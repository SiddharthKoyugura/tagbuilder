package com.assetsense.tagbuilder.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;
import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.service.AssetService;
import com.assetsense.tagbuilder.service.AssetServiceAsync;
import com.assetsense.tagbuilder.service.LookupService;
import com.assetsense.tagbuilder.service.LookupServiceAsync;
import com.assetsense.tagbuilder.utils.JsUtil;
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

	private final JsUtil jsUtil = new JsUtil();

	private FlexTable assetTable;
	private FlexTable obsTable;
	private FlexTable tagTable;

	private FlexTable selectedTable = null;
	private int selectedRow;
	private Asset selectedAsset = null;

	private int selectedAssetRow = 0;

	private TextBox supplierNameField;
	private ListBox supplierField;

	private final Map<String, Measurement> measurementMap = new HashMap<String, Measurement>();

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
			public void onSuccess(final String result) {
				// renderTree(getElements(result), tree);
				lookupService.getMeasurements(new AsyncCallback<List<Measurement>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<Measurement> measurements) {
						for (Measurement measurement : measurements) {
							measurementMap.put(measurement.getName(), measurement);
						}
						getAssets(result, tree);
					}

				});
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

	private void updateTables(String assetId) {
		assetService.getAssetById(assetId, new AsyncCallback<Asset>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Asset asset) {
				selectedAsset = asset;
				int row = getTableLastRow(assetTable);

				TextBox ecnField = new TextBox();
				String ecn = (asset.getEcn() != null && asset.getEcn().trim().length() > 0) ? asset.getEcn() : "";
				ecnField.setText(ecn);

				TextBox assetField = new TextBox();
				String assetName = asset.getName();
				assetField.setText(assetName);

				TextBox locationField = new TextBox();
				String location = asset.getLocation() != null ? asset.getLocation() : "";
				locationField.setText(location);

				assetTable.setWidget(row, 0, ecnField);
				assetTable.setWidget(row, 1, assetField);
				assetTable.setWidget(row, 2, getModelField(asset));
				assetTable.setWidget(row, 3, locationField);

				assetTable.getRowFormatter().addStyleName(row, "selectedRow");

				setCursorPointer(assetTable, row);

				updateObsAndTagTable(asset.getName());

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

	private void updateObsAndTagTable(String assetName) {
		resetTable(obsTable);
		resetTable(tagTable);

		assetService.getAssetByName(assetName, new AsyncCallback<Asset>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Asset asset) {
				selectedAsset = asset;
				int row = getTableLastRow(obsTable);
				for (final Observation observation : asset.getObservations()) {

					final ListBox measurementField = new ListBox();
					measurementField.getElement().getStyle().setBackgroundColor("white");
					measurementField.setWidth("100%");

					final ListBox unitsField = new ListBox();
					unitsField.getElement().getStyle().setBackgroundColor("white");
					unitsField.setWidth("100%");

					if (observation.getMeasurement() == null) {
						measurementField.addItem("<Select>");
					}
					if (observation.getUnitid() == null) {
						unitsField.addItem("<Select>");
					}

					measurementField.addChangeHandler(new ChangeHandler() {

						@Override
						public void onChange(ChangeEvent event) {
							unitsField.clear();
							unitsField.addItem("<Select>");
							updateUnitsField(unitsField, measurementField.getSelectedValue(), false, observation);
						}

					});

					for (Map.Entry<String, Measurement> entry : measurementMap.entrySet()) {
						Measurement measurement = entry.getValue();
						measurementField.addItem(measurement.getName());
						if (observation.getMeasurement() != null) {
							selectListBoxItem(measurementField, observation.getMeasurement().getName());
							updateUnitsField(unitsField, observation.getMeasurement().getName(), true, observation);
						}
					}

					String description = observation.getDescription();

					final TextBox codeField = new TextBox();
					codeField.setText(observation.getCode() != null && observation.getCode().length() > 0
							? observation.getCode() : "");

					final TextBox descriptionField = new TextBox();
					descriptionField.setText(description.trim().length() > 0 ? description.trim() : "");

					final ListBox inputTypeField = new ListBox();
					if (observation.getInputType() == null) {
						inputTypeField.addItem("<Select>");
					}
					inputTypeField.getElement().getStyle().setProperty("width", "100%");

					inputTypeField.addChangeHandler(new ChangeHandler() {

						@Override
						public void onChange(ChangeEvent event) {
							// String inputType =
							// inputTypeField.getSelectedValue();
						}

					});

					final int currentRow = row;
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

							if (observation.getInputType() != null) {
								selectListBoxItem(inputTypeField, observation.getInputType().getName());
							}

							obsTable.setWidget(currentRow, 0, codeField);
							obsTable.setWidget(currentRow, 1, descriptionField);
							obsTable.setWidget(currentRow, 2, inputTypeField);
							obsTable.setWidget(currentRow, 3, measurementField);
							obsTable.setWidget(currentRow, 4, unitsField);

							obsTable.getRowFormatter().addStyleName(currentRow, "selectedRow");
							setCursorPointer(obsTable, currentRow);
						}

					});

					Tag tag = observation.getTag();
					if (tag != null) {
						String ecn = (tag.getAsset() != null && tag.getAsset().getEcn() != null
								&& tag.getAsset().getEcn().length() > 0) ? tag.getAsset().getEcn() : "NULL";
								
						String code = (observation.getCode() != null && !observation.getCode().isEmpty()) ? observation.getCode() : "NULL";
						
						String tagName = (tag.getName() != null && !tag.getName().isEmpty()) ? tag.getName() : "NULL";
						
						TextBox tagNameField = new TextBox();
						tagNameField.setWidth("80%");
						tagNameField.setText(tagName);	

						tagTable.setText(row, 0, ecn);
						tagTable.setText(row, 1, code);
						tagTable.setWidget(row, 2, tagNameField);
					} else {
						tagTable.setText(row, 0, "NULL");
						tagTable.setText(row, 1, "NULL");
						tagTable.setText(row, 2, "NULL");
						Window.alert("Hey");
					}
					tagTable.getRowFormatter().addStyleName(row, "selectedRow");

					setCursorPointer(tagTable, row);

					row++;
				}
			}

		});
	}

	private void updateUnitsField(final ListBox unitsField, final String measurementName, final Boolean hasUnit,
			final Observation observation) {
		lookupService.getLookupByMeasurementName(measurementName, new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<Lookup> lookups) {
				for (Lookup lookup : lookups) {
					unitsField.addItem(lookup.getName());
				}
				if (hasUnit) {
					selectListBoxItem(unitsField, observation.getUnitid().getName());
				}
			}

		});
	}

	private void selectListBoxItem(ListBox listBox, String value) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(value)) {
				listBox.setSelectedIndex(i);
				break;
			}
		}
	}

	private int getTableLastRow(FlexTable table) {
		return table.getRowCount();
	}

	private void setCursorPointer(FlexTable table, int row) {
		table.getRowFormatter().getElement(row).getStyle().setProperty("cursor", "pointer");
	}

	private HorizontalPanel getModelField(final Asset asset) {
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

																				assetService.updateAsset(asset,
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

	private VerticalPanel buildDetailsDashboard() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.setHeight("100%");
		vpanel.getElement().getStyle().setBackgroundColor("#EEEEEE");

		vpanel.add(buildDetailsNavbar());
		vpanel.add(buildTables());

		return vpanel;
	}

	private HorizontalPanel buildDetailsNavbar() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setWidth("100%");

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.getElement().getStyle().setProperty("padding", "20px 20px 0 20px");

		Button saveBtn = new Button("Save");

		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveFields();
			}

		});

		saveBtn.getElement().getStyle().setMarginLeft(20, Unit.PX);

		hpanel.add(saveBtn);

		mainPanel.add(hpanel);

		return mainPanel;
	}

	private VerticalPanel buildTables() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.getElement().getStyle().setPadding(10, Unit.PX);

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setWidth("100%");
		hpanel.getElement().getStyle().setPaddingRight(10, Unit.PX);

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

		mainPanel.add(assetTable);

		setClickHandler(assetTable);

		return mainPanel;
	}

	private VerticalPanel buildObservationTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("300px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");
		mainPanel.getElement().getStyle().setMarginLeft(10, Unit.PX);

		obsTable = new FlexTable();
		obsTable.setWidth("100%");
		obsTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		obsTable.setStyleName("table-padding tableStyle");

		obsTable.setText(0, 0, "Observations");

		obsTable.getFlexCellFormatter().setColSpan(0, 0, 5);
		obsTable.getRowFormatter().setStyleName(0, "bg-blue");
		obsTable.getRowFormatter().getElement(0).getStyle().setProperty("borderBottom", "1px solid black");

		obsTable.setText(1, 0, "Code");
		obsTable.setText(1, 1, "Description");
		obsTable.setText(1, 2, "Input Type");
		obsTable.setText(1, 3, "Measurement");
		obsTable.setText(1, 4, "Units");

		obsTable.getRowFormatter().setStyleName(1, "bg-blue");
		obsTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		mainPanel.add(obsTable);

		setClickHandler(obsTable);

		return mainPanel;
	}

	private VerticalPanel buildTagTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
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
					final int row = cell.getRowIndex();
					if (row > 1) {
						handleSelection(table, row);
					}
				}
			}
		});
	}

	private void resetTableStates() {
		selectedTable = null;
		selectedRow = 0;
	}

	private void handleSelection(FlexTable table, int row) {
		selectedTable = table;
		selectedRow = row;
		if (table == assetTable && selectedAssetRow != row) {
			updateObsAndTagTable(assetTable.getText(row, 1));
			selectedAssetRow = row;
		}
	}

	private void saveFields() {
		final int row = selectedRow;
		final FlexTable table = selectedTable;

		if (table == assetTable) {
			String ecn = ((TextBox) table.getWidget(row, 0)).getText();
			String location = ((TextBox) table.getWidget(row, 3)).getText();
			String assetName = ((TextBox) table.getWidget(row, 1)).getText();

			ecn = ecn.length() > 0 ? ecn : null;
			location = location.length() > 0 ? location : null;
			assetName = assetName.length() > 0 ? assetName : null;

			selectedAsset.setEcn(ecn);
			selectedAsset.setLocation(location);
			selectedAsset.setName(assetName);
			updateAsset(selectedAsset);

		} else if (table == obsTable) {

			String code = ((TextBox) table.getWidget(row, 0)).getText();
			String description = ((TextBox) table.getWidget(row, 1)).getText();
			final String inputType = ((ListBox) table.getWidget(row, 2)).getSelectedValue();
			final String measurement = ((ListBox) table.getWidget(row, 3)).getSelectedValue();
			final String units = ((ListBox) table.getWidget(row, 4)).getSelectedValue();

			code = code.isEmpty() ? null : code;

			final Observation observation = selectedAsset.getObservations().get(row - 2);
			observation.setCode(code);
			observation.setDescription(description);

			lookupService.getLookupByName(inputType, new AsyncCallback<Lookup>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(Lookup inputTypeLookup) {
					observation.setInputType(inputTypeLookup);
					if (!measurement.equals("<Select>")) {
						observation.setMeasurement(measurementMap.get(measurement));
						lookupService.getLookupByName(units, new AsyncCallback<Lookup>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(Lookup unit) {
								observation.setUnitid(unit);
								updateAsset(selectedAsset);
							}
						});
					} else {
						updateAsset(selectedAsset);
					}
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

	}

	private void updateAsset(Asset asset) {
		assetService.updateAsset(asset, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
			}

		});
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

			assetService.saveAssets(assets, new AsyncCallback<List<Asset>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("Message");
				}

				@Override
				public void onSuccess(List<Asset> assetDTOs) {
					renderTree(assetDTOs, tree);
				}

			});
		} else {
			assetService.getParentAssets(new AsyncCallback<List<Asset>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(List<Asset> assets) {
					renderTree(assets, tree);
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
				observation.setDescription(jsUtil.getValueAsString(observationObject, "Name"));

				// Tag
				String piPoint = jsUtil.getValueAsString(observationObject, "PIPoint");
				Tag tag = new Tag();
				if (piPoint != null && !piPoint.isEmpty()) {
					tag.setName(piPoint);
				}
				tag.setAsset(asset);
				tag.setObservation(observation);

				observation.setTag(tag);
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
