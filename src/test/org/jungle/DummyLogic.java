package test.org.jungle;

import org.jungle.Camera;
import org.jungle.Keyboard;
import org.jungle.Mesh;
import org.jungle.MouseInput;
import org.jungle.Spatial;
import org.jungle.Texture;
import org.jungle.Window;
import org.jungle.game.Context;
import org.jungle.game.Game;
import org.jungle.game.IGameLogic;
import org.jungle.hud.TextObject;
import org.jungle.renderers.IRenderer;
import org.jungle.renderers.JungleRender;
import org.jungle.util.DirectionalLight;
import org.jungle.util.Material;
import org.jungle.util.OBJLoader;
import org.jungle.util.PointLight;
import org.jungle.util.SpotLight;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DummyLogic implements IGameLogic {

	private int displxInc = 0;

	private int displyInc = 0;

	private MouseInput mouse;

	private Vector3f cameraInc;

	private int scaleInc = 0;
	
	private float spotAngle = 0, spotInc = 1, lightAngle = 0;

	private float color = 0.2f;
	private int direction = 0;
	private Game game;
	private IRenderer render;
	private Context ctx;

	private Spatial spatial;
	private Mesh mesh;
	private Material material;
	
	private Vector3f ambientLight;
	private PointLight[] pointLightList;
	private SpotLight[] spotLightList;
	private DirectionalLight directionalLight;

	public static final float CAMERA_POS_STEP = 0.11F;
	public static final float MOUSE_SENSITIVITY = 1.11F;

	public DummyLogic() {
		render = new JungleRender();
		lightAngle = -75f;
	}

	@Override
	public void init(Window win) throws Exception {
		render.init(win);
		Texture texture = new Texture("example/assets/grassblock.png");
		mesh = OBJLoader.loadMesh("example/assets/evil.obj");
		material = new Material(texture, 1f);
		mesh.setMaterial(material);
		ambientLight = new Vector3f(1.0f, 1.0f, 1.0f);
		
		//mesh.setTexture(null);
		//mesh.setColour(new Vector3f(1f, 1f, 1f));
		ctx = new Context(game, new Camera());

		ctx.getCamera().setPosition(32, 0, 32);
		for (int i = 0; i < 128; i++) {
			for (int j = 0; j < 128; j++) {
				spatial = new Spatial(mesh);
				spatial.setPosition(i, -1, j);
				spatial.setScale(0.5f);
				ctx.addSpatial(spatial);
			}
		}
		spatial = new Spatial(mesh);
		spatial.setPosition(5, 0f, 5);
		spatial.setScale(0.5f);
		ctx.addSpatial(spatial);
//		spatial = new Spatial(mesh);
//		spatial.setScale(0.5f);
//		spatial.setPosition(0, 0, -2f);
//		ctx.addSpatial(spatial);
		Vector3f lightPosition = new Vector3f(0, 2, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.3f, 0.7f, 1.0f);
        pointLight.setAttenuation(att);
        pointLightList = new PointLight[]{pointLight};

        // Spot Light
        lightPosition = new Vector3f(0, 2.0f, 0f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
        spotLightList = new SpotLight[]{spotLight};

        lightPosition = new Vector3f(0, 1, 0);
        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
		cameraInc = new Vector3f();
		mouse = new MouseInput();
		mouse.init(win);
		
		//TextObject tobj = new TextObject("DEMO", "example/texture/default_font.png", 16, 16);
		//ctx.getHUD().addComponent(tobj);
	}

	@Override
	public void input(Window window) {
		mouse.input(window);
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) { // W
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			cameraInc.y = 1;
		}
	}

	@Override
	public void update(float interval) {
		Camera camera = ctx.getCamera();
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);
		// Update camera based on mouse
		if (mouse.isRightButtonPressed()) {
			Vector2f rotVec = mouse.getDisplVec();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
		float rotation = spatial.getRotation().x + 1.5f;
		if (rotation > 360) {
			rotation = 0;
		}
		//spatial.setRotation(rotation, rotation, rotation);
	}

	@Override
	public void render(Window window) {
		if (window.shouldClose()) {
			game.exit();
		}
		window.setClearColor(new Vector4f(color, color, color, 0.0f));
		System.out.println(ambientLight);
		render.render(window, ctx, ambientLight, pointLightList, spotLightList, directionalLight);
	}

	@Override
	public void bind(Game g) {
		game = g;
	}

	@Override
	public void cleanup() {
		render.cleanup();
	}

}
