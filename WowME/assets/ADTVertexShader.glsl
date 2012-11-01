attribute vec4 vPosition0;
attribute vec4 vNormal0;

uniform mat4 worldViewProj;

varying vec3 vertexNormal;

void main() {
	vertexNormal = vNormal0.xyz;
	vec4 tmpPos = vec4(vPosition0.xyz, 1.0);
	gl_Position = worldViewProj * tmpPos;
}