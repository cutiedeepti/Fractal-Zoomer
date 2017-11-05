/*
 * Fractal Zoomer, Copyright (C) 2017 hrkalona2
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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author hrkalona2
 */
public class ActiveFiltersPanel extends JPanel {
    private JCheckBox[] filters_opt;
    private int i;
    private boolean[] mActiveFilters;
    
    public ActiveFiltersPanel(JCheckBoxMenuItem[] filter_names, boolean[] activeFilters) {
        
        super();
        
        filters_opt = new JCheckBox[filter_names.length];
        
        mActiveFilters = activeFilters;
        
        for(i = 0; i < filters_opt.length; i++) {
            filters_opt[i] = new JCheckBox(filter_names[i].getText());
            filters_opt[i].setToolTipText(filter_names[i].getToolTipText());
            filters_opt[i].setSelected(activeFilters[i]);
            filters_opt[i].setBackground(MainWindow.bg_color);
            filters_opt[i].setFocusable(false);
            
            filters_opt[i].addActionListener(new ActionListener() {
                int k = i;
                @Override
                public void actionPerformed(ActionEvent e) {
                    mActiveFilters[k] = filters_opt[k].isSelected();
                }
                
            });
        }    
        
        JPanel detail_filters = new JPanel();
        JTabbedPane detail_filters_tab = new JTabbedPane();
        
        detail_filters_tab.addTab("Details", detail_filters);
        detail_filters_tab.setIconAt(0, getIcon("/fractalzoomer/icons/filter_details.png"));
        detail_filters_tab.setFocusable(false);
        detail_filters_tab.setBackground(MainWindow.bg_color);  
        
        detail_filters.setBackground(MainWindow.bg_color);
        detail_filters.setPreferredSize(new Dimension(1060, 85));
        detail_filters.setLayout(new GridLayout(1, 8));
    
        
        detail_filters.add(filters_opt[MainWindow.ANTIALIASING]);
        detail_filters.add(filters_opt[MainWindow.EDGE_DETECTION]);
        detail_filters.add(filters_opt[MainWindow.EDGE_DETECTION2]);
        detail_filters.add(filters_opt[MainWindow.SHARPNESS]);
        detail_filters.add(filters_opt[MainWindow.BLURRING]);
        detail_filters.add(filters_opt[MainWindow.EMBOSS]);
        detail_filters.add(filters_opt[MainWindow.GLOW]);
        detail_filters.add(filters_opt[MainWindow.NOISE]);
        
        JPanel color_filters = new JPanel();
        JTabbedPane color_filters_tab = new JTabbedPane();
        
        color_filters_tab.addTab("Colors", color_filters);
        color_filters_tab.setIconAt(0, getIcon("/fractalzoomer/icons/filter_colors.png"));
        color_filters_tab.setFocusable(false);
        color_filters_tab.setBackground(MainWindow.bg_color);  
        
        color_filters.setBackground(MainWindow.bg_color);
        color_filters.setLayout(new GridLayout(3, 6));
        color_filters.setPreferredSize(new Dimension(1060, 170));
        
        color_filters.add(filters_opt[MainWindow.HISTOGRAM_EQUALIZATION]);;
        color_filters.add(filters_opt[MainWindow.POSTERIZE]);
        color_filters.add(filters_opt[MainWindow.CONTRAST_BRIGHTNESS]);
        color_filters.add(filters_opt[MainWindow.GAIN]);
        color_filters.add(filters_opt[MainWindow.GAMMA]);
        color_filters.add(filters_opt[MainWindow.EXPOSURE]);
        color_filters.add(filters_opt[MainWindow.COLOR_TEMPERATURE]);
        color_filters.add(filters_opt[MainWindow.INVERT_COLORS]);
        color_filters.add(filters_opt[MainWindow.SOLARIZE]);
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_MASKING]);
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_SWAPPING]);
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_SWIZZLING]);
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_MIXING]);
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_ADJUSTING]);
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_HSB_ADJUSTING]); 
        color_filters.add(filters_opt[MainWindow.COLOR_CHANNEL_SCALING]);
        color_filters.add(filters_opt[MainWindow.GRAYSCALE]);
        color_filters.add(filters_opt[MainWindow.FADE_OUT]);
        
        JPanel texture_filters = new JPanel();
        JTabbedPane texture_filters_tab = new JTabbedPane();
        
        texture_filters_tab.addTab("Texture", texture_filters);
        texture_filters_tab.setIconAt(0, getIcon("/fractalzoomer/icons/filter_texture.png"));
        texture_filters_tab.setFocusable(false);
        texture_filters_tab.setBackground(MainWindow.bg_color);  
        
        texture_filters.setBackground(MainWindow.bg_color);    
        texture_filters.setPreferredSize(new Dimension(925, 85));
        texture_filters.setLayout(new GridLayout(1, 7));
        
        texture_filters.add(filters_opt[MainWindow.DITHER]);
        texture_filters.add(filters_opt[MainWindow.CRYSTALLIZE]);
        texture_filters.add(filters_opt[MainWindow.POINTILLIZE]);
        texture_filters.add(filters_opt[MainWindow.OIL]);
        texture_filters.add(filters_opt[MainWindow.MARBLE]);
        texture_filters.add(filters_opt[MainWindow.WEAVE]);
        texture_filters.add(filters_opt[MainWindow.SPARKLE]);
        
        JPanel lighting_filters = new JPanel();
        
        JTabbedPane lighting_filters_tab = new JTabbedPane();
        
        lighting_filters_tab.addTab("Lighting", lighting_filters);
        lighting_filters_tab.setIconAt(0, getIcon("/fractalzoomer/icons/filter_lighting.png"));
        lighting_filters_tab.setFocusable(false);
        lighting_filters_tab.setBackground(MainWindow.bg_color); 
        
        
        lighting_filters.setBackground(MainWindow.bg_color);      
        lighting_filters.setPreferredSize(new Dimension(125, 85));
        lighting_filters.setLayout(new GridLayout(1, 1));
        
        lighting_filters.add(filters_opt[MainWindow.LIGHT_EFFECTS]);
        
        add(detail_filters_tab);
        add(color_filters_tab);
        add(texture_filters_tab);
        add(lighting_filters_tab);
        
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()), "Filters", TitledBorder.DEFAULT_POSITION, TitledBorder.DEFAULT_POSITION));
    }
    
    private ImageIcon getIcon(String path) {

        return new ImageIcon(getClass().getResource(path));
        
    }
    
}