package com.etherblood.jme.client.sprites.data;

import com.google.gson.annotations.SerializedName;

public enum FrameType {
    @SerializedName("none")
    NONE,
    @SerializedName("northEast")
    NORTH_EAST,
    @SerializedName("southEast")
    SOUTH_EAST,
    @SerializedName("southWest")
    SOUTH_WEST,
    @SerializedName("northWest")
    NORTH_WEST;
}
