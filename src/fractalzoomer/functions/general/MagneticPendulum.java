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
package fractalzoomer.functions.general;

import fractalzoomer.core.Complex;
import fractalzoomer.fractal_options.iteration_statistics.CosArgDivideInverseNorm;
import fractalzoomer.fractal_options.iteration_statistics.UserStatisticColoringRootFindingMethod;
import fractalzoomer.functions.Fractal;
import fractalzoomer.in_coloring_algorithms.AtanReTimesImTimesAbsReTimesAbsIm;
import fractalzoomer.in_coloring_algorithms.CosMag;
import fractalzoomer.in_coloring_algorithms.DecompositionLike;
import fractalzoomer.in_coloring_algorithms.MagTimesCosReSquared;
import fractalzoomer.in_coloring_algorithms.MaximumIterations;
import fractalzoomer.in_coloring_algorithms.ReDivideIm;
import fractalzoomer.in_coloring_algorithms.SinReSquaredMinusImSquared;
import fractalzoomer.in_coloring_algorithms.Squares;
import fractalzoomer.in_coloring_algorithms.Squares2;
import fractalzoomer.in_coloring_algorithms.UserConditionalInColorAlgorithm;
import fractalzoomer.in_coloring_algorithms.UserInColorAlgorithm;
import fractalzoomer.in_coloring_algorithms.ZMag;
import fractalzoomer.main.MainWindow;
import fractalzoomer.main.app_settings.MagneticPendulumSettings;
import fractalzoomer.main.app_settings.OrbitTrapSettings;
import fractalzoomer.main.app_settings.StatisticsSettings;
import fractalzoomer.out_coloring_algorithms.BinaryDecomposition;
import fractalzoomer.out_coloring_algorithms.BinaryDecomposition2;
import fractalzoomer.out_coloring_algorithms.BinaryDecomposition2MagneticPendulum;
import fractalzoomer.out_coloring_algorithms.BinaryDecompositionMagneticPendulum;
import fractalzoomer.out_coloring_algorithms.ColorDecompositionMagneticPendulum;
import fractalzoomer.out_coloring_algorithms.ColorDecompositionRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.EscapeTime;
import fractalzoomer.out_coloring_algorithms.EscapeTimeAlgorithm1;
import fractalzoomer.out_coloring_algorithms.EscapeTimeColorDecompositionMagneticPendulum;
import fractalzoomer.out_coloring_algorithms.EscapeTimeColorDecompositionRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.EscapeTimeMagneticPendulum;
import fractalzoomer.out_coloring_algorithms.SmoothBinaryDecomposition2RootFindingMethod;
import fractalzoomer.out_coloring_algorithms.SmoothBinaryDecompositionRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.SmoothColorDecompositionRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.SmoothEscapeTimeColorDecompositionRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.SmoothEscapeTimeRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.UserConditionalOutColorAlgorithmRootFindingMethod;
import fractalzoomer.out_coloring_algorithms.UserOutColorAlgorithmRootFindingMethod;
import fractalzoomer.utils.ColorAlgorithm;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 *
 * @author hrkalona2
 */
public class MagneticPendulum extends Fractal {

    private double height_squared;
    private Complex[] magnets;
    private Complex[] strengths;
    private Complex gravity;
    private Complex friction;
    private Complex pendulum;
    private double stepsize;
    private double stepsize_squared;
    private Object[] iterationData;

    public MagneticPendulum(double xCenter, double yCenter, double size, int max_iterations, int out_coloring_algorithm, int user_out_coloring_algorithm, String outcoloring_formula, String[] user_outcoloring_conditions, String[] user_outcoloring_condition_formula, int in_coloring_algorithm, int user_in_coloring_algorithm, String incoloring_formula, String[] user_incoloring_conditions, String[] user_incoloring_condition_formula, boolean smoothing, int plane_type, double[] rotation_vals, double[] rotation_center, String user_plane, int user_plane_algorithm, String[] user_plane_conditions, String[] user_plane_condition_formula, double[] plane_transform_center, double plane_transform_angle, double plane_transform_radius, double[] plane_transform_scales, double[] plane_transform_wavelength, int waveType, double plane_transform_angle2, int plane_transform_sides, double plane_transform_amount, int converging_smooth_algorithm, OrbitTrapSettings ots, StatisticsSettings sts, MagneticPendulumSettings mps) {

        super(xCenter, yCenter, size, max_iterations, 0, 0, "", "", 0, 0, false, plane_type, rotation_vals, rotation_center, user_plane, user_plane_algorithm, user_plane_conditions, user_plane_condition_formula, plane_transform_center, plane_transform_angle, plane_transform_radius, plane_transform_scales, plane_transform_wavelength, waveType, plane_transform_angle2, plane_transform_sides, plane_transform_amount, ots);

        pendulum = new Complex(mps.pendulum[0], mps.pendulum[1]);

        height_squared = mps.height * mps.height;
        stepsize = mps.stepsize;
        stepsize_squared = stepsize * stepsize;

        Predicate<double[]> isEnabled = (double[] strength) -> strength[0] != 0 || strength[1] != 0;

        int count = 0;
        for (int i = 0; i < mps.magnetStrength.length; i++) {
            if (isEnabled.test(mps.magnetStrength[i])) {
                count++;
            }
        }

        magnets = new Complex[count];
        strengths = new Complex[count];

        count = 0;
        for (int i = 0; i < mps.magnetStrength.length; i++) {
            if (isEnabled.test(mps.magnetStrength[i])) {
                magnets[count] = new Complex(mps.magnetLocation[i][0], mps.magnetLocation[i][1]);
                strengths[count] = new Complex(mps.magnetStrength[i][0], mps.magnetStrength[i][1]);
                count++;
            }
        }

        gravity = new Complex(mps.gravity[0], mps.gravity[1]);
        friction = new Complex(mps.friction[0], mps.friction[1]);

        OutColoringAlgorithmFactory(out_coloring_algorithm, smoothing, converging_smooth_algorithm, user_out_coloring_algorithm, outcoloring_formula, user_outcoloring_conditions, user_outcoloring_condition_formula, plane_transform_center);

        InColoringAlgorithmFactory(in_coloring_algorithm, user_in_coloring_algorithm, incoloring_formula, user_incoloring_conditions, user_incoloring_condition_formula, plane_transform_center);

        if (sts.statistic) {
            StatisticFactory(sts, plane_transform_center);
        }
    }

    //orbit
    public MagneticPendulum(double xCenter, double yCenter, double size, int max_iterations, ArrayList<Complex> complex_orbit, int plane_type, double[] rotation_vals, double[] rotation_center, String user_plane, int user_plane_algorithm, String[] user_plane_conditions, String[] user_plane_condition_formula, double[] plane_transform_center, double plane_transform_angle, double plane_transform_radius, double[] plane_transform_scales, double[] plane_transform_wavelength, int waveType, double plane_transform_angle2, int plane_transform_sides, double plane_transform_amount, MagneticPendulumSettings mps) {

        super(xCenter, yCenter, size, max_iterations, complex_orbit, plane_type, rotation_vals, rotation_center, user_plane, user_plane_algorithm, user_plane_conditions, user_plane_condition_formula, plane_transform_center, plane_transform_angle, plane_transform_radius, plane_transform_scales, plane_transform_wavelength, waveType, plane_transform_angle2, plane_transform_sides, plane_transform_amount);

        pendulum = new Complex(mps.pendulum[0], mps.pendulum[1]);

        height_squared = mps.height * mps.height;
        stepsize = mps.stepsize;
        stepsize_squared = stepsize * stepsize;

        Predicate<double[]> isEnabled = (double[] strength) -> strength[0] != 0 || strength[1] != 0;

        int count = 0;
        for (int i = 0; i < mps.magnetStrength.length; i++) {
            if (isEnabled.test(mps.magnetStrength[i])) {
                count++;
            }
        }

        magnets = new Complex[count];
        strengths = new Complex[count];

        count = 0;
        for (int i = 0; i < mps.magnetStrength.length; i++) {
            if (isEnabled.test(mps.magnetStrength[i])) {
                magnets[count] = new Complex(mps.magnetLocation[i][0], mps.magnetLocation[i][1]);
                strengths[count] = new Complex(mps.magnetStrength[i][0], mps.magnetStrength[i][1]);
                count++;
            }
        }

        gravity = new Complex(mps.gravity[0], mps.gravity[1]);
        friction = new Complex(mps.friction[0], mps.friction[1]);
    }

    @Override
    public void function(Complex[] complex) {

        Complex acc_next = new Complex();
        for (int i = 0; i < magnets.length; i++) {
            Complex d = magnets[i].sub(complex[0]);
            double dist = Math.sqrt(d.norm_squared() + height_squared);
            acc_next.plus_mutable(d.times_mutable(strengths[i].divide(dist * dist * dist)));
        }

        acc_next.sub_mutable((complex[0].sub(pendulum)).times(gravity));
        acc_next.sub_mutable(complex[1].times(friction));

        complex[1].plus_mutable((acc_next.times(2).plus(complex[2].times(5).sub(complex[3]))).divide(6).times(stepsize));

        Complex dir = complex[1].times(stepsize).plus((acc_next.times(4).sub(complex[2])).divide(6).times(stepsize_squared));

        complex[4].plus_mutable(dir.norm());

        complex[0].plus_mutable(dir);
        complex[3].assign(complex[2]);

        complex[2].assign(acc_next);

    }

    @Override
    public double calculateFractalWithoutPeriodicity(Complex pixel) {
        int iterations = 0;

        if (trap != null) {
            trap.initialize();
        }

        Complex[] complex = new Complex[5];
        complex[0] = new Complex(pixel);//z
        complex[1] = new Complex();//vel
        complex[2] = new Complex(); //acc
        complex[3] = new Complex(); //acc_prev
        complex[4] = new Complex(); // len

        Complex zold = new Complex();
        Complex zold2 = new Complex();
        Complex start = new Complex(complex[0]);

        for (; iterations < max_iterations; iterations++) {

            if (trap != null) {
                trap.check(complex[0]);
            }

            zold2.assign(zold);
            zold.assign(complex[0]);
            function(complex);

            if (statistic != null) {
                statistic.insert(complex[0], zold, zold2, iterations, pixel, start);
            }

        }

        escaped = true;
        Object[] object = {iterations, complex[0], 0, zold, zold2, pixel, start, complex[4]};
        iterationData = object;
        double out = out_color_algorithm.getResult(object);

        out = getFinalValueOut(out);

        return out;

    }

    @Override
    public void calculateFractalOrbit() {
        int iterations = 0;

        Complex[] complex = new Complex[5];
        complex[0] = new Complex(pixel_orbit);//z
        complex[1] = new Complex();//vel
        complex[2] = new Complex(); //acc
        complex[3] = new Complex(); //acc_prev
        complex[4] = new Complex(); // len

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

        Complex[] complex = new Complex[5];
        complex[0] = new Complex(pixel);//z
        complex[1] = new Complex();//vel
        complex[2] = new Complex(); //acc
        complex[3] = new Complex(); //acc_prev
        complex[4] = new Complex(); // len

        for (; iterations < max_iterations; iterations++) {

            function(complex);

        }

        return complex[0];

    }

    @Override
    public double calculateJulia(Complex pixel) {
        return 0;
    }

    @Override
    public void calculateJuliaOrbit() {
    }

    @Override
    public Complex calculateJuliaDomain(Complex pixel) {

        return null;

    }

    @Override
    protected void OutColoringAlgorithmFactory(int out_coloring_algorithm, boolean smoothing, int converging_smooth_algorithm, int user_out_coloring_algorithm, String outcoloring_formula, String[] user_outcoloring_conditions, String[] user_outcoloring_condition_formula, double[] plane_transform_center) {

        switch (out_coloring_algorithm) {

            case MainWindow.ESCAPE_TIME:
                out_color_algorithm = new EscapeTimeMagneticPendulum();
                break;
            case MainWindow.BINARY_DECOMPOSITION:
                out_color_algorithm = new BinaryDecompositionMagneticPendulum();
                break;
            case MainWindow.BINARY_DECOMPOSITION2:
                out_color_algorithm = new BinaryDecomposition2MagneticPendulum();
                break;
            case MainWindow.COLOR_DECOMPOSITION:
                out_color_algorithm = new ColorDecompositionMagneticPendulum(magnets);
                break;
            case MainWindow.ESCAPE_TIME_COLOR_DECOMPOSITION:
                out_color_algorithm = new EscapeTimeColorDecompositionMagneticPendulum(magnets);
                break;
            case MainWindow.ESCAPE_TIME_ALGORITHM:
                out_color_algorithm = new EscapeTimeAlgorithm1(3);
                break;
            case MainWindow.USER_OUTCOLORING_ALGORITHM:
                if (user_out_coloring_algorithm == 0) {
                    out_color_algorithm = new UserOutColorAlgorithmRootFindingMethod(outcoloring_formula, 0, max_iterations, xCenter, yCenter, size, plane_transform_center, globalVars);
                } else {
                    out_color_algorithm = new UserConditionalOutColorAlgorithmRootFindingMethod(user_outcoloring_conditions, user_outcoloring_condition_formula, 0, max_iterations, xCenter, yCenter, size, plane_transform_center, globalVars);
                }
                break;

        }
    }

    @Override
    public double getFractal3DHeight(double value) {

        if (escaped) {
            double res = out_color_algorithm.getResult3D(iterationData);

            res = getFinalValueOut(res);

            return ColorAlgorithm.transformResultToHeight(res, max_iterations);
        }

        return ColorAlgorithm.transformResultToHeight(value, max_iterations);

    }

    @Override
    public double getJulia3DHeight(double value) {

        return 0;

    }

    @Override
    public int type() {

        return MainWindow.CONVERGING;

    }

    @Override
    protected void StatisticFactory(StatisticsSettings sts, double[] plane_transform_center) {

        statisticIncludeEscaped = sts.statisticIncludeEscaped;
        statisticIncludeNotEscaped = sts.statisticIncludeNotEscaped;
        
        if (sts.statisticGroup == 1) {
            statistic = new UserStatisticColoringRootFindingMethod(sts.statistic_intensity, sts.user_statistic_formula, xCenter, yCenter, max_iterations, size, 0, plane_transform_center, globalVars, sts.useAverage);
            return;
        }

        switch (sts.statistic_type) {

            case MainWindow.COS_ARG_DIVIDE_INVERSE_NORM:
                statistic = new CosArgDivideInverseNorm(sts.statistic_intensity, sts.cosArgInvStripeDensity, sts.StripeDenominatorFactor);
                break;

        }
    }
}