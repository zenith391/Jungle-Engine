package org.jungle.renderers;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.jungle.Spatial;
import org.jungle.Window;
import org.jungle.game.Context;
import org.jungle.util.ShaderProgram;
import org.jungle.util.Utils;

public class JungleRender implements IRenderer {

	private ShaderProgram shaderProgram;
	
	private static final float FOV = (float) Math.toRadians(70.f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.0f;
	private Matrix4f projectionMatrix;
	private Transformation transformation;
	
	public JungleRender() {
		
	}
	
	

	@Override
	public void init(Window window) throws Exception {
		transformation = new Transformation();
        projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource("shaders/default/vertex.vs"));
		shaderProgram.createFragmentShader(Utils.loadResource("shaders/default/fragment.fs"));
		shaderProgram.link();
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("texture_sampler");
		shaderProgram.createUniform("colour");
		shaderProgram.createUniform("useColour");
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void render(Window window, Context ctx) {
		clear();
		Spatial[] spatials = ctx.getSpatials();
	    if ( window.isResized() ) {
	        glViewport(0, 0, window.getWidth(), window.getHeight());
	        window.setResized(false);
	        projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
	    }
		
		shaderProgram.bind();
		
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		shaderProgram.setUniform("texture_sampler", 0);
		
		Matrix4f viewMatrix = transformation.getViewMatrix(ctx.getCamera());
		
		for (Spatial spatial : spatials) {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(spatial, viewMatrix);
			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			if (spatial.getMesh().getColour() != null)
				shaderProgram.setUniform("colour", spatial.getMesh().getColour());
			int val = 0;
			if (!spatial.getMesh().isTextured()) {
				val = 1;
			}
			shaderProgram.setUniform("useColour", val);
			spatial.getMesh().render();
		}
		
		
		shaderProgram.unbind();
	}

	@Override
	public void cleanup() {
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}

}
