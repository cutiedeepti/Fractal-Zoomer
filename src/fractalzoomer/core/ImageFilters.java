/*
 * Copyright (C) 2016 hrkalona
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
package fractalzoomer.core;

import fractalzoomer.filters_utils.image.BumpFilter;
import fractalzoomer.filters_utils.image.ChannelMixFilter;
import fractalzoomer.filters_utils.image.ContrastFilter;
import fractalzoomer.filters_utils.image.CrystallizeFilter;
import fractalzoomer.filters_utils.image.DiffusionFilter;
import fractalzoomer.filters_utils.image.DitherFilter;
import fractalzoomer.filters_utils.image.EmbossFilter;
import fractalzoomer.filters_utils.image.ExposureFilter;
import fractalzoomer.filters_utils.image.GainFilter;
import fractalzoomer.filters_utils.image.GammaFilter;
import fractalzoomer.filters_utils.image.GlowFilter;
import fractalzoomer.filters_utils.image.GrayFilter;
import fractalzoomer.filters_utils.image.HSBAdjustFilter;
import fractalzoomer.filters_utils.image.HighPassFilter;
import fractalzoomer.filters_utils.image.MarbleTexFilter;
import fractalzoomer.filters_utils.image.MaskFilter;
import fractalzoomer.filters_utils.image.MaximumFilter;
import fractalzoomer.filters_utils.image.MedianFilter;
import fractalzoomer.filters_utils.image.MinimumFilter;
import fractalzoomer.filters_utils.image.NoiseFilter;
import fractalzoomer.filters_utils.image.OilFilter;
import fractalzoomer.filters_utils.image.PointillizeFilter;
import fractalzoomer.filters_utils.image.PosterizeFilter;
import fractalzoomer.filters_utils.image.RGBAdjustFilter;
import fractalzoomer.filters_utils.image.RescaleFilter;
import fractalzoomer.filters_utils.image.SolarizeFilter;
import fractalzoomer.filters_utils.image.SparkleFilter;
import fractalzoomer.filters_utils.image.SwizzleFilter;
import fractalzoomer.filters_utils.image.TemperatureFilter;
import fractalzoomer.filters_utils.image.WeaveFilter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;

/**
 *
 * @author hrkalona
 */
public class ImageFilters {

    private static final float[] thick_edges = {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f, -2.0f, -2.0f, -1.0f, -1.0f, -2.0f, 32.0f, -2.0f, -1.0f, -1.0f, -2.0f, -2.0f, -2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    private static final float[] thin_edges = {-1.0f, -1.0f, -1.0f, -1.0f, 8.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    private static final float[] sharpness_high = {-0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, 3.4f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f};
    private static float[] sharpness_low = {0.0f, -0.2f, 0.0f, -0.2f, 1.8f, -0.2f, 0.0f, -0.2f, 0.0f};
    private static final float[] EMBOSS = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f};    

    public static void filterEmboss(BufferedImage image, int filter_value) {

        int image_size = image.getHeight();
        
        int algorithm = (int)(((int)(((int)(filter_value % 100000.0)) % 1000.0)) % 10.0);

        if(algorithm == 2) {
            int kernelWidth = (int)Math.sqrt((double)EMBOSS.length);
            int kernelHeight = kernelWidth;
            int xOffset = (kernelWidth - 1) / 2;
            int yOffset = xOffset;

            BufferedImage newSource = new BufferedImage(image_size + kernelWidth - 1, image_size + kernelHeight - 1, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = newSource.createGraphics();
            graphics.drawImage(image, xOffset, yOffset, null);

            Kernel kernel = new Kernel(kernelWidth, kernelHeight, EMBOSS);
            ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            cop.filter(newSource, image);

            graphics.dispose();
            graphics = null;
            kernel = null;
            cop = null;
            newSource = null;
        }
        else if(algorithm == 3) {
            BumpFilter f = new BumpFilter();
            
            f.filter(image, image);
        }
        else {
            /*int[] raster = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

            for(int i = image_size - 1; i >= 0; i--) {
                for(int j = image_size - 1, loc = i * image_size + j; j >= 0; j--, loc--) {
                    int current = raster[loc];

                    int upperLeft = 0;
                    if(i > 0 && j > 0) {
                        upperLeft = raster[loc - image_size - 1];
                    }

                    int rDiff = ((current >> 16) & 0xff) - ((upperLeft >> 16) & 0xff);
                    int gDiff = ((current >> 8) & 0xff) - ((upperLeft >> 8) & 0xff);
                    int bDiff = (current & 0xff) - (upperLeft & 0xff);

                    int diff = rDiff;
                    if(Math.abs(gDiff) > Math.abs(diff)) {
                        diff = gDiff;
                    }
                    if(Math.abs(bDiff) > Math.abs(diff)) {
                        diff = bDiff;
                    }

                    int grayLevel = Math.max(Math.min(128 + diff, 255), 0);
                    raster[loc] = 0xff000000 | ((grayLevel << 16) + (grayLevel << 8) + grayLevel);
                }
            }*/
            
            double direction = (((int)(((int)(((int)(filter_value % 100000.0)) % 1000.0)) / 10.0)) / 80.0 * 360) * Math.PI / 180.0;
            double elevation = (((int)(((int)(filter_value % 100000.0)) / 1000.0)) / 80.0 * 90) * Math.PI / 180.0;
            double bump_height = ((int)(filter_value / 100000.0)) / 80.0;
            
            EmbossFilter f = new EmbossFilter();
            
            f.setAzimuth((float)direction);
            f.setElevation((float)elevation);
            f.setBumpHeight((float)bump_height);
            
            if(algorithm == 0) {
                f.setEmboss(false);
            }
            else {
                f.setEmboss(true);
            }
            
            
            f.filter(image, image);
        }

    }

    public static void filterEdgeDetection(BufferedImage image, int filter_value, Color filter_color) {

        /*float[] EDGES = {1.0f,   1.0f,  1.0f,
         1.0f, -8.0f,  1.0f,
         1.0f,   1.0f,  1.0f};*/

        /*float[] EDGES = {-7.0f,   -7.0f,  -7.0f,
         -7.0f, 56.0f,  -7.0f,
         -7.0f,   -7.0f,  -7.0f};*/

        /*float[] EDGES = {-1.0f, -1.0f, -2.0f, -1.0f, -1.0f,
         -1.0f, -2.0f, -4.0f, -2.0f, -1.0f,
         -2.0f, -4.0f, 44.0f, -4.0f, -2.0f,
         -1.0f, -2.0f, -4.0f, -2.0f, -1.0f,
         -1.0f, -1.0f, -2.0f, -1.0f, -1.0f};*/
        
        int sensitivity = (int)(filter_value / 100.0);
        int thickness = (int)(filter_value % 100.0);
        
        float[] EDGES = null;

        if(thickness == 1) {
            EDGES = thick_edges;
        }
        else {
            EDGES = thin_edges;
        }

        int kernelWidth = (int)Math.sqrt((double)EDGES.length);

        int image_size = image.getHeight();

        Kernel kernel = new Kernel(kernelWidth, kernelWidth, EDGES);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage newSource = new BufferedImage(image_size, image_size, BufferedImage.TYPE_INT_RGB);
        BufferedImage newSource2 = new BufferedImage(image_size, image_size, BufferedImage.TYPE_INT_RGB);

        System.arraycopy(((DataBufferInt)image.getRaster().getDataBuffer()).getData(), 0, ((DataBufferInt)newSource.getRaster().getDataBuffer()).getData(), 0, image_size * image_size);

        cop.filter(newSource, newSource2);

        int[] raster = ((DataBufferInt)newSource2.getRaster().getDataBuffer()).getData();
        int[] rgbs = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        int condition = image_size * image_size;

        for(int p = 0; p < condition; p++) {
            
            int r = (raster[p] >> 16) & 0xff;
            int g = (raster[p] >> 8) & 0xff;
            int b = raster[p] & 0xff;          
            if(r <= sensitivity && g <= sensitivity && b <= sensitivity) {//(0xff000000 | raster[p]) == black
                rgbs[p] = filter_color.getRGB();
            }
        }

        kernel = null;
        cop = null;
        newSource = null;
        newSource2 = null;

    }

    public static void filterSharpness(BufferedImage image, int filter_value) {

        int image_size = image.getHeight();

        float[] SHARPNESS = null;

        if(filter_value == 0) {
            SHARPNESS = sharpness_low;
        }
        else {
            SHARPNESS = sharpness_high;
        }

        int kernelWidth = (int)Math.sqrt((double)SHARPNESS.length);
        int kernelHeight = kernelWidth;
        int xOffset = (kernelWidth - 1) / 2;
        int yOffset = xOffset;

        BufferedImage newSource = new BufferedImage(image_size + kernelWidth - 1, image_size + kernelHeight - 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = newSource.createGraphics();
        graphics.drawImage(image, xOffset, yOffset, null);

        Kernel kernel = new Kernel(kernelWidth, kernelHeight, SHARPNESS);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        cop.filter(newSource, image);

        graphics.dispose();
        graphics = null;
        kernel = null;
        cop = null;
        newSource = null;
    }

    public static void filterBlurring(BufferedImage image, int filter_value) { //OLD antialiasing method (blurring)
        
        int alg = ((int)(filter_value / 1000.0));
        
 
        
        if(alg >= 0 && alg < 6) {  
            /*     float b = 0.05f;
         float a = 1.0f - (8.0f * b);
        
        
         float[] AA = {b, b, b,    // low-pass filter
         b, a, b,
         b, b, b};
        
        
         /* float c = 0.00390625f;
         float b = 0.046875f;
         float a = 1.0f - (16.0f * c + 8.0f * b);
        
         float[] AA = {c, c, c, c, c,    // low-pass filter
         c, b, b, b, c,
         c, b, a, b, c,
         c, b, b, b, c,
         c, c, c, c, c};*/
        /*  float d = 1.0f / 3096.0f;
         float c = 12.0f * d;
         float b = 12.0f * c;
         float a = 1.0f - (24.0f * d + 16.0f * c + 8.0f * b);
        
         float[] AA = {d, d, d, d, d, d, d,    // low-pass filter
         d, c, c, c, c, c, d,
         d, c, b, b, b, c, d,
         d, c, b, a, b, c, d,
         d, c, b, b, b, c, d,
         d, c, c, c, c, c, d,
         d, d, d, d, d, d, d};*/
        
   
        /*if(blurring == 1) {
         float[] MOTION_BLUR = {0.2f,  0.0f,  0.0f, 0.0f,  0.0f,
         0.0f,   0.2f,  0.0f, 0.0f,  0.0f,
         0.0f,   0.0f,  0.2f, 0.0f,  0.0f,
         0.0f,   0.0f,  0.0f, 0.2f,  0.0f,
         0.0f,   0.0f,  0.0f, 0.0f,  0.2f};
        
         blur = MOTION_BLUR;
         }*/
        //else {
            float[] blur = null;
            
            if(alg == 0) {
                float e = 1.0f / 37184.0f;
                float d = 12.0f * e;
                float c = 12.0f * d;
                float b = 12.0f * c;
                float a = 1.0f - (32.0f * e + 24.0f * d + 16.0f * c + 8.0f * b);

                float[] NORMAL_BLUR = {e, e, e, e, e, e, e, e, e, // low-pass filter
                    e, d, d, d, d, d, d, d, e,
                    e, d, c, c, c, c, c, d, e,
                    e, d, c, b, b, b, c, d, e,
                    e, d, c, b, a, b, c, d, e,
                    e, d, c, b, b, b, c, d, e,
                    e, d, c, c, c, c, c, d, e,
                    e, d, d, d, d, d, d, d, e,
                    e, e, e, e, e, e, e, e, e};

                blur = NORMAL_BLUR;
            }
            else {
                 int radius = alg;
                 double weight = ((int)(filter_value % 1000.0)) / 100.0 * 40;
                 blur = createGaussianKernel((radius - 1) * 2 + 3, weight);
            }
            
                // }


           /*float h = 1.0f / 64260344.0f;
            float g = 12.0f * h;
            float f = 12.0f * g;
            float e = 12.0f * f;
            float d = 12.0f * e;
            float c = 12.0f * d;
            float b = 12.0f * c;
            float a = 1.0f - (56.0f * h + 48.0f * g + 40.0f * f + 32.0f * e + 24.0f * d + 16.0f * c + 8.0f * b);

            float[] AA  = {h, h, h, h, h, h, h, h, h, h, h, h, h, h, h,    // low-pass filter
            h, g, g, g, g, g, g, g, g, g, g, g, g, g, h,
            h, g, f, f, f, f, f, f, f, f, f, f, f, g, h,
            h, g, f, e, e, e, e, e, e, e, e, e, f, g, h,
            h, g, f, e, d, d, d, d, d, d, d, e, f, g, h,
            h, g, f, e, d, c, c, c, c, c, d, e, f, g, h,
            h, g, f, e, d, c, b, b, b, c, d, e, f, g, h,
            h, g, f, e, d, c, b, a, b, c, d, e, f, g, h,
            h, g, f, e, d, c, b, b, b, c, d, e, f, g, h,
            h, g, f, e, d, c, c, c, c, c, d, e, f, g, h,
            h, g, f, e, d, d, d, d, d, d, d, e, f, g, h,
            h, g, f, e, e, e, e, e, e, e, e, e, f, g, h,
            h, g, f, f, f, f, f, f, f, f, f, f, f, g, h,
            h, g, g, g, g, g, g, g, g, g, g, g, g, g, h,
            h, h, h, h, h, h, h, h, h, h, h, h, h, h, h};*/
           //resize the picture to cover the image edges
           int kernelWidth = (int)Math.sqrt((double)blur.length);
           int kernelHeight = kernelWidth;
           int xOffset = (kernelWidth - 1) / 2;
           int yOffset = xOffset;

           int image_size = image.getHeight();

           BufferedImage newSource = new BufferedImage(image_size + kernelWidth - 1, image_size + kernelHeight - 1, BufferedImage.TYPE_INT_RGB);
           Graphics2D graphics = newSource.createGraphics();
           graphics.drawImage(image, xOffset, yOffset, null);

           Kernel kernel = new Kernel(kernelWidth, kernelHeight, blur);
           ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
           cop.filter(newSource, image);

           graphics.dispose();
           graphics = null;
           blur = null;
           kernel = null;
           cop = null;
           newSource = null;
        }
        else if(alg == 6) {
            MaximumFilter f = new MaximumFilter();
            
            f.filter(image, image);
        }
        else if(alg == 7) {
            MedianFilter f = new MedianFilter();
            
            f.filter(image, image);
        }
        else if(alg == 8) {
            MinimumFilter f = new MinimumFilter();
            
            f.filter(image, image);
        }
        else if(alg == 9) {
            double weight = ((int)(filter_value % 1000.0));
            
            HighPassFilter f = new HighPassFilter();
            
            f.setRadius((float)weight);
            
            f.filter(image, image);
        }

       
    }

    public static void filterInvertColors(BufferedImage image, int filter_value) {

        int image_size = image.getHeight();

        int[] raster = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        int condition = image_size * image_size;

        int r, g, b;

        for(int p = 0; p < condition; p++) {

            if(filter_value == 0) {
                raster[p] = ~(raster[p] & 0x00ffffff);
            }
            else if(filter_value == 1){ // Brightness
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                raster[p] = Color.HSBtoRGB(res[0], res[1], 1.0f - res[2]);
            }
            else if(filter_value == 2){ // Hue
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                raster[p] = Color.HSBtoRGB(1.0f - res[0], res[1], res[2]);
            }
            else if(filter_value == 3){ // Saturation
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                raster[p] = Color.HSBtoRGB(res[0], 1.0f - res[1], res[2]);
            }

        }

    }

    public static void filterMaskColors(BufferedImage image, int filter_value) {

        int mask = 0;

        if(filter_value == 0) {
            mask = 0xff00ffff;  //RED
        }
        else if(filter_value == 1) {
            mask = 0xffff00ff;  //GREEN
        }
        else {
            mask = 0xffffff00;  //BLUE
        }

        MaskFilter f = new MaskFilter();
        f.setMask(mask);
        
        f.filter(image, image);

    }

    public static void filterFadeOut(BufferedImage image) {

        GrayFilter f = new GrayFilter();
        
        f.filter(image, image);

    }

    public static void filterColorChannelSwapping(BufferedImage image, int filter_value) {

        int image_size = image.getHeight();

        int[] raster = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        int r, g, b;

        int condition = image_size * image_size;

        for(int p = 0; p < condition; p++) {
            r = (raster[p] >> 16) & 0xff;
            g = (raster[p] >> 8) & 0xff;
            b = raster[p] & 0xff;

            if(filter_value == 0) {
                raster[p] = 0xff000000 | (r << 16) | (b << 8) | g;
            }
            else if(filter_value == 1) {
                raster[p] = 0xff000000 | (g << 16) | (r << 8) | b;
            }
            else if(filter_value == 2) {
                raster[p] = 0xff000000 | (g << 16) | (b << 8) | r;
            }
            else if(filter_value == 3) {
                raster[p] = 0xff000000 | (b << 16) | (g << 8) | r;
            }
            else {
                raster[p] = 0xff000000 | (b << 16) | (r << 8) | g;
            }
        }

    }

    public static void filterContrastBrightness(BufferedImage image, int filter_value) {

        double contrast = (((int)(filter_value / 1000.0)) / 100.0) * 2.0;
        double brightness = (((int)(filter_value % 1000.0)) / 100.0) * 2.0;
        
        ContrastFilter f = new ContrastFilter();
        f.setContrast((float)contrast);
        f.setBrightness((float)brightness);
        
        f.filter(image, image);

    }

    public static void filterColorTemperature(BufferedImage image, int filter_value) {
   
        TemperatureFilter f = new TemperatureFilter();
        
        f.setTemperature((float)filter_value);
        
        f.filter(image, image);
                

    }

    public static void filterGrayscale(BufferedImage image, int filter_value) {

        int image_size = image.getHeight();

        int[] raster = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        int r, g, b, rgb;

        int condition = image_size * image_size;

        for(int p = 0; p < condition; p++) {
            r = (raster[p] >> 16) & 0xff;
            g = (raster[p] >> 8) & 0xff;
            b = raster[p] & 0xff;

            if(filter_value == 0) {
                rgb = (r * 612 + g * 1202 + b * 233) >> 11;	// NTSC luma
                //rgb = (int)(r * 0.299F + g * 0.587F + b * 0.114F);	// NTSC luma
            }
            else if(filter_value == 1) {
                rgb = (r * 435 + g * 1464 + b * 147) >> 11;	// HDTV luma
                //rgb = (int)(r * 0.2126F + g * 0.7152F + b * 0.0722F);	// HDTV luma
            }
            else if(filter_value == 2) {
                rgb = (int)((r + g + b) / 3.0 + 0.5);	// simple average
            }
            else {
                rgb = (int)((Math.max(Math.max(r, g), b) + Math.min(Math.min(r, g), b)) / 2.0 + 0.5);
            }

            raster[p] = 0xff000000 | (rgb << 16) | (rgb << 8) | rgb;
        }

    }

    public static void filterHistogramEqualization(BufferedImage image, int filter_value) {

        int image_size = image.getHeight();

        int[] raster = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        int condition = image_size * image_size;

        /*int r, g, b;
        
         int cdf[] = new int[100001];
        
         for(int p = 0; p < condition; p++) {
         r = (raster[p] >> 16) & 0xff;
         g = (raster[p] >> 8) & 0xff;
         b = raster[p] & 0xff;
        
         float res[] = new float[3];
        
         Color.RGBtoHSB(r, g, b, res);
        
         cdf[(int)(res[2] * 100000 + 0.5)]++;
         }
        
         int min = 0;
         int j = 0;
         while(min == 0) {
         min = cdf[j++];
         }
         int d = condition - min;
        
         for(int i = 1; i < cdf.length; i++) {
         cdf[i] += cdf[i - 1];
         }
        
         for(int p = 0; p < condition; p++) {
         r = (raster[p] >> 16) & 0xff;
         g = (raster[p] >> 8) & 0xff;
         b = raster[p] & 0xff;
        
         float res[] = new float[3];
        
         Color.RGBtoHSB(r, g, b, res);
        
         double temp = ((double)(cdf[(int)(res[2] * 100000 + 0.5)] - min)) / d;
        
         raster[p] = Color.HSBtoRGB(res[0], res[1], (float)(temp < 0 ? 0 : temp));
        
        
         }*/
        if(filter_value == 1) { //gimp levels

            int hist[][] = new int[3][256];

            int i, c;
            double mult, count, percentage, next_percentage;
            int low = 0, high = 0;

            // Fill the histogram by counting the number of pixels with each value
            for(int p = 0; p < condition; p++) {
                hist[0][(raster[p] >> 16) & 0xff]++;
                hist[1][(raster[p] >> 8) & 0xff]++;
                hist[2][raster[p] & 0xff]++;
            }

            count = image_size * image_size;
            // Fore each channel
            for(c = 0; c < hist.length; c++) {
                // Determine the low input value
                next_percentage = hist[c][0] / count;
                for(i = 0; i < 255; i++) {
                    percentage = next_percentage;
                    next_percentage += hist[c][i + 1] / count;
                    if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                        //low = i;//i+1; This is a deviation from the way The GIMP does it
                        low = i + 1;
                        // that prevents any change in the image if it's
                        // already optimal
                        break;
                    }
                }
                // Determine the high input value
                next_percentage = hist[c][255] / count;
                for(i = 255; i > 0; i--) {
                    percentage = next_percentage;
                    next_percentage += hist[c][i - 1] / count;
                    if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                        //high = i;//i-1; This is a deviation from the way The GIMP does it
                        high = i - 1;
                        // that prevents any change in the image if it's
                        // already optimal
                        break;
                    }
                }

                // Turn the histogram into a look up table to stretch the values
                mult = 255.0 / (high - low);

                for(i = 0; i < low; i++) {
                    hist[c][i] = 0;
                }

                for(i = 255; i > high; i--) {
                    hist[c][i] = 255;
                }

                double base = 0.0;

                for(i = low; i <= high; i++) {
                    hist[c][i] = (int)(base + 0.5);
                    base += mult;
                }
            }

            // Now apply the changes (stretch the values)
            int r, g, b;

            for(int p = 0; p < condition; p++) {
                r = hist[0][(raster[p] >> 16) & 0xff];
                g = hist[1][(raster[p] >> 8) & 0xff];
                b = hist[2][raster[p] & 0xff];

                raster[p] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        else if(filter_value == 0) { //brightness
            int hist[] = new int[1025];

            int i;
            double mult, count, percentage, next_percentage;
            int low = 0, high = 0;

            int hist_len = hist.length - 1;

            int r, g, b;

            // Fill the histogram by counting the number of pixels with each value
            for(int p = 0; p < condition; p++) {
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                hist[(int)(res[2] * hist_len + 0.5)]++;
            }

            count = image_size * image_size;

            // Determine the low input value
            next_percentage = hist[0] / count;
            for(i = 0; i < hist_len; i++) {
                percentage = next_percentage;
                next_percentage += hist[i + 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //low = i;//i+1; This is a deviation from the way The GIMP does it
                    low = i + 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }
            // Determine the high input value
            next_percentage = hist[hist_len] / count;
            for(i = hist_len; i > 0; i--) {
                percentage = next_percentage;
                next_percentage += hist[i - 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //high = i;//i-1; This is a deviation from the way The GIMP does it
                    high = i - 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }

            // Turn the histogram into a look up table to stretch the values
            mult = ((double)hist_len) / (high - low);

            for(i = 0; i < low; i++) {
                hist[i] = 0;
            }

            for(i = hist_len; i > high; i--) {
                hist[i] = hist_len;
            }

            double base = 0.0;

            for(i = low; i <= high; i++) {
                hist[i] = (int)(base + 0.5);
                base += mult;
            }

            // Now apply the changes (stretch the values)
            for(int p = 0; p < condition; p++) {
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                double temp = hist[(int)(res[2] * hist_len + 0.5)] / ((double)hist_len);

                raster[p] = Color.HSBtoRGB(res[0], res[1], (float)temp);
            }

        }
        else if(filter_value == 2 || filter_value == 3 || filter_value == 4) { //red, green, blue
            int hist[] = new int[256];

            int i;
            double mult, count, percentage, next_percentage;
            int low = 0, high = 0;

            // Fill the histogram by counting the number of pixels with each value
            for(int p = 0; p < condition; p++) {
                if(filter_value == 2) {
                    hist[(raster[p] >> 16) & 0xff]++;
                }
                else if(filter_value == 3) {
                    hist[(raster[p] >> 8) & 0xff]++;
                }
                else {
                    hist[raster[p] & 0xff]++;
                }
            }

            count = image_size * image_size;
            // Determine the low input value
            next_percentage = hist[0] / count;
            for(i = 0; i < 255; i++) {
                percentage = next_percentage;
                next_percentage += hist[i + 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //low = i;//i+1; This is a deviation from the way The GIMP does it
                    low = i + 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }
            // Determine the high input value
            next_percentage = hist[255] / count;
            for(i = 255; i > 0; i--) {
                percentage = next_percentage;
                next_percentage += hist[i - 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //high = i;//i-1; This is a deviation from the way The GIMP does it
                    high = i - 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }

            // Turn the histogram into a look up table to stretch the values
            mult = 255.0 / (high - low);

            for(i = 0; i < low; i++) {
                hist[i] = 0;
            }

            for(i = 255; i > high; i--) {
                hist[i] = 255;
            }

            double base = 0.0;

            for(i = low; i <= high; i++) {
                hist[i] = (int)(base + 0.5);
                base += mult;
            }

            // Now apply the changes (stretch the values)
            int r, g, b;

            for(int p = 0; p < condition; p++) {

                if(filter_value == 2) {
                    r = hist[(raster[p] >> 16) & 0xff];
                    g = (raster[p] >> 8) & 0xff;
                    b = raster[p] & 0xff;
                }
                else if(filter_value == 3) {
                    r = (raster[p] >> 16) & 0xff;
                    g = hist[(raster[p] >> 8) & 0xff];
                    b = raster[p] & 0xff;
                }
                else {
                    r = (raster[p] >> 16) & 0xff;
                    g = (raster[p] >> 8) & 0xff;
                    b = hist[raster[p] & 0xff];
                }

                raster[p] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        else if(filter_value == 5) { //Hue
            int hist[] = new int[1025];

            int i;
            double mult, count, percentage, next_percentage;
            int low = 0, high = 0;

            int hist_len = hist.length - 1;

            int r, g, b;

            // Fill the histogram by counting the number of pixels with each value
            for(int p = 0; p < condition; p++) {
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                hist[(int)(res[0] * hist_len + 0.5)]++;
            }

            count = image_size * image_size;

            // Determine the low input value
            next_percentage = hist[0] / count;
            for(i = 0; i < hist_len; i++) {
                percentage = next_percentage;
                next_percentage += hist[i + 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //low = i;//i+1; This is a deviation from the way The GIMP does it
                    low = i + 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }
            // Determine the high input value
            next_percentage = hist[hist_len] / count;
            for(i = hist_len; i > 0; i--) {
                percentage = next_percentage;
                next_percentage += hist[i - 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //high = i;//i-1; This is a deviation from the way The GIMP does it
                    high = i - 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }

            // Turn the histogram into a look up table to stretch the values
            mult = ((double)hist_len) / (high - low);

            for(i = 0; i < low; i++) {
                hist[i] = 0;
            }

            for(i = hist_len; i > high; i--) {
                hist[i] = hist_len;
            }

            double base = 0.0;

            for(i = low; i <= high; i++) {
                hist[i] = (int)(base + 0.5);
                base += mult;
            }

            // Now apply the changes (stretch the values)
            for(int p = 0; p < condition; p++) {
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                double temp = hist[(int)(res[0] * hist_len + 0.5)] / ((double)hist_len);

                raster[p] = Color.HSBtoRGB((float)temp, res[1], res[2]);
            }

        }
        else if(filter_value == 6) { //Saturation
            int hist[] = new int[1025];

            int i;
            double mult, count, percentage, next_percentage;
            int low = 0, high = 0;

            int hist_len = hist.length - 1;

            int r, g, b;

            // Fill the histogram by counting the number of pixels with each value
            for(int p = 0; p < condition; p++) {
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                hist[(int)(res[1] * hist_len + 0.5)]++;
            }

            count = image_size * image_size;

            // Determine the low input value
            next_percentage = hist[0] / count;
            for(i = 0; i < hist_len; i++) {
                percentage = next_percentage;
                next_percentage += hist[i + 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //low = i;//i+1; This is a deviation from the way The GIMP does it
                    low = i + 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }
            // Determine the high input value
            next_percentage = hist[hist_len] / count;
            for(i = hist_len; i > 0; i--) {
                percentage = next_percentage;
                next_percentage += hist[i - 1] / count;
                if(Math.abs(percentage - 0.006) < Math.abs(next_percentage - 0.006)) {
                    //high = i;//i-1; This is a deviation from the way The GIMP does it
                    high = i - 1;
                    // that prevents any change in the image if it's
                    // already optimal
                    break;
                }
            }

            // Turn the histogram into a look up table to stretch the values
            mult = ((double)hist_len) / (high - low);

            for(i = 0; i < low; i++) {
                hist[i] = 0;
            }

            for(i = hist_len; i > high; i--) {
                hist[i] = hist_len;
            }

            double base = 0.0;

            for(i = low; i <= high; i++) {
                hist[i] = (int)(base + 0.5);
                base += mult;
            }

            // Now apply the changes (stretch the values)
            for(int p = 0; p < condition; p++) {
                r = (raster[p] >> 16) & 0xff;
                g = (raster[p] >> 8) & 0xff;
                b = raster[p] & 0xff;

                float res[] = new float[3];

                Color.RGBtoHSB(r, g, b, res);

                double temp = hist[(int)(res[1] * hist_len + 0.5)] / ((double)hist_len);

                raster[p] = Color.HSBtoRGB(res[0], (float)temp , res[2]);
            }

        }
        
    }

    public static void filterColorChannelSwizzling(BufferedImage image, int filter_value) {

        
        int[] matrix = {
        1, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0
    };
        
        for(int j = 0; j < 12; j++) {
        
            if(((filter_value >> j) & 0x1) == 1) {
                matrix[j + 6 + j / 4] = 1;
            }

        }
        
        SwizzleFilter f = new SwizzleFilter();
        f.setMatrix(matrix);
        
        f.filter(image, image);

    }

    public static void filterColorChannelAdjusting(BufferedImage image, int filter_value) {

        double rFactor = 2 * ((int)(filter_value / 1000000.0)) / 100.0;
        double gFactor = 2 * ((int)(((int)(filter_value % 1000000.0)) / 1000)) / 100.0;
        double bFactor = 2 * ((int)(((int)(filter_value % 1000000.0)) % 1000)) / 100.0;
        
        RGBAdjustFilter f = new RGBAdjustFilter();
        
        f.setRFactor((float)rFactor - 1);
        f.setGFactor((float)gFactor - 1);
        f.setBFactor((float)bFactor - 1);
        
        f.filter(image, image);
    }

    public static void filterDither(BufferedImage image, int filter_value) {
            
        int levels = (int)(((int)(((int)(filter_value % 100000.0)) % 10000.0)) % 1000.0);
        int dither_mat_number = (int)(((int)(((int)(filter_value % 100000.0)) % 10000.0)) / 1000.0);
        int diffusion = (int)(((int)(filter_value % 100000.0)) / 10000.0);
        int serpentine = ((int)(filter_value / 100000.0));
       
        if(diffusion == 0) {
            DitherFilter f = new DitherFilter();

            switch (dither_mat_number) {
                case 0:
                    f.setMatrix(DitherFilter.ditherMagic2x2Matrix);
                    break;
                case 1:
                    f.setMatrix(DitherFilter.ditherOrdered4x4Matrix);
                    break;
                case 2:
                    f.setMatrix(DitherFilter.ditherOrdered6x6Matrix);
                    break;
                case 3:
                    f.setMatrix(DitherFilter.ditherOrdered8x8Matrix);
                    break;
                case 4:
                    f.setMatrix(DitherFilter.ditherMagic4x4Matrix);
                    break;
                case 5:
                    f.setMatrix(DitherFilter.ditherCluster3Matrix);
                    break;
                case 6:
                    f.setMatrix(DitherFilter.ditherCluster4Matrix);
                    break;
                case 7:
                    f.setMatrix(DitherFilter.ditherCluster8Matrix);
                    break;
                case 8:
                    f.setMatrix(DitherFilter.ditherLines4x4Matrix);
                    break;
                case 9:
                    f.setMatrix(DitherFilter.dither90Halftone6x6Matrix);
                    break;
            }

            f.setLevels(levels);

            f.filter(image, image);
        }
        else {
            DiffusionFilter f = new DiffusionFilter();
            
            if(serpentine == 1) {
                f.setSerpentine(true);
            }
            else {
                f.setSerpentine(false);
            }
            
            f.setLevels(levels);
            f.filter(image, image);
        }
        
        

    }

    public static void filterHSBcolorChannelAdjusting(BufferedImage image, int filter_value) {

        double hFactor = 2 * ((((int)(filter_value / 1000000.0)) / 100.0) - 0.5);
        double sFactor = 2 * ((((int)(((int)(filter_value % 1000000.0)) / 1000)) / 100.0) - 0.5);
        double bFactor = 2 * ((((int)(((int)(filter_value % 1000000.0)) % 1000)) / 100.0) - 0.5);

        HSBAdjustFilter f = new HSBAdjustFilter();
        f.setHFactor((float)hFactor);
        f.setSFactor((float)sFactor);
        f.setBFactor((float)bFactor);
        
        f.filter(image, image);
    }

    public static void filterPosterize(BufferedImage image, int filter_value) {
      
        int numLevels = filter_value;
        
        PosterizeFilter f = new PosterizeFilter();
        f.setNumLevels(numLevels);
        
        f.filter(image, image);

    }

    public static void filterSolarize(BufferedImage image) {

        SolarizeFilter f = new SolarizeFilter();
        
        f.filter(image, image);
        
    }

    public static void filterGain(BufferedImage image, int filter_value) {

        double gain = (((int)(filter_value / 1000.0)) / 100.0);
        double bias = (((int)(filter_value % 1000.0)) / 100.0);

        GainFilter f = new GainFilter();
        f.setGain((float)gain);
        f.setBias((float)bias);
        
        f.filter(image, image);

    }

    public static void filterGamma(BufferedImage image, int filter_value) {

        double gamma = filter_value / 100.0 * 3.0;
        
        GammaFilter f = new GammaFilter();
        f.setGamma((float)gamma);
        
        f.filter(image, image);
        
    }

    public static void filterExposure(BufferedImage image, int filter_value) {

        double exposure = filter_value / 100.0 * 5.0;

        ExposureFilter f = new ExposureFilter();
        f.setExposure((float)exposure);
        
        f.filter(image, image);

    }
    
    public static void filterCrystallize(BufferedImage image, int filter_value, Color filter_color) {
       
        double size = (((int)(((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) % 10000.0)) % 100.0)) / 80.0) * 100;
        double randomness = ((int)(((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) % 10000.0)) / 100.0)) / 80.0;       
        double edge_size = ((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) / 10000.0)) / 80.0;
        int shape = ((int)(((int)(filter_value % 10000000.0)) / 1000000.0));
        int fade_edges = (int)(filter_value / 10000000.0); 
        
        CrystallizeFilter f = new CrystallizeFilter();
        
        if(fade_edges == 1) {
            f.setFadeEdges(true);
        }
        else {
            f.setFadeEdges(false);
        }
                 
        f.setGridType(shape);      
        f.setEdgeThickness((float)edge_size);      
        f.setRandomness((float)randomness);        
        f.setScale((float)size);    
        f.setEdgeColor(filter_color.getRGB());
   
        f.filter(image, image);   
        
    }
    
    public static void filterMarble(BufferedImage image, int filter_value) {
       
        MarbleTexFilter f = new MarbleTexFilter();
        
        double turbulence_factor = ((int)(filter_value / 10000000.0)) / 80.0;
        double turbulence = ((int)(((int)(filter_value % 10000000.0)) / 1000000.0)) / 8.0 * 8;
        double stretch = ((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) / 10000.0)) / 80.0 * 50;
        double angle = ((int)(((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) % 10000.0)) / 100.0)) / 80.0 * 360;
        double scale = ((int)(((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) % 10000.0)) % 100.0)) / 80.0 * 300;
        
        f.setTurbulenceFactor((float)turbulence_factor);
        f.setTurbulence((float)turbulence);
        f.setStretch((float)stretch);
        f.setAngle((float)Math.toRadians(angle));
        f.setScale((float)scale);
                
        f.filter(image, image);
        
        
    }
    
    public static void filterPointillize(BufferedImage image, int filter_value, Color filter_color) {
       
        PointillizeFilter f = new PointillizeFilter();
        
        int fill = (int)(filter_value / 1000000000.0);
        int shape = (int)(((int)(filter_value % 1000000000.0)) / 100000000.0);
        double point_size = ((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) / 1000000.0)) / 80.0;
        double fuzziness = ((int)(((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) % 1000000.0)) / 10000.0)) / 80.0;
        double randomness = ((int)(((int)(((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) % 1000000.0)) % 10000.0)) / 100.0)) / 80.0;
        double grid_size = ((int)(((int)(((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) % 1000000.0)) % 10000.0)) % 100.0)) / 80.0 * 100;
        
        if(fill == 1) {
            f.setFadeEdges(true);
        }
        else {
            f.setFadeEdges(false);
        }
        
        f.setScale((float)grid_size); 
        f.setFuzziness((float)fuzziness);
        f.setEdgeColor(filter_color.getRGB());
        f.setRandomness((float)randomness);
        f.setGridType(shape);
        f.setEdgeThickness((float)point_size);
        
        f.filter(image, image);
        
    }
    
    public static void filterOil(BufferedImage image, int filter_value) {
       
        OilFilter f = new OilFilter();
        
        int levels = (int)(filter_value / 100.0);
        int range = (int)(filter_value % 100.0);
        
        f.setRange(range);
        f.setLevels(levels);
     
        f.filter(image, image);
        
    }
    
    public static void filterWeave(BufferedImage image, int filter_value, Color filter_color) {
       
        WeaveFilter f = new WeaveFilter();    
        
        double x_width = ((int)(((int)(((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) % 1000000.0)) % 10000.0)) % 100.0)) / 80.0 * 256;
        double y_width = ((int)(((int)(((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) % 1000000.0)) % 10000.0)) / 100.0)) / 80.0 * 256;
        double x_gap = ((int)(((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) % 1000000.0)) / 10000.0)) / 80.0 * 256;
        double y_gap = ((int)(((int)(((int)(filter_value % 1000000000.0)) % 100000000.0)) / 1000000.0)) / 80.0 * 256;
        
        int round_threads = (int)(((int)(filter_value % 1000000000.0)) / 100000000.0);
        int shade_crossings = (int)(filter_value / 1000000000.0);
        
        if(round_threads == 1) {
            f.setRoundThreads(true);
        }
        else {
            f.setRoundThreads(false);
        }
        
        if(shade_crossings == 1) {
            f.setShadeCrossings(true);
        }
        else {
            f.setShadeCrossings(false);
        }
        
        f.setXWidth((float)x_width);
        f.setYWidth((float)y_width);
        f.setXGap((float)x_gap);
        f.setYGap((float)y_gap);
        
        f.setColor(filter_color.getRGB());
        
        f.setUseImageColors(true);
        
        f.filter(image, image);     
        
    }
    
    public static void filterSparkle(BufferedImage image, int filter_value, Color filter_color) {
       
        SparkleFilter f = new SparkleFilter();
        
        double rays = (int)(((int)(((int)(filter_value % 1000000.0)) % 10000.0)) % 100.0) / 80.0 * 300;
        double radius = (int)(((int)(((int)(filter_value % 1000000.0)) % 10000.0)) / 100.0) / 80.0 * 300;
        double shine = (int)(((int)(filter_value % 1000000.0)) / 10000.0) / 80.0 * 100;
        double randomness = (int)(filter_value / 1000000.0) / 80.0 * 50;
        
        f.setRays((int)rays);
        f.setRadius((int)radius);
        f.setAmount((int)shine);
        f.setRandomness((int)randomness);
        f.setColor(filter_color.getRGB());
        
        f.filter(image, image);
        
    }
    
    public static void filterGlow(BufferedImage image, int filter_value) {
        
        double softness = ((int)(filter_value / 1000.0));
        double amount = ((int)(filter_value % 1000.0)) / 100.0;
        
        GlowFilter f = new GlowFilter();
        
        f.setAmount((float)amount);
        f.setRadius((float)softness);
        
        f.filter(image, image);
        
    }
    
    public static void filterNoise(BufferedImage image, int filter_value) {
        
        NoiseFilter f = new NoiseFilter();
        
        double density = ((int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) / 1000.0)) / 100.0;
        int amount = (int)(((int)(((int)(filter_value % 10000000.0)) % 1000000.0)) / 1000.0);
        int distribution = (int)(((int)(filter_value % 10000000.0)) / 1000000.0);
        int monochrome = (int)(filter_value / 10000000.0);
        
        if(monochrome == 1) {
            f.setMonochrome(true);
        }
        else {
            f.setMonochrome(false);
        }
        
        f.setDistribution(distribution);       
        f.setAmount(amount);      
        f.setDensity((float)density);
        
        f.filter(image, image);
        
    }
    
    public static void filterColorChannelScaling(BufferedImage image, int filter_value) {
        
        double scale = filter_value / 100.0 * 5;
        
        RescaleFilter f = new RescaleFilter();
        f.setScale((float)scale);
        
        f.filter(image, image);
        
    }
    
    public static void filterColorChannelMixing(BufferedImage image, int filter_value, Color filter_color) {
        
        ChannelMixFilter f = new ChannelMixFilter();
        
        int intoRed = filter_color.getRed();
        int intoGreen = filter_color.getGreen();
        int intoBlue = filter_color.getBlue();
        
        int blueGreen = (int)(((int)(filter_value % 1000000.0)) % 1000.0);
        int redBlue = (int)(((int)(filter_value % 1000000.0)) / 1000.0);
        int greenRed = (int)(filter_value / 1000000.0);
        
        f.setBlueGreen(blueGreen);
        f.setRedBlue(redBlue);
        f.setGreenRed(greenRed);
        f.setIntoR(intoRed);
        f.setIntoG(intoGreen);
        f.setIntoB(intoBlue);
   
        f.filter(image, image);
        
    }
    
    private static float[] createGaussianKernel(int length, double weight) {
        float[] gaussian_kernel = new float[length * length];
        double sumTotal = 0;

        int kernelRadius = length / 2;
        double distance = 0;

        double calculatedEuler = 1.0 / (2.0 * Math.PI * weight * weight);

        float temp;
        for(int filterY = -kernelRadius; filterY <= kernelRadius; filterY++) {
            for(int filterX = -kernelRadius; filterX <= kernelRadius; filterX++) {
                distance = ((filterX * filterX) + (filterY * filterY)) / (2 * (weight * weight));
                temp = gaussian_kernel[(filterY + kernelRadius) * length + filterX + kernelRadius] = (float)(calculatedEuler * Math.exp(-distance));
                sumTotal += temp;
            }
        }

        for(int y = 0; y < length; y++) {
            for(int x = 0; x < length; x++) {
                gaussian_kernel[y * length + x] = (float)(gaussian_kernel[y * length + x] * (1.0 / sumTotal));
            }
        }
        
        return gaussian_kernel;

    }
   

}