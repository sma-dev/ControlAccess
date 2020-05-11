package org.access.driver;


public class Employee {
    private final long id;
    private final int accessLevel;
    private final String rfId;
    private final long openCount;

    public Employee(long id, int accessLevel, String rfId, long openCount) {
        this.id = id;
        this.accessLevel = accessLevel;
        this.rfId = rfId;
        this.openCount = openCount;
    }

    public long getId() {
        return id;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public String getRfId() {
        return rfId;
    }

    public long getOpenCount() {
        return openCount;
    }
}