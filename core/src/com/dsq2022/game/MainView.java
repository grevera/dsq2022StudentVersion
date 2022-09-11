package com.dsq2022.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
/**
 * this is the view for the libgdx-based version of the game.
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
public class MainView {
    private DSQ2022Game game;
    private BitmapFont font;
    private float stringWidth, stringHeight;
    private SpriteBatch sb;
    private SpriteBatch sb2;
    private ShapeRenderer sr;
    private Texture piece[];

    private Texture settings;
    public int cxSettings, cySettings;

    public int size, xOff, yOff;
    //-----------------------------------------------------------------------
    public MainView ( DSQ2022Game game ) {
        this.game = game;

        this.font = new BitmapFont();  //use libGDX's default Arial font
        GlyphLayout layout = new GlyphLayout();
        layout.setText( this.font, "X" );
        this.stringWidth = layout.width;
        this.stringHeight = layout.height;

        this.sb = new SpriteBatch();
        this.sb2 = new SpriteBatch();
        this.sr = new ShapeRenderer();

        this.piece = new Texture[ Piece.values().length ];  //images of various pieces
        //load the images for the (moveable) board pieces
        piece[ Piece.rbNone.ordinal()    ] = null;

        try {
            piece[Piece.rRat.ordinal()] = new Texture( "graphics/rRat.png" );
            piece[Piece.rCat.ordinal()] = new Texture( "graphics/rCat.png" );
            piece[Piece.rDog.ordinal()] = new Texture( "graphics/rDog.png" );
            piece[Piece.rWolf.ordinal()] = new Texture( "graphics/rWolf.png" );
            piece[Piece.rLeopard.ordinal()] = new Texture( "graphics/rLeopard.png" );
            piece[Piece.rTiger.ordinal()] = new Texture( "graphics/rTiger.png" );
            piece[Piece.rLion.ordinal()] = new Texture( "graphics/rLion.png" );
            piece[Piece.rElephant.ordinal()] = new Texture( "graphics/rElephant.png" );

            piece[Piece.bRat.ordinal()] = new Texture( "graphics/bRat.png" );
            piece[Piece.bCat.ordinal()] = new Texture( "graphics/bCat.png" );
            piece[Piece.bDog.ordinal()] = new Texture( "graphics/bDog.png" );
            piece[Piece.bWolf.ordinal()] = new Texture( "graphics/bWolf.png" );
            piece[Piece.bLeopard.ordinal()] = new Texture( "graphics/bLeopard.png" );
            piece[Piece.bTiger.ordinal()] = new Texture( "graphics/bTiger.png" );
            piece[Piece.bLion.ordinal()] = new Texture( "graphics/bLion.png" );
            piece[Piece.bElephant.ordinal()] = new Texture( "graphics/bElephant.png" );
        } catch (Exception e) {
            System.out.println( "Error: " + e.getMessage() );
        }

        for (Texture t : piece) {
            if (t != null)
                System.out.println( t.getWidth() + "x" + t.getHeight() );
        }

        font.setColor( Color.LIGHT_GRAY );
        this.settings = new Texture( "graphics/settings.png" );
        this.cxSettings = this.cySettings = -1;
    }
    //-----------------------------------------------------------------------
    /** note that this function is called repeatedly (30 to 60 fps). this is
     *  different from "ordinary" paint methods which are only called when
     *  necessary.
     */
    public void render ( ) {
        Gdx.graphics.setTitle( this.game.b.bluesTurn ? "Dou Shou Qi (blue's turn)"
                                                     : "Dou Shou Qi (red's turn)" );
        //System.out.println( "render" );
        //double w = (Gdx.graphics.getWidth()  - in.left - in.right  - 1) / Board.fCols;
        //double h = (Gdx.graphics.getHeight() - in.top  - in.bottom - 1) / Board.fRows;
        int sw = Gdx.graphics.getWidth();
        int sh = Gdx.graphics.getHeight();
        double w = Gdx.graphics.getWidth()  / Board.fCols;
        double h = Gdx.graphics.getHeight() / Board.fRows;
        this.size = (int)Math.min( h, w );  //updated cell size

        //calc offsets to center entire board in window
        //this.xOff = ((int)(frameSize.getWidth()  - in.left - in.right  - 1) - Board.fCols*size) / 2;
        //this.yOff = ((int)(frameSize.getHeight() - in.top  - in.bottom - 1) - Board.fRows*size) / 2;
        this.xOff = (Gdx.graphics.getWidth()  - Board.fCols*size) / 2;
        this.yOff = (Gdx.graphics.getHeight() - Board.fRows*size) / 2;

        //ScreenUtils.clear( 1, 0, 0, 1 );
        Gdx.gl.glClearColor( 0.25f, 0.25f, 0.26f, 1.0f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        //camera.update();
        //sb.setProjectionMatrix( camera.combined );

        sr.begin( ShapeRenderer.ShapeType.Filled );
        drawBoardSquares( sr, size, xOff, yOff );
        sr.end();

        sb2.begin();
        drawStrings ( sb2, size, xOff, yOff );
        sb2.end();

        sr.begin( ShapeRenderer.ShapeType.Line );
        drawGridLines( sr, size, xOff, yOff );
        sr.end();

        sb.begin();
        drawPieces( sb, size, xOff, yOff );
        sb.end();

        sr.begin( ShapeRenderer.ShapeType.Line );
        drawOutlines( sr, size, xOff, yOff );
        sr.end();

        sb.begin();
        drawSettings( sb, size, xOff, yOff );
        sb.end();
    }
    //-----------------------------------------------------------------------
    private void drawSettings ( SpriteBatch g, int size, int xOff, int yOff ) {
        int r = -1;
        int c = Board.fCols;
        int tr = Board.fRows - r - 1;  //flip
        int x = c*size+xOff-size/4;
        int y = tr*size+yOff-size/4;
        g.draw( this.settings, x, y, size/5, size/5 );

        //calc center of settings icon/button
        this.cxSettings = x + size/5/2;
        this.cySettings = Gdx.graphics.getHeight() - y - size/5/2;
    }
    //-----------------------------------------------------------------------
    /** returns true when click is "close" to the center of the settings icon. */
    public boolean settingsPressed ( int x, int y ) {
        int dx = x - cxSettings;
        int dy = y - cySettings;
        //return Math.sqrt( dx*dx + dy*dy ) < size/5.0/2.0;
        return Math.sqrt( dx*dx + dy*dy ) < size/4.0;
    }
    //-----------------------------------------------------------------------
    private void drawBoardSquares ( ShapeRenderer g, int size, int xOff, int yOff  ) {
        for (int r=0; r<Board.fRows; r++) {
            for (int c=0; c<Board.fCols; c++) {
                Base v = this.game.b.getBase( r, c );
                if (v == null)    continue;
                switch (v) {
                    case cBDen :
                        g.setColor( Color.BLACK );
                        break;
                    case cBTrap :
                        g.setColor( Color.GRAY );
                        break;
                    case cGround :
                        g.setColor( Color.GREEN );
                        break;
                    case cRDen :
                        g.setColor( Color.RED );
                        break;
                    case cRTrap :
                        g.setColor( Color.PINK );
                        break;
                    case cWater :
                        g.setColor( Color.BLUE );
                        break;
                    default:
                        g.setColor( Color.YELLOW );
                        break;
                }
                int tr = Board.fRows - r - 1;  //flip
                g.rect( c*size + xOff, tr*size + yOff, size, size );
            }
        }
    }
    //-----------------------------------------------------------------------
    private void drawStrings ( SpriteBatch g, int size, int xOff, int yOff ) {
        for (int r=0; r<Board.fRows; r++) {
            for (int c=0; c<Board.fCols; c++) {
                Piece v = this.game.b.getPiece( r, c );
                if (v == null || v == Piece.rbNone) continue;
                String p = v.toString();
                //yucky way to determine color of piece
                /** @todo (for george) use getColor instead! */
                if (p.charAt( 0 ) == 'r')    g.setColor( Color.RED );
                else                         g.setColor( Color.BLUE );

                int tr = Board.fRows - r - 1;  //flip
                int x = c * size + xOff;
                int y = tr * size + yOff;
                //center
                if (this.game.nameOn) {
                    int dx = (int)(0.03 * size);
                    int dy = (int)(0.97 * size);
                    font.draw( g, p, x + dx, y + dy );
                }
                if (this.game.rankOn) {
                    int rank = this.game.b.getRank( r, c );
                    int dx = (int)(0.97 * size - this.stringWidth);
                    int dy = (int)(1.5 * this.stringHeight);
                    font.draw( g, "" + rank, x + dx, y + dy );
                }

            }
        }
    }
    //-----------------------------------------------------------------------
    private void drawGridLines ( ShapeRenderer g, int size, int xOff, int yOff ) {
        Gdx.gl.glLineWidth( 2 );
        g.setColor( Color.DARK_GRAY );
        //horizontal lines
        for (int r=0; r<=Board.fRows; r++) {
            g.line( 0 + xOff, r*size + yOff, Board.fCols*size + xOff, r*size + yOff );
        }
        //vertical lines
        for (int c=0; c<=Board.fCols; c++) {
            g.line( c*size + xOff, 0 + yOff, c*size + xOff, Board.fRows*size + yOff );
        }
    }
    //-----------------------------------------------------------------------
    private void drawPieces ( SpriteBatch g, int size, int xOff, int yOff ) {
        for (int r=0; r<Board.fRows; r++) {
            for (int c = 0; c < Board.fCols; c++) {
                Piece v = this.game.b.getPiece( r, c );
                if (v == null)    continue;
                if (this.piece[v.ordinal()] == null)    continue;
                int tr = Board.fRows - r - 1;  //flip
                g.draw( this.piece[v.ordinal()], c * size + xOff, tr * size + yOff, size, size );
            }
        }
    }
    //-----------------------------------------------------------------------
    private void drawOutlines ( ShapeRenderer g, int size, int xOff, int yOff  ) {
        //outline selected piece (if any)
        if (this.game.fromR != -1) {
            g.setColor( Color.YELLOW );
            int tr = Board.fRows - this.game.fromR - 1;  //flip
            g.rect( this.game.fromC*size + xOff, tr*size + yOff, size, size );
        }

        //outline selected destination (if any)
        if (this.game.toR != -1) {
            --this.game.frameCount;
            if (this.game.frameCount > 0) {
                g.setColor( Color.ORANGE );
                int tr = Board.fRows - this.game.toR - 1;  //flip
                g.rect( this.game.toC * size + xOff, tr * size + yOff, size, size );
            } else {
                this.game.fromR = this.game.fromC = this.game.toR = this.game.toC = -1;
            }
        }
    }
    //-----------------------------------------------------------------------
    public void dispose ( ) {
        this.font.dispose();
        this.sb.dispose();
        this.sb2.dispose();
        this.sr.dispose();
        for (int i=0; i<piece.length; i++) {
            if (piece[i] != null)  {
                piece[i].dispose();
                piece[i] = null;
            }
        }
        piece = null;
    }

}
