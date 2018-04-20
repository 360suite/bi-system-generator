package com.gbs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.gbs.model.DBField;
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
	private static int REPORT_COUNT = 10000;
	private static int REPORT_SOURCE_MIN = 1;
	private static int REPORT_SOURCE_MAX = 4;
	private static int REPORT_SOURCEOBJ_MIN = 5;
	private static int REPORT_SOURCEOBJ_MAX = 10;
	private static String[] docType = {"Web Intelligence", "Desktop Intelligence", "Xcelsius" };
	private static String[] tableName = {"CMS_AL", "CMS_AL_CONTENT", "CMS_APPFOLDER", "CMS_APPLICATION", "CMS_BV", "CMS_BV_BE", "CMS_BV_BE_BF", "CMS_BV_BE_LINK", "CMS_BV_CNX", "CMS_BV_CNX_LINK", "CMS_BV_DF", "CMS_BV_LOV", "CMS_BV_LOV_CR", "CMS_CALENDAR", "CMS_CALENDAR_DAY", "CMS_CONNECTION", "CMS_DOCUMENT", "CMS_DOCUMENT_PROMPT", "CMS_DOC_CATEGORY", "CMS_EMAIL_RECEIVER", "CMS_EVENT", "CMS_EVENT_SCHED", "CMS_FOLDER", "CMS_GROUP", "CMS_GROUP_HIERARCHY", "CMS_LIMIT", "CMS_OVERLOAD", "CMS_OVERLOAD_OBJ", "CMS_OVERLOAD_ROW", "CMS_OVERLOAD_TABLE", "CMS_PERSONAL_FIELD", "CMS_PROFILE", "CMS_PROFILE_PUBLI", "CMS_PROFILE_TARGET", "CMS_PROFILE_VALUE", "CMS_PROMOTION_JOB", "CMS_PROMOTION_JOB_OBJ", "CMS_PUBLICATION_DOC", "CMS_QAAWS", "CMS_REPORT_UNIVERSE", "CMS_SECURITY", "CMS_SECURITY_FINE", "CMS_SERVER", "CMS_SERVER_PROPERTY", "CMS_TIMESTAMP", "CMS_UNIVERSE", "CMS_USER", "CMS_USER_ALIAS", "CMS_USER_APP", "CMS_USER_CONNECTION", "CMS_USER_GROUP", "CMS_USER_SESSION", "DATABASECHANGELOG", "DATABASECHANGELOGLOCK", "EYE_ACTION", "EYE_ACTION_PROMPT", "EYE_ACTION_TEMP", "EYE_COMPLIANCE_ACCESS", "EYE_COMPLIANCE_INFO", "EYE_COMPLIANCE_SERVER_INFO", "EYE_COMPLIANCE_USER", "EYE_CONTENT", "EYE_CRYSTAL", "EYE_CRYSTAL_CONNEXION", "EYE_CRYSTAL_FIELD", "EYE_CRYSTAL_TABLE", "EYE_DATAPROVIDER", "EYE_DATAPROVIDER_PROMPT", "EYE_DATAPROVIDER_SQL", "EYE_DOCUMENT", "EYE_ERROR", "EYE_QUERY", "EYE_QUERY_OBJ", "EYE_SIMILARITY_RATIO", "EYE_SNAPSHOT", "EYE_TRANSLATION_ITEM", "EYE_TRANSLATION_LOCALE", "EYE_UNIVERSE", "EYE_UNIVERSE_CLASS", "EYE_UNIVERSE_COL", "EYE_UNIVERSE_CONN", "EYE_UNIVERSE_CTX", "EYE_UNIVERSE_CTX_JN", "EYE_UNIVERSE_DERIV_SQL", "EYE_UNIVERSE_JOIN", "EYE_UNIVERSE_LOV", "EYE_UNIVERSE_OBJ", "EYE_UNIVERSE_OBJ_COL", "EYE_UNIVERSE_OBJ_TBL", "EYE_UNIVERSE_PARAM", "EYE_UNIVERSE_PERSPECTIVE", "EYE_UNIVERSE_PERSP_OBJ", "EYE_UNIVERSE_TABLE", "EYE_VARIABLE"};
	private static String[] columnName = {"COL1", "COL2", "COL3", "COL4", "COL5", "COL6", "COL7", "COL8", "COL9", "COL10"};



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
							.field("dbFields",
									mockNeat.reflect(DBField.class)
									.field("table", mockNeat.from(tableName))
									.field("column", mockNeat.from(columnName))
									.set(2))
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
