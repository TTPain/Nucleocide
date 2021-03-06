#version 120

#define PI 3.14159
#define LIGHT_INTENSITY 1.5
#define LIGHTS_MAX 32

#define METALNESS_MAX 0.5

struct Light{
	float range;
	vec4 color;
	vec2 location;
};

uniform sampler2D texture1;
uniform sampler2D textureNormal;
uniform sampler2D textureMetalness;
uniform vec2 resolution;

uniform int lightsSize;
uniform Light[LIGHTS_MAX] lights;

float map(float x, float inMin, float inMax, float outMin, float outMax){
	return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
}

// Shadow casting algorithm derived (loosely) from: https://github.com/mattdesl/lwjgl-basics/wiki/2D-Pixel-Perfect-Shadows
void main(){
	vec4 colorFinal = vec4(0.0);
	float alphaFinal = 0.0;
	
	for(int indexLight = 0; indexLight < lightsSize; indexLight++){
		Light light = lights[indexLight];
		
		vec2 sLocationLight = vec2(light.location.x, 1.0 - light.location.y);
		
		vec2 sLocationPixel = gl_TexCoord[0].xy;
		
		vec2 wLocationLight = vec2(0.0);
		wLocationLight.x = map(light.location.x, 0.0, 1.0, -resolution.x / 2.0, resolution.x / 2.0);
		wLocationLight.y = map(light.location.y, 0.0, 1.0, -resolution.y / 2.0, resolution.y / 2.0);
		
		vec2 wLocationPixel = vec2(0.0);
		wLocationPixel.x = map(gl_TexCoord[0].x, 0.0, 1.0, -resolution.x / 2.0, resolution.x / 2.0);
		wLocationPixel.y = map(gl_TexCoord[0].y, 1.0, 0.0, -resolution.y / 2.0, resolution.y / 2.0);
		
		if(abs(wLocationLight.x - wLocationPixel.x) > light.range || abs(wLocationLight.y - wLocationPixel.y) > light.range){
			continue;
			//gl_FragColor = vec4(1.0, 0.0, 0.0, 0.5);
			//gl_FragColor = vec4(0.0);
		}else{
			// === CALCULATE LIGHT VOLUME ============================================
			
			vec4 colorLightVolume = light.color;
			
			vec2 sNormalLight = normalize(sLocationLight - sLocationPixel);
			float sDistanceLight = length(sLocationLight - sLocationPixel);
			float wDistanceLight = length(wLocationLight - wLocationPixel);
		
			// Ray-tracing from light source to current pixel for shadows
			for(float sDistanceTest = 0.0; sDistanceTest < sDistanceLight; sDistanceTest += 1.0 / resolution.x){
				vec2 coordTest = sLocationLight - (sNormalLight * sDistanceTest);
				vec4 colorTest = texture2D(texture1, coordTest);
				colorLightVolume = min(colorLightVolume, 1.0 - colorTest);
				if(colorLightVolume.a <= 0) break;
			}
			
			if(wDistanceLight > light.range) colorLightVolume = vec4(0.0);
			colorLightVolume.a = colorLightVolume.a * (light.range - wDistanceLight) / light.range;
		
			// === CALCULATE NORMAL HIGHLIGHTS ============================================
		
			vec2 normalSurface = texture2D(textureNormal, sLocationPixel).rg * 2.0 - 1.0;
			normalSurface.y *= -1.0;
			float multiplierLightSurface = dot((wLocationLight - wLocationPixel) / light.range, normalSurface) / 2.0 + 0.5;
		
			// === CALCULATE LIGHT-ABSORBING NORMAL HIGHLIGHTS ============================================
		
			vec4 absorbedLight = multiplierLightSurface * texture2D(texture1, sLocationPixel) * (((light.range * 0.5) - wDistanceLight) / (light.range * 0.5)) * 2.0;
			absorbedLight = clamp(absorbedLight, 0.0, 1.0);
			//absorbedLight = 3.0 * (absorbedLight - 0.5) + 0.5;
			//absorbedLight = clamp(absorbedLight, 0.0, 1.0);
			absorbedLight *= light.color;
			absorbedLight = clamp(absorbedLight, 0.0, 1.0);
		
			// === METALNESS ============================================
			
			float metalnessMultiplier = METALNESS_MAX * texture2D(textureMetalness, sLocationPixel).r + 1.0;
			
			// === FINAL COLOR ============================================
			
			vec4 colorAdd = vec4(colorLightVolume.rgb, multiplierLightSurface * colorLightVolume.a * LIGHT_INTENSITY);
			colorAdd += absorbedLight;
			colorAdd = clamp(colorAdd, 0.0, 1.0);
			colorAdd.a = metalnessMultiplier * (colorAdd.a - 0.5) + 0.5;
			colorAdd = clamp(colorAdd, 0.0, 1.0);
			
			float newAlpha = colorFinal.a + colorAdd.a;
			colorFinal = mix(colorFinal, colorAdd, colorAdd.a / (colorAdd.a + alphaFinal));
			colorFinal.a = newAlpha;
			colorFinal = clamp(colorFinal, 0.0, 1.0);
			
			alphaFinal += colorAdd.a;
		}
	}
	
	gl_FragColor = colorFinal;
}
