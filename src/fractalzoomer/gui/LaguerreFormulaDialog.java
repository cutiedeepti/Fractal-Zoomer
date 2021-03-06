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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

/**
 *
 * @author hrkalona2
 */
public class LaguerreFormulaDialog extends JDialog {

    private MainWindow ptra;
    private JOptionPane optionPane;

    public LaguerreFormulaDialog(MainWindow ptr, Settings s, int oldSelected, JRadioButtonMenuItem[] fractal_functions, boolean wasMagnetType, boolean wasConvergingType, boolean wasSimpleType, boolean wasMagneticPendulumType) {

        super();
        
        ptra = ptr;

        setTitle("Laguerre Formula");
        setModal(true);
        setIconImage(getIcon("/fractalzoomer/icons/mandel2.png").getImage());

        String f1 = s.fns.user_fz_formula;
        String f2 = s.fns.user_dfz_formula;
        String f3 = s.fns.user_ddfz_formula;

        if (s.fns.function != oldSelected) {
            f1 = "z^3 - 1";
            f2 = "3*z^2";
            f3 = "6*z";
        }

        JTextField field_fz_formula9 = new JTextField(50);
        field_fz_formula9.setText(f1);

        JTextField field_dfz_formula9 = new JTextField(50);
        field_dfz_formula9.setText(f2);

        JTextField field_ddfz_formula9 = new JTextField(50);
        field_ddfz_formula9.setText(f3);

        JPanel formula_fz_panel9 = new JPanel();

        formula_fz_panel9.add(new JLabel("f(z) ="));
        formula_fz_panel9.add(field_fz_formula9);

        JPanel formula_dfz_panel9 = new JPanel();

        formula_dfz_panel9.add(new JLabel("f '(z) ="));
        formula_dfz_panel9.add(field_dfz_formula9);

        JPanel formula_ddfz_panel9 = new JPanel();

        formula_ddfz_panel9.add(new JLabel("f ''(z) ="));
        formula_ddfz_panel9.add(field_ddfz_formula9);

        JLabel imagelabel91 = new JLabel();
        imagelabel91.setIcon(getIcon("/fractalzoomer/icons/laguerre.png"));
        JPanel imagepanel91 = new JPanel();
        imagepanel91.add(imagelabel91);

        JPanel degree_panel = new JPanel();

        JTextField field_real8 = new JTextField(20);
        field_real8.setText("" + s.fns.laguerre_deg[0]);

        JTextField field_imaginary8 = new JTextField(20);
        field_imaginary8.setText("" + s.fns.laguerre_deg[1]);

        degree_panel.add(new JLabel("Degree = "));
        degree_panel.add(new JLabel("Real:"));
        degree_panel.add(field_real8);
        degree_panel.add(new JLabel(" Imaginary:"));
        degree_panel.add(field_imaginary8);

        Object[] labels91 = ptra.createUserFormulaLabels("z, s, p, pp, n, maxn, center, size, sizei, v1 - v30, point");

        Object[] message91 = {
            labels91,
            " ",
            imagepanel91,
            " ",
            "Insert the function, its derivatives and the degree.",
            formula_fz_panel9,
            formula_dfz_panel9,
            formula_ddfz_panel9,
            degree_panel,};

        optionPane = new JOptionPane(message91, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);

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
                        double temp5 = Double.parseDouble(field_real8.getText());
                        double temp6 = Double.parseDouble(field_imaginary8.getText());

                        s.parser.parse(field_fz_formula9.getText());
                        if (s.parser.foundBail() || s.parser.foundCbail() || s.parser.foundC() || s.parser.foundR()) {
                            JOptionPane.showMessageDialog(ptra, "The variables: c, bail, cbail, r cannot be used in the f(z) formula.", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        s.parser.parse(field_dfz_formula9.getText());

                        if (s.parser.foundBail() || s.parser.foundCbail() || s.parser.foundC() || s.parser.foundR()) {
                            JOptionPane.showMessageDialog(ptra, "The variables: c, bail, cbail, r cannot be used in the f '(z) formula.", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        s.parser.parse(field_ddfz_formula9.getText());

                        if (s.parser.foundBail() || s.parser.foundCbail() || s.parser.foundC() || s.parser.foundR()) {
                            JOptionPane.showMessageDialog(ptra, "The variables: c, bail, cbail, r cannot be used in the f ''(z) formula.", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        s.fns.user_fz_formula = field_fz_formula9.getText();
                        s.fns.user_dfz_formula = field_dfz_formula9.getText();
                        s.fns.user_ddfz_formula = field_ddfz_formula9.getText();
                        s.fns.laguerre_deg[0] = temp5;
                        s.fns.laguerre_deg[1] = temp6;
                    } catch (ParserException ex) {
                        JOptionPane.showMessageDialog(ptra, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(ptra, "Illegal Argument!", "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    ptra.optionsEnableShortcut2();
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
