# BigQuery Schema Parser
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ailyenko/bigquery-schema-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ailyenko/bigquery-schema-parser)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a3ab51d2635c4b7288e3ee84c131ddea)](https://www.codacy.com/app/aleksey-ilyenko/bigquery-schema-parser?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ailyenko/bigquery-schema-parser&amp;utm_campaign=Badge_Grade)

This is Google BigQuery Parser which transforms usual BigQuery JSON schema (both its representations - String and JsonObject) or
QueryResponse (Google BigQuery Java SDK) into List of usual JsonElements for further processing. Each JsonElement object contains all fields and values of the given row and can be mapped to a POJO object with the help of Gson library.

For example this kind of JSON:
```json
{
  "cacheHit" : false,
  "etag" : "someEtag",
  "jobComplete" : true,
  "jobReference" : {
    "jobId" : "someJobId",
    "projectId" : "someProjectId"
  },
  "kind" : "bigquery#getQueryResultsResponse",
  "rows" : [ {
    "f" : [ {
      "v" : "9.3.3"
    }, {
      "v" : "ios"
  } ]
  }, {
    "f" : [ {
      "v" : "4.4.4"
    }, {
      "v" : "android"
    } ]
  } ],
  "schema" : {
    "fields" : [ {
      "mode" : "NULLABLE",
      "name" : "version",
      "type" : "STRING"
    }, {
      "mode" : "NULLABLE",
      "name" : "platform",
      "type" : "STRING"
    } ]
  },
  "totalBytesProcessed" : "201954676",
  "totalRows" : "2"
}
```
will be transformed to the List\<JsonElement\>:
```java
[{"version":"9.3.3","platform":"ios"}, {"version":"4.4.4","platform":"android"}]
```

**Uses Java 8.**

## Maven dependency
```xml
<dependency>
    <groupId>com.github.ailyenko</groupId>
    <artifactId>bigquery-schema-parser</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Examples 
```java
// from QueryResponse:
QueryResponse queryResponse = bigQuery.jobs().query(projectId, queryRequest).execute();
List<JsonElement> rows = BigQuerySchemaParser.parse(queryResponse);

// from JsonObject:
JsonObject jsonObject = queryResponseJsonElement.getAsJsonObject();
List<JsonElement> rows = BigQuerySchemaParser.parse(jsonObject);

// from String:
List<JsonElement> rows = BigQuerySchemaParser.parse(queryResponseJsonString);
```

##Development
You're always welcomed to contribute to the project!:beers:
