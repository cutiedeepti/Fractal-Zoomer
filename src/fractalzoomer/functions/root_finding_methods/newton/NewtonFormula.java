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
package fractalzoomer.functions.root_finding_methods.newton;

import fractalzoomer.core.Complex;
import fractalzoomer.core.ThreadDraw;
import fractalzoomer.in_coloring_algorithms.InColorAlgorithm;
import fractalzoomer.main.MainWindow;
import fractalzoomer.main.app_settings.OrbitTrapSettings;
import fractalzoomer.out_coloring_algorithms.OutColorAlgorithm;
import fractalzoomer.parser.ExpressionNode;
import fractalzoomer.parser.Parser;
import java.util.ArrayList;

/**
 *
 * @author hrkalona2
 */
public class NewtonFormula extends NewtonRootFindingMethod {

    private ExpressionNode expr;
    private Parser parser;
    private ExpressionNode expr2;
    private Parser parser2;
    private int iterations;
    private Complex point;

    public NewtonFormula(double xCenter, double yCenter, double size, int max_iterations, int out_coloring_algorithm, int user_out_coloring_algorithm, String outcoloring_formula, String[] user_outcoloring_conditions, String[] user_outcoloring_condition_formula, int in_coloring_algorithm, int user_in_coloring_algorithm, String incoloring_formula, String[] user_incoloring_conditions, String[] user_incoloring_condition_formula, boolean smoothing, int plane_type, double[] rotation_vals, double[] rotation_center, String user_plane, int user_plane_algorithm, String[] user_plane_conditions, String[] user_plane_condition_formula, double[] plane_transform_center, double plane_transform_angle, double plane_transform_radius, double[] plane_transform_scales, double[] plane_transform_wavelength, int waveType, double plane_transform_angle2, int plane_transform_sides, double plane_transform_amount, int converging_smooth_algorithm, String user_fz_formula, String user_dfz_formula, OrbitTrapSettings ots) {

        super(xCenter, yCenter, size, max_iterations, plane_type, rotation_vals, rotation_center, user_plane, user_plane_algorithm, user_plane_conditions, user_plane_condition_formula, plane_transform_center, plane_transform_angle, plane_transform_radius, plane_transform_scales, plane_transform_wavelength, waveType, plane_transform_angle2, plane_transform_sides, plane_transform_amount, ots);

        switch (out_coloring_algorithm) {

            case MainWindow.BINARY_DECOMPOSITION:
                convergent_bailout = 1E-7;
                break;
            case MainWindow.BINARY_DECOMPOSITION2:
                convergent_bailout = 1E-7;
                break;
            case MainWindow.USER_OUTCOLORING_ALGORITHM:
                convergent_bailout = 1E-7;
                break;

        }

        OutColoringAlgorithmFactory(out_coloring_algorithm, smoothing, converging_smooth_algorithm, user_out_coloring_algorithm, outcoloring_formula, user_outcoloring_conditions, user_outcoloring_condition_formula, plane_transform_center);

        InColoringAlgorithmFactory(in_coloring_algorithm, user_in_coloring_algorithm, incoloring_formula, user_incoloring_conditions, user_incoloring_condition_formula, plane_transform_center);

        parser = new Parser();
        expr = parser.parse(user_fz_formula);

        parser2 = new Parser();
        expr2 = parser2.parse(user_dfz_formula);

        point = new Complex(plane_transform_center[0], plane_transform_center[1]);

    }

    //orbit
    public NewtonFormula(double xCenter, double yCenter, double size, int max_iterations, ArrayList<Complex> complex_orbit, int plane_type, double[] rotation_vals, double[] rotation_center, String user_plane, int user_plane_algorithm, String[] user_plane_conditions, String[] user_plane_condition_formula, double[] plane_transform_center, double plane_transform_angle, double plane_transform_radius, double[] plane_transform_scales, double[] plane_transform_wavelength, int waveType, double plane_transform_angle2, int plane_transform_sides, double plane_transform_amount, String user_fz_formula, String user_dfz_formula) {

        super(xCenter, yCenter, size, max_iterations, complex_orbit, plane_type, rotation_vals, rotation_center, user_plane, user_plane_algorithm, user_plane_conditions, user_plane_condition_formula, plane_transform_center, plane_transform_angle, plane_transform_radius, plane_transform_scales, plane_transform_wavelength, waveType, plane_transform_angle2, plane_transform_sides, plane_transform_amount);

        parser = new Parser();
        expr = parser.parse(user_fz_formula);

        parser2 = new Parser();
        expr2 = parser2.parse(user_dfz_formula);

        point = new Complex(plane_transform_center[0], plane_transform_center[1]);

    }

    @Override
    protected void function(Complex[] complex) {

        if (parser.foundZ()) {
            parser.setZvalue(complex[0]);
        }

        if (parser.foundN()) {
            parser.setNvalue(new Complex(iterations, 0));
        }

        for (int i = 0; i < Parser.EXTRA_VARS; i++) {
            if (parser.foundVar(i)) {
                parser.setVarsvalue(i, globalVars[i]);
            }
        }

        Complex fz = expr.getValue();

        if (parser2.foundZ()) {
            parser2.setZvalue(complex[0]);
        }

        if (parser2.foundN()) {
            parser2.setNvalue(new Complex(iterations, 0));
        }

        for (int i = 0; i < Parser.EXTRA_VARS; i++) {
            if (parser2.foundVar(i)) {
                parser2.setVarsvalue(i, globalVars[i]);
            }
        }

        Complex dfz = expr2.getValue();

        newtonMethod(complex[0], fz, dfz);
    }

    @Override
    public double calculateFractalWithoutPeriodicity(Complex pixel) {
        iterations = 0;
        double temp = 0;

        if (trap != null) {
            trap.initialize();
        }

        Complex[] complex = new Complex[1];
        complex[0] = new Complex(pixel);//z

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        setInitVariables(start, zold, zold2);

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

            setVariables(zold, zold2);

        }

        Object[] object = {complex[0], zold, zold2, pixel, start};
        return in_color_algorithm.getResult(object);

    }

    @Override
    public double[] calculateFractal3DWithoutPeriodicity(Complex pixel) {
        iterations = 0;
        double temp = 0;

        if (trap != null) {
            trap.initialize();
        }

        Complex[] complex = new Complex[1];
        complex[0] = new Complex(pixel);//z

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        setInitVariables(start, zold, zold2);

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

            setVariables(zold, zold2);

        }

        Object[] object = {complex[0], zold, zold2, pixel, start};
        double temp2 = in_color_algorithm.getResult(object);
        double[] array = {InColorAlgorithm.transformResultToHeight(temp2, max_iterations), temp2};
        return array;

    }

    @Override
    public void calculateFractalOrbit() {
        iterations = 0;

        Complex[] complex = new Complex[1];
        complex[0] = new Complex(pixel_orbit);//z

        Complex temp = null;

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        setInitVariables(start, zold, zold2);

        for (; iterations < max_iterations; iterations++) {
            zold2.assign(zold);
            zold.assign(complex[0]);
            function(complex);

            setVariables(zold, zold2);

            temp = rotation.rotateInverse(complex[0]);

            if (Double.isNaN(temp.getRe()) || Double.isNaN(temp.getIm()) || Double.isInfinite(temp.getRe()) || Double.isInfinite(temp.getIm())) {
                break;
            }

            complex_orbit.add(temp);
        }

    }

    @Override
    public Complex iterateFractalDomain(Complex pixel) {
        iterations = 0;

        Complex[] complex = new Complex[1];
        complex[0] = new Complex(pixel);//z

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        setInitVariables(start, zold, zold2);

        for (; iterations < max_iterations; iterations++) {

            zold2.assign(zold);
            zold.assign(complex[0]);
            function(complex);

            setVariables(zold, zold2);

        }

        return complex[0];

    }

    private void setVariables(Complex zold, Complex zold2) {

        if (parser.foundP()) {
            parser.setPvalue(zold);
        }

        if (parser2.foundP()) {
            parser2.setPvalue(zold);
        }

        if (parser.foundPP()) {
            parser.setPPvalue(zold2);
        }

        if (parser2.foundPP()) {
            parser2.setPPvalue(zold2);
        }

    }

    private void setInitVariables(Complex start, Complex zold, Complex zold2) {

        if (parser.foundS()) {
            parser.setSvalue(start);
        }

        if (parser2.foundS()) {
            parser2.setSvalue(start);
        }

        if (parser.foundMaxn()) {
            parser.setMaxnvalue(new Complex(max_iterations, 0));
        }

        if (parser2.foundMaxn()) {
            parser2.setMaxnvalue(new Complex(max_iterations, 0));
        }

        if (parser.foundP()) {
            parser.setPvalue(zold);
        }

        if (parser2.foundP()) {
            parser2.setPvalue(zold);
        }

        if (parser.foundPP()) {
            parser.setPPvalue(zold2);
        }

        if (parser2.foundPP()) {
            parser2.setPPvalue(zold2);
        }

        Complex c_center = new Complex(xCenter, yCenter);

        if (parser.foundCenter()) {
            parser.setCentervalue(c_center);
        }

        if (parser2.foundCenter()) {
            parser2.setCentervalue(c_center);
        }

        Complex c_size = new Complex(size, 0);

        if (parser.foundSize()) {
            parser.setSizevalue(c_size);
        }

        if (parser2.foundSize()) {
            parser2.setSizevalue(c_size);
        }

        Complex c_isize = new Complex(ThreadDraw.IMAGE_SIZE, 0);
        if (parser.foundISize()) {
            parser.setISizevalue(c_isize);
        }

        if (parser2.foundISize()) {
            parser2.setISizevalue(c_isize);
        }

        if (parser.foundPoint()) {
            parser.setPointvalue(point);
        }

        if (parser2.foundPoint()) {
            parser2.setPointvalue(point);
        }

    }

}
