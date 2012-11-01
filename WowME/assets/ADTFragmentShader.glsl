precision highp float;

varying vec3 vertexNormal;

const vec3 sunDirection = vec3(1, 1, -1);
const vec4 diffuseLight = vec4(0.7, 0.5, 0.6, 1.0);

void main() {
	float light = dot(vertexNormal, -normalize(sunDirection));
	if(light < 0.0)
		light = 0.0;
	if(light > 0.5)
		 light = 0.5 + (light - 0.5) * 0.65;
		
	vec4 diffuse = diffuseLight * light;
	diffuse += vec4(0.2, 0.2, 0.2, 0); 
	diffuse = clamp(diffuse, 0.0, 1.0);
		
	gl_FragColor = vec4(diffuse.rgb, 1);
}