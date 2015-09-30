package com.ruinedlabs.gl_playground;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zack on 9/26/15.
 */
public  class CoolRenderer implements GLSurfaceView.Renderer {

    private Triangle triangle;
    private Cube cube;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        triangle = new Triangle();
        cube = new Cube();
    }

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private float[] mRotationMatrix2 = new float[16];



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 0.1f, 1000.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, 90.0f, ratio, 0.1f, 1000.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);


        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 40000L;
        Matrix.setRotateM(mRotationMatrix, 0, mAngleY, 1.f, 0, 0);
        Matrix.setRotateM(mRotationMatrix2, 0, mAngleX, 0, 1.0f, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mRotationMatrix, 0, mRotationMatrix2, 0);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, scratch, 0);

        // Draw triangle
        //triangle.draw(scratch);

        //Matrix.translateM(scratch, 0, scratch, 0, -2.0f, 0, 0);
        cube.draw(scratch);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public volatile float mAngleX;

    public float getAngleX() {
        return mAngleX;
    }

    public void setAngleX(float angle) {
        mAngleX = angle;
    }

    public volatile float mAngleY;

    public float getAngleY() {
        return mAngleY;
    }

    public void setAngleY(float angle) {
        mAngleY = angle;
    }

}