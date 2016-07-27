# BigQuery Schema Parser
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

