package org.jungle.renderers;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jungle.Camera;
import org.jungle.Mesh;
import org.jungle.Spatial;
import org.jungle.Window;
import org.jungle.game.Context;
import org.jungle.hud.IHud;
import org.jungle.util.DirectionalLight;
import org.jungle.util.PointLight;
import org.jungle.util.ShaderProgram;
import org.jungle.util.SpotLight;
import org.jungle.util.Utils;

public class JungleRender implements IRenderer {

	private ShaderProgram shaderProgram;
	private ShaderProgram hudShaderProgram;

	private static final float FOV = (float) Math.toRadians(70.f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.0f;
	private Matrix4f projectionMatrix;
	private boolean inited = false;
	private Transformation transformation;
	private String shaders = "example/shaders/default";

	public static final int MAX_POINT_LIGHTS = 5;
	public static final int MAX_SPOT_LIGHTS = 5;
	private float specularPower = 0.75f;

	public JungleRender() {

	}
	
	public void setShaderFolder(String shaders) {
		if (inited)
			throw new UnsupportedOperationException();
		this.shaders = shaders;
	}

	@Override
	public void init(Window window) throws Exception {
		transformation = new Transformation();
		projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR,
				Z_FAR);
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource(shaders + "/vertex.vs"));
		shaderProgram.createFragmentShader(Utils.loadResource(shaders + "/fragment.fs"));
		shaderProgram.link();
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("modelViewMatrix");
		shaderProgram.createUniform("texture_sampler");
		shaderProgram.createUniform("ambientLight");
		shaderProgram.createUniform("specularPower");
		shaderProgram.createMaterialUniform("material");
		shaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
		shaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
		shaderProgram.createDirectionalLightUniform("directionalLight");
		
		setupHudShader();
		
		inited = true;
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void render(Window window, Context ctx, Vector3f ambientLight,
			PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight) {
		clear();
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
			projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR,
					Z_FAR);
		}
		
		renderScene(ctx, ambientLight, pointLightList, spotLightList, directionalLight);
		renderHud(window, ctx.getHUD());
		
	}
	
	public void renderScene(Context ctx, Vector3f ambientLight, PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight) {
		Spatial[] spatials = ctx.getSpatials();
		shaderProgram.bind();

		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		shaderProgram.setUniform("texture_sampler", 0);

		Matrix4f viewMatrix = transformation.getViewMatrix(ctx.getCamera());
		
		renderLights(viewMatrix, ambientLight, pointLightList, spotLightList, directionalLight);

		for (Spatial spatial : spatials) {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(spatial, viewMatrix);
			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			shaderProgram.setUniform("material", spatial.getMesh().getMaterial());
			spatial.getMesh().render();
		}
		
		shaderProgram.unbind();
	}
	
	private void renderHud(Window window, IHud hud) {

	    hudShaderProgram.bind();

	    Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
	    for (Spatial component : hud.getComponents()) {
	        Mesh mesh = component.getMesh();
	        // Set ortohtaphic and model matrix for this HUD item
	        Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(component, ortho);
	        hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
	        hudShaderProgram.setUniform("colour", component.getMesh().getMaterial().getAmbientColour());

	        // Render the mesh for this HUD item
	        mesh.render();
	    }

	    hudShaderProgram.unbind();
	}
	
	private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(Utils.loadResource(shaders + "/hud_vertex.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource(shaders + "/hud_fragment.fs"));
        hudShaderProgram.link();

        // Create uniforms for Orthographic-model projection matrix and base colour
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("colour");
    }

	private void renderLights(Matrix4f viewMatrix, Vector3f ambientLight, PointLight[] pointLightList,
			SpotLight[] spotLightList, DirectionalLight directionalLight) {

		shaderProgram.setUniform("ambientLight", ambientLight);
		shaderProgram.setUniform("specularPower", specularPower);

		// Process Point Lights
		int numLights = pointLightList != null ? pointLightList.length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the point light object and transform its position to view
			// coordinates
			PointLight currPointLight = new PointLight(pointLightList[i]);
			Vector3f lightPos = currPointLight.getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;
			shaderProgram.setUniform("pointLights", currPointLight, i);
		}

		// Process Spot Ligths
		numLights = spotLightList != null ? spotLightList.length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the spot light object and transform its position and cone
			// direction to view coordinates
			SpotLight currSpotLight = new SpotLight(spotLightList[i]);
			Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
			dir.mul(viewMatrix);
			currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
			Vector3f lightPos = currSpotLight.getPointLight().getPosition();

			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;

			shaderProgram.setUniform("spotLights", currSpotLight, i);
		}

		// Get a copy of the directional light object and transform its position to view
		// coordinates
		DirectionalLight currDirLight = new DirectionalLight(directionalLight);
		Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
		//dir.mul(viewMatrix);
		currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		shaderProgram.setUniform("directionalLight", currDirLight);

	}

	@Override
	public void cleanup() {
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}

}
