package com.yaw.examples;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.PointParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.IParticleInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * @author YaW
 * This class shows an implementation of a Digging effect with the AndEngine ParticleSystem
 */
public class Particle_DigEffect extends BaseGameActivity implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private TextureRegion mPartReg;
	//Number of particles to show
	private int mNumPart = 10;
	//Time to show the particles
	private int mTimePart = 3;
	//Speed of the particles
	private float mSpdInitial = 150.0f;
	private float mSpdParticle = mSpdInitial;
    private float mSpdIncr = 125.0f/mNumPart;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		//Particle
		Texture mPart = new Texture(16, 16, TextureOptions.BILINEAR);
		mPartReg = TextureRegionFactory.createFromAsset(mPart, this, "gfx/particle.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mPart);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		scene.setOnSceneTouchListener(this);

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent touch) {
		createDig(touch.getX(), touch.getY());
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * Generate the dig particle effect
	 * @param posX
	 * @param posY
	 */
	private void createDig(float posX, float posY) {
		//Init the particle system
		final ParticleSystem particleSystem = new ParticleSystem(new PointParticleEmitter(posX, posY), 60*mNumPart, 60*mNumPart, mNumPart, mPartReg);
		
		//Dirt color
		particleSystem.addParticleInitializer(new ColorInitializer(0.5450f, 0.2705f, 0.0745f));
		//They will shrink
		particleSystem.addParticleModifier(new ScaleModifier(1, 0, 0, mTimePart));
		
		final Random generator = new Random();
		
		//Init the particles with random in the angle
		particleSystem.addParticleInitializer(new IParticleInitializer() {
        	 
            @Override
            public void onInitializeParticle(Particle pParticle) {
            	//Create particles between 225º - 315º
            	int ang = generator.nextInt(90) + 225;
            	mSpdParticle -= mSpdIncr;
                float fVelocityX = (float) (Math.cos(Math.toRadians(ang)) * mSpdParticle);
                float fVelocityY = (float) (Math.sin(Math.toRadians(ang)) * mSpdParticle);
                
                pParticle.getPhysicsHandler().setVelocity(fVelocityX, fVelocityY);
                // calculate air resistance that acts opposite to particle velocity
                float fVelXopposite = (fVelocityX * (-1));
                float fVelYopposite = (fVelocityY * (-1));
                // x% of deceleration is applied (that is opposite to velocity)
                pParticle.getPhysicsHandler().setAcceleration(fVelXopposite * (50/100.0f), fVelYopposite * (75/100.0f));
            }
        });
        
        mSpdParticle = mSpdInitial;
        
		mEngine.getScene().attachChild(particleSystem);
		mEngine.getScene().sortChildren();
		
		//Remove the particles from the scene
		mEngine.registerUpdateHandler(new TimerHandler(mTimePart, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
            	mEngine.getScene().detachChild(particleSystem);
            	mEngine.getScene().sortChildren();
            	mEngine.getScene().unregisterUpdateHandler(pTimerHandler);
            }
        }));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}