package com.capgemini.model;


public class DestinationJsonResponse {
	private Destination destination;
	   private boolean validated;
	  
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
}