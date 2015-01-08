package kmucs.capstone.furnidiy;

// yhchung
// parsing

public class ModelObject {
    private String name;
    public float[] vertices;
    public short[] polygons;
    public float[] textureCoordinates;
    public float[] angle;

    public ModelObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
