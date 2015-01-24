package com.hibernate.tutorial;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.hibernate.tutorial.domain.Event;
import com.hibernate.tutorial.domain.Person;
import com.hibernate.tutorial.util.HibernateUtil;

public class EventManager {

    public static void main(String[] args) {
        EventManager mgr = new EventManager();

        if (args[0].equals("store")) {
            mgr.createAndStoreEvent("My Event", new Date());
        } else if (args[0].equals("list")) {
            List<Event> events = mgr.listEvents();
            for (int i = 0; i < events.size(); i++) {
                Event theEvent = (Event) events.get(i);
                System.out.println(
                        "Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate()
                );
            }
        } else if (args[0].equals("addpersontoevent")) {
            Long eventId = mgr.createAndStoreEvent("An Event", new Date());
            Long personId = mgr.createAndStorePerson("Bruce", "Harris", 26);
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);
        } else if (args[0].equals("addemailtoperson")) {
        	Long personId = mgr.createAndStorePerson("Steve", "Dickinson", 25);
        	String emailAddress = "eddie@gmail.com";
        	mgr.addEmailToPerson(personId, emailAddress);
        	System.out.println("Added email " + emailAddress + " to person " + personId);
        }

        HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreEvent(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);

        session.getTransaction().commit();
        
        return theEvent.getId();
    }

    private Long createAndStorePerson(String firstname, String lastname, int age) {
    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	session.beginTransaction();
    	
    	Person person = new Person();
    	person.setFirstname(firstname);
    	person.setLastname(lastname);
    	person.setAge(age);
    	session.save(person);
    	
    	session.getTransaction().commit();
    	
    	return person.getId();
    }
    
    private List<Event> listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Event> result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
    }
    
    private void addPersonToEvent(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);
        aPerson.getEvents().add(anEvent);

        session.getTransaction().commit();
    }
    
    private void addEmailToPerson(Long personId, String emailAddress) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        // adding to the emailAddress collection might trigger a lazy load of the collection
        aPerson.getEmailAddresses().add(emailAddress);

        session.getTransaction().commit();
    }
}