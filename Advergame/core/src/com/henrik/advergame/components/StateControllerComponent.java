package com.henrik.advergame.components;

import com.henrik.advergame.State;

/**
 * A controller component that consists of a given state.
 */
public class StateControllerComponent extends ControllerComponent {
    protected State currentState;

    StateControllerComponent() {
        super();
    }

    public void setState(State state) {
        currentState = state;
    }
    public State getState() { return currentState; }

    @Override
    public void dispose() {

    }
}
