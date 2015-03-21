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

	@Override
	public void create() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}
}
