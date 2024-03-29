/*
 * -----------------------------------------------------------------------
 * Project: SCJD
 * -----------------------------------------------------------------------
 * File: ComponentStatusBar.java
 * -----------------------------------------------------------------------
 * Author: Alex Belisle-Turcot
 * -----------------------------------------------------------------------
 * Prometric Id: SP9339741
 * -----------------------------------------------------------------------
 * Email: alex.belisleturcot+scjd@gmail.com
 * -----------------------------------------------------------------------
 * Date of creation: 2007-11-30
 * -----------------------------------------------------------------------
 */
package com.proserus.stocks.ui.view.transactions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventBus;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.model.Filter;
import com.proserus.stocks.ui.controller.ViewControllers;
import com.proserus.stocks.ui.view.common.SortedComboBoxModel;
import com.proserus.stocks.ui.view.general.ComboBoxModelEditor;
import com.proserus.stocks.ui.view.general.ComboBoxModelRenderer;
import com.proserus.stocks.ui.view.symbols.EmptySymbol;

public class TransactionSymbolFilterPanel extends JPanel implements EventListener, ActionListener {
	private static final long serialVersionUID = 201404042021L;
	private Filter filter = ViewControllers.getFilter();

	private static final String SYMBOL = "Symbol:";

	private SortedComboBoxModel model = new SortedComboBoxModel();

	public TransactionSymbolFilterPanel() {
		EventBus.getInstance().add(this, ModelChangeEvents.SYMBOLS_UPDATED);
		FlowLayout ff = new FlowLayout();
		ff.setAlignment(FlowLayout.LEFT);
		setLayout(ff);
		add(new JLabel(SYMBOL));
		JComboBox dropDown = new JComboBox();
		dropDown.setRenderer(new ComboBoxModelRenderer());
		dropDown.setEditor(new ComboBoxModelEditor());
		dropDown.setModel(model);
		dropDown.addActionListener(this);
		add(dropDown);
	}

	@Override
	public void update(Event event, Object mo) {
		if (ModelChangeEvents.SYMBOLS_UPDATED.equals(event)) {
			model.removeAllElements();
			for (Symbol symbol : ModelChangeEvents.SYMBOLS_UPDATED.resolveModel(mo)) {
				model.addElement(symbol);
			}
			Symbol s = new EmptySymbol();
			model.addElement(s);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			Object o = ((JComboBox) e.getSource()).getSelectedItem();
			if (o instanceof Symbol) {
				if (((Symbol) o).getId() != null) {
					filter.setSymbol((Symbol) o);
				} else {
					filter.setSymbol(null);
				}
				ViewControllers.getController().refreshFilteredData();
			}
		}
	}
}
