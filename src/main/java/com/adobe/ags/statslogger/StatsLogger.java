package com.adobe.ags.statslogger;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class StatsLogger {

    static String jmxStatsURI = "/system/sling/monitoring/mbeans/com/adobe/granite/workflow/Statistics/%7Bnoname%7D.json";
    static String resetStatisticsURI = "/system/console/jmx/com.adobe.granite.workflow%3Atype%3DStatistics/op/clearRecords/";
    static String counterQueryURI="/bin/querybuilder.json?path=%2fvar%2fstatistics%2fworkflows%2fetc&property=jcr%3acontent%2fmodel%2f.stats%2f%40node0_currentCount&property.operation=exists&p.hits=full&p.nodedepth=3&p.guessTotal=true&p.limit=-1";
    static String workflowModelListURI="/bin/querybuilder.json?path=%2fetc%2fworkflow%2fmodels&property=%40cq%3atemplate&property.value=%2flibs%2fcq%2fworkflow%2ftemplates%2fmodel&p.guessTotal=true&p.limit=-1&p.hits=selective&p.properties=jcr:title%20jcr:path";
    
    static Options options = new Options();
    static Gson gson = new Gson();

    public static void main(String[] args) throws MalformedURLException, IOException {
        CommandLine cmd = getParsedAndValidatedCmdArgs(args);
        configureAuthenticator(cmd);
        String stats = generateStatistics(cmd);
        if (cmd.hasOption('r')) {
            resetStatistics(cmd);
        }

        File latest = new File(cmd.getOptionValue('f'), "workflowStats.json");
        try (Writer fileWriter = new FileWriter(latest, false)) {
            fileWriter.write(stats);
            fileWriter.close();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String logName = "workflowStats_" + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH) + 1) + "_" + cal.get(Calendar.DAY_OF_MONTH) + ".json";
        File dailyLog = new File(cmd.getOptionValue('f'), logName);
        long oldLength = dailyLog.length();
        boolean isNew = !dailyLog.exists() || oldLength < 3;

        try (RandomAccessFile log = new RandomAccessFile(dailyLog, "rw")) {
            PrintWriter writer = new PrintWriter(Channels.newOutputStream(log.getChannel()));
            if (isNew) {
                writer.println("{'entries':[");
            } else {
                log.seek(oldLength - 4);
                writer.println(",");
            }
            writer.println(stats);
            writer.println("]}");
            writer.flush();
            log.close();
        }
    }

    private static String generateStatistics(CommandLine cmd) throws IOException {
        URL modelInfoUrl = buildUrl(cmd, workflowModelListURI);
        Map<String, String> modelMapping = WorkflowStatsParser.extractPaths(new InputStreamReader((InputStream) modelInfoUrl.getContent()));        
        URL modelCountersUrl = buildUrl(cmd, counterQueryURI);
        Map<String, WorkflowStats> counters = WorkflowStatsParser.extractCounts(new InputStreamReader((InputStream) modelCountersUrl.getContent()), modelMapping);
        URL jmxStatsUrl = buildUrl(cmd, jmxStatsURI);
        Map<String, WorkflowStats> stats = WorkflowStatsParser.parseJMXStatsData(new InputStreamReader((InputStream) jmxStatsUrl.getContent()));
        WorkflowStatsParser.mergeCountsWithStats(stats, counters);
        Map<String, Object> outputData = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        outputData.put("Date", SimpleDateFormat.getDateTimeInstance().format(new Date()));
        outputData.put("Stats", stats);
        String out = gson.toJson(outputData);
        return out;
    }

    static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar StatsLogger.jar", options);
    }

    static void buildOptions() {
        options.addOption("?", false, "Shows help (this message)");
        options.addOption("h", true, "host:port, defaults to localhost:4502");
        options.addOption("l", true, "location for logs to be written, required");
        options.addOption("p", true, "password, required");
        options.addOption("r", false, "if specified reset JMX statistics");
        options.addOption("s", false, "if specified use HTTPS");
        options.addOption("u", true, "user name, defaults to admin");
    }

    private static CommandLine getParsedAndValidatedCmdArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        buildOptions();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("Error parsing command line arguments: " + ex.getLocalizedMessage());
            printHelp();
            System.exit(1);
        }
        if (cmd.hasOption('?')) {
            printHelp();
            System.exit(0);
        }
        if (!cmd.hasOption('p')) {
            System.err.println("Password was not specified (-? for help)");
            System.exit(1);
        }
        if (!cmd.hasOption('l')) {
            System.err.println("Log directory path not specified (-? for help)");
            System.exit(1);
        }
        File dir = new File(cmd.getOptionValue("l"));
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Specified directory " + cmd.getOptionValue("l") + " does not exist (-? for help)");
            System.exit(1);
        }
        return cmd;
    }

    private static void configureAuthenticator(final CommandLine cmd) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(cmd.getOptionValue('u', "admin"), cmd.getOptionValue('p').toCharArray());
            }
        });
    }

    private static URL buildUrl(CommandLine cmd, String uri) throws MalformedURLException {
        return new URL("http" + (cmd.hasOption('s') ? "s://" : "://") + cmd.getOptionValue('h', "localhost:4502") + uri);
    }

    private static void resetStatistics(CommandLine cmd) throws MalformedURLException, IOException {
        URL url = buildUrl(cmd, resetStatisticsURI);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream())) {
            String content = "";
            printout.writeBytes(content);
            printout.flush();
            printout.close();
        }
    }
}
