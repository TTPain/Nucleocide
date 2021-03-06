package com.hyprgloo.nucleocide.common;

import org.newdawn.slick.Color;

public final class Util {

	private Util(){}
	
	public static Color colorFromHSV(float h, float s, float v){
		java.awt.Color colorJava = java.awt.Color.getHSBColor(h, s, v);
		return new Color(colorJava.getRed(), colorJava.getGreen(), colorJava.getBlue());
	}
	
	// Source: https://github.com/Unity-Technologies/UnityCsReference/blob/master/Runtime/Export/Math/Mathf.cs#L255
	public static float lerpSmooth(float arg1, float arg2, float alpha){
		alpha = -2f * alpha * alpha * alpha + 3f * alpha * alpha;
		return arg2 * alpha + arg1 * (1f - alpha);
	}
	
}
