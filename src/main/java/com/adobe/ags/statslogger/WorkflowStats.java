package com.adobe.ags.statslogger;

public class WorkflowStats {

    private double rateMax, rateMid, rateMin;
    private long timeMax, timeMid, timeMin;
    private int total, totalAllTime, pendingCount, instanceCount;
    private String workflow, workflowPath;

    public void setRateMax(double max) {
        rateMax = max;
    }

    public double getRateMax() {
        return rateMax;
    }

    public void setRateMid(double mid) {
        rateMid = mid;
    }

    public double getRateMid() {
        return rateMid;
    }

    public void setRateMin(double min) {
        rateMin = min;
    }

    public double getRateMin() {
        return rateMin;
    }

    public void setTimeMax(long max) {
        timeMax = max;
    }

    public long getTimeMax() {
        return timeMax;
    }

    public void setTimeMid(long mid) {
        timeMid = mid;
    }

    public long getTimeMid() {
        return timeMid;
    }

    public void setTimeMin(long min) {
        timeMin = min;
    }

    public long getTimeMin() {
        return timeMin;
    }

    public void setTotal(int tot) {
        total = tot;
    }

    public int getTotal() {
        return total;
    }

    public void setTotalAllTime(int tot) {
        totalAllTime = tot;
    }

    public int getTotalAllTime() {
        return totalAllTime;
    }

    public void setWorkflow(String name) {
        workflow = name;
    }

    public String getWorkflow() {
        return workflow;
    }

    /**
     * @return the pendingCount
     */
    public int getPendingCount() {
        return pendingCount;
    }

    /**
     * @param pendingCount the pendingCount to set
     */
    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    /**
     * @return the instanceCount
     */
    public int getInstanceCount() {
        return instanceCount;
    }

    /**
     * @param instanceCount the instanceCount to set
     */
    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    /**
     * @return the workflowPath
     */
    public String getWorkflowPath() {
        return workflowPath;
    }

    /**
     * @param workflowPath the workflowPath to set
     */
    public void setWorkflowPath(String workflowPath) {
        this.workflowPath = workflowPath;
    }

}
