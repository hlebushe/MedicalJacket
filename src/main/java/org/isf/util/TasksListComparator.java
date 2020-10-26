package org.isf.util;

import org.isf.models.TaskModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TasksListComparator implements Comparator<TaskModel> {
    private DateFormat primaryFormat = new SimpleDateFormat("H:mm");
    private DateFormat secondaryFormat = new SimpleDateFormat("h:mm a");

    @Override
    public int compare(TaskModel o1, TaskModel o2) {
        try {
            return timeInMillis(o1.getTaskTime()) - timeInMillis(o2.getTaskTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int timeInMillis(String time) throws ParseException {
        return timeInMillis(time, primaryFormat);
    }

    private int timeInMillis(String time, DateFormat format) throws ParseException {
        // you may need more advanced logic here when parsing the time if some times have am/pm and others don't.
        try {
            Date date = format.parse(time);
            return (int) date.getTime();
        } catch (ParseException e) {
            if (format != secondaryFormat) {
                return timeInMillis(time, secondaryFormat);
            } else {
                throw e;
            }
        }
    }
}
