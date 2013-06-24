package fractalzoomer.functions.general;


import fractalzoomer.in_coloring_algorithms.AtanReTimesImTimesAbsReTimesAbsIm;
import fractalzoomer.out_coloring_algorithms.BinaryDecomposition;
import fractalzoomer.out_coloring_algorithms.BinaryDecomposition2;
import fractalzoomer.out_coloring_algorithms.Biomorphs;
import fractalzoomer.out_coloring_algorithms.ColorDecomposition;
import fractalzoomer.core.Complex;
import fractalzoomer.fractal_options.DefaultInitialValue;
import fractalzoomer.in_coloring_algorithms.CosMag;
import fractalzoomer.in_coloring_algorithms.DecompositionLike;
import fractalzoomer.out_coloring_algorithms.EscapeTime;
import fractalzoomer.out_coloring_algorithms.EscapeTimeColorDecomposition;
import fractalzoomer.out_coloring_algorithms.EscapeTimeGaussianInteger;
import fractalzoomer.out_coloring_algorithms.EscapeTimePlusIm;
import fractalzoomer.out_coloring_algorithms.EscapeTimePlusRe;
import fractalzoomer.out_coloring_algorithms.EscapeTimePlusRePlusImPlusReDivideIm;
import fractalzoomer.fractal_options.DefaultPerturbation;
import fractalzoomer.fractal_options.InitialValue;
import fractalzoomer.in_coloring_algorithms.MagTimesCosReSquared;
import fractalzoomer.main.MainWindow;
import fractalzoomer.in_coloring_algorithms.MaximumIterations;
import fractalzoomer.fractal_options.Perturbation;
import fractalzoomer.functions.Julia;
import fractalzoomer.in_coloring_algorithms.ReDivideIm;
import fractalzoomer.in_coloring_algorithms.SinReSquaredMinusImSquared;
import fractalzoomer.in_coloring_algorithms.Squares;
import fractalzoomer.in_coloring_algorithms.ZMag;
import fractalzoomer.out_coloring_algorithms.Smooth;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hrkalona
 */
public class Phoenix extends Julia {

    public Phoenix(double xCenter, double yCenter, double size, int max_iterations, int bailout_test_algorithm, double bailout, int out_coloring_algorithm, int in_coloring_algorithm, boolean periodicity_checking, int plane_type, double[] rotation_vals, boolean perturbation, double[] perturbation_vals, boolean init_value, double[] initial_vals) {

        super(xCenter, yCenter, size, max_iterations, bailout_test_algorithm, bailout, periodicity_checking, plane_type, rotation_vals);

        if(perturbation) {
            pertur_val = new Perturbation(perturbation_vals[0], perturbation_vals[1]);
        }
        else {
            pertur_val = new DefaultPerturbation(perturbation_vals[0], perturbation_vals[1]);
        }
        
        if(init_value) {
            init_val = new InitialValue(initial_vals[0], initial_vals[1]);
        }
        else {
            init_val = new DefaultInitialValue(initial_vals[0], initial_vals[1]);
        }
        
        switch (out_coloring_algorithm) {

            case MainWindow.ESCAPE_TIME:
                out_color_algorithm = new EscapeTime();
                break;
            case MainWindow.SMOOTH_COLOR:
                out_color_algorithm = new Smooth(Math.log(bailout_squared));
                break;
            case MainWindow.BINARY_DECOMPOSITION:
                out_color_algorithm = new BinaryDecomposition();
                break;
            case MainWindow.BINARY_DECOMPOSITION2:
                out_color_algorithm = new BinaryDecomposition2();
                break;
            case MainWindow.ITERATIONS_PLUS_RE:
                out_color_algorithm = new EscapeTimePlusRe();
                break;
            case MainWindow.ITERATIONS_PLUS_IM:
                out_color_algorithm = new EscapeTimePlusIm();
                break;
            case MainWindow.ITERATIONS_PLUS_RE_PLUS_IM_PLUS_RE_DIVIDE_IM:
                out_color_algorithm = new EscapeTimePlusRePlusImPlusReDivideIm();
                break;
            case MainWindow.BIOMORPH:
                out_color_algorithm = new Biomorphs(bailout);
                break;
            case MainWindow.COLOR_DECOMPOSITION:
                out_color_algorithm = new ColorDecomposition();
                break;
            case MainWindow. ESCAPE_TIME_COLOR_DECOMPOSITION:
                out_color_algorithm = new EscapeTimeColorDecomposition();
                break;
            case MainWindow.ESCAPE_TIME_GAUSSIAN_INTEGER:
                out_color_algorithm = new EscapeTimeGaussianInteger();
                break;

        }
        
        switch (in_coloring_algorithm) {
            
            case MainWindow.MAXIMUM_ITERATIONS:
                in_color_algorithm = new MaximumIterations();
                break;
            case MainWindow.Z_MAG:
                in_color_algorithm = new ZMag(out_coloring_algorithm);
                break;
            case MainWindow.DECOMPOSITION_LIKE:
                in_color_algorithm = new DecompositionLike(out_coloring_algorithm);       
                break;
            case MainWindow.RE_DIVIDE_IM:
                in_color_algorithm = new ReDivideIm(out_coloring_algorithm);       
                break;
            case MainWindow.COS_MAG:
                in_color_algorithm = new CosMag(out_coloring_algorithm);       
                break;
            case MainWindow.MAG_TIMES_COS_RE_SQUARED:
                in_color_algorithm = new MagTimesCosReSquared(out_coloring_algorithm);       
                break;
            case MainWindow.SIN_RE_SQUARED_MINUS_IM_SQUARED:
                in_color_algorithm = new SinReSquaredMinusImSquared(out_coloring_algorithm);       
                break;
            case MainWindow.ATAN_RE_TIMES_IM_TIMES_ABS_RE_TIMES_ABS_IM:
                in_color_algorithm = new AtanReTimesImTimesAbsReTimesAbsIm(out_coloring_algorithm);       
                break;
            case MainWindow.SQUARES:
                in_color_algorithm = new Squares(out_coloring_algorithm);       
                break;
                
        }

    }

    public Phoenix(double xCenter, double yCenter, double size, int max_iterations, int bailout_test_algorithm, double bailout, int out_coloring_algorithm, int in_coloring_algorithm, boolean periodicity_checking, int plane_type, double[] rotation_vals, double xJuliaCenter, double yJuliaCenter) {

        super(xCenter, yCenter, size, max_iterations, bailout_test_algorithm, bailout, periodicity_checking, plane_type, rotation_vals, xJuliaCenter, yJuliaCenter);

        switch (out_coloring_algorithm) {

            case MainWindow.ESCAPE_TIME:
                out_color_algorithm = new EscapeTime();
                break;
            case MainWindow.SMOOTH_COLOR:
                out_color_algorithm = new Smooth(Math.log(bailout_squared));
                break;
            case MainWindow.BINARY_DECOMPOSITION:
                out_color_algorithm = new BinaryDecomposition();
                break;
            case MainWindow.BINARY_DECOMPOSITION2:
                out_color_algorithm = new BinaryDecomposition2();
                break;
            case MainWindow.ITERATIONS_PLUS_RE:
                out_color_algorithm = new EscapeTimePlusRe();
                break;
            case MainWindow.ITERATIONS_PLUS_IM:
                out_color_algorithm = new EscapeTimePlusIm();
                break;
            case MainWindow.ITERATIONS_PLUS_RE_PLUS_IM_PLUS_RE_DIVIDE_IM:
                out_color_algorithm = new EscapeTimePlusRePlusImPlusReDivideIm();
                break;
            case MainWindow.BIOMORPH:
                out_color_algorithm = new Biomorphs(bailout);
                break;
            case MainWindow.COLOR_DECOMPOSITION:
                out_color_algorithm = new ColorDecomposition();
                break;
            case MainWindow. ESCAPE_TIME_COLOR_DECOMPOSITION:
                out_color_algorithm = new EscapeTimeColorDecomposition();
                break;
            case MainWindow.ESCAPE_TIME_GAUSSIAN_INTEGER:
                out_color_algorithm = new EscapeTimeGaussianInteger();
                break;

        }
        
        switch (in_coloring_algorithm) {
            
            case MainWindow.MAXIMUM_ITERATIONS:
                in_color_algorithm = new MaximumIterations();
                break;
            case MainWindow.Z_MAG:
                in_color_algorithm = new ZMag(out_coloring_algorithm);
                break;
            case MainWindow.DECOMPOSITION_LIKE:
                in_color_algorithm = new DecompositionLike(out_coloring_algorithm);       
                break;
            case MainWindow.RE_DIVIDE_IM:
                in_color_algorithm = new ReDivideIm(out_coloring_algorithm);       
                break;
            case MainWindow.COS_MAG:
                in_color_algorithm = new CosMag(out_coloring_algorithm);       
                break;
            case MainWindow.MAG_TIMES_COS_RE_SQUARED:
                in_color_algorithm = new MagTimesCosReSquared(out_coloring_algorithm);       
                break;
            case MainWindow.SIN_RE_SQUARED_MINUS_IM_SQUARED:
                in_color_algorithm = new SinReSquaredMinusImSquared(out_coloring_algorithm);       
                break;
            case MainWindow.ATAN_RE_TIMES_IM_TIMES_ABS_RE_TIMES_ABS_IM:
                in_color_algorithm = new AtanReTimesImTimesAbsReTimesAbsIm(out_coloring_algorithm);       
                break;
            case MainWindow.SQUARES:
                in_color_algorithm = new Squares(out_coloring_algorithm);       
                break;
                
        }

    }

    //orbit
    public Phoenix(double xCenter, double yCenter, double size, int max_iterations, ArrayList<Complex> complex_orbit, int plane_type, double[] rotation_vals, boolean perturbation, double[] perturbation_vals, boolean init_value, double[] initial_vals) {

        super(xCenter, yCenter, size, max_iterations, complex_orbit, plane_type, rotation_vals);
        
        if(perturbation) {
            pertur_val = new Perturbation(perturbation_vals[0], perturbation_vals[1]);
        }
        else {
            pertur_val = new DefaultPerturbation(perturbation_vals[0], perturbation_vals[1]);
        }
        
        if(init_value) {
            init_val = new InitialValue(initial_vals[0], initial_vals[1]);
        }
        else {
            init_val = new DefaultInitialValue(initial_vals[0], initial_vals[1]);
        }

    }

    public Phoenix(double xCenter, double yCenter, double size, int max_iterations, ArrayList<Complex> complex_orbit, int plane_type, double[] rotation_vals, double xJuliaCenter, double yJuliaCenter) {

        super(xCenter, yCenter, size, max_iterations, complex_orbit, plane_type, rotation_vals, xJuliaCenter, yJuliaCenter);

    }

    @Override
    protected void function(Complex[] complex) {


        double temp = complex[1].getIm();
        Complex tempz = complex[0].square().plus(new Complex(temp * complex[2].getRe() + complex[1].getRe(), temp * complex[2].getIm()));
        complex[2] = complex[0];
        complex[0] = tempz;
     
    }

    @Override
    public double calculateFractalWithPeriodicity(Complex pixel) {
      int iterations = 0;

        check = 3;
        check_counter = 0;

        update = 10;
        update_counter = 0;

        period = new Complex();
        
        Complex temp_z = pertur_val.getPixel(init_val.getPixel(pixel));
        
        Complex[] complex = new Complex[3];
        complex[0] = temp_z;//z
        complex[1] = pixel;//c
        complex[2] = new Complex();//s
        
        Complex zold = new Complex();

        for (; iterations < max_iterations; iterations++) {
            if(bailout_algorithm.escaped(complex[0])) {
                Object[] object = {iterations, complex[0], zold};
                return out_color_algorithm.getResult(object);
            }
            zold = complex[0];
            function(complex);

            if(periodicityCheck(complex[0])) {
               return max_iterations;
            }

        }

        return max_iterations;
    }

    @Override
    public double calculateFractalWithoutPeriodicity(Complex pixel) {
      int iterations = 0;

        Complex temp_z = pertur_val.getPixel(init_val.getPixel(pixel));
        
        Complex[] complex = new Complex[3];
        complex[0] = temp_z;//z
        complex[1] = pixel;//c
        complex[2] = new Complex();//s
        
        Complex zold = new Complex();

        for (; iterations < max_iterations; iterations++) {
            if(bailout_algorithm.escaped(complex[0])) {
                Object[] object = {iterations, complex[0], zold};
                return out_color_algorithm.getResult(object);
            }
            zold = complex[0];
            function(complex);

        }

        Object[] object = {max_iterations, complex[0]};
        return in_color_algorithm.getResult(object);
        
    }

    @Override
    public double calculateJuliaWithPeriodicity(Complex pixel) {
      int iterations = 0;

        check = 3;
        check_counter = 0;

        update = 10;
        update_counter = 0;

        period = new Complex();

        Complex[] complex = new Complex[3];
        complex[0] = pixel;//z
        complex[1] = seed;//c
        complex[2] = new Complex();//s
        
        Complex zold = new Complex();
 
        for (; iterations < max_iterations; iterations++) {
            if(bailout_algorithm.escaped(complex[0])) {
                Object[] object = {iterations, complex[0], zold};
                return out_color_algorithm.getResult(object);
            }
            zold = complex[0];
            function(complex);

            if(periodicityCheck(complex[0])) {
               return max_iterations;
            }
            
        }

        return max_iterations;
    }

    @Override
    public double calculateJuliaWithoutPeriodicity(Complex pixel) {
      int iterations = 0;

        Complex[] complex = new Complex[3];
        complex[0] = pixel;//z
        complex[1] = seed;//c
        complex[2] = new Complex();//s
        
        Complex zold = new Complex();

        for (; iterations < max_iterations; iterations++) {
            if(bailout_algorithm.escaped(complex[0])) {
                Object[] object = {iterations, complex[0], zold};
                return out_color_algorithm.getResult(object);
            }
            zold = complex[0];
            function(complex);
   
        }

        Object[] object = {max_iterations, complex[0]};
        return in_color_algorithm.getResult(object);
        
    }

    @Override
    public void calculateFractalOrbit() {
      int iterations = 0;

        Complex[] complex = new Complex[3];
        complex[0] = pertur_val.getPixel(init_val.getPixel(pixel_orbit));//z
        complex[1] = pixel_orbit;//c
        complex[2] = new Complex();//s

        for(; iterations < max_iterations; iterations++) {
           function(complex);
           complex_orbit.add(rotation.getPixel(complex[0], true));
           //if(z.getRe() >= (xCenter + size) / 2 || z.getRe() <= (xCenter - size) / 2 || z.getIm() >= (yCenter + size) / 2 || z.getIm() <= (yCenter - size) / 2) {
               //return;  //keep only the visible ones
           //}
        }

    }

    @Override
    public void calculateJuliaOrbit() {
      int iterations = 0;

        Complex[] complex = new Complex[3];
        complex[0] = pixel_orbit;//z
        complex[1] = seed;//c
        complex[2] = new Complex();//s

        for(; iterations < max_iterations; iterations++) {
           function(complex);
           complex_orbit.add(rotation.getPixel(complex[0], true));
           //if(z.getRe() >= (xCenter + size) / 2 || z.getRe() <= (xCenter - size) / 2 || z.getIm() >= (yCenter + size) / 2 || z.getIm() <= (yCenter - size) / 2) {
               //return; //keep only the visible ones
           //}
        }

    }

}