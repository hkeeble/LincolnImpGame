package com.henrik.advergame;

import java.util.ArrayList;

/**
 * Created by Henri on 03/02/2015.
 */
public class Subject {

    private ArrayList<Observer> observers;

    public Subject() {
        observers = new ArrayList<Observer>();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void emitUpdate() {
        for(Observer obj : observers) {
            obj.notifyUpdate(this);
        }
    }

}
