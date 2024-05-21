package com.eng1.heslingtonhustle;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Application;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import static org.mockito.ArgumentMatchers.anyString;


public class GdxTestRunner extends BlockJUnit4ClassRunner implements ApplicationListener {

	private Map<FrameworkMethod, RunNotifier> invokeInRender = new HashMap<FrameworkMethod, RunNotifier>();
	
	public GdxTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		new HeadlessApplication(this, conf);

		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = mock(GL20.class);
		Gdx.files = mock(Files.class);
        Gdx.audio = mock(Audio.class);
		Gdx.app = mock(Application.class);

		FileHandle mockFileHandle = mock(FileHandle.class);
		when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);
		when(mockFileHandle.exists()).thenReturn(true);
		when(mockFileHandle.name()).thenReturn("mockTexture.png");

	}

	@Override
	public void create() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		synchronized (invokeInRender) {
			for (Map.Entry<FrameworkMethod, RunNotifier> each : invokeInRender.entrySet()) {
				super.runChild(each.getKey(), each.getValue());
			}
			invokeInRender.clear();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		synchronized (invokeInRender) {
			// add for invoking in render phase, where gl context is available
			invokeInRender.put(method, notifier);
		}
		// wait until that test was invoked
		waitUntilInvokedInRenderMethod();
	}

	/**
	    *
	    */
	private void waitUntilInvokedInRenderMethod() {
		try {
			while (true) {
				Thread.sleep(10);
				synchronized (invokeInRender) {
					if (invokeInRender.isEmpty())
						break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
