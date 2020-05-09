package org.access.driver;


public class Employee {
    private final long id;
    private final int access_level;
    private final String rf_id;

    public Employee(long id, int access_level, String rf_id) {
        this.id = id;
        this.access_level = access_level;
        this.rf_id = rf_id;
    }

    public long getId() {
        return id;
    }

    public int getAccess_level() {
        return access_level;
    }

    public String getRf_id() {
        return rf_id;
    }
}