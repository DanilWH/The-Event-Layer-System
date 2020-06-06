package com.javacore.app.sandbox;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import com.javacore.app.events.Dispatcher;
import com.javacore.app.events.Event;
import com.javacore.app.events.types.MouseMotionEvent;
import com.javacore.app.events.types.MousePressedEvent;
import com.javacore.app.events.types.MouseReleasedEvent;
import com.javacore.app.layers.Layer;

public class Example extends Layer {
	
	private String name;
	private Color color;
	private Rectangle rect;
	private boolean dragging = false;
	private int px, py;
	
	private static final Random random = new Random();
	
	public Example(String name, Color color) {
		this.name = name;
		this.color = color;
		
		this.rect = new Rectangle(random.nextInt(100) + 150, random.nextInt(100) + 250, 120, 240);
	}
	
	@Override
	public void onEvent(Event event) {
		Dispatcher dispatcher = new Dispatcher(event);
		
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onPressed((MousePressedEvent) e));
		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onReleased((MouseReleasedEvent) e));
		dispatcher.dispatch(Event.Type.MOUSE_MOVED, (Event e) -> onMoved((MouseMotionEvent) e));
		
		
	}
	
	@Override
	public void onRender(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.rect.x, this.rect.y, this.rect.width, this.rect.height);
		
		g.setColor(Color.WHITE);
		g.drawString(this.name, this.rect.x + 15, this.rect.y + 15);
	}
	
	private boolean onPressed(MousePressedEvent event) {
		if (this.rect.contains(new Point(event.getX(), event.getY())))
			this.dragging = true;
		return this.dragging;
	}
	
	private boolean onReleased(MouseReleasedEvent event) {
		this.dragging = false;
		return this.dragging;
	}
	
	private boolean onMoved(MouseMotionEvent event) {
		if (this.dragging) {
			this.rect.x += event.getX() - this.px;
			this.rect.y += event.getY() - this.py;
		}
		
		this.px = event.getX();
		this.py = event.getY();
		
		return this.dragging;
	}
}
