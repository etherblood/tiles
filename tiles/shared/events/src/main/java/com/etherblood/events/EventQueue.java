package com.etherblood.events;

/**
 *
 * @author Philipp
 */
public interface EventQueue {
    
    void cancelEvent();

    void action(int eventId);

    void action(int eventId, int arg0);

    void action(int eventId, int arg0, int arg1);

    void action(int eventId, int arg0, int arg1, int arg2);

    void action(int eventId, int... args);

    void trigger(int eventId);

    void trigger(int eventId, int arg0);

    void trigger(int eventId, int arg0, int arg1);

    void trigger(int eventId, int arg0, int arg1, int arg2);

    void trigger(int eventId, int... args);

    void response(int eventId);

    void response(int eventId, int arg0);

    void response(int eventId, int arg0, int arg1);

    void response(int eventId, int arg0, int arg1, int arg2);

    void response(int eventId, int... args);

}
