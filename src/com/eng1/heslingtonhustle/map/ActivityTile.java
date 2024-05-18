/**
 * Represents a tile on the map where activities can take place.
 */
package com.eng1.heslingtonhustle.map;

import com.badlogic.gdx.math.Rectangle;

public class ActivityTile {
    private final Rectangle rectangle;

    /**
     * Constructs an ActivityTile with the given rectangle.
     * @param rectangle The rectangle representing the tile area
     */
    public ActivityTile(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Gets the rectangle representing the tile area.
     * @return The rectangle representing the tile area
     */
    public Rectangle getRectangle() {
        return rectangle;
    }
}
