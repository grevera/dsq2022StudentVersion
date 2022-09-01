package com.dsq2022.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
/** many skins can be found at https://github.com/czyzby/gdx-skins.
 *  https://gamedev.stackexchange.com/questions/151188/libgdx-table-layout
 */
public class PrefController extends ScreenAdapter {
    private DSQ2022Game game;
    private Stage stage;
    //-----------------------------------------------------------------------
    public PrefController ( DSQ2022Game game ) {
        this.game = game;
    }
    //-----------------------------------------------------------------------
    /**
     *  <pre>
     *      stage
     *          container
     *              table
     *                  turn
     *                  options
     *                  checkbox
     *                  checkbox
     *                  checkbox
     *                  button | button
     *  </pre>
     */
    @Override public void show ( ) {
        System.out.println( "show called" );
        stage = new Stage();
        //skin = new Skin( Gdx.files.internal("assets/skins/arcade/arcade-ui.json") );  //doesn't work!
        //skin = new Skin( Gdx.files.internal("assets/skins/comic/comic-ui.json") ); //requires a lighter background color
        //skin = new Skin( Gdx.files.internal("assets/skins/default/uiskin.json") );
        //skin = new Skin( Gdx.files.internal("assets/skins/metal/metal-ui.json") ); //requires a lighter background color
        Skin skin = new Skin( Gdx.files.internal("skins/neon/neon-ui.json") );
        //skin = new Skin( Gdx.files.internal("assets/skins/star-soldier/star-soldier-ui.json") );

        Container<Table> tableContainer = new Container<>();
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cw = sw * 0.9f;
        float ch = sh * 0.9f;
        tableContainer.setSize( cw, ch );
        tableContainer.setPosition( (sw - cw) / 2.0f, (sh - ch) / 2.0f );
        tableContainer.fillX();

        Table table = new Table( skin );
        table.pad( 100 );

        Label turnLabel = new Label( this.game.b.bluesTurn ? "It's blue's turn." : "It's red's turn.", skin );
        turnLabel.setAlignment( Align.left );
        //turnLabel.setFontScale( 2 );

        Label topLabel = new Label( "Options ...", skin );
        topLabel.setAlignment( Align.left );

        final CheckBox soundOnCB = new CheckBox( "sound on", skin );
        soundOnCB.addListener( new ChangeListener ( ) {
            @Override public void changed ( ChangeEvent event, Actor actor ) {
                game.soundOn = soundOnCB.isChecked();
            }
        });
        soundOnCB.align( Align.left );
        soundOnCB.padLeft( 50 );
        soundOnCB.setChecked( this.game.soundOn );

        final CheckBox nameOnCB = new CheckBox( "name on",  skin );
        nameOnCB.addListener( new ChangeListener ( ) {
            @Override public void changed ( ChangeEvent event, Actor actor ) {
                game.nameOn = nameOnCB.isChecked();
            }
        });
        nameOnCB.align( Align.left );
        nameOnCB.padLeft( 50 );
        nameOnCB.setChecked( this.game.nameOn );

        final CheckBox rankOnCB = new CheckBox( "rank on",  skin );
        rankOnCB.addListener( new ChangeListener ( ) {
            @Override public void changed ( ChangeEvent event, Actor actor ) {
                game.rankOn = rankOnCB.isChecked();
            }
        });
        rankOnCB.align( Align.left );
        rankOnCB.padLeft( 50 );
        rankOnCB.setChecked( this.game.rankOn );

        Table buttonTable = new Table( skin );

        TextButton newGameB = new TextButton( "New Game", skin );
        newGameB.addListener( new ChangeListener ( ) {
            @Override public void changed ( ChangeEvent event, Actor actor ) {
                System.out.println( "new game clicked" );
                Board b = new Board();
                game.b = b;
                game.setScreen( game.mc );
            }
        });

        TextButton backB = new TextButton( "Back", skin );
        backB.addListener( new ChangeListener ( ) {
            @Override public void changed ( ChangeEvent event, Actor actor ) {
                System.out.println( "back clicked" );
                game.setScreen( game.mc );
            }
        });

        table.row().colspan( 3 ).expandX().fillX();
        table.add( turnLabel ).fillX();

        table.row().colspan( 3 ).expandX().fillX();
        table.add( topLabel ).fillX();

        table.row().colspan( 3 ).expandX().fillX();
        table.add().size( 50, 50 );

        table.row().expandX().fillX().pad( 10 );
        table.add( soundOnCB ).expandX().fillX();

        table.row().expandX().fillX().pad( 10 );
        table.add( nameOnCB ).expandX().fillX();

        table.row().expandX().fillX().pad( 10 );
        table.add( rankOnCB ).expandX().fillX();

        table.row().expandX().fillX();
        table.add( buttonTable ).colspan( 3 );

        buttonTable.pad( 50 );
        buttonTable.row().fillX().expandX();
        buttonTable.add( backB ).width( cw/5.0f );
        buttonTable.add().width( cw/5.0f );
        buttonTable.add( newGameB ).width( cw/5.0f );

        tableContainer.setActor( table );
        stage.addActor( tableContainer );
        Gdx.input.setInputProcessor( stage );
    }
    //-----------------------------------------------------------------------
    @Override public void render ( float delta ) {
        Gdx.gl.glClearColor( 0,0,0.2f,1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        stage.act();
        stage.draw();
    }
    //-----------------------------------------------------------------------
    @Override public void hide ( ) {
        Gdx.input.setInputProcessor( null );
    }
}
