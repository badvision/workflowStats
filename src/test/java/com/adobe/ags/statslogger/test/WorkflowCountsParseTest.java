package com.adobe.ags.statslogger.test;

import com.adobe.ags.statslogger.WorkflowStats;
import com.adobe.ags.statslogger.WorkflowStatsParser;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkflowCountsParseTest {

    public static String countsJsonExample
            = "{\"success\":true,\"results\":3,\"total\":3,\"more\":false,\"offset\":0,\"hits\":[{\"jcr:path\""
            + ":\"/var/statistics/workflows/etc/workflow/models/dam-xmp-writeback\",\"jcr:primaryType\":\"nt"
            + ":unstructured\",\"jcr:content\":{\"jcr:primaryType\":\"nt:unstructured\",\"model\":{\"jcr:"
            + "primaryType\":\"nt:unstructured\",\".stats\":{\"jcr:primaryType\":\"nt:unstructured\",\"node0"
            + "_totalCount\":81,\"node1_currentCount\":81,\"node0_totalTime\":1440,\"node0_currentCount\":0}"
            + "}}},{\"jcr:path\":\"/var/statistics/workflows/etc/workflow/models/dam/dam_set_last_modified\""
            + ",\"jcr:primaryType\":\"nt:unstructured\",\"jcr:content\":{\"jcr:primaryType\":\"nt:"
            + "unstructured\",\"model\":{\"jcr:primaryType\":\"nt:unstructured\",\".stats\":{\"jcr:primary"
            + "Type\":\"nt:unstructured\",\"node0_totalCount\":46,\"node1_currentCount\":46,\"node0_total"
            + "Time\":245,\"node0_currentCount\":0}}}},{\"jcr:path\":\"/var/statistics/workflows/etc/"
            + "workflow/models/dam/update_asset\",\"jcr:primaryType\":\"nt:unstructured\",\"jcr:content\":{"
            + "\"jcr:primaryType\":\"nt:unstructured\",\"model\":{\"jcr:primaryType\":\"nt:unstructured\","
            + "\".stats\":{\"jcr:primaryType\":\"nt:unstructured\",\"node0_totalCount\":593,\"node1_current"
            + "Count\":593,\"node0_totalTime\":6123,\"node0_currentCount\":0}}}}]}";

    public static String workflowInfoExample = 
            "{\"success\":true,\"results\":47,\"total\":47,\"more\":false,\"offset\":0,\"hits\":[{\"jcr:path\""
            + ":\"/etc/workflow/models/publish_to_campaign/jcr:content\",\"jcr:title\":\"Publish to Adobe Camp"
            + "aign\"},{\"jcr:path\":\"/etc/workflow/models/dam-indesign-proxy/jcr:content\",\"jcr:title\":\"D"
            + "AM InDesign Proxy\"},{\"jcr:path\":\"/etc/workflow/models/ac-newsletter-workflow-simple/jcr:con"
            + "tent\",\"jcr:title\":\"Approve for Adobe Campaign\"},{\"jcr:path\":\"/etc/workflow/models/dps/u"
            + "pload-content/jcr:content\",\"jcr:title\":\"Upload to DPS\"},{\"jcr:path\":\"/etc/workflow/mode"
            + "ls/projects/approval_workflow/jcr:content\",\"jcr:title\":\"Project Approval Workflow\"},{\"jcr"
            + ":path\":\"/etc/workflow/models/projects/product_photo_shoot/jcr:content\",\"jcr:title\":\"Produ"
            + "ct Photo Shoot (Commerce Integration)\"},{\"jcr:path\":\"/etc/workflow/models/projects/photo_sh"
            + "oot_submission/jcr:content\",\"jcr:title\":\"Product Photo Shoot\"},{\"jcr:path\":\"/etc/workfl"
            + "ow/models/projects/request_launch/jcr:content\",\"jcr:title\":\"Request Launch\"},{\"jcr:path\""
            + ":\"/etc/workflow/models/projects/request_email/jcr:content\",\"jcr:title\":\"Request Email\"},{"
            + "\"jcr:path\":\"/etc/workflow/models/projects/request_copy/jcr:content\",\"jcr:title\":\"Request"
            + " Copy\"},{\"jcr:path\":\"/etc/workflow/models/projects/request_landing_page/jcr:content\",\"jcr"
            + ":title\":\"Request Landing Page\"},{\"jcr:path\":\"/etc/workflow/models/salesforce-com-export/j"
            + "cr:content\",\"jcr:title\":\"Salesforce.com Export\"},{\"jcr:path\":\"/etc/workflow/models/scen"
            + "e7/jcr:content\",\"jcr:title\":\"Scene7\"},{\"jcr:path\":\"/etc/workflow/models/request_for_del"
            + "etion/jcr:content\",\"jcr:title\":\"Request for Deletion\"},{\"jcr:path\":\"/etc/workflow/model"
            + "s/geometrixx/signup/jcr:content\",\"jcr:title\":\"Geometrixx Signup\"},{\"jcr:path\":\"/etc/wor"
            + "kflow/models/publish_example/jcr:content\",\"jcr:title\":\"Publish Example\"},{\"jcr:path\":\"/"
            + "etc/workflow/models/social/commons/comment_moderation/jcr:content\",\"jcr:title\":\"Social Mode"
            + "ration\"},{\"jcr:path\":\"/etc/workflow/models/dam-xmp-writeback/jcr:content\",\"jcr:title\":\""
            + "DAM MetaData Writeback\"},{\"jcr:path\":\"/etc/workflow/models/dam/update_asset_offloading/jcr:"
            + "content\",\"jcr:title\":\"DAM Update Asset Offloading\"},{\"jcr:path\":\"/etc/workflow/models/d"
            + "am/update_from_lightbox/jcr:content\",\"jcr:title\":\"DAM Update from Lightbox\"},{\"jcr:path\""
            + ":\"/etc/workflow/models/dam/dam_set_last_modified/jcr:content\",\"jcr:title\":\"DAM Set last mo"
            + "dified date\"},{\"jcr:path\":\"/etc/workflow/models/dam/update_asset_s7dam_on_premise/jcr:conte"
            + "nt\",\"jcr:title\":\"Dynamic Media Update Asset On-Premise \"},{\"jcr:path\":\"/etc/workflow/mo"
            + "dels/dam/update_asset/jcr:content\",\"jcr:title\":\"DAM Update Asset\"},{\"jcr:path\":\"/etc/wo"
            + "rkflow/models/dam/dam-create-language-copy/jcr:content\",\"jcr:title\":\"DAM Create Language Co"
            + "py\"},{\"jcr:path\":\"/etc/workflow/models/dam/dam-create-and-translate-language-copy/jcr:conte"
            + "nt\",\"jcr:title\":\"DAM Create and Translate Language Copy\"},{\"jcr:path\":\"/etc/workflow/mo"
            + "dels/dam/dam_download_asset/jcr:content\",\"jcr:title\":\"Download Asset\"},{\"jcr:path\":\"/et"
            + "c/workflow/models/dam/adddamsize/jcr:content\",\"jcr:title\":\"Add Asset Size\"},{\"jcr:path\":"
            + "\"/etc/workflow/models/scheduled_activation/jcr:content\",\"jcr:title\":\"Scheduled Page/Asset "
            + "Activation\"},{\"jcr:path\":\"/etc/workflow/models/phonegap/initiate-phonegap-build/jcr:content"
            + "\",\"jcr:title\":\"Initiate PhoneGap Build\"},{\"jcr:path\":\"/etc/workflow/models/reverse_repl"
            + "ication/jcr:content\",\"jcr:title\":\"Reverse Replication\"},{\"jcr:path\":\"/etc/workflow/mode"
            + "ls/cloudservices/DTM_bundle_download/jcr:content\",\"jcr:title\":\"Default DTM Bundle Download"
            + "\"},{\"jcr:path\":\"/etc/workflow/models/cloudservices/publish-test-target-offer/jcr:content\","
            + "\"jcr:title\":\"Publish Adobe Target Offer\"},{\"jcr:path\":\"/etc/workflow/models/request_to_c"
            + "omplete_move_operation/jcr:content\",\"jcr:title\":\"Request to complete Move operation\"},{\"j"
            + "cr:path\":\"/etc/workflow/models/scheduled_deactivation/jcr:content\",\"jcr:title\":\"Scheduled"
            + " Page/Asset Deactivation\"},{\"jcr:path\":\"/etc/workflow/models/wcm-translation/translate-lang"
            + "uage-copy/jcr:content\",\"jcr:title\":\"WCM: Translate Language Copy\"},{\"jcr:path\":\"/etc/wo"
            + "rkflow/models/wcm-translation/sync_translation_job/jcr:content\",\"jcr:title\":\"WCM: Sync Tran"
            + "slation Job\"},{\"jcr:path\":\"/etc/workflow/models/wcm-translation/create_language_copy/jcr:co"
            + "ntent\",\"jcr:title\":\"WCM: Create Language Copy\"},{\"jcr:path\":\"/etc/workflow/models/wcm-t"
            + "ranslation/prepare_translation_project/jcr:content\",\"jcr:title\":\"WCM: Prepare Translation P"
            + "roject\"},{\"jcr:path\":\"/etc/workflow/models/wcm-translation/update_language_copy/jcr:content"
            + "\",\"jcr:title\":\"WCM: Update Language Copy\"},{\"jcr:path\":\"/etc/workflow/models/dummy-acti"
            + "vity-handler/jcr:content\",\"jcr:title\":\"Newsletter Activity Handler\"},{\"jcr:path\":\"/etc/"
            + "workflow/models/saint-product-export-handler/jcr:content\",\"jcr:title\":\"SAINT Product Export"
            + " Handler\"},{\"jcr:path\":\"/etc/workflow/models/social_ugcc_componentsync/jcr:content\",\"jcr:"
            + "title\":\"Sync Soco Components\"},{\"jcr:path\":\"/etc/workflow/models/newsletter_bounce_check/"
            + "jcr:content\",\"jcr:title\":\"Newsletter Bounce Counter\"},{\"jcr:path\":\"/etc/workflow/models"
            + "/request_for_activation/jcr:content\",\"jcr:title\":\"Request for Activation\"},{\"jcr:path\":"
            + "\"/etc/workflow/models/dam-parse-word-documents/jcr:content\",\"jcr:title\":\"DAM Parse Word Do"
            + "cuments\"},{\"jcr:path\":\"/etc/workflow/models/request_for_deactivation/jcr:content\",\"jcr:ti"
            + "tle\":\"Request for Deactivation\"},{\"jcr:path\":\"/etc/workflow/models/translation/jcr:conten"
            + "t\",\"jcr:title\":\"Translation\"}]}";

    public WorkflowCountsParseTest() {
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
    public void parseWorkflowPaths() {
        Map<String, String> models = WorkflowStatsParser.extractPaths(workflowInfoExample);
        assertNotNull(models);
        assertFalse(models.isEmpty());
        assertEquals("Request Copy", models.get("/etc/workflow/models/projects/request_copy"));
        assertEquals("DAM Update Asset", models.get("/etc/workflow/models/dam/update_asset"));
    }
    
    @Test
    public void parseWorkflowCounts() {
        Map<String, String> models = WorkflowStatsParser.extractPaths(workflowInfoExample);
        assertNotNull(models);
        Map<String, WorkflowStats> workflowCounts = WorkflowStatsParser.extractCounts(countsJsonExample, models);
        assertNotNull(workflowCounts);
        assertFalse(workflowCounts.isEmpty());
        WorkflowStats damStats = workflowCounts.get("DAM Update Asset");
        assertNotNull(damStats);
        assertEquals(0, damStats.getPendingCount());
        assertEquals(593, damStats.getInstanceCount());
        assertEquals("/etc/workflow/models/dam/update_asset", damStats.getWorkflowPath());        
    }
}
