package Architecture;

public interface Image {
    String name();
    Object data();
    int width();
    Image prev();
    Image next();
}
