package JWork_5.src.util;

import JWork_5.src.Interactive;

public class Contact {
    public Interactive object;
    public float contactX, contactY;

    public Contact(Interactive obj, float x, float y) {
        object = obj;
        contactX = x;
        contactY = y;
    }
}
