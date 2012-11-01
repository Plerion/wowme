precision mediump float;

uniform sampler2D quadSampler;

varying vec4 fragColor;
varying vec2 texCoord0;


vec4 sampleDiffs(vec2 texCoord)
{
	float stepX = 1.0 / 256.0;
	float stepY = 1.0 / 256.0;

	vec4 c1 = texture2D(quadSampler, texCoord);
	vec4 c2 = texture2D(quadSampler, vec2(texCoord.x, texCoord.y + stepY));
	vec4 c3 = texture2D(quadSampler, vec2(texCoord.x, texCoord.y - stepY));
	vec4 c4 = texture2D(quadSampler, vec2(texCoord.x + stepX, texCoord.y));
	vec4 c5 = texture2D(quadSampler, vec2(texCoord.x - stepX, texCoord.y));

	return (c1 + c2 + c3 + c4 + c5) / 5.0;
}

void main() {
	vec4 texColor = texture2D(quadSampler, texCoord0);//sampleDiffs(texCoord0);
	gl_FragColor = texColor * fragColor;
}