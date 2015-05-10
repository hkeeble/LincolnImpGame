package com.henrik.advergame.utils;

import com.badlogic.gdx.Gdx;
import com.henrik.gdxFramework.core.CollisionObject;
import com.henrik.gdxFramework.entities.GameObject;

import java.util.Comparator;

/**
 * Created by Henri on 13/03/2015.
 */
public class ObjectDistanceSorter implements Comparator<CollisionObject> {

    private GameObject referenceObject;

    public ObjectDistanceSorter(GameObject referenceObject) {
        this.referenceObject = referenceObject;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == referenceObject) {
            return true;
        }

        if(((GameObject)obj).getPosition().equals(referenceObject.getPosition())) {
            return true;
        }

        return false;
    }

    @Override
    public int compare(CollisionObject o1, CollisionObject o2) {
        Gdx.app.log("Sort","");

        float obj1Dist = o1.getObject().getPosition().dst(referenceObject.getPosition());
        float obj2Dist = o2.getObject().getPosition().dst(referenceObject.getPosition());

        if(obj1Dist < obj2Dist) {
            return -1;
        } else if(obj1Dist > obj2Dist) {
            return 1;
        } else {
            return 0;
        }
    }
}
