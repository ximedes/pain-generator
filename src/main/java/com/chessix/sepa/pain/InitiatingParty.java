package com.chessix.sepa.pain;

public class InitiatingParty {
    private String name;

    /**
     * The initiating party.
     *
     * @param name may be null but not longer than 70 characters
     */
    public InitiatingParty(String name) {
        if (name != null && name.length() > 70) {
            throw new IllegalArgumentException("Name of initiating party cannot be longer than 70 characters");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
