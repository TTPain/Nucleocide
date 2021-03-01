#define PI 3.14159

uniform sampler2D texture1;
uniform vec2 resolution;
uniform vec4 colorLight;

float atan2(float y, float x){
	return abs(x) > abs(y) ? atan(y, x) : PI / 2.0 - atan(x, y);
}

// Shadow casting algorithm derived (loosely) from: https://github.com/mattdesl/lwjgl-basics/wiki/2D-Pixel-Perfect-Shadows
void main(){
	vec4 light = colorLight;
	
	vec2 normalLight = gl_TexCoord[0].xy * 2.0 - 1.0;
	vec2 dirLight = normalize(normalLight);
	float rLight = length(normalLight);

	for(float rTest = 0.0; rTest < rLight; rTest += 0.5 / resolution.y){
		vec2 coordTest = vec2(0.5) + (dirLight * rTest / 2.0);
		vec4 colorTest = texture2D(texture1, coordTest);
		light = min(light, 1.0 - colorTest);
	}
	if(rLight > 1.0) light = vec4(0.0);

	gl_FragColor = vec4(light.r, light.g, light.b, min(light.a, 1.0 - rLight));
}
