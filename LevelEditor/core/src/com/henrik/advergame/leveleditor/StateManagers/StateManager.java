package com.henrik.advergame.leveleditor.StateManagers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.henrik.advergame.leveleditor.shared.State;
import com.henrik.advergame.leveleditor.shared.TileState;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Henri on 27/02/2015.
 */
public class StateManager {

    protected HashMap<State, Texture> stateMap;

    protected StateManager(AssetManager manager) {
        stateMap = new HashMap<State, Texture>();
    }

    public void renderID(SpriteBatch batch, int id, int x, int y) {
        Iterator iter = stateMap.entrySet().iterator();
        while(iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            State state = (State)entry.getKey();
            Texture tex = (Texture)entry.getValue();

            if(id == state.getID())
                batch.draw(tex, x, y);
        }
    }

    public Texture getStateIDTexture(int id) {
        Iterator iter = stateMap.entrySet().iterator();
        while(iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            State state = (State)entry.getKey();
            Texture tex = (Texture)entry.getValue();

            if(id == state.getID())
                return tex;
        }
        return null;
    }

    public State getState(int id) {
        Iterator iter = stateMap.entrySet().iterator();
        while(iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            State state = (State)entry.getKey();

            if(id == state.getID())
                return state;
        }

        return null;
    }

}
