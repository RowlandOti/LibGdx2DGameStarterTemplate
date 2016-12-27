package com.rowland.tests.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.magnetideas.loaders.ShaderLoader;

/**
 * A port of ShaderLesson3 from lwjgl-basics to LibGDX:
 * https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson3
 *
 * @author davedes
 */
public class ShaderLesson3 implements ApplicationListener {

    //Minor differences:
    //LibGDX Position attribute is a vec4
    //u_projView is called u_projTrans
    //we need to set ShaderProgram.pedantic to false
    //LibGDX uses lower-left as origin (0, 0)
    //TexCoord attribute requires "0" appended at end to denote GL_TEXTURE0
    //ShaderProgram.TEXCOORD_ATTRIBUTE+"0"
    //It's wise to use LOWP when possible in GL ES, for values between 0-1
    //In LibGDX ShaderProgram uses begin() and end()


    Texture tex;
    SpriteBatch batch;
    OrthographicCamera camera;
    ShaderProgram shader;

    @Override
    public void create() {


        //the texture does not matter since we will ignore it anyways
        tex = new Texture(Gdx.files.internal("data/yoyo_sprite2.png"));

        if (shader == null)
            shader = ShaderLoader.fromFile("VignetteVertexShader", "VignetteFragmentShader");

        batch = new SpriteBatch(1000, shader);
        batch.setShader(shader);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 32f, 18f);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        batch.setProjectionMatrix(camera.combined);

        //bind the shader, then set the uniform, then unbind the shader
        shader.begin();
        shader.setUniformf("resolution", width, height);
        shader.end();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        shader.dispose();
        tex.dispose();
    }
}