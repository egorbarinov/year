package ru.geekbrains.year.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.geekbrains.year.YearGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = YearGame.WIDTH;
		config.height = YearGame.HEIGHT;
		new LwjglApplication(new YearGame(), config);
	}
}
