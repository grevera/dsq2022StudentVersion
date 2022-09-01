package com.dsq2022.game;

/**
 * Define constants for moveable playing pieces (or none).
 * <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Here</a>
 * is a nice discussion regarding enum's in Java.
 *
 * This is how one would do an enum in C/C++. However, Java enum's are more "powerful."
 * <pre>
 *     public enum Piece {
 *         rbNone,  //no piece present
 *         //red pieces
 *         rRat, rCat, rDog, rWolf, rLeopard, rTiger, rLion, rElephant,
 *         //blue pieces
 *         bRat, bCat, bDog, bWolf, bLeopard, bTiger, bLion, bElephant
 *     }
 * </pre>
 *
 * Copyright © George J. Grevera, 2016. All rights reserved.
 */
public enum Piece {
    rbNone(    GameColor.None ),  //no piece present
    //red pieces
    rRat(      GameColor.Red ),
    rCat(      GameColor.Red ),
    rDog(      GameColor.Red ),
    rWolf(     GameColor.Red ),
    rLeopard(  GameColor.Red ),
    rTiger(    GameColor.Red ),
    rLion(     GameColor.Red ),
    rElephant( GameColor.Red ),
    //blue pieces
    bRat(      GameColor.Blue ),
    bCat(      GameColor.Blue ),
    bDog(      GameColor.Blue ),
    bWolf(     GameColor.Blue ),
    bLeopard(  GameColor.Blue ),
    bTiger(    GameColor.Blue ),
    bLion(     GameColor.Blue ),
    bElephant( GameColor.Blue );

    public final GameColor color;
    /** This ctor is never used directly. */
    Piece ( GameColor c ) {
        this.color = c;
    }
}
