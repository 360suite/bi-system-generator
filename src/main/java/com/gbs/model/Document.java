package com.gbs.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {  
    String name;
    String type;
    String internalId;
    Set<SourceObject> sourceObjects;
    Set<Source> source;
}