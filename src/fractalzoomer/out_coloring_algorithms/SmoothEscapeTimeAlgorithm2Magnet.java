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

import fractalzoomer.core.Complex;

/**
 *
 * @author hrkalona2
 */
public class SmoothEscapeTimeAlgorithm2Magnet extends OutColorAlgorithm {

    protected double log_convergent_bailout;
    protected int algorithm;

    public SmoothEscapeTimeAlgorithm2Magnet(double log_convergent_bailout, int algorithm) {

        super();
        this.log_convergent_bailout = log_convergent_bailout;
        this.algorithm = algorithm;

    }

    @Override
    public double getResult(Object[] object) {


        double temp3 = 0;
        if(!(Boolean)object[2]) {
            if(algorithm == 0) {
                double temp = Math.log(((Complex)object[4]).distance_squared(1));
                temp3 = (log_convergent_bailout - temp) / (Math.log((Double)object[3]) - temp);
            }
            else {
                double temp4 = Math.log(((Double)object[3]));

                double power = temp4 / Math.log(((Complex)object[4]).distance_squared(1));

                double f = Math.log(log_convergent_bailout / temp4) / Math.log(power);

                temp3 = f;
            }
        }

        Complex temp = ((Complex)object[1]).sub(((Complex)object[1]).sin());

        return (Integer)object[0] + Math.abs(Math.atan(temp.getIm() / temp.getRe())) * 8 + temp3 + 100800;

    }

    @Override
    public double getResult3D(Object[] object) {

        return getResult(object);

    }
}
