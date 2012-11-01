precision mediump float;

uniform sampler2D quadSampler;

varying vec4 fragColor;
varying vec2 texCoord0;

void main() {
	gl_FragColor = texture2D(quadSampler, texCoord0);
}