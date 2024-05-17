package com.assetsense.tagbuilder.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;
import com.assetsense.tagbuilder.c2.domain.Model;
import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.service.AssetService;
import com.assetsense.tagbuilder.service.AssetServiceAsync;
import com.assetsense.tagbuilder.service.LookupService;
import com.assetsense.tagbuilder.service.LookupServiceAsync;
import com.assetsense.tagbuilder.service.ModelService;
import com.assetsense.tagbuilder.service.ModelServiceAsync;
import com.assetsense.tagbuilder.service.TagService;
import com.assetsense.tagbuilder.service.TagServiceAsync;
import com.assetsense.tagbuilder.utils.JsUtil;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
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
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
	private final TagServiceAsync tagService = GWT.create(TagService.class);
	private final ModelServiceAsync modelService = GWT.create(ModelService.class);

	private final JsUtil jsUtil = new JsUtil();

	private FlexTable assetTable;
	private FlexTable obsTable;
	private FlexTable tagTable;

	private Asset selectedAsset = null;
	private int selectedAssetRow = 0;

	private Set<Label> selectedTagLabels = new HashSet<Label>();
	private String currentAssetCategory;

	private Label messageLabel;

	private final Map<String, Measurement> measurementMap = new HashMap<String, Measurement>();
	private final Map<String, Lookup> lookupMap = new HashMap<String, Lookup>();

	public void loadTagBuilderPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildTaskPage());
	}

	private SplitLayoutPanel buildTaskPage() {
		SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
		splitLayoutPanel.setSize("100%", "100%");

		splitLayoutPanel.addStyleName("mySplitLayoutPanel");

		splitLayoutPanel.addNorth(buildNavbar(), 48);
		splitLayoutPanel.addWest(buildLeftSidebar(), 300);

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

		vpanel.add(buildSearchPanel());

		vpanel.add(buildAssetTagPanels());

		mainPanel.add(vpanel);

		return mainPanel;
	}

	private VerticalPanel buildAssetTagPanels() {
		final VerticalPanel mainPanel = new VerticalPanel();

		final Tree tree = new Tree();
		tree.getElement().getStyle().setMarginTop(10, Unit.PX);

		final ScrollPanel spanel1 = new ScrollPanel();
		spanel1.setSize("300px", "280px");
		spanel1.addStyleName("surroundBorder");

		spanel1.getElement().getStyle().setMarginBottom(10, Unit.PX);

		final ScrollPanel spanel2 = new ScrollPanel();
		spanel2.setSize("300px", "300px");
		spanel2.getElement().getStyle().setPadding(10, Unit.PX);

		final HorizontalPanel searchPanel = new HorizontalPanel();
		searchPanel.setWidth("100%");
		searchPanel.setStyleName("searchPanel");

		final TextBox searchField = new TextBox();

		searchField.getElement().getStyle().setBackgroundColor("white");
		searchField.getElement().setPropertyString("placeholder", "Search a tag");

		searchPanel.add(searchField);

		searchField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				tagService.getTagsByNameSubString(searchField.getText(), new AsyncCallback<List<Tag>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(List<Tag> tags) {
						spanel2.clear();

						final VerticalPanel vpanel = new VerticalPanel();
						for (Tag tag : tags) {
							final Label label = new Label(tag.getName());
							label.addStyleName("taskLabel");
							label.getElement().setDraggable("true");
							label.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (selectedTagLabels.contains(label)) {
										label.removeStyleName("selectedLabel");
										selectedTagLabels.remove(label);
									} else {
										label.addStyleName("selectedLabel");
										selectedTagLabels.add(label);
									}
								}

							});

							label.addDragStartHandler(new DragStartHandler() {

								@Override
								public void onDragStart(DragStartEvent event) {
									event.setData("tagName", label.getText());
								}

							});

							vpanel.add(label);
						}
						spanel2.add(vpanel);
					}

				});
			}

		});

		jsUtil.sendMessageToServer("{\"request\":\"elements\", \"id\":\"\"}", new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(final String result) {
				lookupService.getLookups(new AsyncCallback<List<Lookup>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(List<Lookup> lookups) {
						for (Lookup lookup : lookups) {
							lookupMap.put(lookup.getName(), lookup);
						}

						lookupService.getMeasurements(new AsyncCallback<List<Measurement>>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(List<Measurement> measurements) {
								for (Measurement measurement : measurements) {
									measurementMap.put(measurement.getName(), measurement);
								}
								buildAssets(result, tree);
								spanel1.add(tree);
								mainPanel.add(spanel1);
								mainPanel.add(searchPanel);

								jsUtil.sendMessageToServer("{\"request\":\"tags\", \"id\":\"\"}",
										new AsyncCallback<String>() {

											@Override
											public void onFailure(Throwable caught) {

											}

											@Override
											public void onSuccess(String result) {
												tagService.saveTags(getTags(result), new AsyncCallback<List<Tag>>() {

													@Override
													public void onFailure(Throwable caught) {

													}

													@Override
													public void onSuccess(final List<Tag> tags) {
														final VerticalPanel vpanel = new VerticalPanel();
														for (Tag tag : tags) {
															final Label label = new Label(tag.getName());
															label.addStyleName("taskLabel");
															label.getElement().setDraggable("true");
															label.addClickHandler(new ClickHandler() {

																@Override
																public void onClick(ClickEvent event) {
																	if (selectedTagLabels.contains(label)) {
																		label.removeStyleName("selectedLabel");
																		selectedTagLabels.remove(label);
																	} else {
																		label.addStyleName("selectedLabel");
																		selectedTagLabels.add(label);
																	}
																}

															});

															label.addDragStartHandler(new DragStartHandler() {

																@Override
																public void onDragStart(DragStartEvent event) {
																	event.setData("tagName", label.getText());
																}

															});

															vpanel.add(label);
														}
														spanel2.add(vpanel);

														mainPanel.add(spanel2);
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

	private List<Tag> getTags(String data) {
		List<Tag> tags = new ArrayList<>();
		if (data != null) {
			JavaScriptObject jsArray = JsonUtils.safeEval(data);
			for (int i = 0; i < jsUtil.getArrayLength(jsArray); i++) {
				Tag tag = new Tag();
				JavaScriptObject piPoint = jsUtil.getArrayElement(jsArray, i);
				String name = jsUtil.getValueAsString(piPoint, "Name");
				tag.setName(name);
				tags.add(tag);
			}
		}
		return tags;
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
				Window.alert("Validation error");
			}

			@Override
			public void onSuccess(final Asset asset) {
				if (isAssetSaved(asset)) {
					messageLabel.setText(
							"Warning: This asset has already saved. You can edit this asset in Asset Master");
				} else {
					messageLabel.setText("");
					selectedAsset = asset;
					final int row = getTableLastRow(assetTable);

					if (row != 2 && (asset.getAssetCategory() == null || (currentAssetCategory != null
							&& !currentAssetCategory.equals(asset.getAssetCategory().getName())))) {
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

						final Grid grid = new Grid(2, 1);
						grid.setCellPadding(10);
						grid.getElement().getStyle().setProperty("padding", "20px");
						grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
						grid.setWidth("100%");

						Label label = new Label(
								"You may lost your changes, would you like to continue saving or abort current changes?");
						label.getElement().getStyle().setFontWeight(FontWeight.BOLD);

						Button continueBtn = new Button("Continue");
						Button abortBtn = new Button("Abort");

						abortBtn.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								dialogBox.hide();
								resetTable(assetTable);
								buildAssetFields(asset, row);
							}

						});

						continueBtn.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								dialogBox.hide();
							}

						});

						HorizontalPanel hpanel = new HorizontalPanel();
						hpanel.add(continueBtn);
						hpanel.add(abortBtn);

						abortBtn.getElement().getStyle().setMarginLeft(10, Unit.PX);

						grid.setWidget(0, 0, label);
						grid.setWidget(1, 0, hpanel);

						vpanel.add(h1panel);
						vpanel.add(grid);

						dialogBox.add(vpanel);

						dialogBox.center();
					} else {
						buildAssetFields(asset, row);
					}
				}
			}

		});
	}

	private Boolean isAssetSaved(Asset asset) {
		if (asset.getEcn() == null || asset.getName() == null || asset.getModel() == null || asset.getLocation() == null
				|| asset.getAssetCategory() == null) {
			return false;
		}
		for (Observation observation : asset.getObservations()) {
			if (observation.getCode() == null || observation.getInputType() == null
					|| observation.getDescription() == null || observation.getMeasurement() == null
					|| observation.getUnitid() == null || observation.getLowerLimit() == null
					|| observation.getUpperLimit() == null || observation.getTag() == null) {
				return false;
			}
			Tag tag = observation.getTag();
			if (tag.getName() == null || tag.getName().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private void buildAssetFields(Asset asset, int row) {
		final TextBox ecnField = new TextBox();
		String ecn = (asset.getEcn() != null && asset.getEcn().trim().length() > 0) ? asset.getEcn() : "";
		ecnField.setText(ecn);

		ecnField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String ecn = ecnField.getText();
				for (int row = 2; row < getTableLastRow(tagTable); row++) {
					updateTagTableField(row, 0, ecn);
				}
			}

		});

		TextBox assetField = new TextBox();
		final String assetName = asset.getName();
		assetField.setText(assetName);

		// Delete Button
		Button removeRowBtn = new Button("-");

		removeRowBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int row = getAssetTableRowByAssetName(assetName);
				assetTable.removeRow(row);
				if (row == selectedAssetRow) {
					resetTable(obsTable);
					resetTable(tagTable);

					if (assetTable.getRowCount() > 2) {
						selectedAssetRow = 2;
						updateObsAndTagTable(((TextBox) assetTable.getWidget(selectedAssetRow, 1)).getText());
					}

				} else if (row < selectedAssetRow) {
					selectedAssetRow--;
				}
			}
		});

		assetTable.setWidget(row, 0, ecnField);
		assetTable.setWidget(row, 1, assetField);
		assetTable.setWidget(row, 2, getModelField(asset));
		assetTable.setWidget(row, 3, getLocationField(asset));
		assetTable.setWidget(row, 4, removeRowBtn);

		assetTable.getRowFormatter().addStyleName(row, "selectedRow");

		setCursorPointer(assetTable, row);

		updateObsAndTagTable(asset.getName());

		selectedAssetRow = row;
		if (asset.getAssetCategory() != null) {
			currentAssetCategory = asset.getAssetCategory().getName();
		}
	}

	private void updateObsAndTagTable(String assetName) {
		resetTable(obsTable);
		resetTable(tagTable);

		assetService.getAssetByName(assetName, new AsyncCallback<Asset>() {

			@Override
			public void onFailure(Throwable caught) {

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
					}

					if (observation.getMeasurement() != null) {
						selectListBoxItem(measurementField, observation.getMeasurement().getName());
						updateUnitsField(unitsField, observation.getMeasurement().getName(), true, observation);
					}

					String description = observation.getDescription();

					final TextBox codeField = new TextBox();
					codeField.setWidth("98%");
					final String currentCode = observation.getCode() != null && observation.getCode().length() > 0
							? observation.getCode() : "";
					codeField.setText(currentCode);

					final TextBox descriptionField = new TextBox();
					descriptionField.setWidth("98%");
					descriptionField.setText(description.trim().length() > 0 ? description.trim() : "");

					final ListBox inputTypeField = new ListBox();
					inputTypeField.getElement().getStyle().setBackgroundColor("white");
					inputTypeField.getElement().getStyle().setProperty("width", "100%");

					final TextBox lowerLimitField = new TextBox();
					lowerLimitField.setWidth("80%");
					setDecimalInputCriteria(lowerLimitField);

					final TextBox upperLimitField = new TextBox();
					upperLimitField.setWidth("80%");
					setDecimalInputCriteria(upperLimitField);

					if (observation.getLowerLimit() != null) {
						lowerLimitField.setText(observation.getLowerLimit().toString());
					}
					if (observation.getUpperLimit() != null) {
						upperLimitField.setText(observation.getUpperLimit().toString());
					}

					final int currentRow = row;

					lookupService.getLookupByCategory("102", new AsyncCallback<List<Lookup>>() {

						@Override
						public void onFailure(Throwable caught) {

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
							obsTable.setWidget(currentRow, 5, lowerLimitField);
							obsTable.setWidget(currentRow, 6, upperLimitField);

							obsTable.getRowFormatter().addStyleName(currentRow, "selectedRow");
							setCursorPointer(obsTable, currentRow);
						}

					});

					codeField.addKeyUpHandler(new KeyUpHandler() {
						@Override
						public void onKeyUp(KeyUpEvent event) {
							String code = codeField.getText();
							updateTagTableField(currentRow, 1, code);
						}

					});

					Tag tag = observation.getTag();
					if (tag != null) {
						String ecn = (tag.getAsset() != null && tag.getAsset().getEcn() != null
								&& tag.getAsset().getEcn().length() > 0) ? tag.getAsset().getEcn() : "";

						String code = (observation.getCode() != null && !observation.getCode().isEmpty())
								? observation.getCode() : "";

						String tagName = (tag.getName() != null && !tag.getName().isEmpty()) ? tag.getName() : "";

						TextBox tagNameField = new TextBox();
						tagNameField.setWidth("80%");
						tagNameField.setText(tagName);

						tagTable.setText(row, 0, ecn);
						tagTable.setText(row, 1, code);
						tagTable.setWidget(row, 2, tagNameField);
					} else {
						tagTable.setText(row, 0, "");
						tagTable.setText(row, 1, "");
						tagTable.setText(row, 2, "");
					}
					tagTable.getRowFormatter().addStyleName(row, "selectedRow");

					setCursorPointer(tagTable, row);

					row++;
				}
			}

		});
	}

	private int getAssetTableRowByAssetName(String assetName) {
		for (int i = 2; i < assetTable.getRowCount(); i++) {
			TextBox assetNameField = (TextBox) assetTable.getWidget(i, 1);
			if (assetNameField.getText().equals(assetName)) {
				return i;
			}
		}
		return -1;
	}

	private void setDecimalInputCriteria(final TextBox textBox) {
		textBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				char inputChar = event.getCharCode();
				if (!isValidCharacter(inputChar, textBox.getText())) {
					textBox.cancelKey();
				}
			}
		});
	}

	private boolean isValidCharacter(char ch, String currentText) {
		if (Character.isDigit(ch)) {
			return true;
		} else if (ch == '.') {
			return !currentText.contains(".");
		}
		return false;
	}

	private void updateTagTableField(int row, int col, String data) {
		tagTable.setText(row, col, data);
	}

	private void updateUnitsField(final ListBox unitsField, final String measurementName, final Boolean hasUnit,
			final Observation observation) {
		lookupService.getLookupByMeasurementName(measurementName, new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {

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

		if (asset.getAssetCategory() != null) {
			modelService.getModelsByAssetCategoryName(asset.getAssetCategory().getName(),
					new AsyncCallback<List<Model>>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(List<Model> models) {
							for (Model model : models) {
								modelItems.addItem(model.getName());
							}
							if (asset.getModel() != null) {
								selectListBoxItem(modelItems, asset.getModel().getName());
							}
						}

					});
		}
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

				final ListBox assetCategoryListField = new ListBox();
				assetCategoryListField.setStyleName("inputFieldStyle");

				final TextBox assetCategoryNameField = new TextBox();
				assetCategoryNameField.setStyleName("inputFieldStyle");

				final ListBox supplierField = new ListBox();
				supplierField.setStyleName("inputFieldStyle");

				final TextBox supplierNameField = new TextBox();
				supplierNameField.setStyleName("inputFieldStyle");

				grid.setText(0, 0, "Model #:");
				grid.setWidget(0, 1, modelField);

				lookupService.getLookupByCategory("100", new AsyncCallback<List<Lookup>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(List<Lookup> assetTypes) {
						for (Lookup assetType : assetTypes) {
							assetTypeField.addItem(assetType.getName());
						}

						grid.setText(1, 0, "Asset Category:");
						toggleFields(grid, true, 1, assetCategoryListField, assetCategoryNameField, "111", asset);

						grid.setText(2, 0, "Asset Type:");
						grid.setWidget(2, 1, assetTypeField);

						grid.setText(3, 0, "Supplier Master:");
						toggleFields(grid, true, 3, supplierField, supplierNameField, "101", asset);

						Button saveBtn = new Button("Save");
						grid.setWidget(4, 1, saveBtn);

						saveBtn.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								String assetCategory = null;
								if (assetCategoryNameField.getText().isEmpty()) {
									assetCategory = assetCategoryListField.getSelectedValue();
								} else {
									assetCategory = assetCategoryNameField.getText();
									final Lookup assetCategoryLookup = new Lookup();
									assetCategoryLookup.setCategoryId("111");
									assetCategoryLookup.setName(assetCategory);

									lookupService.saveLookup(assetCategoryLookup, new AsyncCallback<Lookup>() {

										@Override
										public void onFailure(Throwable caught) {

										}

										@Override
										public void onSuccess(Lookup result) {
											String supplierName = null;
											if (supplierNameField.getText().isEmpty()) {
												supplierName = supplierField.getSelectedValue();
											} else {
												supplierName = supplierNameField.getText();
											}
											final Lookup supplier = new Lookup();
											supplier.setCategoryId("101");
											supplier.setName(supplierName);

											lookupService.saveLookup(supplier, new AsyncCallback<Lookup>() {

												@Override
												public void onFailure(Throwable caught) {

												}

												@Override
												public void onSuccess(Lookup result) {
													List<String> lookupNames = new ArrayList<>();
													lookupNames.add(assetCategoryLookup.getName());
													lookupNames.add(assetTypeField.getSelectedValue());
													lookupNames.add(supplier.getName());

													saveModel(asset, lookupNames, modelItems, dialogBox, modelField);
												}

											});
										}

									});
								}

								String supplierName = null;
								if (supplierNameField.getText().isEmpty()) {
									supplierName = supplierField.getSelectedValue();
								} else {
									supplierName = supplierNameField.getText();
									final Lookup supplier = new Lookup();
									supplier.setCategoryId("101");
									supplier.setName(supplierName);

									final String assetCategoryName = assetCategory;

									lookupService.saveLookup(supplier, new AsyncCallback<Lookup>() {

										@Override
										public void onFailure(Throwable caught) {

										}

										@Override
										public void onSuccess(Lookup result) {
											List<String> lookupNames = new ArrayList<>();
											lookupNames.add(assetCategoryName);
											lookupNames.add(assetTypeField.getSelectedValue());
											lookupNames.add(supplier.getName());

											saveModel(asset, lookupNames, modelItems, dialogBox, modelField);
										}

									});
								}

								List<String> lookupNames = new ArrayList<>();
								lookupNames.add(assetCategory);
								lookupNames.add(assetTypeField.getSelectedValue());
								lookupNames.add(supplierName);

								saveModel(asset, lookupNames, modelItems, dialogBox, modelField);
							}

						});

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

	private void saveModel(final Asset asset, final List<String> lookupNames, final ListBox modelItems,
			final DialogBox dialogBox, final TextBox modelField) {
		lookupService.getLookupsByNames(lookupNames, new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<Lookup> lookups) {

				Model model = new Model();
				model.setName(modelField.getValue());

				model.setAssetCategory(lookups.get(0));
				model.setAssetType(lookups.get(1));
				model.setSupplierName(lookups.get(2));

				asset.setAssetCategory(lookups.get(0));

				modelService.saveModel(model, new AsyncCallback<Model>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Model model) {
						modelItems.addItem(modelField.getText());
						dialogBox.hide();

						asset.setModel(model);

						assetService.updateAsset(asset, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Void result) {
								messageLabel.setText("Model saved");
							}

						});
					}

				});
			}

		});
	}

	private HorizontalPanel getLocationField(final Asset asset) {
		final Asset finalAsset;
		if (asset == null) {
			finalAsset = new Asset();
		} else {
			finalAsset = asset;
		}

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setStyleName("removeBorder");

		final ListBox locationField = new ListBox();
		locationField.getElement().getStyle().setBackgroundColor("white");
		locationField.setWidth("100%");

		lookupService.getLookupByCategory("location", new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<Lookup> lookups) {
				if (finalAsset.getLocation() == null) {
					locationField.addItem("<Select>");
				}
				for (Lookup lookup : lookups) {
					locationField.addItem(lookup.getName());
				}
				if (finalAsset.getLocation() != null) {
					selectListBoxItem(locationField, finalAsset.getLocation().getName());
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

				final Grid grid = new Grid(2, 3);
				grid.setCellPadding(10);
				grid.getElement().getStyle().setProperty("padding", "20px");
				grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
				grid.setWidth("100%");

				final TextBox locationNameField = new TextBox();
				locationNameField.setStyleName("inputFieldStyle");
				locationNameField.setWidth("40%");

				grid.setText(0, 0, "Location:");
				grid.setWidget(0, 1, locationNameField);

				Button saveBtn = new Button("Save");
				grid.setWidget(1, 1, saveBtn);

				saveBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						final String location = locationNameField.getText();
						Lookup lookup = new Lookup();
						lookup.setCategoryId("location");
						lookup.setName(location);

						lookupService.saveLookup(lookup, new AsyncCallback<Lookup>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Lookup result) {
								locationField.addItem(location);
								dialogBox.hide();
							}

						});
					}

				});

				grid.getCellFormatter().getElement(1, 1).getStyle().setProperty("textAlign", "right");

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

		horizontalPanel.add(locationField);
		horizontalPanel.add(buttonPanel);

		horizontalPanel.setCellWidth(locationField, "100%");

		return horizontalPanel;
	}

	private void toggleFields(final Grid grid, Boolean isBegin, final int row, final ListBox listField,
			final TextBox textField, final String lookupCategory, final Asset asset) {
		if (isBegin) {
			textField.setText("");
			final Button addBtn = new Button("+");

			addBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					toggleFields(grid, false, row, listField, textField, lookupCategory, asset);
				}

			});

			addBtn.getElement().getStyle().setMarginLeft(5, Unit.PX);

			lookupService.getLookupByCategory(lookupCategory, new AsyncCallback<List<Lookup>>() {

				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(List<Lookup> lookups) {
					for (Lookup lookup : lookups) {
						listField.addItem(lookup.getName());
					}

					grid.setWidget(row, 1, listField);
					grid.setWidget(row, 2, addBtn);

					if (row == 1 && asset.getAssetCategory() != null) {
						selectListBoxItem(listField, asset.getAssetCategory().getName());
					}
				}

			});
		} else {
			Button backBtn = new Button("X");
			backBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					toggleFields(grid, true, row, listField, textField, lookupCategory, asset);
				}

			});

			backBtn.getElement().getStyle().setMarginLeft(5, Unit.PX);

			grid.setWidget(row, 1, textField);
			grid.setWidget(row, 2, backBtn);
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

		mainPanel.getElement().getStyle().setProperty("padding", "20px 20px 0 20px");

		HorizontalPanel hpanel = new HorizontalPanel();

		Button saveBtn = new Button("Save");

		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveFields();
			}

		});

		Button clearBtn = new Button("Clear");

		clearBtn.getElement().getStyle().setMarginLeft(20, Unit.PX);

		clearBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				messageLabel.setText("");
				resetTable(assetTable);
				resetTable(obsTable);
				resetTable(tagTable);
				currentAssetCategory = null;
			}
		});

		hpanel.add(saveBtn);
		hpanel.add(clearBtn);

		messageLabel = new Label();
		messageLabel.addStyleName("messageLabel");
		hpanel.add(messageLabel);

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
		hpanel.add(buildTagTable());

		mainPanel.add(hpanel);
		mainPanel.add(buildObservationTable());

		return mainPanel;
	}

	private void updateTablesFromTagDrop(Tag tag) {
		if (tag != null) {
			int assetRow = assetTable.getRowCount();
			Asset asset = new Asset();
			tag.setAsset(asset);
			if (assetRow == 2) {
				updateAssetTable(assetRow, asset);
				updateObservationTable();
				updateTagTable(tag, asset);
			} else {
				updateObservationTable();
				updateTagTable(tag, selectedAsset);
			}
		}
	}

	private void updateAssetTable(int row, Asset asset) {

		final TextBox ecnField = new TextBox();

		ecnField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String ecn = ecnField.getText();
				for (int row = 2; row < getTableLastRow(tagTable); row++) {
					updateTagTableField(row, 0, ecn);
				}
			}

		});

		TextBox assetField = new TextBox();

		if (asset.getEcn() != null) {
			ecnField.setText(asset.getEcn());
		}
		if (asset.getName() != null) {
			assetField.setText(asset.getName());
		}

		// Delete Button
		Button removeRowBtn = new Button("-");

		removeRowBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int row = getAssetTableRowByAssetName("");
				assetTable.removeRow(row);
				if (row == selectedAssetRow) {
					resetTable(obsTable);
					resetTable(tagTable);

					if (assetTable.getRowCount() > 2) {
						selectedAssetRow = 2;
						updateObsAndTagTable(((TextBox) assetTable.getWidget(selectedAssetRow, 1)).getText());
					}

				} else if (row < selectedAssetRow) {
					selectedAssetRow--;
				}
			}
		});

		assetTable.setWidget(row, 0, ecnField);
		assetTable.setWidget(row, 1, assetField);
		assetTable.setWidget(row, 2, getModelField(asset));
		assetTable.setWidget(row, 3, getLocationField(asset));
		assetTable.setWidget(row, 4, removeRowBtn);

		assetTable.getRowFormatter().addStyleName(row, "selectedRow");

		selectedAsset = asset;
		selectedAssetRow = row;
		setCursorPointer(assetTable, row);
	}

	private void updateTagTable(Tag tag, Asset asset) {
		int row = tagTable.getRowCount();
		String ecn = (tag.getAsset() != null && tag.getAsset().getEcn() != null && tag.getAsset().getEcn().length() > 0)
				? tag.getAsset().getEcn() : "";

		Observation observation = new Observation();
		observation.setTag(tag);

		String code = (observation != null && observation.getCode() != null && !observation.getCode().isEmpty())
				? observation.getCode() : "";

		String tagName = tag.getName();

		TextBox tagNameField = new TextBox();
		tagNameField.setWidth("80%");
		tagNameField.setText(tagName);

		tagTable.setText(row, 0, ecn);
		tagTable.setText(row, 1, code);
		tagTable.setWidget(row, 2, tagNameField);
		tagTable.getRowFormatter().addStyleName(row, "selectedRow");

		setCursorPointer(tagTable, row);

		asset.getObservations().add(observation);
	}

	private void updateObservationTable() {
		final int obsRow = obsTable.getRowCount();

		final ListBox measurementField = new ListBox();
		measurementField.getElement().getStyle().setBackgroundColor("white");
		measurementField.setWidth("100%");

		final ListBox unitsField = new ListBox();
		unitsField.getElement().getStyle().setBackgroundColor("white");
		unitsField.setWidth("100%");

		measurementField.addItem("<Select>");
		unitsField.addItem("<Select>");

		measurementField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				unitsField.clear();
				unitsField.addItem("<Select>");
				updateUnitsField(unitsField, measurementField.getSelectedValue(), false, null);
			}

		});

		for (Map.Entry<String, Measurement> entry : measurementMap.entrySet()) {
			Measurement measurement = entry.getValue();
			measurementField.addItem(measurement.getName());
		}

		final TextBox codeField = new TextBox();
		codeField.setWidth("98%");

		final TextBox descriptionField = new TextBox();
		descriptionField.setWidth("98%");

		final ListBox inputTypeField = new ListBox();

		inputTypeField.getElement().getStyle().setBackgroundColor("white");
		inputTypeField.getElement().getStyle().setProperty("width", "100%");

		final TextBox lowerLimitField = new TextBox();
		lowerLimitField.setWidth("80%");
		setDecimalInputCriteria(lowerLimitField);

		final TextBox upperLimitField = new TextBox();
		upperLimitField.setWidth("80%");
		setDecimalInputCriteria(upperLimitField);

		lookupService.getLookupByCategory("102", new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<Lookup> inputTypes) {
				for (Lookup inputType : inputTypes) {
					inputTypeField.addItem(inputType.getName());
				}

				obsTable.setWidget(obsRow, 0, codeField);
				obsTable.setWidget(obsRow, 1, descriptionField);
				obsTable.setWidget(obsRow, 2, inputTypeField);
				obsTable.setWidget(obsRow, 3, measurementField);
				obsTable.setWidget(obsRow, 4, unitsField);
				obsTable.setWidget(obsRow, 5, lowerLimitField);
				obsTable.setWidget(obsRow, 6, upperLimitField);

				obsTable.getRowFormatter().addStyleName(obsRow, "selectedRow");
				setCursorPointer(obsTable, obsRow);
			}

		});

		codeField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String code = codeField.getText();
				updateTagTableField(obsRow, 1, code);
			}

		});

		obsTable.getRowFormatter().addStyleName(obsRow, "selectedRow");
		setCursorPointer(obsTable, obsRow);
	}

	private ScrollPanel buildAssetTable() {
		ScrollPanel mainPanel = new ScrollPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("300px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");

		assetTable = new FlexTable();
		assetTable.setWidth("100%");

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
				if (elementId == null || elementId.isEmpty()) {
					return;
				}
				updateTables(elementId);
			}

		}, DropEvent.getType());

		assetTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		assetTable.setStyleName("table-padding tableStyle");

		assetTable.setText(0, 0, "Asset Master");

		assetTable.getFlexCellFormatter().setColSpan(0, 0, 5);
		assetTable.getRowFormatter().setStyleName(0, "bg-blue");
		assetTable.getRowFormatter().getElement(0).getStyle().setProperty("borderBottom", "1px solid black");

		assetTable.setText(1, 0, "ECN");
		assetTable.setText(1, 1, "Asset name");
		assetTable.setText(1, 2, "Model#");
		assetTable.setText(1, 3, "Location");
		assetTable.setText(1, 4, "");

		assetTable.getRowFormatter().setStyleName(1, "bg-blue");
		assetTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		mainPanel.add(assetTable);

		setClickHandler(assetTable);

		return mainPanel;
	}

	private ScrollPanel buildTagTable() {
		ScrollPanel mainPanel = new ScrollPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("300px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");
		mainPanel.getElement().getStyle().setMarginLeft(10, Unit.PX);

		mainPanel.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				event.preventDefault();
			}

		}, DragOverEvent.getType());

		mainPanel.addDomHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				String tagName = event.getData("tagName");
				if (tagName == null || tagName.isEmpty()) {
					return;
				}
				List<String> tagNames = new ArrayList<>();
				for (Label label : selectedTagLabels) {
					tagNames.add(label.getText());
					label.removeStyleName("selectedLabel");
				}
				tagService.getTagsByNames(tagNames, new AsyncCallback<List<Tag>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(List<Tag> tags) {
						for (Tag tag : tags) {
							updateTablesFromTagDrop(tag);
						}
						selectedTagLabels.clear();
					}

				});
			}

		}, DropEvent.getType());

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

	private ScrollPanel buildObservationTable() {
		ScrollPanel mainPanel = new ScrollPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("270px");
		mainPanel.getElement().getStyle().setBackgroundColor("#D9D9D9");
		mainPanel.getElement().getStyle().setProperty("border", "1px solid black");
		mainPanel.getElement().getStyle().setMarginTop(10, Unit.PX);

		obsTable = new FlexTable();
		obsTable.setWidth("100%");
		obsTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		obsTable.setStyleName("table-padding tableStyle");

		obsTable.setText(0, 0, "Observations");

		obsTable.getFlexCellFormatter().setColSpan(0, 0, 7);
		obsTable.getRowFormatter().setStyleName(0, "bg-blue");
		obsTable.getRowFormatter().getElement(0).getStyle().setProperty("borderBottom", "1px solid black");

		obsTable.setText(1, 0, "Code");
		obsTable.setText(1, 1, "Description");
		obsTable.setText(1, 2, "Input Type");
		obsTable.setText(1, 3, "Measurement");
		obsTable.setText(1, 4, "Units");
		obsTable.setText(1, 5, "Lower limit");
		obsTable.setText(1, 6, "Upper limit");

		obsTable.getRowFormatter().setStyleName(1, "bg-blue");
		obsTable.getRowFormatter().getElement(1).getStyle().setProperty("cursor", "pointer");

		mainPanel.add(obsTable);

		setClickHandler(obsTable);

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

	private void handleSelection(FlexTable table, int row) {
		if (table == assetTable && selectedAssetRow != row) {
			String assetName = ((TextBox) assetTable.getWidget(row, 1)).getText();
			updateObsAndTagTable(assetName);
			selectedAssetRow = row;
		}
	}

	private void saveFields() {
		int row = selectedAssetRow;

		String ecn = ((TextBox) assetTable.getWidget(row, 0)).getText();
		String assetName = ((TextBox) assetTable.getWidget(row, 1)).getText();
		final String modelName = ((ListBox) ((HorizontalPanel) assetTable.getWidget(row, 2)).getWidget(0))
				.getSelectedValue();
		String location = ((ListBox) ((HorizontalPanel) assetTable.getWidget(row, 3)).getWidget(0)).getSelectedValue();

		ecn = ecn.length() > 0 ? ecn : null;
		assetName = assetName.length() > 0 ? assetName : null;

		selectedAsset.setEcn(ecn);
		selectedAsset.setName(assetName);

		lookupService.getLookupByName(location, new AsyncCallback<Lookup>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Lookup lookup) {
				selectedAsset.setLocation(lookup);

				modelService.getModelByName(modelName, new AsyncCallback<Model>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Model model) {
						if (model != null) {
							selectedAsset.setModel(model);
							selectedAsset.setAssetCategory(model.getAssetCategory());
						}

						for (int row = 2; row < obsTable.getRowCount(); row++) {
							String code = ((TextBox) obsTable.getWidget(row, 0)).getText();
							String description = ((TextBox) obsTable.getWidget(row, 1)).getText();
							final String inputType = ((ListBox) obsTable.getWidget(row, 2)).getSelectedValue();
							final String measurement = ((ListBox) obsTable.getWidget(row, 3)).getSelectedValue();
							final String units = ((ListBox) obsTable.getWidget(row, 4)).getSelectedValue();

							TextBox lowerLimitField = (TextBox) obsTable.getWidget(row, 5);
							TextBox upperLimitField = (TextBox) obsTable.getWidget(row, 6);
							final String lowerLimitString = lowerLimitField != null ? lowerLimitField.getText() : "";
							final String upperLimitString = upperLimitField != null ? upperLimitField.getText() : "";

							code = code.length() > 0 ? code : null;

							final Observation observation = selectedAsset.getObservations().get(row - 2);
							observation.setCode(code);
							observation.setDescription(description);
							final Double lowerLimit = lowerLimitString.isEmpty() ? null
									: Double.parseDouble(lowerLimitString);
							final Double upperLimit = upperLimitString.isEmpty() ? null
									: Double.parseDouble(upperLimitString);
							observation.setLowerLimit(lowerLimit);
							observation.setUpperLimit(upperLimit);

							String tag = ((TextBox) tagTable.getWidget(row, 2)).getText();

							observation.getTag().setName(tag);

							if (lookupMap.containsKey(inputType)) {
								Lookup inputTypeLookup = lookupMap.get(inputType);

								observation.setInputType(inputTypeLookup);
								if (!measurement.equals("<Select>")) {
									observation.setMeasurement(measurementMap.get(measurement));

									if (lookupMap.containsKey(units)) {
										observation.setUnitid(lookupMap.get(units));
									}
								}
							}
						}

						updateAsset(selectedAsset);
					}

				});

			}

		});
	}

	private void updateAsset(Asset asset) {
		assetService.saveAsset(asset, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				messageLabel.setText("Failed!");
			}

			@Override
			public void onSuccess(Void result) {
				messageLabel.setText("Success!");
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

	private void buildAssets(String data, final Tree tree) {

		if (data != null) {
			JavaScriptObject jsArray = JsonUtils.safeEval(data);

			List<Asset> assets = getAssetHierrarchy(jsArray);

			assetService.saveAssets(assets, new AsyncCallback<List<Asset>>() {

				@Override
				public void onFailure(Throwable caught) {
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

				}

				@Override
				public void onSuccess(List<Asset> assets) {
					renderTree(assets, tree);
				}

			});
		}
	}

	private List<Asset> getAssetHierrarchy(JavaScriptObject assetArray) {
		final List<Asset> assets = new ArrayList<>();
		for (int i = 0; i < jsUtil.getArrayLength(assetArray); i++) {
			final JavaScriptObject assetObject = jsUtil.getArrayElement(assetArray, i);

			final Asset asset = new Asset();
			String name = jsUtil.getValueAsString(assetObject, "Name");
			String id = jsUtil.getValueAsString(assetObject, "ID");
			String category = jsUtil.getValueAsString(assetObject, "Template");

			asset.setId(id);
			asset.setName(name);
			// asset.setCategory(category);

			Lookup assetCategory = new Lookup();
			assetCategory.setCategoryId("111");
			assetCategory.setName(category);

			if (category != null && !category.isEmpty()) {
				asset.setAssetCategory(assetCategory);
			}
			final List<Observation> observations = new ArrayList<>();

			JavaScriptObject observationArray = jsUtil.getObjectProperty(assetObject, "Attributes");

			for (int j = 0; j < jsUtil.getArrayLength(observationArray); j++) {
				JavaScriptObject observationObject = jsUtil.getArrayElement(observationArray, j);
				final Observation observation = new Observation();
				observation.setDescription(jsUtil.getValueAsString(observationObject, "Name"));

				String units = "";
				String unitData = jsUtil.getValueAsString(observationObject, "DefaultUOM");
				String measurement = "";
				if (unitData != null) {
					unitData = unitData.toLowerCase();
					if (unitData.contains("celsius")) {
						units = "celsius";
						measurement = "Temperature";
					} else if (unitData.contains("fahrenheit")) {
						units = "Fahrenheit";
						measurement = "Temperature";
					} else if (unitData.contains("kelvin")) {
						measurement = "Temperature";
						units = "kelvin";
					} else if (unitData.contains("atmosphere")) {
						measurement = "Pressure";
						units = "atm";
					} else if (unitData.contains("bar")) {
						measurement = "Pressure";
						units = "bar";
					} else if (unitData.contains("pascal")) {
						measurement = "Pressure";
						units = "Pa";
					}
				}

				// Tag
				String piPoint = jsUtil.getValueAsString(observationObject, "PIPoint");
				Tag tag = new Tag();
				if (piPoint != null && !piPoint.isEmpty()) {
					tag.setName(piPoint);
				}
				tag.setAsset(asset);
				tag.setObservation(observation);

				if (lookupMap.containsKey(units)) {
					observation.setUnitid(lookupMap.get(units));
					if (measurementMap.containsKey(measurement)) {
						observation.setMeasurement(measurementMap.get(measurement));
					}
				}

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
