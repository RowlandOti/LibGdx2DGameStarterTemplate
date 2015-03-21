#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
	#else
	#define LOWP
	#endif

	varying LOWP vec4 v_color;
	varying vec2 v_texCoords;
	varying vec2 t0c, t1c, t2c, t3c;

	uniform mat4 u_projTrans;
	uniform sampler2D u_texture0;
	uniform sampler2D u_texture1;
	uniform sampler2D u_texture2;
	uniform sampler2D u_texture3;

	void main()
	{
	  vec4 texel0, texel1, texel2, texel3;

	  texel0 = texture2D(u_texture0, t0c);
	  texel1 = texture2D(u_texture1, t1c);
	  texel2 = texture2D(u_texture2, t2c);
	  texel3 = texture2D(u_texture3, t3c);

	  gl_FragColor = texel0;
	  gl_FragColor = vec4(texel1.a) * texel1 + vec4(1.0 - texel1.a) * gl_FragColor;
	  gl_FragColor = vec4(texel2.a) * texel2 + vec4(1.0 - texel2.a) * gl_FragColor;
	  gl_FragColor = vec4(texel3.a) * texel3 + vec4(1.0 - texel3.a) * gl_FragColor;

	}