package com.fastpowered.raft.protocol;

import java.util.Arrays;
import java.util.Optional;

public interface NodeStatus {

    int FOLLOWER = 0;
    int CANDIDATE = 1;
    int LEADER = 2;

    enum Enum {
        FOLLOWER(0), CANDIDATE(1), LEADER(2);

        private int code;

        Enum(int code) {
            this.code = code;
        }

        public static Enum value(int value) {
            Optional<Enum> optional = Arrays.stream(Enum.values()).filter(item -> item.code == value ? true : false).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new IllegalStateException("Node status value is not supported");
            }
        }
    }

}
