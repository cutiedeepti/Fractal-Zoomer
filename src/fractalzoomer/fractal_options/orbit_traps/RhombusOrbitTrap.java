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
package fractalzoomer.fractal_options.orbit_traps;

import fractalzoomer.core.Complex;

/**
 *
 * @author hrkalona2
 */
public class RhombusOrbitTrap extends OrbitTrap {

    public RhombusOrbitTrap(double pointRe, double pointIm, double trapLength, double trapWidth) {

        super(pointRe, pointIm, trapLength, trapWidth);

    }

    @Override
    public void check(Complex val, int iteration) {
        
        Complex diff = val.sub(point);
        
        double dist = Math.abs(diff.getAbsRe() + diff.getAbsIm() - trapLength);

        if(dist < trapWidth && dist < distance) {
            distance = dist;
            trapId = 0;
            setTrappedData(val, iteration);
        }

    }
    
    @Override
    public double getMaxValue() {
        return trapWidth;
    }
    
}
