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
import org.jungle.renderers.IRenderer;
import org.jungle.renderers.JungleRender;
import org.jungle.util.OBJLoader;

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

	private float color = 0.2f;
	private int direction = 0;
	private Game game;
	private IRenderer render;
	private Context ctx;

	private Spatial spatial;
	private Mesh mesh;

	public static final float CAMERA_POS_STEP = 0.11F;
	public static final float MOUSE_SENSITIVITY = 1.11F;

	public DummyLogic() {
		render = new JungleRender();
	}

	@Override
	public void init(Window win) throws Exception {
		render.init(win);
		//Texture texture = new Texture("example/assets/grass.png");
		mesh = OBJLoader.loadMesh("example/assets/test.obj");
		//mesh.setTexture(texture);
		//mesh.setColour(new Vector3f(1f, 1f, 1f));
		ctx = new Context(game, new Camera());
//		for (int i = 0; i < 10; i++) {
//			for (int j = 0; j < 10; j++) {
//				spatial = new Spatial(mesh);
//				spatial.setPosition(i, 0, j);
//				ctx.addSpatial(spatial);
//			}
//		}
		spatial = new Spatial(mesh);
		spatial.setScale(0.1f);
		spatial.setPosition(0, 0, -2f);
		ctx.addSpatial(spatial);
		cameraInc = new Vector3f();
		mouse = new MouseInput();
		mouse.init(win);
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
		if (window.isKeyPressed(GLFW_KEY_Z)) {
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
		render.render(window, ctx);
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
