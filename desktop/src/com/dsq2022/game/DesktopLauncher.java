package com.dsq2022.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.dsq2022.game.DSQ2022Game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
/** libgdx desktop (linux, windows, macos) launcher for the game. */
public class DesktopLauncher {
	public static void main ( String[] arg ) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS( 60 );
		config.setTitle( "Dou Shou Qi Game" );
		config.setWindowedMode( 600, 800 );
		new Lwjgl3Application( new DSQ2022Game(), config );
	}
}
