package org.jungle;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.jungle.game.GameOptions;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

public class Window {

	private long handle;
	private int width, height;
	private boolean resized;
	private Matrix4f projectionMatrix;
	private GameOptions opt;
	private boolean fullscreen = false;
	
	private Vector4f clearColor;
	
	public Vector4f getClearColor() {
		return clearColor;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public void setClearColor(Vector4f clearColor) {
		this.clearColor = clearColor;
		glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
	}
	
	public GameOptions getOptions() {
		return opt;
	}

	public boolean isResized() {
		return resized;
	}
	
	public long getWindowHandle() {
		return handle;
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setSize(int width, int height) {
		glfwSetWindowSize(handle, width, height);
	}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(handle, title);
	}
	
	public void setFullscreen(boolean fullscreen) {
		if (fullscreen != this.fullscreen) {
			this.fullscreen = fullscreen;
			if (fullscreen) {
				glfwSetWindowMonitor(handle, glfwGetPrimaryMonitor(), 0, 0, 1920, 1080, GLFW_DONT_CARE);
			} else {
				glfwSetWindowMonitor(handle, NULL, 0, 0, 800, 600, GLFW_DONT_CARE);
			}
		}
	}
	
	public void setOptions(GameOptions opt) {
		this.opt = opt;
		glfwMakeContextCurrent(handle);
		if (opt.showTriangles) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		} else {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
		if (opt.antialiasing) {
		    glfwWindowHint(GLFW_SAMPLES, 2);
		} else {
			glfwWindowHint(GLFW_SAMPLES, 0);
		}
		if (opt.cullFace) {
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		} else {
			glDisable(GL_CULL_FACE);
		}
		if (opt.blending) {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		} else {
			glDisable(GL_BLEND);
		}
		setFullscreen(opt.fullscreen);
	}
	
	private static boolean inited;

	private void init(GameOptions opt, long monitorID) {
		if (!inited) {
			GLFWErrorCallback.createPrint(System.err).set();
			if (!glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW");
			inited = true;
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		handle = glfwCreateWindow(620, 480, "Jungle Game", monitorID, NULL);
		if (handle == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(handle, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(handle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		
		
		glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				Keyboard.setKeyPressed(key, true);
			}
			if (action == GLFW_RELEASE) {
				Keyboard.setKeyPressed(key, false);
			}
		});
		
		glfwSetFramebufferSizeCallback(handle, (window, width, height) -> {
		    Window.this.width = width;
		    Window.this.height = height;
		    Window.this.setResized(true);
		});
		
		glfwMakeContextCurrent(handle);
		GL.createCapabilities();
		if (clearColor == null) {
			setClearColor(new Vector4f(0.f, 0.f, 0.f, 0.f));
		}
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_STENCIL_TEST);
		setOptions(opt);
	}
	
	public void init(GameOptions opt) {
		init(opt, NULL);
	}
	
	public void restoreState() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (opt.cullFace) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
	}
	
	public void show() {
		glfwShowWindow(handle);
	}
	
	public boolean isKeyPressed(int keyCode) {
		return Keyboard.pressedKeys.contains(keyCode);
	}
	
	public void update() {
		
	}
	
	public void render() {
		glfwMakeContextCurrent(handle);
		glfwSwapBuffers(handle);
		glfwPollEvents();
	}
	
}
