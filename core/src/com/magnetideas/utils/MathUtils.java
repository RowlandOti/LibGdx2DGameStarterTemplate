package com.magnetideas.utils;

import java.awt.image.BufferedImage;

/**
 * @author Rowland
 *
 */
public class MathUtils {
	
	public static BufferedImage reSize(BufferedImage img) 
	{
		int w = nearestPowerOfTwo(img.getWidth());
		int h = nearestPowerOfTwo(img.getHeight());
		BufferedImage rc = new BufferedImage(w, h, img.getType());
		rc.getGraphics().drawImage(img, 0, 0, w, h, null);
		return rc;
	}

	public static int nearestPowerOfTwo(int value) 
	{
		int next = nextPowerOfTwo(value);
		int last = lastPowerOfTwo(value);
		return (value - last < next - value) ? last : next;
	}
	 
	public static int lastPowerOfTwo(int value) 
	{
		return (int) Math.pow(2.0, Math.floor(log10(value) / log10(2)));
	}

	

	private static double log10(int value) 
	{
		double x = Math.log(value);
		
		return x;
	}

	public static int nextPowerOfTwo(int value) 
	{
		return (int) Math.pow(2.0, Math.ceil(log10(value) / log10(2)));
	}
	
	

}
