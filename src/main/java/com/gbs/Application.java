package com.gbs;

import java.util.Set;

import com.gbs.model.Document;
import com.gbs.model.Source;
import com.gbs.model.SourceObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.andreinc.mockneat.MockNeat;

public class Application {

	public static void main(String[] args) {
		MockNeat mockNeat = MockNeat.threadLocal();
		Gson gson = new GsonBuilder()
		                        .setPrettyPrinting()
		                        .create();

		String[] docType = {"Web Intelligence", "Desktop Intelligence", "Xcelsius" };
		
		Set<Source> sources = mockNeat
                .reflect(Source.class)
                .field("internalId", mockNeat.uuids())
                .field("name", mockNeat.regex("universe-[a-z]{2}"))
                .field("internalId", mockNeat.uuids())
                .field("sourceObjects",
                           mockNeat.reflect(SourceObject.class)
                                   .field("internalId", mockNeat.uuids())
                                   .field("name", mockNeat.regex("universeObject\\d{5}"))
                                   .set(10))
                .set(100)
                .val();
		

		System.out.println(gson.toJson(sources));
		
		Set<Document> docs = mockNeat
                .reflect(Document.class)
                .field("name", mockNeat.words())
                .field("type", mockNeat.from(docType))
                .field("internalId", mockNeat.uuids())
                .field("sourceObjects",
                           mockNeat.reflect(SourceObject.class)
                                   .field("internalId", mockNeat.uuids())
                                   .field("name", mockNeat.regex("universeObject\\d{5}"))
                                   .set(10))
                .field("source",
                        mockNeat.reflect(Source.class)
                                .field("internalId", mockNeat.uuids())
                                .field("name", mockNeat.regex("universe-[a-z]{2}"))
                                .set(2))
                .set(10000)
                .val();
		

		System.out.println(gson.toJson(docs));

	}
}
