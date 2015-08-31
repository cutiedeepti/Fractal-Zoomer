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

package fractalzoomer.planes.user_plane;

import fractalzoomer.core.Complex;
import fractalzoomer.parser.ExpressionNode;
import fractalzoomer.parser.Parser;
import fractalzoomer.planes.Plane;

/**
 *
 * @author hrkalona2
 */
public class UserPlane extends Plane {

    private ExpressionNode expr;
    private Parser parser;

    public UserPlane(String user_plane) {

        super();

        parser = new Parser();
        expr = parser.parse(user_plane);

    }

    @Override
    public Complex getPixel(Complex pixel) {

        //expr.accept(new SetVariable("z", pixel));
        //expr.accept(new SetVariable("z", complex[0]));
        if(parser.foundZ()) {
            parser.setZvalue(pixel);
        }
        return expr.getValue();

    }

}
