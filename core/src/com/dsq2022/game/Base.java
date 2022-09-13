package com.dsq2022.game;
/**
 * Define constants for the board itself.
 * These never move as they are part of the board.
 *
 * Copyright © George J. Grevera, 2016. All rights reserved.
 */
public enum Base {
    cGround,  ///< ordinary ground
    cWater,   ///< water
    cRTrap,   ///< red (side of board) trap
    cBTrap,   ///< blue (side of board) trap
    cRDen,    ///< red (side of board) den
    cBDen,    ///< blue (side of board) den
    cNone     ///< not used/out of bounds
}
