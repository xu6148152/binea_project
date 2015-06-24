package demo.binea.com.openglesdemo.model;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by xubinggui on 15/4/11.
 */
public class Square {

	public Square() {
		initializeBuffers();
		initializeProgram();
	}

	private FloatBuffer verticesBuffer;
	private FloatBuffer textureBuffer;

	private float vertices[] = {
			-1f, -1f,
			1f, -1f,
			-1f, 1f,
			1f, 1f,
	};

	private float textureVertices[] = {
			0f,1f,
			1f,1f,
			0f,0f,
			1f,0f
	};

	private void initializeBuffers(){
		ByteBuffer buff = ByteBuffer.allocateDirect(vertices.length * 4);
		buff.order(ByteOrder.nativeOrder());
		verticesBuffer = buff.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);

		buff = ByteBuffer.allocateDirect(textureVertices.length * 4);
		buff.order(ByteOrder.nativeOrder());
		textureBuffer = buff.asFloatBuffer();
		textureBuffer.put(textureVertices);
		textureBuffer.position(0);
	}

	private final String vertexShaderCode =
			"attribute vec4 aPosition;" +
					"attribute vec2 aTexPosition;" +
					"varying vec2 vTexPosition;" +
					"void main() {" +
					"  gl_Position = aPosition;" +
					"  vTexPosition = aTexPosition;" +
					"}";

	private final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform sampler2D uTexture;" +
					"varying vec2 vTexPosition;" +
					"void main() {" +
					"  gl_FragColor = texture2D(uTexture, vTexPosition);" +
					"}";

	private int vertexShader;
	private int fragmentShader;
	private int program;

	private void initializeProgram(){
		vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShader, vertexShaderCode);
		GLES20.glCompileShader(vertexShader);

		fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
		GLES20.glCompileShader(fragmentShader);

		program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);

		GLES20.glLinkProgram(program);
	}

	/**
	 1.Use glBindFramebuffer to create a named frame buffer object (often called FBO).
	 2.Use glUseProgram to start using the program we just linked.
	 3.Pass the value GL_BLEND to glDisable to disable the blending of colors while rendering.
	 4.Use glGetAttribLocation to get a handle to the variables aPosition and aTexPosition mentioned in the vertex shader code.
	 5.Use glGetUniformLocation to get a handle to the constant uTexture mentioned in the fragment shader code.
	 6.Use the glVertexAttribPointer to associate the aPosition and aTexPosition handles with the verticesBuffer and the textureBuffer respectively.
	 7.Use glBindTexture to bind the texture (passed as an argument to the draw method) to the fragment shader.
	 8.Clear the contents of the GLSurfaceView using glClear.
	 9.Finally, use the glDrawArrays method to actually draw the two triangles (and thus the square).
	 * @param texture
	 */
	public void draw(int texture){
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glUseProgram(program);
		GLES20.glDisable(GLES20.GL_BLEND);

		int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
		int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
		int texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition");

		GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
		GLES20.glEnableVertexAttribArray(texturePositionHandle);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		GLES20.glUniform1i(textureHandle, 0);

		GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer);
		GLES20.glEnableVertexAttribArray(positionHandle);

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}
