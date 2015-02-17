/**
 * 
 */
package com.rowland.tests;

/**
 * @author Rowland
 *
 */
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;
import com.magnetideas.platforms.PlatformResolver;

public abstract class GdxSample extends InputAdapter implements
		ApplicationListener {
	protected static PlatformResolver m_platformResolver = null;

	public static PlatformResolver getPlatformResolver() {
		return m_platformResolver;
	}

	public static void setPlatformResolver(PlatformResolver platformResolver) {
		m_platformResolver = platformResolver;
	}

	public void create() {
	}

	public void resume() {
	}

	public void render() {
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void dispose() {
	}
}
