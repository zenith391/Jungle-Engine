package org.jungle;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {

	private final int vaoId;
	private int vboId;
	private final int vertexCount;
	private Texture texture;
	private Vector3f colour;

	private List<Integer> vboIdList = new ArrayList<>();

	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {

		FloatBuffer verticesBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			vertexCount = indices.length;
			verticesBuffer.put(positions).flip();

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			vboId = glGenBuffers();
			vboIdList.add(vboId);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			vboId = glGenBuffers();
			vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			MemoryUtil.memFree(indicesBuffer);

			// // Colour VBO
			// colourVboId = glGenBuffers();
			// FloatBuffer colourBuffer = MemoryUtil.memAllocFloat(colours.length);
			// colourBuffer.put(colours).flip();
			// glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
			// glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
			// MemoryUtil.memFree(colourBuffer);
			// glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

			vboId = glGenBuffers();
			vboIdList.add(vboId);
			FloatBuffer textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
			textCoordsBuffer.put(textCoords).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			// Vertex normals VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			FloatBuffer vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			vecNormalsBuffer.put(normals).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

			MemoryUtil.memFree(textCoordsBuffer);

			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glBindVertexArray(0);
		} finally {
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
		}

	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public void render() {
		if (texture != null) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		// Draw the mesh
		glBindVertexArray(getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);

		if (texture != null) {
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}

	public boolean isTextured() {
		return getColour() == null;
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : vboIdList) {
			glDeleteBuffers(vboId);
		}
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

}
