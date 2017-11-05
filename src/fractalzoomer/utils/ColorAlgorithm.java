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
package fractalzoomer.utils;

/**
 *
 * @author hrkalona2
 */
public class ColorAlgorithm {

    public static final int MAGIC_OFFSET_NUMBER = 100800;
    public static final int INCREMENT = 50;
    public static boolean OutNotUsingIncrement = true;
    public static boolean InNotUsingIncrement = true;

    public static double getResultWithoutIncrement(double result) {

        if(OutNotUsingIncrement && InNotUsingIncrement) {
            return result;
        }

        if(result <= -MAGIC_OFFSET_NUMBER - INCREMENT) {
            return result + INCREMENT;
        }

        return result;

    }
    
    public static double transformResultToColor(double result) {
        
        return result + MAGIC_OFFSET_NUMBER;
        
    }
}