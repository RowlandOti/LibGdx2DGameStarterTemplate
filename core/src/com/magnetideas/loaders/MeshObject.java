package com.magnetideas.loaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * @author Rowland
 *
 */
public class MeshObject {

	private Mesh mesh;
	private float vertices[];
	private Array<Float> vertexArray;
	private short indicesArray[];
	private Color clearColor;
	public Mesh fullscreenQuadMesh;

	public MeshObject()
	{
		vertexArray = new Array<Float>();
		//clearColor = new Color(0Xbeaf7bff);
		clearColor = new Color(72.0f/255f, 53.0f/255f, 83.0f/255f, 0f);
	}

	public void create()
	{
	    vertices = new float[vertexArray.size];

	    for(int i=0;i<vertices.length;i++)
	    {
	        vertices[i] = vertexArray.get(i).floatValue();
	    }

	    mesh = new Mesh(true, vertices.length/9, indicesArray.length,
	    		new VertexAttribute(Usage.Position,3,"a_position"),
	    		new VertexAttribute( Usage.TextureCoordinates, 2, "a_texCoords"),
	    		new VertexAttribute( Usage.ColorUnpacked, 4, "a_color"));

	    mesh.setVertices(vertices);
	    mesh.setIndices(indicesArray);
	}

	public void render(ShaderProgram shader){
	    mesh.render(shader,GL20.GL_TRIANGLE_STRIP,0,indicesArray.length);
	}

	protected void addVertex(float x,float y,float z, float u, float v, Color color)
	{
	    vertexArray.add(new Float(x));    //x coordinate
	    vertexArray.add(new Float(y));    //y cordinate
	    vertexArray.add(new Float(z));    //z cordinate
	    vertexArray.add(new Float(u));    //u texture coordinate
	    vertexArray.add(new Float(v));    //v texture coordinate
	    vertexArray.add(new Float(color.r));    //r component of rgb color
	    vertexArray.add(new Float(color.g));    //g component of rgb color
	    vertexArray.add(new Float(color.b));    //b component of rgb color
	    vertexArray.add(new Float(color.a));    //a component of rgb color
	}

	protected void addIndices(short[] indeces)
	{
	    this.indicesArray = indeces;
	}

	protected float[] createTriangle(TextureRegion region, int x, int y, int z, int width, int height, Color color)
	{
        float w = region.getRegionWidth();
        float h = region.getRegionHeight();

        float c = color.toFloatBits();
        float u = region.getU();
        float v = region.getV();
        float u2 = region.getU2();
        float v2 = region.getV2();

		return new float[] {
				x, y, c, u, v,
				x, y + h, c, u, v2,
				x + w, y, c, u2, v,
				x, y, c, u, v };
}

	public void setVertices(Mesh ObjectMesh, TextureRegion region, Color clearColor)
	{
		float[] vertices = {
				-1f, -1f, 0, region.getU(), region.getV2(),clearColor.r,clearColor.g,clearColor.b,clearColor.a, // bottom left
	            1f, -1f, 0, region.getU2(), region.getV2(),clearColor.r,clearColor.g,clearColor.b,clearColor.a, // bottom right
	            1f, 1f, 0, region.getU2(), region.getV(),clearColor.r,clearColor.g,clearColor.b,clearColor.a, // top right
	            -1f, 1f, 0, region.getU(), region.getV(),clearColor.r,clearColor.g,clearColor.b,clearColor.a,  // top left
	            };

		ObjectMesh.setVertices(vertices);

	}

	public Mesh BuildMesh(Mesh ObjectMesh, short[] SourceIndices, float[] vertices)
	{

	    ObjectMesh.setVertices(vertices);
	    ObjectMesh.setIndices(SourceIndices);

	    return ObjectMesh;
	}

	public void createFullScreenQuad(TextureRegion region)
	{

	      float[] verts = new float[36];
	      int i = 0;

	      // bottom left
	      verts[i++] = -1f; // x1
	      verts[i++] = -1f; // y1
	      verts[i++] = 0;  // z1
	      verts[i++] = region.getU(); // u
	      verts[i++] = region.getV2(); // v2
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;

	      // bottom right
	      verts[i++] = 1f; // x2
	      verts[i++] = -1f; // y2
	      verts[i++] = 0;  // z2
	      verts[i++] = region.getU2(); // u2
	      verts[i++] = region.getV2(); // v2
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;

	      // top right
	      verts[i++] = 1f; // x3
	      verts[i++] = 1f; // y3
	      verts[i++] = 0;  // z3
	      verts[i++] = region.getU2(); // u2
	      verts[i++] = region.getV(); // v
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;

	      // top left
	      verts[i++] = -1f; // x4
	      verts[i++] = 1f; // y4
	      verts[i++] = 0;  // z4
	      verts[i++] = region.getU(); // u
	      verts[i++] = region.getV(); // v
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;



	      // static mesh with 4 vertices and no indices
	      fullscreenQuadMesh = createMesh(false, 4, 6);

	      fullscreenQuadMesh.setVertices(verts);
	      fullscreenQuadMesh.setIndices(new short[] {
	    		  0, 1, 2, // first triangle (bottom left - top left - top right)
	    		  2, 3, 0 // second triangle (bottom left - top right - bottom right)
	    		  });



	   }

	public Mesh createMesh(boolean isStatic, int maxVertices, int maxIndices)
	{
		// static mesh with 4 vertices and no indices
		Mesh ObjectMesh = new Mesh(isStatic, maxVertices, maxIndices,
				new VertexAttribute( Usage.Position, 3, "a_position"),
				new VertexAttribute( Usage.TextureCoordinates, 2, "a_texCoords"),
				new VertexAttribute( Usage.ColorUnpacked, 4, "a_color")
				);

		return ObjectMesh;

	}

	public void  rebuildMeshUVtoTextureRegion(Mesh ObjectMesh, TextureRegion UVMapPos)
	{
		int numFloats = ObjectMesh.getNumVertices() * ObjectMesh.getVertexSize() / 4;
	    float[] vertices = new float[numFloats];
	    ObjectMesh.getVertices(vertices);

	    int numIndices = ObjectMesh.getNumIndices();
	    short SourceIndices[] = new short[numIndices];
	    ObjectMesh.getIndices(SourceIndices);


		final int floatsPerVertex = 5;
	    int TimesToLoop = ((vertices.length) /floatsPerVertex);

	    float previousU;
	    float previousV;

	    float FullMapHeight = UVMapPos.getTexture().getHeight();
	    float FullMapWidth  = UVMapPos.getTexture().getWidth();
	    float NewMapWidth = UVMapPos.getRegionWidth();
	    float NewMapHeight = UVMapPos.getRegionHeight();

	    float FullMapUPercent;
	    float FullMapVPercent;

	    for (int i = 0; i < TimesToLoop; i++)
	    {
	        previousU = (vertices[(i * floatsPerVertex) + 3]);
	        previousV = (vertices[(i * floatsPerVertex) + 4]);
	        FullMapUPercent = previousU / FullMapWidth;
	        FullMapVPercent = previousV / FullMapHeight;
	        vertices[(i * floatsPerVertex) + 3] = (NewMapWidth * FullMapUPercent) + UVMapPos.getU(); //New U
	        vertices[(i * floatsPerVertex) + 4] = (NewMapHeight * FullMapVPercent) + UVMapPos.getV();//New V
	    }
	}

	 /**
	    * Creates a mesh which will draw a repeated texture. To be used whenever we are dealing with a region of a TextureAtlas
	    * @param vertices For re-use, the vertices to use for the mesh. If insufficient are provided, a new array will be constructed
	    * @param region The AtlasRegion to use
	    * @param scale The factor by which we want to repeat our texture
	    * @param x
	    * @param y
	    * @param originX
	    * @param originY
	    * @param width
	    * @param height
	    * @param scaleX Scale by which we want to expand the mesh on X
	    * @param scaleY Scale by which we want to expand the mesh on Y
	    * @param rotation Degrees of rotation for mesh
	    * @param colorBase The initial base color
	    * @param alpha The alpha by which to mult the colorBase by; resulting in the end interpolation target.
	    * @return
	    */
	public static final float[] constructEndingMesh(float[] vertices, AtlasRegion region, int scale, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, Color colorT, float alpha)
	{
	    if(scale * 20 > vertices.length)

	        vertices = new float[20*scale];

	    float color = colorT.toFloatBits();
	    float colorE;

	    int idx = 0;

	    // bottom left and top right corner points relative to origin
	    final float worldOriginX = x + originX;
	    final float worldOriginY = y + originY;
	    float fx = -originX;
	    float fy = -originY;
	    float fx2 = width - originX;
	    float fy2 = height - originY;

	    // scale
	    if (scaleX != 1 || scaleY != 1) {
	        fx *= scaleX;
	        fy *= scaleY;
	        fx2 *= scaleX;
	        fy2 *= scaleY;
	    }

	    // construct corner points, start from top left and go counter clockwise
	    final float p1x = fx;
	    final float p1y = fy;
	    final float p2x = fx;
	    final float p2y = fy2;
	    final float p3x = fx2;
	    final float p3y = fy2;
	    final float p4x = fx2;
	    final float p4y = fy;

	    float Fx1;
	    float Fy1;
	    float Fx2;
	    float Fy2;
	    float Fx3;
	    float Fy3;
	    float Fx4;
	    float Fy4;

	    // rotate
	    if (rotation != 0)
	    {
	        final float cos = MathUtils.cosDeg(rotation);
	        final float sin = MathUtils.sinDeg(rotation);

	        Fx1 = cos * p1x - sin * p1y;
	        Fy1 = sin * p1x + cos * p1y;

	        Fx2 = cos * p2x - sin * p2y;
	        Fy2 = sin * p2x + cos * p2y;

	        Fx3 = cos * p3x - sin * p3y;
	        Fy3 = sin * p3x + cos * p3y;

	        Fx4 = Fx1 + (Fx3 - Fx2);
	        Fy4 = Fy3 - (Fy2 - Fy1);
	    } else
	    {
	        Fx1 = p1x;
	        Fy1 = p1y;

	        Fx2 = p2x;
	        Fy2 = p2y;

	        Fx3 = p3x;
	        Fy3 = p3y;

	        Fx4 = p4x;
	        Fy4 = p4y;
	    }

	    float x1 = Fx1 + worldOriginX;
	    float y1 = Fy1 + worldOriginY;
	    float x2 = Fx2 + worldOriginX;
	    float y2 = Fy2 + worldOriginY;

	    float scaleX2 = ((Fx2-Fx1) / scale);
	    float scaleY2 = ((Fy2-Fy1) / scale);
	    float scaleX3 = ((Fx3-Fx4) / scale);
	    float scaleY3 = ((Fy3-Fy4) / scale);
	    float scaleAlpha = (colorT.a - (colorT.a*alpha)) / scale;

	    float x3 = x1;
	    float y3 = y1;
	    float x4 = x2;
	    float y4 = y2;

	    final float u = region.getU();
	    final float v = region.getV();
	    final float u2 = region.getU2();
	    final float v2 = region.getV2();

	    for (int i = 1; i <= scale; i++)
	    {
	        x1 = Fx1 + scaleX2*(i-1) + worldOriginX;
	        y1 = Fy1 + scaleY2*(i-1) + worldOriginY;
	        x2 = Fx1 + scaleX2*i + worldOriginX;
	        y2 = Fy1 + scaleY2*i + worldOriginY;

	        x3 = Fx4 + scaleX3*i + worldOriginX;
	        y3 = Fy4 + scaleY3*i + worldOriginY;
	        x4 = Fx4 + scaleX3*(i-1) + worldOriginX;
	        y4 = Fy4 + scaleY3*(i-1) + worldOriginY;

	        color = colorT.toFloatBits();
	        colorT.a-=scaleAlpha;
	        colorE = colorT.toFloatBits();

	        vertices[idx++] = x1;
	        vertices[idx++] = y1;
	        vertices[idx++] = color;
	        vertices[idx++] = u;
	        vertices[idx++] = v;

	        vertices[idx++] = x2;
	        vertices[idx++] = y2;
	        vertices[idx++] = colorE;
	        vertices[idx++] = u;
	        vertices[idx++] = v2;

	        vertices[idx++] = x3;
	        vertices[idx++] = y3;
	        vertices[idx++] = colorE;
	        vertices[idx++] = u2;
	        vertices[idx++] = v2;

	        vertices[idx++] = x4;
	        vertices[idx++] = y4;
	        vertices[idx++] = color;
	        vertices[idx++] = u2;
	        vertices[idx++] = v;

	    }

	    return vertices;
	}
}
