/*
 * Fractal Zoomer, Copyright (C) 2019 hrkalona2
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author kaloch
 */
public class HelpMenu extends JMenu {
	private static final long serialVersionUID = 4789486723955292724L;
	private MainWindow ptr;
    private JMenuItem help_contents;
    private JMenuItem about;
    private JMenuItem update;
    private JMenuItem useful_links;
    
    public HelpMenu(MainWindow ptr2, String name) {

        super(name);

        this.ptr = ptr2;
        
        help_contents = new JMenuItem("Help Contents", getIcon("/fractalzoomer/icons/help.png"));

        about = new JMenuItem("About", getIcon("/fractalzoomer/icons/about.png"));

        useful_links = new JMenuItem("Useful Links", getIcon("/fractalzoomer/icons/useful_links.png"));

        update = new JMenuItem("Software Update", getIcon("/fractalzoomer/icons/update.png"));
        
        help_contents.setToolTipText("Loads the help file.");
        update.setToolTipText("Checks for software update.");
        useful_links.setToolTipText("Provides some useful links about fractals.");
        
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        help_contents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0));
        update.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0));
        
        help_contents.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ptr.showCHMHelpFile();

            }
        });

        useful_links.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ptr.showUsefulLinks();

            }
        });

        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ptr.displayAboutInfo();

            }
        });

        update.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ptr.checkForUpdate(true);

            }
        });
        
        add(help_contents);
        addSeparator();
        add(useful_links);
        addSeparator();
        add(update);
        addSeparator();
        add(about);
        
    }
    
    private ImageIcon getIcon(String path) {

        return new ImageIcon(getClass().getResource(path));

    }
    
}
