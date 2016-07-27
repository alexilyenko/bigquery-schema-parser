# BigQuery Schema Parser

Google BigQuery Parser which transforms usual BigQuery JSON schema (both its representations - String and JsonObject) or
QueryResponse (Google BigQuery SDK class) into List of usual JsonElement for further processing. Each JSON element contains all fields and values of the given row and can be mapped to a POJO object with the help of Gson library.

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
can be transformed to List\<JsonElement\>:
```java
[{"version":"9.3.3","platform":"ios"}, {"version":"4.4.4","platform":"android"}]
```

