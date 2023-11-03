package com.example.project;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Broker {

    private static HashMap<String, ArrayList<Subscriber>> subscribers=new HashMap<>();

    /**
     * adds a subscriber and topic(s) to the list of subscribers
     * @param subscriber new subscriber
     * @param topics topic(s) subscribing too
     */
    public static void subscribe(Subscriber subscriber, String... topics)
    {
        for(String topic: topics)
        {
            ArrayList<Subscriber> sub=subscribers.computeIfAbsent(topic, k->new ArrayList<>());
            if(!sub.contains(subscriber))
                sub.add(subscriber);
        }
    }

    /**
     * notifies all those who subscribed to the topic
     *  @param topic topic subscribed
     * @param publication information to be sent to the subscribers
     */
    @SafeVarargs
    public static void publish(String topic, Map.Entry<String, Object>... publication) {
        ArrayList<Subscriber> subs = subscribers.get(topic);
        if (subs == null)
            return;
        for (Subscriber subscriber : subs)
            subscriber.readPublished(publication);
    }

    /**
     * unregisters the subscriber to the specified topic(s)
     *
     * @param subscriber the unregistering subscriber
     * @param topics topic to which the subscriber is unregistering
     */
    public static void unsubscribe(Subscriber subscriber, String... topics) {
        for (String topic : topics) {
            ArrayList<Subscriber> sub = subscribers.get(topic);
            if (sub != null) {
                sub.remove(subscriber);
                if (sub.size() == 0)
                    subscribers.remove(topic);
            }
        }
    }

}
