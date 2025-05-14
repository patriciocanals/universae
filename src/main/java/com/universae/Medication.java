package com.universae;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Medication {
    private final SimpleStringProperty name;
    private final SimpleStringProperty dose;
    private final SimpleStringProperty schedule;
    private final SimpleBooleanProperty taken;

    public Medication(String name, String dose, String schedule, boolean taken) {
        this.name = new SimpleStringProperty(name);
        this.dose = new SimpleStringProperty(dose);
        this.schedule = new SimpleStringProperty(schedule);
        this.taken = new SimpleBooleanProperty(taken);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getDose() {
        return dose.get();
    }

    public SimpleStringProperty doseProperty() {
        return dose;
    }

    public String getSchedule() {
        return schedule.get();
    }

    public SimpleStringProperty scheduleProperty() {
        return schedule;
    }

    public boolean isTaken() {
        return taken.get();
    }

    public SimpleBooleanProperty takenProperty() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken.set(taken);
    }
}