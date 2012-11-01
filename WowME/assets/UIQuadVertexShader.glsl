attribute vec4 vPosition0;
attribute vec4 vColor0;
attribute vec2 vTexCoord0;

uniform mat4 matTranslation;

varying vec4 fragColor;
varying vec2 texCoord0;

void main() {

	vec4 tmpPos = vec4(vPosition0.xyz, 1.0);
	gl_Position = matTranslation * tmpPos;
	fragColor = vColor0;
	texCoord0 = vTexCoord0;
}