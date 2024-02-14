package uk.ac.aston.ip.myeyehealth.reminders;

import java.util.HashMap;
import java.util.Map;

public class ReminderTypeMap {
    private Map<String, String> reminderType;

    public ReminderTypeMap() {
        this.reminderType = new HashMap<>();

        this.reminderType.put("Eye Gel", "drop");
        this.reminderType.put("Eye Drops", "drop");
        this.reminderType.put("Capsules", "capsule");
        this.reminderType.put("Tablets", "tablet");
    }

    public String[] getMedicationUnits() {
        return (String[]) this.reminderType.keySet().toArray();
    }

    public String[] getMedicationTypes() {
        return (String[]) this.reminderType.values().toArray();
    }

    public String getMedicationUnit(String type) {
        return this.reminderType.get(type);
    }
}
