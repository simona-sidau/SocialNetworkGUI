package org.example.utils.events;

import org.example.domain.Friendship;

public class PrietenieEntityChangeEvent implements Event {

    private ChangeEventType type;

    private Friendship data, oldData;

    public PrietenieEntityChangeEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }
    public PrietenieEntityChangeEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }
    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }


}
