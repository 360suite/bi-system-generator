package com.gbs.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document {  
    
	String name;
    String type;
    String internalId;
    Set<SourceObject> sourceObjects;
    Set<Source> source;
    
    public Document() {
    	sourceObjects = new HashSet<>();
    	source = new HashSet<>();
	}
}