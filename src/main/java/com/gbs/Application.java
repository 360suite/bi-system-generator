package com.gbs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.gbs.model.Document;
import com.gbs.model.Source;
import com.gbs.model.SourceObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.andreinc.mockneat.MockNeat;

public class Application {
	private static int SOURCE_COUNT = 50;
	private static int SOURCEOBJ_MIN = 20;
	private static int SOURCEOBJ_MAX = 40;
	private static int REPORT_COUNT = 1000;
	private static int REPORT_SOURCE_MIN = 1;
	private static int REPORT_SOURCE_MAX = 4;
	private static int REPORT_SOURCEOBJ_MIN = 5;
	private static int REPORT_SOURCEOBJ_MAX = 10;
	private static String[] docType = {"Web Intelligence", "Desktop Intelligence", "Xcelsius" };


	public static void main(String[] args) {
		MockNeat mockNeat = MockNeat.threadLocal();
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();



		Set<Source> sources = new HashSet<>();
		for (int i = 0; i < SOURCE_COUNT; i++) {
			Source source = mockNeat
					.reflect(Source.class)
					.field("internalId", mockNeat.uuids())
					.field("name", mockNeat.regex("universe-[a-z]{2}"))
					.field("internalId", mockNeat.uuids())
					.field("sourceObjects",
							mockNeat.reflect(SourceObject.class)
							.field("internalId", mockNeat.uuids())
							.field("name", mockNeat.regex("universeObject\\d{4}"))
							.set(mockNeat.ints().range(SOURCEOBJ_MIN, SOURCEOBJ_MAX).val()))
					.val();
			sources.add(source);
		}
		try {
			FileWriter writer = new FileWriter("sources.json");
			writer.write(gson.toJson(sources));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		Set<Document> documents = new HashSet<>();
		for (int i = 0; i < REPORT_COUNT; i++) {
			int sourceCount = mockNeat.ints().range(REPORT_SOURCE_MIN,REPORT_SOURCE_MAX).val();
			//System.out.println("sources count: " + sourceCount);

			Document doc = mockNeat
					.reflect(Document.class)
					.field("name", mockNeat.words())
					.field("type", mockNeat.from(docType))
					.field("internalId", mockNeat.uuids())
					.val();

			doc.setSource(new HashSet<>());
			doc.setSourceObjects(new HashSet<>());

			for (int j = 0; j < sourceCount; j++) {
				int sourceObjCount = mockNeat.ints().range(REPORT_SOURCEOBJ_MIN,REPORT_SOURCEOBJ_MAX).val();
				Source src = mockNeat.from(sources.toArray(new Source[0])).val();
				doc.getSource().add(mockNeat.reflect(Source.class)
						.field("internalId", src.getInternalId())
						.field("name", src.getName())
						.val());

				doc.getSourceObjects().addAll(mockNeat
						.from(src.getSourceObjects().toArray(new SourceObject[0]))
						.set(sourceObjCount)
						.val());
			}
			documents.add(doc);
		}

		try {
			FileWriter writer = new FileWriter("reports.json");
			writer.write(gson.toJson(documents));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
