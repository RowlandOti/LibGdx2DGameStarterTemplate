#ifdef GL_ES

#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

//texture 0
uniform sampler2D u_texture;

//our screen resolution, set from Java whenever the display is resized
uniform vec2 resolution;
// radius of blur
uniform float radius;
// the direction of blur
uniform vec2 dir;

//in attributes from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

//RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.75;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.25;

//sepia colour, adjust to taste
const vec3 SEPIA = vec3(1.2, 1.0, 0.8); 

void main() {
	//sample our texture
	vec4 texColor = texture2D(u_texture, vTexCoord);
		
	//1. VIGNETTE
	
	//determine center position
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);
	
	//determine the vector length of the center position
	float len = length(position);
	
	//use smoothstep to create a smooth vignette
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	
	//apply the vignette with 50% opacity
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);
		
	//2. GRAYSCALE
	
	//convert to grayscale using NTSC conversion weights
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	
	//3. SEPIA
	
	//create our sepia tone from some constant value
	vec3 sepiaColor = vec3(gray) * SEPIA;
		
	//again we'll use mix so that the sepia effect is at 75%
	texColor.rgb = mix(texColor.rgb, sepiaColor, 0.75);


	//3. BLUR

	vec4 sum = vec4(0.0);
	vec2 tc = vTexCoord;
	float blur = radius/resolution.x; 
			 
	float hstep = dir.x;
    float vstep = dir.y;

	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.05;
	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.09;
	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.12;
	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.15;
	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.16;
	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.15;
	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.12;
	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.09;
	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.05;
	
	//gl_FragColor *=  vec4(sum.rgb, 1.0);
	//final colour, multiplied by vertex colour
	gl_FragColor = texColor * vColor *vec4(sum.rgb, 1.0);
}