#version 120

#define PI 3.14159
#define SIZE_MATERIAL 128

uniform sampler2D texture1;
uniform sampler2D texturePlasma;
uniform sampler2D textureMaterial;
uniform sampler2D textureOcclusion;
uniform vec2 resolution;
uniform vec2 locationCamera;

float map(float x, float inMin, float inMax, float outMin, float outMax){
	return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
}

void main(){
	vec4 colorInput = texture2D(texture1, gl_TexCoord[0].xy);

	vec2 coordMaterial = vec2(locationCamera.x / SIZE_MATERIAL, locationCamera.y / SIZE_MATERIAL);
	coordMaterial.x += map(gl_TexCoord[0].x, 0.0, 1.0, 0.0, resolution.x / SIZE_MATERIAL);
	coordMaterial.y += map(gl_TexCoord[0].y, 1.0, 0.0, 0.0, resolution.y / SIZE_MATERIAL);
	
	vec4 colorMaterial = texture2D(textureMaterial, coordMaterial);
	
	vec4 colorOcclusion = texture2D(textureOcclusion, gl_TexCoord[0].xy);

	gl_FragColor = colorInput * colorMaterial * (1.0 - colorOcclusion);
}
