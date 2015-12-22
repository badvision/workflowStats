package com.adobe.ags.statslogger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class WorkflowStatsParser {

    static JsonParser parser = new JsonParser();
    static Gson gson = new Gson();

    public static Map<String, WorkflowStats> parseJMXStatsData(String data) {
        return _parseJMXStatsData(parser.parse(data));
    }

    public static Map<String, WorkflowStats> parseJMXStatsData(Reader data) {
        return _parseJMXStatsData(parser.parse(data));
    }

    public static Map<String, String> extractPaths(String data) {
        return _extractPaths(parser.parse(data));
    }

    public static Map<String, String> extractPaths(Reader data) {
        return _extractPaths(parser.parse(data));
    }

    public static Map<String, WorkflowStats> extractCounts(String data, Map<String, String> models) {
        return _extractCounts(parser.parse(data), models);
    }

    public static Map<String, WorkflowStats> extractCounts(Reader data, Map<String, String> models) {
        return _extractCounts(parser.parse(data), models);
    }

    private static Map<String, WorkflowStats> _parseJMXStatsData(JsonElement data) {
        Map<String, WorkflowStats> results = new LinkedHashMap<>();
        String rawData = data.getAsJsonObject().get("Results").getAsString();
        int idx = 0;
        while ((idx = rawData.indexOf("contents={rateMax", idx + 1)) > 0) {
            int endIdx = rawData.indexOf('}', idx);
            String row = rawData.substring(idx + 9, endIdx);
            row = row.replace("workflow=", "workflow=\"");
            row += "\"}";
            WorkflowStats stats = gson.fromJson(row, WorkflowStats.class);
            results.put(stats.getWorkflow(), stats);
        }

        return results;
    }

    private static Map<String, String> _extractPaths(JsonElement parse) {
        Map<String, String> output = new TreeMap<>();
        JsonArray hits = parse.getAsJsonObject().get("hits").getAsJsonArray();
        for (Iterator<JsonElement> iterator = hits.iterator(); iterator.hasNext();) {
            JsonObject model = iterator.next().getAsJsonObject();
            String path = model.get("jcr:path").getAsString().replace("/jcr:content", "");
            String title = model.get("jcr:title").getAsString();
            output.put(path, title);
        }
        return output;
    }

    private static Map<String, WorkflowStats> _extractCounts(JsonElement parse, Map<String, String> models) {
        Map<String, WorkflowStats> output = new LinkedHashMap<>();
        JsonArray hits = parse.getAsJsonObject().get("hits").getAsJsonArray();
        for (Iterator<JsonElement> iterator = hits.iterator(); iterator.hasNext();) {
            JsonObject model = iterator.next().getAsJsonObject();
            WorkflowStats stats = new WorkflowStats();
            String path = model.get("jcr:path").getAsString().replace("/var/statistics/workflows", "");
            JsonObject jsonStats = model
                    .get("jcr:content").getAsJsonObject()
                    .get("model").getAsJsonObject()
                    .get(".stats").getAsJsonObject();
            stats.setWorkflowPath(path);
            stats.setWorkflow(models.get(path));
            stats.setInstanceCount(jsonStats.getAsJsonPrimitive("node0_totalCount").getAsInt());
            stats.setPendingCount(jsonStats.getAsJsonPrimitive("node0_currentCount").getAsInt());
            output.put(stats.getWorkflow(), stats);            
        }
        return output;
    }
    
    public static void mergeCountsWithStats(Map<String, WorkflowStats> stats, Map<String, WorkflowStats> counts) {
        for (WorkflowStats count : counts.values()) {
            if (stats.containsKey(count.getWorkflow())) {
                WorkflowStats mergedStat = stats.get(count.getWorkflow());
                mergedStat.setWorkflowPath(count.getWorkflowPath());
                mergedStat.setInstanceCount(count.getInstanceCount());
                mergedStat.setPendingCount(count.getPendingCount());
            } else {
                stats.put(count.getWorkflow(), count);
            }
        }
    }

}
