#define PI 3.14159

uniform sampler2D texture1;
uniform sampler2D textureNormal;
uniform vec2 resolution;

uniform float rangeLight;
uniform vec4 colorLight;
uniform vec2 coordLight;

const float LIGHT_INTENSITY = 1.5;

float map(float x, float inMin, float inMax, float outMin, float outMax){
	return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
}

// Shadow casting algorithm derived (loosely) from: https://github.com/mattdesl/lwjgl-basics/wiki/2D-Pixel-Perfect-Shadows
void main(){
	
	vec2 sLocationLight = vec2(coordLight.x, 1.0 - coordLight.y);
	
	vec2 sLocationPixel = gl_TexCoord[0].xy;
	
	vec2 wLocationLight = vec2(0.0);
	wLocationLight.x = map(coordLight.x, 0.0, 1.0, -resolution.x / 2.0, resolution.x / 2.0);
	wLocationLight.y = map(coordLight.y, 0.0, 1.0, -resolution.y / 2.0, resolution.y / 2.0);
	
	vec2 wLocationPixel = vec2(0.0);
	wLocationPixel.x = map(gl_TexCoord[0].x, 0.0, 1.0, -resolution.x / 2.0, resolution.x / 2.0);
	wLocationPixel.y = map(gl_TexCoord[0].y, 1.0, 0.0, -resolution.y / 2.0, resolution.y / 2.0);
	
	if(abs(wLocationLight.x - wLocationPixel.x) > rangeLight || abs(wLocationLight.y - wLocationPixel.y) > rangeLight){
		//gl_FragColor = vec4(1.0, 0.0, 0.0, 0.5);
		gl_FragColor = vec4(0.0);
	}else{
	
		// === CALCULATE LIGHT VOLUME ============================================
		
		vec4 colorLightVolume = colorLight;
		
		vec2 sNormalLight = normalize(sLocationLight - sLocationPixel);
		float sDistanceLight = length(sLocationLight - sLocationPixel);
		float wDistanceLight = length(wLocationLight - wLocationPixel);
	
		// Ray-tracing from light source to current pixel for shadows
		for(float sDistanceTest = 0.0; sDistanceTest < sDistanceLight; sDistanceTest += 1.0 / resolution.x){
			vec2 coordTest = sLocationLight - (sNormalLight * sDistanceTest);
			vec4 colorTest = texture2D(texture1, coordTest);
			colorLightVolume = min(colorLightVolume, 1.0 - colorTest);
		}
		
		if(wDistanceLight > rangeLight) colorLightVolume = vec4(0.0);
		colorLightVolume.a = min(colorLightVolume.a, (rangeLight - wDistanceLight) / rangeLight);
	
		// === CALCULATE NORMAL HIGHLIGHTS ============================================
	
		vec2 normalSurface = texture2D(textureNormal, sLocationPixel).rg * 2.0 - 1.0;
		normalSurface.y *= -1.0;
		float multiplierLightSurface = dot((wLocationLight - wLocationPixel) / rangeLight, normalSurface) / 2.0 + 0.5;
	
		// === FINAL COLOR ============================================
		
		gl_FragColor = vec4(colorLightVolume.rgb, multiplierLightSurface * colorLightVolume.a * LIGHT_INTENSITY);
		
	}
}
