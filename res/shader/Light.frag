#version 120

#define PI 3.14159
#define LIGHTS_MAX 32

struct Light{
	float range;
	vec4 color;
	vec2 location;
};

uniform sampler2D texture1;
uniform sampler2D textureOcclusion;
uniform sampler2D textureNormal;
uniform vec2 resolution;

uniform int lightsSize;
uniform Light[LIGHTS_MAX] lights;

float map(float x, float inMin, float inMax, float outMin, float outMax){
	return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
}

// Shadow casting algorithm derived (loosely) from: https://github.com/mattdesl/lwjgl-basics/wiki/2D-Pixel-Perfect-Shadows
void main(){
	vec4 colorFinal = vec4(0.0);
	
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
				vec4 colorTest = texture2D(textureOcclusion, coordTest);
				colorLightVolume = min(colorLightVolume, 1.0 - colorTest);
			}
			
			if(wDistanceLight > light.range) colorLightVolume = vec4(0.0);
			colorLightVolume.a = min(colorLightVolume.a, (light.range - wDistanceLight) / light.range);
		
			// === CALCULATE NORMAL HIGHLIGHTS ============================================
		
			vec2 normalSurface = texture2D(textureNormal, sLocationPixel).rg * 2.0 - 1.0;
			normalSurface.y *= -1.0;
			float multiplierLightSurface = dot((wLocationLight - wLocationPixel) / light.range, normalSurface) / 2.0 + 0.5;
		
			// === FINAL COLOR ============================================
			
			vec4 colorAdd = vec4(colorLightVolume.rgb, multiplierLightSurface * colorLightVolume.a);
			//colorAdd.rgb = colorAdd.rgb * colorAdd.a;
			colorAdd = clamp(colorAdd, 0.0, 1.0);
			
			colorFinal = clamp(colorFinal + colorAdd, 0.0, 1.0);
		}
	}
	
	gl_FragColor = max(colorFinal, texture2D(texture1, gl_TexCoord[0].xy));
}
