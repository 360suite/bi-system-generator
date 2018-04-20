package com.gbs.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceObject {
	String internalId;
	String name;
	Set<DBField> dbFields;
}
