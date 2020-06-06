package com.javacore.app.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.javacore.app.events.Event;
import com.javacore.app.events.types.MouseMotionEvent;
import com.javacore.app.events.types.MousePressedEvent;
import com.javacore.app.events.types.MouseReleasedEvent;
import com.javacore.app.layers.Layer;

public class Window extends Canvas{
	
	private BufferStrategy bs;
	private Graphics g;
	private JFrame frame;
	private List<Layer> layers = new ArrayList<Layer>();
	
	public Window(String name, int width, int height) {
		// set the window size.
		setPreferredSize(new Dimension(width, height));
		// initialize the window.
		init(name);
		
		/*** events process. ***/
		
		// processing mouse click.
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				MousePressedEvent event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
				onEvent(event);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
				onEvent(event);
			}
		});
		
		// processing mouse motion.
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				MouseMotionEvent event = new MouseMotionEvent(e.getX(), e.getY(), false);
				onEvent(event);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				MouseMotionEvent event = new MouseMotionEvent(e.getX(), e.getY(), true);
				onEvent(event);
			}
		});
		
		// render the window.
		render();
	}
	
	public void init(String name) {
		// create the window.
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		// locate the window in the center of the screen.
		frame.setLocationRelativeTo(null);
		// the window won't be resizable.
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	private void render() {
		if (bs == null)
			createBufferStrategy(3);
		bs = getBufferStrategy();
		
		// get a context where we can draw the graphics.
		g = bs.getDrawGraphics();
		// set the color of the window.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		onRender(g);
		// clear the systems' resource from the previous graphics.
		g.dispose();
		
		// swap the buffers.
		bs.show();
		
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
		}
		
		EventQueue.invokeLater(() -> render());
		/*
		 *         ||
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				render();
			}
		});
		*/
	}
	
	private void onRender(Graphics g) {
		/*** Renders layers from the first created layer to the latest created layer. ***/
		for (int i = 0; i < layers.size(); i++)
			layers.get(i).onRender(g);
	}
	
	private void onEvent(Event event) {
		/*** Processes layers events from the latest created layer to the first created layer. ***/
		for (int i = layers.size() - 1; i >= 0; i--) {
			layers.get(i).onEvent(event);
		}
	}
	
	public void addLayer(Layer layer) {
		/*** Adds one layer to the canvas. ***/
		layers.add(layer);
	}
}
