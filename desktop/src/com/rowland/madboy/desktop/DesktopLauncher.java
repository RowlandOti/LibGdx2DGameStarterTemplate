package com.rowland.madboy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rowland.madboy.MadBoyGame;
import com.rowland.tests.GameListener;
import com.rowland.tests.MaskTest;
import com.rowland.tests.ScrollingBackground;
import com.rowland.tests.ShaderBlurTest;
import com.rowland.tests.SmoothCamTest;
import com.rowland.tests.screen.ShaderLesson3;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Mad Boy";
        config.width = 960;
        config.height = 540;

        //new LwjglApplication(new ScrollingBackground(), config);
        //new LwjglApplication(new GameListener(), config);
        // new LwjglApplication(new ShaderBlurTest(), config);
        //new LwjglApplication(new SmoothCamTest(), config);
        //new LwjglApplication(new MaskTest(), config);
        //new LwjglApplication(new ShaderLesson3(), config);
        new LwjglApplication(new MadBoyGame(), config);
    }
}
