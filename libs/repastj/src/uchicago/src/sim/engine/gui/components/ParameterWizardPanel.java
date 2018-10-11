/*$$
 * Copyright (c) 2004, Argonne National Laboratory (ANL)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of ANL nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package uchicago.src.sim.engine.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import uchicago.src.sim.engine.gui.ParameterWizard;
import uchicago.src.sim.engine.gui.model.ConstantParameter;
import uchicago.src.sim.engine.gui.model.DataParameter;
import uchicago.src.sim.engine.gui.model.IncrementParameter;
import uchicago.src.sim.engine.gui.model.ListParameter;
import uchicago.src.sim.engine.gui.model.ParamBlockParameter;

/**
 * 
 * @author wes maciorowski
 */
public class ParameterWizardPanel extends JPanel {
	private static final long serialVersionUID = 4051327855340041520L;

	private final static String treeName = "Parameter Definitions:";

	private DefaultMutableTreeNode top = null;

	// private ArrayListListModel configuredParmListModel = null;
	private ArrayListListModel inputParmListModel = null;

	private ButtonGroup paramTypeGroup;

	private EnhancedJTable allParameterTable;

	// parameter/run operation buttons
	private JButton addNestParamButton = new JButton();
	private JButton removeParamButton = new JButton();
	private JButton addParamButton = new JButton();

	private JButton removeParamBlockButton = new JButton();
	private JButton addParamBlockButton = new JButton();
	
	private JPanel runEditPanel;
	
//	private JRadioButton blockTypeParamButton = new JRadioButton();
//	private JRadioButton blockTypeParamBlockButton = new JRadioButton();
//	
//	private ButtonGroup blockTypeGroup;
	
	// dialog buttons
	private JPanel dialogButtonPanel;
	private JButton okButton;
	private JButton cancelButton;

	// file select
	private JPanel outputLocationPanel;
	private JButton selectButton;
	private JLabel fileNameLabel;
	private JLabel outputLocationLabel;
	
	// increment parameter labels/text fields
	private JLabel startLabel = new JLabel();	
	private JLabel endLabel = new JLabel();
	private JLabel incrLabel = new JLabel();

	private JTextField startTextField = new JTextField();
	private JTextField endTextField = new JTextField();
	private JTextField incrTextField = new JTextField();
	private JLabel commaLabel;
	
	// runs
	private JTextField runsTextField = new JTextField("1");
	private JLabel runsLabel = new JLabel();

	

	private JList inputParameterList = new JList();
	private JPanel allParametersPanel;
	private JPanel inputParametersPanel;

	// parameter type
	private JRadioButton listOfValuesRadioButton = new JRadioButton();
	private JRadioButton incrValueRadioButton = new JRadioButton();
	private JRadioButton constValueRadioButton = new JRadioButton();

	
	private JPanel paramEditPanel;
	
	private JTabbedPane tabPane;


	private JTree configuredParamTree = null;

	private ParameterData aParameterData;

	private ParameterDataObjectTableModel aParameterDataObjectTableModel;

	private ParameterWizard aParameterWizard;

	private TreePath curPath;

	
	/** Creates new form DataWizardFrame */
	public ParameterWizardPanel(ParameterWizard aParameterWizard) {
		this.aParameterWizard = aParameterWizard;
		initComponents();
	}

	/**
	 * @param inputParameterList
	 *            The inputParameterList to set.
	 */
	public void setInputParameterList(javax.swing.JList inputParameterList) {
		this.inputParameterList = inputParameterList;
	}

	/**
	 * @return Returns the inputParameterList.
	 */
	public JList getInputParameterList() {
		return inputParameterList;
	}

	/**
	 * returns the user selected output location
	 * 
	 * @return the user selected output location
	 */
	public String getOutputLocation() {
		return outputLocationLabel.getText();
	}

	
	public void setOutputLocation(String aFileName) {
		outputLocationLabel.setText(aFileName);
	}

	public void setParameterData(ParameterData aParameterData) {
		this.aParameterData = aParameterData;
		aParameterDataObjectTableModel.setAParameterData(aParameterData);
		inputParmListModel.setSomeList(aParameterData.getInputParameterList());

		ArrayList rootNodes = aParameterData.getRootNodes();
		DefaultTreeModel model = (DefaultTreeModel) configuredParamTree
				.getModel();
		DefaultMutableTreeNode aNode = null;

		for (int i = 0; i < rootNodes.size(); i++) {
			aNode = (DefaultMutableTreeNode) rootNodes.get(i);
			model.insertNodeInto(aNode, top, top.getChildCount());
		}

		expandAll(configuredParamTree, true);
		setOutputLocation(aParameterData.getOutputLocation());
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();

		// Traverse tree from root
		expandAll(tree, new TreePath(root), expand);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();

		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	public void configureOutputFilePanel() {
		GridBagConstraints gridBagConstraints;

		outputLocationPanel.setLayout(new GridBagLayout());
		fileNameLabel = new JLabel();
		outputLocationLabel = new JLabel();
		selectButton = new JButton();
		fileNameLabel.setFont(new Font("Microsoft Sans Serif", 1, 11));
		fileNameLabel.setText("File Name:");

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);

		// gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		outputLocationPanel.add(fileNameLabel, gridBagConstraints);

		outputLocationLabel.setText("File Name Not Specified.");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		outputLocationPanel.add(outputLocationLabel, gridBagConstraints);

		selectButton.setText("Select");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		outputLocationPanel.add(selectButton, gridBagConstraints);
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				selectButtonAction(false);
			}
		});
	}

	public void rePaintTree() {
		configuredParamTree.repaint();
		configuredParamTree.invalidate();

		DefaultTreeModel model = (DefaultTreeModel) configuredParamTree
				.getModel();
		model.nodeChanged((TreeNode) curPath.getLastPathComponent());
	}

	/**
	 * Loads scenarios and runs into the tree menu.
	 */
	public void refreshTree() {
		top = new DefaultMutableTreeNode(treeName);

		// loadCases(top);
		configuredParamTree = new JTree(top);
		configuredParamTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		configuredParamTree.setRootVisible(false);

		// Listen for when the selection changes.
		configuredParamTree
				.addTreeSelectionListener(new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
//						DefaultMutableTreeNode node = (DefaultMutableTreeNode) configuredParmTree
//								.getLastSelectedPathComponent();

						updateGUIState();

//						if (node == null) {
//							return;
//						}

						// Object nodeInfo = node.getUserObject();
						// if (nodeInfo instanceof
						// uchicago.src.sim.engine.gui.model.Parameter) {
						// } else if (nodeInfo instanceof
						// com.pg.ecmo.meta.Scenario) {
						// }
						// curParameter =
						// (uchicago.src.sim.engine.gui.model.Parameter)
						// nodeInfo;
						// aParameterWizard.actionPerformed(new
						// ActionEvent(configuredParmTree,
						// ActionEvent.ACTION_PERFORMED, VIEW_CASE));
					}
				});

		configuredParamTree.getModel().addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				DefaultMutableTreeNode node;
				node = (DefaultMutableTreeNode) (e.getTreePath()
						.getLastPathComponent());

				/*
				 * If the event lists children, then the changed node is the
				 * child of the node we've already gotten. Otherwise, the
				 * changed node and the specified node are the same.
				 */
				try {
					int index = e.getChildIndices()[0];
					node = (DefaultMutableTreeNode) (node.getChildAt(index));
				} catch (NullPointerException exc) {
				}

				// System.out.println("The user has finished editing the
				// node.");
				// System.out.println("New value: " + node.getUserObject());
			}

			public void treeNodesInserted(TreeModelEvent arg0) {
				updateGUIState();
			}

			public void treeNodesRemoved(TreeModelEvent arg0) {
				updateGUIState();
			}

			public void treeStructureChanged(TreeModelEvent arg0) {
			}
		});
	}

	private void updateGUIState() {
		// enable the panel that modifies parameters only if there is a root
		// runs node
		if (configuredParamTree.getModel().getChildCount(top) == 0) {
			changePanelEnableState(paramEditPanel, false);
			
			this.removeParamBlockButton.setEnabled(false);
			this.removeParamButton.setEnabled(false);
		} else {
			// disable the add parameter block (run) button if a parameter
			// block is selected
			if (configuredParamTree.getSelectionPath() != null  
					&& ((DefaultMutableTreeNode) configuredParamTree.getLastSelectedPathComponent()).getUserObject() instanceof ParamBlockParameter) {
				addParamBlockButton.setEnabled(false);				
			} else {
				addParamBlockButton.setEnabled(true);
			}
			
			this.removeParamBlockButton.setEnabled(true);
			
			changePanelEnableState(paramEditPanel, true);
			
			// if something isn't selected in the parameter list
			// disable the add parameter buttons
			if (inputParameterList.isSelectionEmpty()) {
				addNestParamButton.setEnabled(false);
				addParamButton.setEnabled(false);
			} else {
				// don't let them add params to the root, they must nest
				if (configuredParamTree.getSelectionPath() != null
						&& configuredParamTree.getSelectionPath().getParentPath().getLastPathComponent() != top) {
					addParamButton.setEnabled(true);
				} else {
					addParamButton.setEnabled(false);
				}
				
				addNestParamButton.setEnabled(true);
			}
			
			// if something is not selected in the parameter tree
			// disable the remove buttons
			if (configuredParamTree.getSelectionCount() == 0) {
				removeParamButton.setEnabled(false);
				removeParamBlockButton.setEnabled(false);
			} else {
				removeParamButton.setEnabled(true);
				removeParamBlockButton.setEnabled(true);
			}
		}
	}
	
	protected void CancelActionPerformed(ActionEvent e) {
		aParameterWizard.actionPerformed(new ActionEvent(outputLocationLabel,
				ActionEvent.ACTION_PERFORMED, ParameterWizard.EXIT));
	}

	protected void OKActionPerformed(ActionEvent e) {
		boolean dataOK = false;

		if (outputLocationLabel.getText() == null
				|| outputLocationLabel.getText().equals("")) {
			selectButtonAction(true);

			if (outputLocationLabel.getText() == null
					|| outputLocationLabel.getText().equals("")) {
				JOptionPane
						.showMessageDialog(
								this.aParameterWizard.getDialog(),
								"You must select an output parameter file to run in Multi-Run mode.",
								"Alert", JOptionPane.ERROR_MESSAGE);
			}

			if (outputLocationLabel.getText() == null
					|| outputLocationLabel.getText().equals("")) {
				dataOK = false;
			} else {
				dataOK = true;
			}
		} else {
			dataOK = true;
		}

		if (dataOK) {
			aParameterWizard.actionPerformed(new ActionEvent(
					outputLocationLabel, ActionEvent.ACTION_PERFORMED,
					ParameterWizard.RUN_SIMULATION));
		}
	}

	protected void addNestParamActionPerformed(ActionEvent e) {
		DataParameter aParameter = null;

		try {
			aParameter = createParameterNode();
		} catch (DataTypeMismatchException e1) {
			JOptionPane.showMessageDialog(this.aParameterWizard.getDialog(), e1
					.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);

			return;
		}

		DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(aParameter);
		DefaultTreeModel model = (DefaultTreeModel) configuredParamTree
				.getModel();
		DefaultMutableTreeNode parentNode = null;

		if (configuredParamTree.getSelectionPath() != null) {
			parentNode = (DefaultMutableTreeNode) configuredParamTree
					.getSelectionPath().getLastPathComponent();
		} else {
			parentNode = top;
		}

		model.insertNodeInto(aNode, parentNode, parentNode.getChildCount());

		TreePath aTreePath = new TreePath(aNode.getPath());
		configuredParamTree.scrollPathToVisible(aTreePath);
		configuredParamTree.setSelectionPath(aTreePath);
	}

	protected void addParmActionPerformed(ActionEvent e) {
		DataParameter aParameter = null;

		try {
			aParameter = createParameterNode();
		} catch (DataTypeMismatchException e1) {
			JOptionPane.showMessageDialog(this.aParameterWizard.getDialog(), e1
					.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);

			return;
		}

		DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(aParameter);
		DefaultTreeModel model = (DefaultTreeModel) configuredParamTree
				.getModel();
		DefaultMutableTreeNode parentNode = null;

		// grab the selected componenet's parent and add this
		// new node under it
		if (configuredParamTree.getSelectionPath() != null) {
			parentNode = (DefaultMutableTreeNode) configuredParamTree
					.getSelectionPath().getParentPath().getLastPathComponent();
		} else {
			parentNode = top;
		}
		
		model.insertNodeInto(aNode, parentNode, parentNode.getChildCount());

		TreePath aTreePath = new TreePath(aNode.getPath());
		configuredParamTree.scrollPathToVisible(aTreePath);
		configuredParamTree.setSelectionPath(aTreePath);
	}
	
	protected void addBlockActionPerformed(ActionEvent e) {
		DataParameter aParameter = null;

		try {
			aParameter = createBlockNode();
		} catch (DataTypeMismatchException e1) {
			JOptionPane.showMessageDialog(this.aParameterWizard.getDialog(), e1
					.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);

			return;
		}

		DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(aParameter);
		DefaultTreeModel model = (DefaultTreeModel) configuredParamTree
				.getModel();
		DefaultMutableTreeNode parentNode = null;

		if (configuredParamTree.getSelectionPath() != null) {
			parentNode = (DefaultMutableTreeNode) configuredParamTree
					.getSelectionPath().getLastPathComponent();
		} else {
			parentNode = top;
		}
		model.insertNodeInto(aNode, parentNode, parentNode.getChildCount());

		TreePath aTreePath = new TreePath(aNode.getPath());
		configuredParamTree.scrollPathToVisible(aTreePath);
		configuredParamTree.setSelectionPath(aTreePath);
	}

	protected void inputParameterListChanged(ListSelectionEvent evt) {
		updateGUIState();
	}

	protected void removeActionPerformed(ActionEvent e) {
		DefaultMutableTreeNode parentNode = null;

		if (configuredParamTree.getSelectionPath() != null) {
			parentNode = (DefaultMutableTreeNode) configuredParamTree
					.getSelectionPath().getLastPathComponent();
		}

		if (!parentNode.getUserObject().equals(treeName)) {
			((DefaultTreeModel) configuredParamTree.getModel())
					.removeNodeFromParent(parentNode);
		}
	}

	protected void valueTypeSpecificationPerformed(ActionEvent evt) {
		if (listOfValuesRadioButton.isSelected()) {
			endTextField.setVisible(false);
			incrTextField.setVisible(false);
			endLabel.setVisible(false);
			incrLabel.setVisible(false);
			startLabel.setText("List of Values:");
			commaLabel.setText("Note: values are space separated");
		} else if (constValueRadioButton.isSelected()) {
			endTextField.setVisible(false);
			incrTextField.setVisible(false);
			endLabel.setVisible(false);
			incrLabel.setVisible(false);
			startLabel.setText("Const Value:");
			commaLabel.setText("");
		} else {
			endTextField.setVisible(true);
			incrTextField.setVisible(true);
			endLabel.setVisible(true);
			incrLabel.setVisible(true);
			startLabel.setText("Start Value:");
			commaLabel.setText("");
		}
	}

//	protected void blockTypeEventPerformed(ActionEvent evt) {
//		if (blockTypeParamButton.isSelected()) {
//			paramEditPanel.setVisible(true);
//			runEditPanel.setVisible(false);
//			
//			// make sure this gets enabled when switching views
//			if (configuredParmTree.getModel().getChildCount(top) > 0) {
//				addNestParmButton.setEnabled(true);
//			}
//			
//		} else if (blockTypeParamBlockButton.isSelected()) {
//			paramEditPanel.setVisible(false);
//			runEditPanel.setVisible(true);
//		}
//	}

//	private JPanel createBlockTypePanel() {
//		JPanel typePanel = new JPanel(new GridBagLayout());
//		GridBagConstraints gridBagConstraints = new GridBagConstraints();
//		
//		JLabel typeLabel = new JLabel("Choose whether to add a parameter or a run block");
//		
//		blockTypeGroup = new ButtonGroup();
//		blockTypeGroup.add(blockTypeParamButton);
//		blockTypeGroup.add(blockTypeParamBlockButton);
//		
//		blockTypeParamBlockButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				blockTypeEventPerformed(evt);
//			}
//		});
//		blockTypeParamButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				blockTypeEventPerformed(evt);
//			}
//		});
//		
//		
//		blockTypeParamButton.setText("Parameter");
//		
//		blockTypeParamBlockButton.setText("Run block");
//		blockTypeParamBlockButton.setSelected(true);
//
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy = 0;
//		gridBagConstraints.anchor = GridBagConstraints.WEST;
//		typePanel.add(typeLabel, gridBagConstraints);
//		
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy++;
//		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//		typePanel.add(blockTypeParamBlockButton, gridBagConstraints);
//		
//		gridBagConstraints.gridx = 1;
//		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
//		typePanel.add(blockTypeParamButton, gridBagConstraints);
//		
//		return typePanel;
//	}
	
	private void addParamTypeRadios(JPanel toAddTo, GridBagConstraints gridBagConstraints) {
		JLabel paramTypeLabel = new JLabel();
		paramTypeLabel.setText(" Parameter Definition: ");
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);

		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		toAddTo.add(paramTypeLabel, gridBagConstraints);

		incrValueRadioButton.setText("Increment");
		incrValueRadioButton.setSelected(true);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy++;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		toAddTo.add(incrValueRadioButton, gridBagConstraints);

		listOfValuesRadioButton.setText("List");
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		toAddTo.add(listOfValuesRadioButton, gridBagConstraints);

		constValueRadioButton.setText("Constant");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		toAddTo.add(constValueRadioButton, gridBagConstraints);
		
		paramTypeGroup = new ButtonGroup();
		
		incrValueRadioButton
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						valueTypeSpecificationPerformed(evt);
					}
				});

		listOfValuesRadioButton
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						valueTypeSpecificationPerformed(evt);
					}
				});

		constValueRadioButton
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						valueTypeSpecificationPerformed(evt);
					}
				});

		paramTypeGroup.add(incrValueRadioButton);
		paramTypeGroup.add(listOfValuesRadioButton);
		paramTypeGroup.add(constValueRadioButton);
	}
	
	private JPanel createParameterEditPanel() {
		paramEditPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints.gridy = 0;
		addParamTypeRadios(paramEditPanel, gridBagConstraints);
		
		startLabel.setText("Start Value:");
		gridBagConstraints.gridy++;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		paramEditPanel.add(startLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		paramEditPanel.add(startTextField, gridBagConstraints);

		endLabel.setText("End Value:");
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy++;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		paramEditPanel.add(endLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		paramEditPanel.add(endTextField, gridBagConstraints);

		incrLabel.setText("Step:");
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy++;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		paramEditPanel.add(incrLabel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		paramEditPanel.add(incrTextField, gridBagConstraints);

		commaLabel = new JLabel();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy++;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.LINE_END;
		paramEditPanel.add(commaLabel, gridBagConstraints);
		
		gridBagConstraints.gridy++;
		gridBagConstraints.gridwidth = 3;
		paramEditPanel.add(new JLabel(
				"Note: you must select a parameter to the left"),
				gridBagConstraints);
		
		gridBagConstraints.gridy++;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		paramEditPanel.add(new JLabel(
				"before you can add"),
				gridBagConstraints);			

		
		// gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy++;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		paramEditPanel.add(createParamOperationsPanel(), gridBagConstraints);
		
		paramEditPanel.setBorder(BorderFactory.createTitledBorder("Parameters (disabled until a run is added)"));
		
		changePanelEnableState(paramEditPanel, false);
		
		return paramEditPanel;
	}
	
	private void changePanelEnableState(JPanel panel, boolean enableState) {
		for (int i = 0; i < panel.getComponents().length; i++) {
			panel.getComponent(i).setEnabled(enableState);
		}
	}
	
	private JPanel createRightSidePanel() {
		JPanel rightSidePanel = new JPanel(new GridBagLayout());
		
		paramEditPanel = createParameterEditPanel();
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//		rightSidePanel.add(createBlockTypePanel(), gridBagConstraints);
		
		gridBagConstraints.gridy++;
		runEditPanel = createRunEditPanel();
		rightSidePanel.add(runEditPanel, gridBagConstraints);
		
		gridBagConstraints.gridy++;
		rightSidePanel.add(paramEditPanel, gridBagConstraints);
		paramEditPanel.setEnabled(false);
		
		gridBagConstraints.gridy++;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		rightSidePanel.add(createCurrentParametersPanel(), gridBagConstraints);
		
		return rightSidePanel;
	}
	
	private JPanel createCurrentParametersPanel() {
		JPanel currentParameters = new JPanel(new GridBagLayout());
		
		currentParameters.setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		JLabel parameterDefsLabel = new JLabel();
		parameterDefsLabel.setText("List of parameter definitions:");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy++;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		currentParameters.add(parameterDefsLabel, gridBagConstraints);

		JScrollPane listScroller = new JScrollPane(configuredParamTree);
		listScroller.setPreferredSize(new Dimension(250, 80));
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy++;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		currentParameters.add(listScroller, gridBagConstraints);
		
		return currentParameters;
	}
	
	
	private JPanel createRunEditPanel() {
		runEditPanel = new JPanel(new GridBagLayout());
		
		runsLabel.setText("Runs:");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		runEditPanel.add(runsLabel, gridBagConstraints);

		runsTextField = new JTextField("1");
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		runEditPanel.add(runsTextField, gridBagConstraints);
		
		gridBagConstraints.gridy++;
		gridBagConstraints.gridx = 0;
		runEditPanel.add(new JLabel(
				"Note: you can only add a \"run block\" under a parameter"),
				gridBagConstraints);
		
		addParamBlockButton.setToolTipText("Add a runs block nested under the selected paramter");
		addParamBlockButton.setText("Add/Nest");
		addParamBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBlockActionPerformed(e);
			}
		});
		gridBagConstraints.gridy++;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		runEditPanel.add(addParamBlockButton, gridBagConstraints);
		
		removeParamBlockButton.setToolTipText("Remove the selected paramater/run");
		removeParamBlockButton.setText("Remove");
		removeParamBlockButton.setEnabled(false);
		removeParamBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeActionPerformed(e);
			}
		});
		
		gridBagConstraints.gridx++;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		runEditPanel.add(removeParamBlockButton, gridBagConstraints);
		
		runEditPanel.setBorder(BorderFactory.createTitledBorder("Runs"));
		
		return runEditPanel;
	}
	
	private JPanel createParamOperationsPanel() {
		// the panel for add/nest/remove/... buttons
		JPanel paramOperationPanel = new JPanel();
		addParamButton.setToolTipText("Add the parameter at the level of the selected parameter/run");
		addParamButton.setText("Add");
		addParamButton.setEnabled(false);
		paramOperationPanel.add(addParamButton, BorderLayout.WEST);

		addNestParamButton.setToolTipText("Add the parameter nested under the selected parameter/run");
		addNestParamButton.setText("Add Nested");
		addNestParamButton.setEnabled(false);
		paramOperationPanel.add(addNestParamButton, BorderLayout.CENTER);

		removeParamButton.setToolTipText("Remove the selected paramater/run");
		removeParamButton.setText("Remove");
		removeParamButton.setEnabled(false);
		paramOperationPanel.add(removeParamButton, BorderLayout.EAST);

		addParamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addParmActionPerformed(e);
			}
		});
		addNestParamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNestParamActionPerformed(e);
			}
		});
		removeParamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeActionPerformed(e);
			}
		});
		
		return paramOperationPanel;
	}
	
	private void configureInputParametersPanel() {
		refreshTree();

		inputParametersPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		// the available parameters side panel
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		inputParametersPanel.add(createAvailableParamsPanel(), gridBagConstraints);
		
		// the panel that contains the parameter editing stuff
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		inputParametersPanel.add(createRightSidePanel(), gridBagConstraints);
		
		inputParametersPanel.setBorder(BorderFactory
				.createTitledBorder(" Specify Input Parameters Values "));
	}
	
	private JPanel createAvailableParamsPanel() {
		JPanel availableParamsPanel = new JPanel(new GridBagLayout());

		availableParamsPanel.setBorder(BorderFactory.createTitledBorder("Available Parameters"));
		
		inputParmListModel = new ArrayListListModel(new ArrayList());
		inputParameterList = new JList(inputParmListModel);
		
//		JLabel availableParamsLabel = new JLabel();
//		availableParamsLabel.setText("Available parameters:");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy = 0;
//		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
//		gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
//		availableParamsPanel.add(availableParamsLabel, gridBagConstraints);

		inputParameterList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inputParameterList
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						inputParameterListChanged(evt);
					}
				});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		JScrollPane listScroller = new JScrollPane(inputParameterList);
		availableParamsPanel.add(listScroller, gridBagConstraints);
		
		return availableParamsPanel;
	}

	private Object parseValue(String stringValue, String className)
			throws DataTypeMismatchException {
		try {
			if (className.equalsIgnoreCase("java.lang.Integer")) {
				Integer anInteger = new Integer(stringValue);

				return anInteger;
			} else if (className.equalsIgnoreCase("java.lang.Long")) {
				Long aLong = new Long(stringValue);

				return aLong;
			} else if (className.equalsIgnoreCase("java.lang.String")) {
				return stringValue;
			} else if (className.equalsIgnoreCase("java.lang.Float")) {
				Float aFloat = new Float(stringValue);

				return aFloat;
			} else if (className.equalsIgnoreCase("java.lang.Byte")) {
				Byte aByte = new Byte(stringValue);

				return aByte;
			} else if (className.equalsIgnoreCase("java.lang.Double")) {
				Double aDouble = new Double(stringValue);

				return aDouble;
			} else if (className.equalsIgnoreCase("java.lang.Boolean")) {
				Boolean aBoolean = new Boolean(stringValue);

				return aBoolean;
			}
		} catch (NumberFormatException e) {
			throw new DataTypeMismatchException(stringValue
					+ " is not of type " + className);
		}

		return null;
	}

	private DataParameter createParameterNode()
			throws DataTypeMismatchException {
		String name = null;
		String dataType = null;
		boolean input = true;
		DataParameter aParameter = (DataParameter) inputParameterList
				.getSelectedValue();

		String currLabelName = "";

		try {

			if (aParameter != null) {
				name = aParameter.getName();
				dataType = aParameter.getDataType();
			}

			int runs;

			try {
				runs = Integer.parseInt(runsTextField.getText());
			} catch (NumberFormatException e) {
				runs = 0;
			}

			if (incrValueRadioButton.isSelected()) {
				currLabelName = "Start";
				Object start = parseValue(startTextField.getText(), dataType);
				currLabelName = "End";
				Object end = parseValue(endTextField.getText(), dataType);
				currLabelName = "Step";
				Object increment = parseValue(incrTextField.getText(), dataType);

				IncrementParameter aValueParameter = null;
				aValueParameter = new IncrementParameter(runs, name, dataType,
						input, start, end, increment);

				return aValueParameter;
			}

			if (constValueRadioButton.isSelected()) {
				currLabelName = "Start";
				Object value = parseValue(startTextField.getText(), dataType);

				ConstantParameter aConstParameter = null;
				aConstParameter = new ConstantParameter(runs, name, dataType,
						input, value);

				return aConstParameter;
			}

			if (listOfValuesRadioButton.isSelected()) {
				Object[] value = null;
				currLabelName = "List value";
				StringTokenizer st = new StringTokenizer(startTextField
						.getText().trim());

				if (st.countTokens() > 0) {
					value = new Object[st.countTokens()];

					int curTok = 0;

					while (st.hasMoreTokens()) {
						value[curTok] = parseValue(st.nextToken(), dataType);
						curTok++;
					}
				}

				ListParameter aListParameter = null;
				aListParameter = new ListParameter(runs, name, dataType, input,
						value);

				return aListParameter;
			}

			return null;
		} catch (DataTypeMismatchException ex) {
			throw new DataTypeMismatchException(currLabelName + " "
					+ ex.getMessage(), ex);
		}
	}
	
	private DataParameter createBlockNode()
			throws DataTypeMismatchException {

		int runs;
		try {
			runs = Integer.parseInt(runsTextField.getText());
		} catch (NumberFormatException e) {
			runs = 0;
		}
		
		return new ParamBlockParameter(runs);
	}

	// /** Exit the Application */
	// private void exitForm(java.awt.event.WindowEvent evt) {
	// aParameterWizard.actionPerformed(new ActionEvent(jLabel2,
	// ActionEvent.ACTION_PERFORMED,
	// ParameterWizard.EXIT));
	// }

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// setTitle("Parameter Wizard");
		dialogButtonPanel = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		tabPane = new JTabbedPane();
		inputParametersPanel = new JPanel();
		outputLocationPanel = new JPanel();

		// addWindowListener(new java.awt.event.WindowAdapter() {
		// public void windowClosing(java.awt.event.WindowEvent evt) {
		// exitForm(evt);
		// }
		// });
		// buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel,
		// javax.swing.BoxLayout.X_AXIS));
		okButton.setText("OK");
		dialogButtonPanel.add(okButton);

		cancelButton.setText("Cancel");
		dialogButtonPanel.add(cancelButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OKActionPerformed(e);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelActionPerformed(e);
			}
		});

		allParametersPanel = new JPanel();
		allParametersPanel.setLayout(new GridLayout(1, 1));
		aParameterDataObjectTableModel = new ParameterDataObjectTableModel(
				aParameterWizard, aParameterData);
		allParameterTable = new EnhancedJTable(aParameterDataObjectTableModel,
				90);

		String[] tmpLabels = new String[2];
		tmpLabels[0] = "Input";
		tmpLabels[1] = "Output";

		int[] tmpVals = new int[2];
		tmpVals[0] = 0;
		tmpVals[1] = 1;
		allParameterTable.setDefaultRenderer(Integer.class, new RadioBarPanel(
				aParameterWizard, tmpLabels, tmpVals));
		allParameterTable.setDefaultEditor(Integer.class, new RadioBarPanel(
				aParameterWizard, tmpLabels, tmpVals));

		allParametersPanel.add(new JScrollPane(allParameterTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		allParametersPanel.setBorder(BorderFactory
				.createTitledBorder(" Specify Parameters Function "));

		tabPane.addTab("All Parameters", allParametersPanel);
		configureInputParametersPanel();
		tabPane.addTab("Input Parameters", inputParametersPanel);

		configureOutputFilePanel();
		tabPane.addTab("Parameter File Location", outputLocationPanel);
		setLayout(new BorderLayout());
		add(tabPane, java.awt.BorderLayout.CENTER);
		add(dialogButtonPanel, java.awt.BorderLayout.SOUTH);

		// pack();
	}

	private void selectButtonAction(boolean isSave) { // GEN-FIRST:event_jButton2ActionPerformed

		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle("Choose file to store parameters");

		// In response to a button click:
		int returnVal;
		if (isSave)
			returnVal = fc.showSaveDialog(this);
		else
			returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String storeFile = cleanupFileName(fc.getSelectedFile().getPath());

			outputLocationLabel.setText(storeFile);
			aParameterWizard.actionPerformed(new ActionEvent(
					outputLocationLabel, ActionEvent.ACTION_PERFORMED,
					ParameterWizard.CHANGED_OUTPUT_LOCATION));
		}
	}

	private String cleanupFileName(String originalName) {
		if (!originalName.endsWith(".xml"))
			return originalName + ".xml";
		else
			return originalName;
	}

	/**
	 * Provides access to specified parameters data model
	 * 
	 * @return Returns the top.
	 */
	public DefaultMutableTreeNode getTreeTop() {
		return top;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		ParameterWizardPanel aParameterWizardFrame = new ParameterWizardPanel(
				null);
		JFrame aFrame = new JFrame();
		aFrame.getContentPane().add(aParameterWizardFrame);
		aFrame.pack();
		aFrame.setVisible(true);
	}
}
