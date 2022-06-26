package ru.ccfit.idrisova.task3.Observer;

public interface Observable {
    public void reg(Observer o);
    public void notifySubscribers();
}
