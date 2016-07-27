package com.github.ailyenko.bigquery_schema_parser;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Google BigQuery Parser which transforms usual BigQuery JSON schema (both representations - String and JsonObject) or
 * QueryResponse into List of usual JsonElement for further processing. Each JSON element contains all fields and values
 * of the given row and can be mapped to a POJO object with the help of Gson.class
 *
 * @author Alex Ilyenko
 * @see QueryResponse
 * @see JsonObject
 * @see JsonElement
 * @see com.google.gson.Gson#fromJson(JsonElement, Type)
 * @see <a href="https://cloud.google.com/bigquery/docs/">Google BigQuery API Documentation</a>
 * @see <a href="https://cloud.google.com/bigquery/docs/reference/v2/jobs/getQueryResults">BigQuery getQueryResults Job</a>
 * @see <a href="https://cloud.google.com/bigquery/docs/reference/v2/jobs/query#response">BigQuery query Response</a>
 */
public class BigQuerySchemaParser {
    private static final JsonParser PARSER = new JsonParser();

    /**
     * Parses Google BigQuery {@code QueryResponse} object into {@code List<JsonElement>}
     *
     * @param queryResponse representing given response from Google BigQuery
     * @return List of JsonElements representing each row in the response
     * @see QueryResponse
     * @see #parse(String)
     */
    public static List<JsonElement> parse(QueryResponse queryResponse) {
        return parse(queryResponse.toString());
    }

    /**
     * Parses {@code String} representation of Google BigQuery {@code QueryResponse} or {@code GetQueryResultsResponse}
     * object into {@code List<JsonElement>}
     *
     * @param jsonString representing given {@code String} representation of the response from Google BigQuery
     * @return List of JsonElements representing each row in the response
     * @see QueryResponse
     * @see #parse(JsonObject)
     */
    public static List<JsonElement> parse(String jsonString) {
        JsonObject json = PARSER.parse(jsonString).getAsJsonObject();
        return parse(json);
    }

    /**
     * Parses {@code JsonObject} representation of Google BigQuery {@code QueryResponse} or {@code GetQueryResultsResponse}
     * object into {@code List<JsonElement>}
     * Note: field names are parsed sequentially but rows because of their potentially large number are parsed in
     * parallel so their order is not guaranteed.
     *
     * @param jsonObject representing given {@code JsonObject} received from Google BigQuery
     * @return List of JsonElements representing each row in the response
     * @see QueryResponse
     * @see #parse(JsonObject)
     */
    public static List<JsonElement> parse(JsonObject jsonObject) {
        List<String> fieldNames = stream(jsonObject.getAsJsonObject("schema")
                .getAsJsonArray("fields"), false)
                .map(JsonElement::getAsJsonObject)
                .map(field -> field.get("name"))
                .map(JsonElement::getAsString)
                .collect(toList());

        return stream(jsonObject.getAsJsonArray("rows"), true)
                .map(row -> transformRow(row, fieldNames))
                .collect(toList());
    }

    private static Stream<JsonElement> stream(JsonArray array, boolean parallel) {
        return StreamSupport.stream(array.spliterator(), parallel);
    }

    private static JsonObject transformRow(JsonElement row, List<String> fieldNames) {
        JsonObject el = new JsonObject();
        JsonArray f = getF(row);
        int size = fieldNames.size();
        IntStream.range(0, size).forEach(i ->
                el.add(fieldNames.get(i), getV(f.get(i))));
        return el;
    }

    private static JsonArray getF(JsonElement el) {
        return el.getAsJsonObject().getAsJsonArray("f");
    }

    private static JsonElement getV(JsonElement el) {
        return el.getAsJsonObject().get("v");
    }
}
