package com.adobe.ags.statslogger.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.adobe.ags.statslogger.WorkflowStatsParser;
import com.adobe.ags.statslogger.WorkflowStats;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author brobert
 */
public class JMXTableParseTest {

    static String singleRowTest = "{\"mbean:className\":\"com.adobe.granite.workflow.core.mbean.WorkflowStatsMBeanImpl\","
            + "\"Results\":\"javax.management.openmbean.TabularDataSupport(tabularType=javax.management.openmbean.TabularType("
            + "name=com.adobe.granite.workflow.core.mbean.WorkflowData,rowType=javax.management.openmbean.CompositeType(name="
            + "com.adobe.granite.workflow.core.mbean.WorkflowData,items=((itemName=rateMax,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Float)),(itemName=rateMid,itemType=javax.management.openmbean.SimpleType(name="
            + "java.lang.Float)),(itemName=rateMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),"
            + "(itemName=timeMax,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMid,"
            + "itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMin,itemType=javax.management."
            + "openmbean.SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax.management.openmbean.SimpleType("
            + "name=java.lang.Long)),(itemName=totalAllTime,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long"
            + ")),(itemName=workflow,itemType=javax.management.openmbean.SimpleType(name=java.lang.String)))),indexNames="
            + "(workflow)),contents={[DAM Update Asset]=javax.management.openmbean.CompositeDataSupport(compositeType=javax."
            + "management.openmbean.CompositeType(name=com.adobe.granite.workflow.core.mbean.WorkflowData,items=((itemName="
            + "rateMax,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),(itemName=rateMid,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Float)),(itemName=rateMin,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Float)),(itemName=timeMax,itemType=javax.management.openmbean.SimpleType(name=java."
            + "lang.Long)),(itemName=timeMid,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName="
            + "timeMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Long)),(itemName=totalAllTime,itemType=javax.management"
            + ".openmbean.SimpleType(name=java.lang.Long)),(itemName=workflow,itemType=javax.management.openmbean.SimpleType("
            + "name=java.lang.String)))),contents={rateMax=46.0, rateMid=10.0, rateMin=0.0, timeMax=4443, timeMid=3580, "
            + "timeMin=1508, total=46, totalAllTime=46, workflow=DAM Update Asset})})\",\"DataRate\":300,\"sling:resourceType\""
            + ":\"sling:mbean\",\"mbean:description\":\"Workflow Stats MBean\",\"DataLifeTime\":3600,\"DataFidelityTime\":60,"
            + "\"mbean:objectName\":\"com.adobe.granite.workflow:type=Statistics\",\"DataProcessRate\":60}";
    static String twoRowTest = "{"
            + "\"mbean:className\": \"com.adobe.granite.workflow.core.mbean.WorkflowStatsMBeanImpl\","
            + "\"Results\": \"javax.management.openmbean.TabularDataSupport(tabularType=javax.management.openmbean.TabularType("
            + "name=com.adobe.granite.workflow.core.mbean.WorkflowData,rowType=javax.management.openmbean.CompositeType(name="
            + "com.adobe.granite.workflow.core.mbean.WorkflowData,items=((itemName=rateMax,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Float)),(itemName=rateMid,itemType=javax.management.openmbean.SimpleType(name=java."
            + "lang.Float)),(itemName=rateMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),(itemName="
            + "timeMax,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMid,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMin,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax.management.openmbean.SimpleType(name=java.lang."
            + "Long)),(itemName=totalAllTime,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName="
            + "workflow,itemType=javax.management.openmbean.SimpleType(name=java.lang.String)))),indexNames=(workflow)),"
            + "contents={[DAM Set last modified date]=javax.management.openmbean.CompositeDataSupport(compositeType=javax."
            + "management.openmbean.CompositeType(name=com.adobe.granite.workflow.core.mbean.WorkflowData,items=((itemName="
            + "rateMax,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),(itemName=rateMid,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Float)),(itemName=rateMin,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Float)),(itemName=timeMax,itemType=javax.management.openmbean.SimpleType(name=java."
            + "lang.Long)),(itemName=timeMid,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName="
            + "timeMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Long)),(itemName=totalAllTime,itemType=javax.management."
            + "openmbean.SimpleType(name=java.lang.Long)),(itemName=workflow,itemType=javax.management.openmbean.SimpleType"
            + "(name=java.lang.String)))),contents={rateMax=0.0, rateMid=0.0, rateMin=0.0, timeMax=0, timeMid=0, timeMin=0,"
            + " total=0, totalAllTime=0, workflow=DAM Set last modified date}), [DAM Update Asset]=javax.management."
            + "openmbean.CompositeDataSupport(compositeType=javax.management.openmbean.CompositeType(name=com.adobe.granite."
            + "workflow.core.mbean.WorkflowData,items=((itemName=rateMax,itemType=javax.management.openmbean.SimpleType("
            + "name=java.lang.Float)),(itemName=rateMid,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float"
            + ")),(itemName=rateMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),(itemName=timeMax,"
            + "itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMid,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMin,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax.management.openmbean.SimpleType(name=java.lang."
            + "Long)),(itemName=totalAllTime,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName="
            + "workflow,itemType=javax.management.openmbean.SimpleType(name=java.lang.String)))),contents={rateMax=0.0, "
            + "rateMid=0.0, rateMin=0.0, timeMax=0, timeMid=0, timeMin=0, total=0, totalAllTime=46, workflow=DAM Update Asset})})\","
            + "\"DataRate\": 300,"
            + "\"sling:resourceType\": \"sling:mbean\","
            + "\"mbean:description\": \"Workflow Stats MBean\","
            + "\"DataLifeTime\": 3600,"
            + "\"DataFidelityTime\": 60,"
            + "\"mbean:objectName\": \"com.adobe.granite.workflow:type=Statistics\","
            + "\"DataProcessRate\": 60"
            + "}";
    static String bigNumberTest = "{\"mbean:className\":\"com.adobe.granite.workflow.core.mbean.WorkflowStatsMBeanImpl\","
            + "\"Results\":\"javax.management.openmbean.TabularDataSupport(tabularType=javax.management.openmbean.TabularType("
            + "name=com.adobe.granite.workflow.core.mbean.WorkflowData,rowType=javax.management.openmbean.CompositeType(name="
            + "com.adobe.granite.workflow.core.mbean.WorkflowData,items=((itemName=rateMax,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Float)),(itemName=rateMid,itemType=javax.management.openmbean.SimpleType(name="
            + "java.lang.Float)),(itemName=rateMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),"
            + "(itemName=timeMax,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMid,"
            + "itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=timeMin,itemType=javax.management."
            + "openmbean.SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax.management.openmbean.SimpleType("
            + "name=java.lang.Long)),(itemName=totalAllTime,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long"
            + ")),(itemName=workflow,itemType=javax.management.openmbean.SimpleType(name=java.lang.String)))),indexNames="
            + "(workflow)),contents={[DAM Update Asset]=javax.management.openmbean.CompositeDataSupport(compositeType=javax."
            + "management.openmbean.CompositeType(name=com.adobe.granite.workflow.core.mbean.WorkflowData,items=((itemName="
            + "rateMax,itemType=javax.management.openmbean.SimpleType(name=java.lang.Float)),(itemName=rateMid,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Float)),(itemName=rateMin,itemType=javax.management.openmbean."
            + "SimpleType(name=java.lang.Float)),(itemName=timeMax,itemType=javax.management.openmbean.SimpleType(name=java."
            + "lang.Long)),(itemName=timeMid,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName="
            + "timeMin,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=total,itemType=javax."
            + "management.openmbean.SimpleType(name=java.lang.Long)),(itemName=totalAllTime,itemType=javax.management"
            + ".openmbean.SimpleType(name=java.lang.Long)),(itemName=workflow,itemType=javax.management.openmbean.SimpleType("
            + "name=java.lang.String)))),contents={rateMax=46.0, rateMid=10.0, rateMin=0.0, timeMax=1450886750454, timeMid=1450886750453, "
            + "timeMin=1450886750452, total=46, totalAllTime=46, workflow=DAM Update Asset})})\",\"DataRate\":300,\"sling:resourceType\""
            + ":\"sling:mbean\",\"mbean:description\":\"Workflow Stats MBean\",\"DataLifeTime\":3600,\"DataFidelityTime\":60,"
            + "\"mbean:objectName\":\"com.adobe.granite.workflow:type=Statistics\",\"DataProcessRate\":60}";

    public JMXTableParseTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testParseSingle() {
        Map<String, WorkflowStats> stats = WorkflowStatsParser.parseJMXStatsData(singleRowTest);
        assertNotNull(stats);
        assertTrue("Stats must have one entry", stats.size() == 1);
        WorkflowStats damAssetStats = stats.get("DAM Update Asset");
        assertNotNull(damAssetStats);

        assertEquals(46.0, damAssetStats.getRateMax(), 0.005);
        assertEquals(10.0, damAssetStats.getRateMid(), 0.005);
        assertEquals(0.0, damAssetStats.getRateMin(), 0.005);
        assertEquals(4443, damAssetStats.getTimeMax());
        assertEquals(3580, damAssetStats.getTimeMid());
        assertEquals(1508, damAssetStats.getTimeMin());
        assertEquals(46, damAssetStats.getTotal());
        assertEquals(46, damAssetStats.getTotalAllTime());
        assertEquals("DAM Update Asset", damAssetStats.getWorkflow());
    }

    @Test
    public void testParseDouble() {
        Map<String, WorkflowStats> stats = WorkflowStatsParser.parseJMXStatsData(twoRowTest);
        assertNotNull(stats);
        assertTrue("Stats must have two entries", stats.size() == 2);
        WorkflowStats damAssetStats = stats.get("DAM Update Asset");
        assertNotNull(damAssetStats);
        assertEquals("DAM Update Asset", damAssetStats.getWorkflow());
        
        WorkflowStats lastModDate = stats.get("DAM Set last modified date");
        assertNotNull(lastModDate);
        assertEquals("DAM Set last modified date", lastModDate.getWorkflow());
    }

    @Test
    public void testParseBigNumbers() {
        Map<String, WorkflowStats> stats = WorkflowStatsParser.parseJMXStatsData(bigNumberTest);
        assertNotNull(stats);
        assertTrue("Stats must have one entry", stats.size() == 1);
        WorkflowStats damAssetStats = stats.get("DAM Update Asset");
        assertNotNull(damAssetStats);

        assertEquals(46.0, damAssetStats.getRateMax(), 0.005);
        assertEquals(10.0, damAssetStats.getRateMid(), 0.005);
        assertEquals(0.0, damAssetStats.getRateMin(), 0.005);
        assertEquals(1450886750454L, damAssetStats.getTimeMax());
        assertEquals(1450886750453L, damAssetStats.getTimeMid());
        assertEquals(1450886750452L, damAssetStats.getTimeMin());
        assertEquals(46, damAssetStats.getTotal());
        assertEquals(46, damAssetStats.getTotalAllTime());
        assertEquals("DAM Update Asset", damAssetStats.getWorkflow());
    }
    
}
