package com.dsq2022.game;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * This is simply a "decoration" that indicates that the function should be
 * something else (e.g., private), but has been made public for testing.
 * Example: <br/>
 *     @PublicForTesting( shouldBe="private" ) <br/>
 *     public boolean waitingForRotation = false;  ///< second move (rotation) of turn
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface PublicForTesting {
    String shouldBe();  ///< indicates what it should be
}
