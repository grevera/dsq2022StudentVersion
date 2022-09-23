package com.dsq2022.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

/**
 * this is the controller for the libgdx-based version of the game.
 * Caution!
 * <pre>
 *   mouse/touch coordinate system         drawing/graphics coordinate system
 *   (0,0) ----------> +x                  +y
 *     |                                   ^
 *     |                                   |
 *     |                                   |
 *     |                                   |
 *     v                                   |
 *     +y                                (0,0) ----------> +x
 * </pre>
 */
public class MainController extends ScreenAdapter implements InputProcessor {
    private DSQ2022Game game;
    private enum GameSound { firstClick, secondClick, badMove, capture, winner, loser }
    private Sound clipCache[] = new Sound[ GameSound.values().length ];
    //-----------------------------------------------------------------------
    public MainController ( DSQ2022Game game ) {
        this.game = game;

        //loads sounds into clipCache
        //String path = "assets/sounds/";
        String path = "sounds/";
        for (GameSound s : GameSound.values()) {
            String str = null;
            switch (s) {
                case badMove:
                    str = "BananaImpact.mp3";     break;
                case capture:
                    str = "croc_chomp_x.mp3";     break;
                case firstClick:
                    str = "click_one.mp3";        break;
                case secondClick:
                    str = "Mousclik.mp3";         break;
                case winner:
                    str = "cheering.mp3";         break;
                case loser:
                    str = "pacman_dies_y.mp3";    break;
            }
            Sound clip = Gdx.audio.newSound( Gdx.files.internal(path+str) );
            clipCache[ s.ordinal() ] = clip;
        }
    }
    // ScreenAdapter methods ------------------------------------------------
    @Override public void show ( ) {
        Gdx.input.setInputProcessor( this );
    }
    //-----------------------------------------------------------------------
    @Override public void render ( float delta ) {
        this.game.mv.render();
    }
    //-----------------------------------------------------------------------
    @Override public void hide ( ) {
        Gdx.input.setInputProcessor( null );
    }
    // input processor methods ----------------------------------------------
    @Override public boolean keyDown ( int keycode ) { return false; }
    @Override public boolean keyUp   ( int keycode ) { return false; }

    @Override public boolean keyTyped ( char character ) {
        //'h' for hint or 's' for suggest
        if (character!='h' && character!='s')    return true;

        return true;
    }

    @Override public boolean touchDown ( int screenX, int screenY, int pointer, int button ) {
        return false;
    }

    /** this function mainly performs moves in response to clicks; it also
     *  displays the settings screen when the settings icon is clocked.
     */
    @Override public boolean touchUp ( int screenX, int screenY, int pointer, int button ) {
        System.out.println( "touchUp: x=" + screenX + ",y=" + screenY );
        //check for a touch on the settings icon
        System.out.println( "settings x=" + this.game.mv.cxSettings + ",y=" + this.game.mv.cySettings );
        if (this.game.mv.settingsPressed( screenX, screenY )) {
            System.out.println( "settings clicked" );
            this.game.setScreen( this.game.pc );
            return true;
        }
        //map click from screen coordinates to array coordinates
        int  r = (screenY - this.game.mv.yOff) / this.game.mv.size;
        int  c = (screenX - this.game.mv.xOff) / this.game.mv.size;

        //bounds check
        if (r < 0 || c < 0)    return true;
        if (r >= Board.fRows || c >= Board.fCols)    return true;

        System.out.println( "touchUp: r=" + r + ", c=" + c );
        if (this.game.fromR == -1) {  //are we are waiting for a click on the first (from) piece?
            this.game.fromR = r;
            this.game.fromC = c;
            if (this.game.soundOn)    clipCache[ GameSound.firstClick.ordinal() ].play();
            this.game.toR = this.game.toC = -1;
            //this.game.mv.render();
            this.game.savePrefs();
            return true;
        }

        if (this.game.toR == -1) {  //are we are waiting for the second click (to)?
            this.game.toR = r;
            this.game.toC = c;
            if (this.game.soundOn)    clipCache[ GameSound.secondClick.ordinal() ].play();
            //this.game.mv.render();  //board has not changed yet, but _possible_ destination was indicated
            if (!this.game.b.doMove( this.game.fromR, this.game.fromC, this.game.toR, this.game.toC )) {  //bad move?
                if (this.game.soundOn)    clipCache[ GameSound.badMove.ordinal() ].play();
            } else {  //good move
                if (this.game.b.moveWasCapture)
                    if (this.game.soundOn)    clipCache[ GameSound.capture.ordinal() ].play();
            }
            this.game.frameCount = 25;
            this.game.savePrefs();
            return true;
        }

        return true;
    }

    @Override public boolean touchDragged ( int screenX, int screenY, int pointer ) {
        return false;
    }

    @Override public boolean mouseMoved ( int screenX, int screenY ) {
        return false;
    }

    @Override public boolean scrolled ( float amountX, float amountY ) {
        return false;
    }
}
