package com.magnetideas.parallax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * @author Rowland
 *
 */
public class ParallaxWidget extends Widget {

	   private static final int LAYER_COUNT = 2;
	   private Texture[] textures = new Texture[LAYER_COUNT];
	   private float[] scrollFactors = new float[LAYER_COUNT];
	   private float[] scrollAmounts = new float[LAYER_COUNT];


	   public ParallaxWidget(String path0, float factor0, String path1, float factor1)
	   {
	      scrollFactors[0] = factor0;
	      scrollFactors[1] = factor1;

	      scrollAmounts[0] = 0.0f;
	      scrollAmounts[1] = 0.0f;

	      textures[0] = new Texture(Gdx.files.internal(path0));
	      textures[1] = new Texture(Gdx.files.internal(path1));

	      textures[0].setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
	      textures[1].setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
	   }

	   @Override
	   public void draw(Batch batch, float parentAlpha)
	   {
	      super.draw(batch, parentAlpha);

	      Color color = getColor();
	      batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

	      for (int i = 0; i < LAYER_COUNT; ++i)
	      {
	         drawLayer(batch, i);
	      }
	   }

	   public void updateScroll(float value)
	   {
	      for (int i = 0; i < LAYER_COUNT; ++i)
	      {
	         scrollAmounts[i] = value * scrollFactors[i];
	      }
	   }

	   private void drawLayer(Batch batch, int index)
	   {
	      float x = getX();
	      float y = getY();

	      float w = getWidth();
	      float h = getHeight();

	      float th = textures[index].getHeight();
	      float tw = textures[index].getWidth() * h / th;

	      float u = scrollAmounts[index] / tw;
	      float v = 1.0f;

	      float u2 = u + (w / tw);
	      float v2 = 0.0f;

	      batch.draw(textures[index], x, y, w, h, u, v, u2, v2);
	   }
	}
