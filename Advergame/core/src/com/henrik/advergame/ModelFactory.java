package com.henrik.advergame;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * Used to facilitate building models.
 */
public class ModelFactory {

    public static Model BuildDoorAcross(ModelBuilder mb, Texture texture, Texture edgeTexture, int cellSize, Vector3 center, Vector3 dimensions) {

        mb.begin();

        MeshPartBuilder mpb = mb.part("doorMain", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(texture)));
        buildLeft(mpb, cellSize, center, dimensions);
        buildRight(mpb, cellSize, center, dimensions);

        mpb = mb.part("doorMain", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(edgeTexture)));
        buildBack(mpb, cellSize, center, dimensions);
        buildFront(mpb, cellSize, center, dimensions);
        buildTop(mpb, cellSize, center, dimensions);

        return mb.end();
    }

    public static Model BuildDoorDown(ModelBuilder mb,  Texture texture, Texture edgeTexture, int cellSize, Vector3 center, Vector3 dimensions) {

        mb.begin();

        MeshPartBuilder mpb = mb.part("doorMain", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(texture)));
        buildBack(mpb, cellSize, center, dimensions);
        buildFront(mpb, cellSize, center, dimensions);

        mpb = mb.part("doorMain", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(edgeTexture)));
        buildRight(mpb, cellSize, center, dimensions);
        buildLeft(mpb, cellSize, center, dimensions);
        buildTop(mpb, cellSize, center, dimensions);

        return mb.end();
    }

    private static void buildTop(MeshPartBuilder mpb, int cellSize, Vector3 center, Vector3 dimensions) {
        mpb.rect(new Vector3(center.x - (dimensions.x / 2f), cellSize / 2f, center.z - (dimensions.z / 2f)), // lower left
                new Vector3(center.x - (dimensions.x / 2f), cellSize / 2f, center.z + (dimensions.z / 2f)), // upper left
                new Vector3(center.x + (dimensions.x / 2f), cellSize / 2f, center.z + (dimensions.z / 2f)), // upper right
                new Vector3(center.x + (dimensions.x / 2f), cellSize / 2f, center.z - (dimensions.z / 2f)), // lower right
                new Vector3(0, 1, 0));
    }

    private static void buildLeft(MeshPartBuilder mpb, int cellSize, Vector3 center, Vector3 dimensions) {
        mpb.rect(new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper right
                new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower right
                new Vector3(-1,0,0));
    }

    private static void buildRight(MeshPartBuilder mpb, int cellSize, Vector3 center, Vector3 dimensions) {
        mpb.rect(new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper right
                new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower right
                new Vector3(1,0,0));
    }

    private static void buildBack(MeshPartBuilder mpb, int cellSize, Vector3 center, Vector3 dimensions) {
        mpb.rect(new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper right
                new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower right
                new Vector3(0,0,-1));
    }

    private static void buildFront(MeshPartBuilder mpb, int cellSize, Vector3 center, Vector3 dimensions) {
        mpb.rect(new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper right
                new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower right
                new Vector3(0,0,-1));
    }
}
