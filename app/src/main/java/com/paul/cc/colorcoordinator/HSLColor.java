package com.paul.cc.colorcoordinator;

import android.graphics.Color;

/**
 * Code obtained from tips4java.wordpress.com, written by Rob Camick
 *
 * "We assume no responsibility for the code. You are free to use and/or
 * modify and/or distribute any or all code posted
 * on the Java Tips Weblog without restriction. A credit in the code
 * comments would be nice, but not in any way mandatory."
 */


/**
 *  The HSLColor class provides methods to manipulate HSL (Hue, Saturation
 *  Luminance) values to create a corresponding Color object using the RGB
 *  ColorSpace.
 *
 *  The HUE is the color, the Saturation is the purity of the color (with
 *  respect to grey) and Luminance is the brightness of the color (with respect
 *  to black and white)
 *
 *  The Hue is specified as an angel between 0 - 360 degrees where red is 0,
 *  green is 120 and blue is 240. In between you have the colors of the rainbow.
 *  Saturation is specified as a percentage between 0 - 100 where 100 is fully
 *  saturated and 0 approaches gray. Luminance is specified as a percentage
 *  between 0 - 100 where 0 is black and 100 is white.
 *
 *  In particular the HSL color space makes it easier change the Tone or Shade
 *  of a color by adjusting the luminance value.
 */
public class HSLColor
{
    private int rgb;
    private float[] hsl;
    private float alpha;

    /**
     *  Create a HSLColor object using an RGB Color object.
     *
     *  @param color the RGB Color object
     */
    public HSLColor(int color)
    {
        this.rgb = color;
        hsl = fromRGB( color );
        alpha = Color.alpha(color) / 255.0f;
    }

    /**
     *  Create a HSLColor object using individual HSL values and a default
     * alpha value of 1.0.
     *
     *  @param h is the Hue value in degrees between 0 - 360
     *  @param s is the Saturation percentage between 0 - 100
     *  @param l is the Lumanance percentage between 0 - 100
     */
    public HSLColor(float h, float s, float l)
    {
        this(h, s, l, 1.0f);
    }

    /**
     *  Create a HSLColor object using individual HSL values.
     *
     *  @param h     the Hue value in degrees between 0 - 360
     *  @param s     the Saturation percentage between 0 - 100
     *  @param l     the Lumanance percentage between 0 - 100
     *  @param alpha the alpha value between 0 - 1
     */
    public HSLColor(float h, float s, float l, float alpha)
    {
        hsl = new float[] {h, s, l};
        this.alpha = alpha;
        rgb = toRGB(hsl, alpha);
    }

    /**
     *  Create a HSLColor object using an an array containing the
     *  individual HSL values and with a default alpha value of 1.
     *
     *  @param hsl  array containing HSL values
     */
    public HSLColor(float[] hsl)
    {
        this(hsl, 1.0f);
    }

    /**
     *  Create a HSLColor object using an an array containing the
     *  individual HSL values.
     *
     *  @param hsl  array containing HSL values
     *  @param alpha the alpha value between 0 - 1
     */
    public HSLColor(float[] hsl, float alpha)
    {
        this.hsl = hsl;
        this.alpha = alpha;
        rgb = toRGB(hsl, alpha);
    }


    /**
     *  Get the Alpha value.
     *
     *  @return the Alpha value.
     */
    public float getAlpha()
    {
        return alpha;
    }

    /**
     *  Get the Hue value.
     *
     *  @return the Hue value.
     */
    public float getHue()
    {
        return hsl[0];
    }

    /**
     *  Get the HSL values.
     *
     *  @return the HSL values.
     */
    public float[] getHSL()
    {
        return hsl;
    }

    /**
     *  Get the Luminance value.
     *
     *  @return the Luminance value.
     */
    public float getLuminance()
    {
        return hsl[2];
    }

    /**
     *  Get the RGB Color object represented by this HDLColor.
     *
     *  @return the RGB Color object.
     */
    public int getRGB()
    {
        return rgb;
    }

    /**
     *  Get the Saturation value.
     *
     *  @return the Saturation value.
     */
    public float getSaturation()
    {
        return hsl[1];
    }

    public float getDegreesBetween(HSLColor color){
        float between;
        between = Math.abs(hsl[0] - color.getHue());
        if(between > (float) 180){
            between = 360 - between;
        }

        return between;
    }

    public float getDegreesBetween(Float hue){
        float between;
        between = Math.abs(hsl[0] - hue);
        if(between > (float) 180){
            between = 360 - between;
        }

        return between;
    }

    public String toString()
    {
        String toString =
                "HSLColor[h=" + hsl[0] +
                        ",s=" + hsl[1] +
                        ",l=" + hsl[2] +
                        ",alpha=" + alpha + "]";

        return toString;
    }

    /**
     *  Convert a RGB Color to it corresponding HSL values.
     *
     *  @return an array containing the 3 HSL values.
     */
    public static float[] fromRGB(int color)
    {
        //  Get RYB values in the range 0 - 1

        double[] ryb = rgb2ryb(color); //this is to get color wheel with green as 180, not cyan.
        float r = (float) ryb[0];
        float y = (float) ryb[1];
        float b = (float) ryb[2];

        //	Minimum and Maximum RYB values are used in the HSL calculations

        float min = Math.min(r, Math.min(y, b));
        float max = Math.max(r, Math.max(y, b));

        //  Calculate the Hue

        float h = 0;

        if (max == min)
            h = 0;
        else if (max == r)
            h = ((60 * (y - b) / (max - min)) + 360) % 360;
        else if (max == y)
            h = (60 * (b - r) / (max - min)) + 120;
        else if (max == b)
            h = (60 * (r - y) / (max - min)) + 240;

        //  Calculate the Luminance
        // In ryb, luminance is reversed
        float l = (max + min) / 2;

        //  Calculate the Saturation

        float s = 0;

        if (max == min)
            s = 0;
        else if (l <= .5f)
            s = (max - min) / (max + min);
        else
            s = (max - min) / (2 - max - min);

        return new float[] {h, s * 100, l * 100};
    }

    /**
     *  Convert HSL values to a RGB Color with a default alpha value of 1.
     *  H (Hue) is specified as degrees in the range 0 - 360.
     *  S (Saturation) is specified as a percentage in the range 1 - 100.
     *  L (Lumanance) is specified as a percentage in the range 1 - 100.
     *
     *  @param hsl an array containing the 3 HSL values
     *
     *  @returns the RGB Color object
     */
    public static int toRGB(float[] hsl)
    {
        return toRGB(hsl, 1.0f);
    }

    /**
     *  Convert HSL values to a RGB Color.
     *  H (Hue) is specified as degrees in the range 0 - 360.
     *  S (Saturation) is specified as a percentage in the range 1 - 100.
     *  L (Lumanance) is specified as a percentage in the range 1 - 100.
     *
     *  @param hsl    an array containing the 3 HSL values
     *  @param alpha  the alpha value between 0 - 1
     *
     *  @returns the RGB Color object
     */
    public static int toRGB(float[] hsl, float alpha)
    {
        return toRGB(hsl[0], hsl[1], hsl[2], alpha);
    }

    /**
     *  Convert HSL values to a RGB Color with a default alpha value of 1.
     *
     *  @param h Hue is specified as degrees in the range 0 - 360.
     *  @param s Saturation is specified as a percentage in the range 1 - 100.
     *  @param l Lumanance is specified as a percentage in the range 1 - 100.
     *
     *  @returns the RGB Color object
     */
    public static int toRGB(float h, float s, float l)
    {
        return toRGB(h, s, l, 1.0f);
    }

    /**
     *  Convert HSL values to a RGB Color.
     *
     *  @param h Hue is specified as degrees in the range 0 - 360.
     *  @param s Saturation is specified as a percentage in the range 1 - 100.
     *  @param l Lumanance is specified as a percentage in the range 1 - 100.
     *  @param alpha  the alpha value between 0 - 1
     *
     *  @returns the RGB Color object
     */
    public static int toRGB(float h, float s, float l, float alpha)
    {
        if (s <0.0f || s > 100.0f)
        {
            String message = "Color parameter outside of expected range - Saturation";
            throw new IllegalArgumentException( message );
        }

        if (l <0.0f || l > 100.0f)
        {
            String message = "Color parameter outside of expected range - Luminance";
            throw new IllegalArgumentException( message );
        }

        if (alpha <0.0f || alpha > 1.0f)
        {
            String message = "Color parameter outside of expected range - Alpha";
            throw new IllegalArgumentException( message );
        }

        //  Formula needs all values between 0 - 1.

        h = h % 360.0f;
        h /= 360f;
        s /= 100f;
        l /= 100f;

        float q = 0;

        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);

        float p = 2 * l - q;

        float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
        float y = Math.max(0, HueToRGB(p, q, h));
        float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

        r = Math.min(r, 1.0f);
        y = Math.min(y, 1.0f);
        b = Math.min(b, 1.0f);

        double[] ryb = {(double)r,(double)y,(double)b};

        return ryb2rgb(ryb,alpha);// must convert back from ryb to rgb colorspace
    }

    private static float HueToRGB(float p, float q, float h)
    {
        if (h < 0) h += 1;

        if (h > 1 ) h -= 1;

        if (6 * h < 1)
        {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1 )
        {
            return  q;
        }

        if (3 * h < 2)
        {
            return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );
        }

        return p;
    }

    // Convert back from RYB to RGB color representation
    private static int ryb2rgb(double[] ryb, float alpha){

        double R = (double) ryb[0];
        double Y = (double) ryb[1];
        double B = (double) ryb[2];
        double r,y,b,newR, newG, newB, Iw;

        Iw = Math.min(Math.min(R, Y), B);
        r = R - Iw;
        y = Y - Iw;
        b = B - Iw;

        newR = r + y - Math.min(y, b);
        newG = y + (2*Math.min(y, b));
        newB = 2*(b - Math.min(y, b));

        double n = Math.max(Math.max(newR, newG ), newB) / Math.max(Math.max(r, y), b);
        newR = newR / n;
        newG = newG / n;
        newB = newB / n;

        double Ib = Math.min(Math.min(1 - R, 1 - Y), 1 - B);
        newR = newR + Ib;
        newG = newG + Ib;
        newB = newB + Ib;
        int ret = Color.argb((int) alpha * 255, (int) newR * 255, (int) newG * 255, (int) newB * 255);
        return ret;
    }
    //Code to convert an RGB color to RYB color. Useful for color matching, as a designer's color
    // wheel expects red to be 0 and green to be 180. RGB color wheel leads to cyan as 180.
    private static double[] rgb2ryb(int color){

        double R = (double) Color.red(color)/255.0;
        double G = (double) Color.green(color)/255.0;
        double B = (double) Color.blue(color)/255.0;
        double r,g,b,newR, newY, newB, Iw;

        Iw = Math.min(Math.min(R, G), B);
        r = R - Iw;
        g = G - Iw;
        b = B - Iw;

        newR = r - Math.min(r, g);
        newY = (g + Math.min(r, g)) / 2;
        newB = (b + g - Math.min(r, g)) / 2;

        double n = Math.max(Math.max(newR, newY ), newB) / Math.max(Math.max(r, g), b);
        newR = newR / n;
        newY = newY / n;
        newB = newB / n;

        double Ib = Math.min(Math.min(1 - R, 1 - G), 1 - B);
        newR = newR + Ib;
        newY = newY + Ib;
        newB = newB + Ib;
        double[] ret = {newR,newY,newB};
        return ret;
    }
    //Experimental color naming with HSL in the RYB mode
    public static String printColor(HSLColor c){
        int s = (int) c.getSaturation();
        int l = (int) c.getLuminance();
        if(l < 15){return "white";}
        if(l > 85){return "black";}
        if(s < 10){return "gray";}
        int hue = (int) c.getHue();
        if(hue < 15){
            return "red";
        }
        if(hue < 45){return "red-orange";}
        if(hue < 75){return "orange/brown";}
        if(hue < 105){return "orange-yellow";}
        if(hue < 135){return "yellow";}
        if(hue < 165){return "yellow-green";}
        if(hue < 195){return "green";}
        if(hue < 225){return "teal";}
        if(hue < 255){return "blue";}
        if(hue < 285){return "violet";}
        if(hue < 315){return "purple";}
        if(hue < 345){return "magenta";}
        else{return "red";}
    }


}