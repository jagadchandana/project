package observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements Subject {
    private List<Observer> observers;

    public NotificationManager() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void notifySpecificObserver(Observer observer, String message) {
        if (observers.contains(observer)) {
            observer.update(message);
        }
    }
}
