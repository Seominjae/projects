package kmucs.capstone.furnidiy;

import java.util.LinkedList;

// yhchung
// parsing

public class Model {

    public LinkedList<ModelObject> objects = new LinkedList<ModelObject>();

    public ModelObject newModelObject(String name) {
        ModelObject object = new ModelObject(name);
        objects.push(object);
        return object;
    }
}
