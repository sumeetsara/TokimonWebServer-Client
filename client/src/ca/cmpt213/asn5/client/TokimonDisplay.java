package ca.cmpt213.asn5.client;

public class TokimonDisplay {
    private final String name;
    private final double weight;
    private final double height;
    private final String colour;
    private final long tid;

    public TokimonDisplay(String name, Double weight, Double height, String colour, Long tid) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.colour = colour;
        this.tid = tid;
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

    public String getColour() {
        return colour;
    }

    public long getTid() {
        return tid;
    }
}
