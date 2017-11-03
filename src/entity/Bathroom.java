package entity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bathroom {
    private Lock lock = new ReentrantLock();
    private Integer limitOfPersons;
    private Queue<Person> persons = new LinkedList<>();
    private Queue<Person> waitingPersons = new LinkedList<>();

    private static Bathroom bathroom;

    private Bathroom() {
    }

    public static Bathroom getInstance() {
        if (bathroom == null) {
            bathroom = new Bathroom();
        }
        return bathroom;
    }

    public Integer getLimitOfPersons() {
        return limitOfPersons;
    }

    public void setLimitOfPersons(Integer limitOfPersons) {
        this.limitOfPersons = limitOfPersons;
    }

    public Queue<Person> getPersons() {
        return persons;
    }

    public void setPersons(Queue<Person> persons) {
        this.persons = persons;
    }

    public Boolean addPersonInBathroom(Person p) {
        if ( (persons.size() == 0 || persons.element().getClass() == p.getClass()) && persons.size() < limitOfPersons) {
            persons.add(p);
            System.out.println(p.getClass().getName().replace("entity.", "") + " " + p.getPersonName() + " enter in bathroom. Time of use: " + p.getTimeUsingBath() + "ms");
            System.out.println("There are " + persons.size() + " persons in bathroom");
            return true;
        }
        return false;
    }

    public void removePersonOfBathroom(Person p){
        persons.remove(p);
        System.out.println(p.getClass().getName().replace("entity.", "") + " " + p.getPersonName() + " exit of bathroom.");
        System.out.println("There are " + persons.size() + " persons in bathroom");
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Queue<Person> getWaitingPersons() {
        return waitingPersons;
    }

    public void setWaitingPersons(Queue<Person> waitingPersons) {
        this.waitingPersons = waitingPersons;
    }

    public void addWaitingPersonInBathroom(){
        if (waitingPersons.size() > 0 && addPersonInBathroom(waitingPersons.element())) {
            waitingPersons.remove();
            addWaitingPersonInBathroom();
        }
    }

    public Boolean isPersonInBathroom(Person person){
        try {
            lock.lock();
            return persons.contains(person);
        }
        finally {
            lock.unlock();
        }
    }
}
