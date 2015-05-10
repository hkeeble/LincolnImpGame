package com.henrik.advergame.entities;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

public class PoolablePhysicalEntity extends PhysicalEntity implements Poolable {

	public PoolablePhysicalEntity(PhysicsComponent physicsComponent) {
		super(physicsComponent);
	}

	@Override
	public void reset() {
		
	}
}
