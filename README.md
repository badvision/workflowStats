# WorkflowStats
Collect and report a unified series of AEM workflow statistics for simplified monitoring purposes.

The overall goal for this is to produce a stand-alone program that collects various sources of workflow statistics and record them in a easy-to-consume JSON data log.  This allows integration with other monitoring solutions and/or web interfaces that are capable of working with JSON data.

# Building

Maven 3+ and Java 7 (or higher) are required to build this project.  All other dependencies are retrieved and bundled in the resulting JAR file.

*mvn clean install* will produce a jar file "StatsLogger-1.0-snapshot.jar" or similar, which you can copy out of there and rename as StatsLogger.jar

# Usage

With java in your path, invocation is simply: **java -jar StatsLogger.jar** *options*
where options are:
```
 -?         Shows help (this message)
 -h <arg>   host:port, defaults to localhost:4502
 -l <arg>   location for logs to be written, required
 -p <arg>   password, required
 -r         if specified reset JMX statistics
 -s         if specified use HTTPS
 -u <arg>   user name, defaults to admin
```

# Output

There are two files output by this program: workflowStats.json and workflow_YYYY_MM_DD.json.  The first file is overwritten each time and indicates the most recent statistics.  The second file is a series of data collected throughout the day and is structured as a JSON array.

Here's an example of the workflowStats.json file:
```
{"Date":"Dec 22, 2015 12:41:14 PM","Stats":{"DAM Set last modified date":
{"rateMax":0.0,"rateMid":0.0,"rateMin":0.0,"timeMax":0,"timeMid":0,"timeMin":0,"total":0,"totalAllTime":46,"pendingCount":0,"instanceCount":46,"workflow":"DAM Set last modified date","workflowPath":"/etc/workflow/models/dam/dam_set_last_modified"}
,"DAM Update Asset":
{"rateMax":0.0,"rateMid":0.0,"rateMin":0.0,"timeMax":0,"timeMid":0,"timeMin":0,"total":0,"totalAllTime":92,"pendingCount":0,"instanceCount":593,"workflow":"DAM Update Asset","workflowPath":"/etc/workflow/models/dam/update_asset"}
,"DAM MetaData Writeback":
{"rateMax":0.0,"rateMid":0.0,"rateMin":0.0,"timeMax":0,"timeMid":0,"timeMin":0,"total":0,"totalAllTime":0,"pendingCount":0,"instanceCount":81,"workflow":"DAM MetaData Writeback","workflowPath":"/etc/workflow/models/dam-xmp-writeback"}
}}
```

For each workflow, you get a stats object that has rateMax, rateMid, rateMin, timeMax, timeMid, timeMin, total, totalAllTime – these come from the JMX bean and will be reset if you use the -r command line argument. The additional merged in stats are workflowPath, pendingCount and instanceCount. totalAllTime and instanceCount will not always line up because "totalAllTime" is a count of all workflows processed since the server was started and "instanceCount" is a count of all instances in the JCR.
