package test.org.jungle;

import org.jungle.Camera;
import org.jungle.Mesh;
import org.jungle.MouseInput;
import org.jungle.Spatial;
import org.jungle.Texture;
import org.jungle.Window;
import org.jungle.game.Context;
import org.jungle.game.Game;
import org.jungle.game.IGameLogic;
import org.jungle.hud.Font;
import org.jungle.renderers.IRenderer;
import org.jungle.renderers.JungleRender;
import org.jungle.util.DirectionalLight;
import org.jungle.util.Material;
import org.jungle.util.MouseOperatedMSD;
import org.jungle.util.OBJLoader;
import org.jungle.util.PointLight;
import org.jungle.util.SpotLight;
import org.jungle.util.StaticMeshesLoader;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DummyLogic implements IGameLogic {

	private MouseInput mouse;

	private Vector3f cameraInc;
	private Game game;
	private IRenderer render;
	private Context ctx;

	private Spatial spatial;
	private Mesh[] mesh;
	private Material material;
	
	private Vector3f ambientLight;
	private PointLight[] pointLightList;
	private SpotLight[] spotLightList;
	private DirectionalLight directionalLight;
	private MouseOperatedMSD msd;

	public static final float CAMERA_POS_STEP = 0.11F;
	public static final float MOUSE_SENSITIVITY = 1.11F;
	

	public DummyLogic() {
		render = new JungleRender();
	}

	@Override
	public void init(Window win) throws Exception {
		Texture texture = new Texture("example/assets/grassblock.png");
		System.out.println("Loading mesh..");
		Mesh[] meshs = StaticMeshesLoader.load("example/assets/evil.obj", "example/assets/");
		System.out.println("Mesh loaded!");
		System.out.println("meshes: " + meshs.length);
		mesh = meshs;
		material = new Material(texture, 1f);
		for (Mesh m : mesh) {
			m.setMaterial(material);
		}
		msd = new MouseOperatedMSD((JungleRender) render);
		ambientLight = new Vector3f(0.7f, 0.7f, 0.7f);
		
		ctx = new Context(game, new Camera());
		
		
		
		spatial = new Spatial(meshs);
		spatial.setPosition(0, 3, 0);
		spatial.setScale(2f);
		ctx.addSpatial(spatial);
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

        lightPosition = new Vector3f(0, 0.75f, 0.25f);
        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
		cameraInc = new Vector3f();
		mouse = new MouseInput();
		mouse.init(win);
		//mouse.setGrabbed(true);
		System.out.println("Initing renderer..");
		render.init(win);
		System.out.println("Renderer: OK!");
	}
	
	int cooldown;

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
		if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
			mouse.setGrabbed(false);
		}
		if (mouse.isLeftButtonPressed()) {
			mouse.setGrabbed(true);
			Spatial s = msd.getSelectedSpatial();
			if (s != null && cooldown == 0) {
				//ctx.removeSpatial(s);
				cooldown = 1;
			}
		}
		if (cooldown > 0) {
			cooldown--;
		}
	}

	@Override
	public void update(float interval) {
		//System.out.println("upd");
		Camera camera = ctx.getCamera();
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);
		// Update camera based on mouse
		if (mouse.isRightButtonPressed() || mouse.isGrabbed()) {
			Vector2f rotVec = mouse.getDisplVec();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
		float rotation = spatial.getRotation().x + 1.5f;
		if (rotation > 360) {
			rotation = 0;
		}
		msd.selectSpatial(ctx.getSpatials(), ctx.getWindow(), mouse.getCurrentPos(), camera);
		spatial.setRotation(rotation, rotation, rotation);
		mouse.clearPos(game.getWindow().getWidth()/2, game.getWindow().getHeight()/2);
		
		ctx.getCamera().setPosition(35, -20, 0);
		ctx.getCamera().setRotation(325, 265, 0);
		//System.out.println(ctx.getCamera().getRotation().z);
	}

	@Override
	public void render(Window window) {
		//System.out.println("ren");
		if (window.shouldClose()) {
			game.exit();
		}
		//System.out.println(ambientLight);
		render.render(window, ctx, ambientLight, pointLightList, spotLightList, directionalLight);
		//System.out.println("rendered!");
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
