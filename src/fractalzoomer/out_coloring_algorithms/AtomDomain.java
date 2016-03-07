/* 
 * Fractal Zoomer, Copyright (C) 2015 hrkalona2
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

package fractalzoomer.out_coloring_algorithms;

public class AtomDomain extends OutColorAlgorithm {

    private double factor;

    public AtomDomain() {

        super();
        factor = 10 * Math.PI;
    }

    @Override
    public double getResult(Object[] object) {

        return (Integer)object[0] * factor + 100800;

    }

    @Override
    public double getResult3D(Object[] object) {

        return getResult(object);

    }
}