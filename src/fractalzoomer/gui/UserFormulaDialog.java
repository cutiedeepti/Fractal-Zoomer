/*
 * Copyright (C) 2019 hrkalona2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fractalzoomer.gui;

import fractalzoomer.main.MainWindow;
import fractalzoomer.main.app_settings.Settings;
import fractalzoomer.parser.ParserException;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

/**
 *
 * @author hrkalona2
 */
public class UserFormulaDialog extends JDialog {

    private MainWindow ptra;
    private JOptionPane optionPane;

    public UserFormulaDialog(MainWindow ptr, Settings s, int oldSelected, JRadioButtonMenuItem[] fractal_functions, boolean wasMagnetType, boolean wasConvergingType, boolean wasSimpleType, boolean wasMagneticPendulumType) {

        super();
        
        ptra = ptr;

        setTitle("User Formula");
        setModal(true);
        setIconImage(getIcon("/fractalzoomer/icons/mandel2.png").getImage());

        JTextField field_formula = new JTextField(50);
        field_formula.setText(s.fns.user_formula);

        JTextField field_formula2 = new JTextField(50);
        field_formula2.setText(s.fns.user_formula2);

        JPanel formula_panel = new JPanel();

        formula_panel.add(new JLabel("z ="));
        formula_panel.add(field_formula);

        JPanel formula_panel2 = new JPanel();

        formula_panel2.add(new JLabel("c ="));
        formula_panel2.add(field_formula2);

        JLabel bail = new JLabel("Bailout Technique:");
        bail.setFont(new Font("Arial", Font.BOLD, 11));

        String[] method4 = {"Escaping Algorithm", "Converging Algorithm"};

        JComboBox method4_choice = new JComboBox(method4);
        method4_choice.setSelectedIndex(s.fns.bail_technique);
        method4_choice.setToolTipText("Selects the bailout technique.");
        method4_choice.setFocusable(false);

        Object[] labels3 = ptr.createUserFormulaLabels("z, c, s, p, pp, n, maxn, center, size, sizei, v1 - v30, point");

        Object[] message3 = {
            labels3,
            bail,
            method4_choice,
            " ",
            "Insert your formula, that will be evaluated in every iteration.",
            formula_panel,
            formula_panel2,};

        optionPane = new JOptionPane(message3, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
            }
        });

        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {

                    Object value = optionPane.getValue();

                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    //Reset the JOptionPane's value.
                    //If you don't do this, then if the user
                    //presses the same button next time, no
                    //property change event will be fired.
                    optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

                    if ((Integer) value == JOptionPane.CANCEL_OPTION || (Integer) value == JOptionPane.NO_OPTION || (Integer) value == JOptionPane.CLOSED_OPTION) {
                        fractal_functions[oldSelected].setSelected(true);
                        s.fns.function = oldSelected;
                        dispose();
                        return;
                    }

                    try {
                        s.parser.parse(field_formula.getText());
                        if (s.parser.foundBail() || s.parser.foundCbail() || s.parser.foundR()) {
                            JOptionPane.showMessageDialog(ptra, "The variables: bail, cbail, r cannot be used in the z formula.", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        boolean temp_bool = s.parser.foundC();

                        s.parser.parse(field_formula2.getText());

                        if (s.parser.foundBail() || s.parser.foundCbail() || s.parser.foundR()) {
                            JOptionPane.showMessageDialog(ptra, "The variables: bail, cbail, r cannot be used in the c formula.", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        s.fns.user_formula = field_formula.getText();
                        s.fns.user_formula2 = field_formula2.getText();
                        s.userFormulaHasC = temp_bool;
                        s.fns.bail_technique = method4_choice.getSelectedIndex();

                        ptra.setUserFormulaOptions(true);          
                    } catch (ParserException ex) {
                        JOptionPane.showMessageDialog(ptra, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    ptra.optionsEnableShortcut();
                    dispose();
                    ptra.setFunctionPost(wasMagnetType, wasConvergingType, wasSimpleType, wasMagneticPendulumType);
                }
            }
        });

        //Make this dialog display it.
        setContentPane(optionPane);

        pack();

        setResizable(false);
        setLocation((int) (ptra.getLocation().getX() + ptra.getSize().getWidth() / 2) - (getWidth() / 2), (int) (ptra.getLocation().getY() + ptra.getSize().getHeight() / 2) - (getHeight() / 2));
        setVisible(true);

    }

    private ImageIcon getIcon(String path) {

        return new ImageIcon(getClass().getResource(path));

    }

}
