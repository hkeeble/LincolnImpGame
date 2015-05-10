package com.henrik.advergame.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.henrik.advergame.entities.PoolablePhysicalEntity;
import com.henrik.advergame.systems.CannonBall;

public class CannonBallPool extends Pool<CannonBall> {
	
	private Texture texture;

	private Vector3 position;
	
	public CannonBallPool(Texture texture) {
		this.texture = texture;
		position = new Vector3(0,0,0);
	}
	
	@Override
	protected CannonBall newObject() {
		return new CannonBall(texture, position, position);
	}
}
