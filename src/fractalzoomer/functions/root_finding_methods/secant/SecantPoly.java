/* 
 * Fractal Zoomer, Copyright (C) 2018 hrkalona2
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
package fractalzoomer.functions.root_finding_methods.secant;

import fractalzoomer.core.Complex;
import fractalzoomer.in_coloring_algorithms.InColorAlgorithm;
import fractalzoomer.main.MainWindow;
import fractalzoomer.main.app_settings.OrbitTrapSettings;
import fractalzoomer.out_coloring_algorithms.OutColorAlgorithm;
import java.util.ArrayList;

/**
 *
 * @author hrkalona
 */
public class SecantPoly extends SecantRootFindingMethod {

    private double[] coefficients;

    public SecantPoly(double xCenter, double yCenter, double size, int max_iterations, int out_coloring_algorithm, int user_out_coloring_algorithm, String outcoloring_formula, String[] user_outcoloring_conditions, String[] user_outcoloring_condition_formula, int in_coloring_algorithm, int user_in_coloring_algorithm, String incoloring_formula, String[] user_incoloring_conditions, String[] user_incoloring_condition_formula, boolean smoothing, int plane_type, double[] rotation_vals, double[] rotation_center, double[] coefficients, String user_plane, int user_plane_algorithm, String[] user_plane_conditions, String[] user_plane_condition_formula, double[] plane_transform_center, double plane_transform_angle, double plane_transform_radius, double[] plane_transform_scales, double[] plane_transform_wavelength, int waveType, double plane_transform_angle2, int plane_transform_sides, double plane_transform_amount, int converging_smooth_algorithm, OrbitTrapSettings ots) {

        super(xCenter, yCenter, size, max_iterations, plane_type, rotation_vals, rotation_center, user_plane, user_plane_algorithm, user_plane_conditions, user_plane_condition_formula, plane_transform_center, plane_transform_angle, plane_transform_radius, plane_transform_scales, plane_transform_wavelength, waveType, plane_transform_angle2, plane_transform_sides, plane_transform_amount, ots);

        this.coefficients = coefficients;

        switch (out_coloring_algorithm) {

            case MainWindow.USER_OUTCOLORING_ALGORITHM:
                convergent_bailout = 1E-7;
                break;

        }

        OutColoringAlgorithmFactory(out_coloring_algorithm, smoothing, converging_smooth_algorithm, user_out_coloring_algorithm, outcoloring_formula, user_outcoloring_conditions, user_outcoloring_condition_formula, plane_transform_center);

        InColoringAlgorithmFactory(in_coloring_algorithm, user_in_coloring_algorithm, incoloring_formula, user_incoloring_conditions, user_incoloring_condition_formula, plane_transform_center);

    }

    //orbit
    public SecantPoly(double xCenter, double yCenter, double size, int max_iterations, ArrayList<Complex> complex_orbit, int plane_type, double[] rotation_vals, double[] rotation_center, double[] coefficients, String user_plane, int user_plane_algorithm, String[] user_plane_conditions, String[] user_plane_condition_formula, double[] plane_transform_center, double plane_transform_angle, double plane_transform_radius, double[] plane_transform_scales, double[] plane_transform_wavelength, int waveType, double plane_transform_angle2, int plane_transform_sides, double plane_transform_amount) {

        super(xCenter, yCenter, size, max_iterations, complex_orbit, plane_type, rotation_vals, rotation_center, user_plane, user_plane_algorithm, user_plane_conditions, user_plane_condition_formula, plane_transform_center, plane_transform_angle, plane_transform_radius, plane_transform_scales, plane_transform_wavelength, waveType, plane_transform_angle2, plane_transform_sides, plane_transform_amount);

        this.coefficients = coefficients;

    }

    @Override
    protected void function(Complex[] complex) {

        Complex fz1 = complex[0].tenth().times_mutable(coefficients[0]).plus_mutable(complex[0].ninth().times_mutable(coefficients[1])).plus_mutable(complex[0].eighth().times_mutable(coefficients[2])).plus_mutable(complex[0].seventh().times_mutable(coefficients[3])).plus_mutable(complex[0].sixth().times_mutable(coefficients[4])).plus_mutable(complex[0].fifth().times_mutable(coefficients[5])).plus_mutable(complex[0].fourth().times_mutable(coefficients[6])).plus_mutable(complex[0].cube().times_mutable(coefficients[7])).plus_mutable(complex[0].square().times_mutable(coefficients[8])).plus_mutable(complex[0].times(coefficients[9])).plus_mutable(coefficients[10]);

        Complex temp = new Complex(complex[0]);
        complex[0].sub_mutable(fz1.times((complex[0].sub(complex[1])).divide_mutable(fz1.sub(complex[2]))));
        complex[1].assign(temp);
        complex[2].assign(fz1);

    }

    @Override
    public double calculateFractalWithoutPeriodicity(Complex pixel) {
        int iterations = 0;
        double temp = 0;

        if (trap != null) {
            trap.initialize();
        }

        Complex[] complex = new Complex[3];
        complex[0] = new Complex(pixel);//z
        complex[1] = new Complex();
        complex[2] = new Complex(coefficients[10], 0);

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        for (; iterations < max_iterations; iterations++) {

            if (trap != null) {
                trap.check(complex[0]);
            }

            if ((temp = complex[0].distance_squared(zold)) <= convergent_bailout) {
                Object[] object = {iterations, complex[0], temp, zold, zold2, pixel, start};
                return out_color_algorithm.getResult(object);
            }
            zold2.assign(zold);
            zold.assign(complex[0]);
            function(complex);

        }

        Object[] object = {complex[0], zold, zold2, pixel, start};
        return in_color_algorithm.getResult(object);

    }

    @Override
    public double[] calculateFractal3DWithoutPeriodicity(Complex pixel) {
        int iterations = 0;
        double temp = 0;

        if (trap != null) {
            trap.initialize();
        }

        Complex[] complex = new Complex[3];
        complex[0] = new Complex(pixel);//z
        complex[1] = new Complex(); //zold
        complex[2] = new Complex(coefficients[10], 0);

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        for (; iterations < max_iterations; iterations++) {

            if (trap != null) {
                trap.check(complex[0]);
            }

            if ((temp = complex[0].distance_squared(zold)) <= convergent_bailout) {
                Object[] object = {iterations, complex[0], temp, zold, zold2, pixel, start};
                double[] array = {OutColorAlgorithm.transformResultToHeight(out_color_algorithm.getResult3D(object), max_iterations), out_color_algorithm.getResult(object)};
                return array;
            }
            zold2.assign(zold);
            zold.assign(complex[0]);
            function(complex);

        }

        Object[] object = {complex[0], zold, zold2, pixel, start};
        double temp2 = in_color_algorithm.getResult(object);
        double[] array = {InColorAlgorithm.transformResultToHeight(temp2, max_iterations), temp2};
        return array;

    }

    @Override
    public void calculateFractalOrbit() {
        int iterations = 0;

        Complex[] complex = new Complex[3];
        complex[0] = new Complex(pixel_orbit);//z
        complex[1] = new Complex();
        complex[2] = new Complex(coefficients[10], 0);

        Complex temp = null;

        for (; iterations < max_iterations; iterations++) {
            function(complex);
            temp = rotation.rotateInverse(complex[0]);

            if (Double.isNaN(temp.getRe()) || Double.isNaN(temp.getIm()) || Double.isInfinite(temp.getRe()) || Double.isInfinite(temp.getIm())) {
                break;
            }

            complex_orbit.add(temp);
        }

    }

    @Override
    public Complex iterateFractalDomain(Complex pixel) {
        int iterations = 0;

        Complex[] complex = new Complex[3];
        complex[0] = new Complex(pixel);//z
        complex[1] = new Complex();
        complex[2] = new Complex(coefficients[10], 0);

        for (; iterations < max_iterations; iterations++) {

            function(complex);

        }

        return complex[0];

    }

}
