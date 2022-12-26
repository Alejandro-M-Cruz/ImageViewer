package Architecture;

import static java.lang.Math.abs;

public class ImagePresenter {
    private Image image;
    private final ImageDisplay display;

    public static ImagePresenter with(Image image, ImageDisplay imageDisplay) {
        return new ImagePresenter(image, imageDisplay);
    }

    private ImagePresenter(Image image, ImageDisplay display) {
        this.image = image;
        this.display = display;
        this.display.onDragged(this::onDragged);
        this.display.onReleased(this::onReleased);
        this.display.updateName(image.name());
        this.refresh();
    }
    
    public void show(Image image) {
        this.image = image;
        this.display.updateName(image.name());
        this.refresh();
    }
    
    private void onDragged(int offset) {
        display.clear();
        display.paint(image.data(), offset);
        if (offset > 0) {
            Image prev = image.prev();
            display.paint(prev.data(), prevOffset(offset,prev.width(),display.width()));
        }
        else {
            Image next = image.next();
            display.paint(next.data(), nextOffset(offset,next.width(),display.width())); 
        }
    }
    
    private int prevOffset(int offset, int width, int panelWidth) {
        return panelWidth > width ? offset - (width+panelWidth)/2 : offset - panelWidth;
    }
    
    private int nextOffset(int offset, int width, int panelWidth) {
        return panelWidth > width ? (panelWidth-width)/-2 + panelWidth + offset : panelWidth + offset;
    }
       
    private void onReleased(int offset) {
        if (abs(offset) > display.width() / 2) 
            this.image = offset < 0 ? image.next() : image.prev();
        display.updateName(image.name());
        refresh();
    }
    
    private void refresh() {
        display.clear();
        display.paint(image.data(), 0);
    }
    
    public Image current() {
        return image;
    }
}