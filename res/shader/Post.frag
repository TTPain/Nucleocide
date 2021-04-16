#version 120

#define PI 3.14159
#define DISPLACE_INTENSITY 100.0

uniform sampler2D texture1;
uniform sampler2D textureDisplace;
uniform vec2 resolution;

float map(float x, float inMin, float inMax, float outMin, float outMax){
	return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
}

void main(){
	vec4 colorDisplace = texture2D(textureDisplace, gl_TexCoord[0].xy);
	vec2 locationDisplace = vec2((colorDisplace.r - 0.5) * 2.0 * DISPLACE_INTENSITY / resolution.x,
								 (colorDisplace.b - 0.5) * 2.0 * DISPLACE_INTENSITY / resolution.y);
	locationDisplace *= colorDisplace.a;

	gl_FragColor = texture2D(texture1, gl_TexCoord[0].xy + locationDisplace);
}
