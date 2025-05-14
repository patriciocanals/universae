package com.universae;

import javafx.beans.property.SimpleStringProperty;

public class Medication {
    private final SimpleStringProperty name;
    private final SimpleStringProperty dose;
    private final SimpleStringProperty schedule;

    public Medication(String name, String dose, String schedule) {
        this.name = new SimpleStringProperty(name);
        this.dose = new SimpleStringProperty(dose);
        this.schedule = new SimpleStringProperty(schedule);
    }

    public String getName() { return name.get(); }
    public SimpleStringProperty nameProperty() { return name; }

    public String getDose() { return dose.get(); }
    public SimpleStringProperty doseProperty() { return dose; }

    public String getSchedule() { return schedule.get(); }
    public SimpleStringProperty scheduleProperty() { return schedule; }
}