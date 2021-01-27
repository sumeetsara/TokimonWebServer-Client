package ca.cmpt213.asn5.server.model;

/**
 *  Tokimon class stores the fields of a tokimon
 *  Getters for all the fields and a setter for the id
 */
public class Tokimon {
    static private long total = 0;
    private long tID;
    private String name;
    private Double weight;
    private Double height;
    private String ability;
    private Double strength;
    private String colour;

    public Tokimon(String name, double weight, double height, String ability, double strength, String colour) {
        total++;
        tID = total;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.ability = ability;
        this.strength = strength;
        this.colour = colour;
    }

    public void settID(long tID) {
        this.tID = tID;
    }

    public long getTID() {
        return tID;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getAbility() {
        return ability;
    }

    public double getStrength() {
        return strength;
    }

    public String getColour() {
        return colour;
    }
}
