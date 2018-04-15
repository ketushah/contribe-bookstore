package BookStore.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ketu.shah on 4/5/2018.
 */
public enum Status {
    OK(0),
    NOT_IN_STOCK(1),
    DOES_NOT_EXIST(2);

    private static final Map<Integer, Status> STATUS_MAP = new HashMap<Integer, Status>();
    static {
        for (Status status : values()) {
            STATUS_MAP.put(status.getStatus(), status);
        }
    }

    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public static Status getStatusName(int status) {
        return STATUS_MAP.get(status);
    }
}
