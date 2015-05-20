package com.henrik.gdxFramework.core;

import com.badlogic.gdx.utils.Array;
import com.henrik.advergame.Game;
import com.henrik.advergame.State;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 12/11/2014.
 */
public class GameStateCollection {

    private Array<GameState> states;

    public GameStateCollection() {
        states = new Array<GameState>();
    }

    public void add(GameState state) {
        states.add(state);
    }

    public void remove(Class<?> stateType) {
        for(int i = 0; i < states.size; i++) {
            if(states.get(i).getClass() == stateType) {
                states.removeIndex(i);
                return;
            }
        }
    }

    /**
     * Enables the given component, and disables all other states.
     */
    public void enable(Class<?> type) {
        for(int i = 0; i < states.size; i++) {
            if(states.get(i).getClass() == type) {
                states.get(i).setEnabled(true);
            } else {
                states.get(i).setEnabled(false);
                states.get(i).clear();
            }
        }
    }

    public void update() {
        for(int i = 0; i < states.size; i++) {
            GameState state = states.get(i);

            if(state.isUpdating()) {
                state.update();
            }
            if(state.isRendering()) {
                state.render();
            }
        }
    }

    public GameState get(Class<?> type) {
        for(int i = 0; i < states.size; i++) {
            if(states.get(i).getClass() == type) {
                return states.get(i);
            }
        }

        return null;
    }

    public  void dispose() {
        for(int i = 0; i < states.size; i++) {
            states.get(i).dispose();
        }

        states.clear();
    }
}
