package fractalzoomer.out_coloring_algorithms;

import fractalzoomer.core.Complex;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hrkalona2
 */
public class EscapeTimeGaussianInteger extends OutColorAlgorithm {
    
    public EscapeTimeGaussianInteger() {
        
        super();
        
    }
     
    @Override
    public double getResult(Object[] object) {
        
        return (int)((Integer)object[0] + ((Complex)(object[1])).sub(((Complex)(object[1])).gaussian_integer()).norm_squared() * 95 + 100800);
 
    }   
    
}