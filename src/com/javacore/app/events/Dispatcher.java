package com.javacore.app.events;

public class Dispatcher {
	
	private Event event;
	
	public Dispatcher(Event event) {
		this.event = event;
	}
	
	public void dispatch(Event.Type type, EventHandler handler) {
		if (this.event.handled)
			return;
		
		if (this.event.getType() == type)
			this.event.handled = handler.handle(this.event);
	}
}
