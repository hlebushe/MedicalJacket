package org.isf.models;

import lombok.Data;

@Data
public class TaskModel {

    public String taskName;
    public String taskTime;

    public String getTaskString() {
        return this.taskName + " at " + this.taskTime;
    }

    public String getTaskStringForDb() {
        return this.taskName + "-" + this.taskTime;
    }
}
