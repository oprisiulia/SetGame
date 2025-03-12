package set;

import set.enums.CardColor;
import set.enums.Count;
import set.enums.Shading;
import set.enums.Shape;

public class Card {
    // set kartyak -> 4 tulajdonsag
    private Count count;
    private Shape shape;
    private CardColor color;
    private Shading shading;

    public Card(Count count, Shape shape, CardColor color, Shading shading) {
        this.count = count;
        this.shape = shape;
        this.color = color;
        this.shading = shading;
    }

    public Count getCount() {
        return count;
    }

    public Shape getShape() {
        return shape;
    }

    public CardColor getColor() {
        return color;
    }

    public Shading getShading() {
        return shading;
    }

    public String getImageFileName() {
        String countStr;
        switch (count) {
            case ONE:
                countStr = "1"; break;
            case TWO:
                countStr = "2"; break;
            case THREE:
                countStr = "3"; break;
            default:
                countStr = "unknown";
        }

        String filename = countStr + "_"
                + color.name().toLowerCase() + "_"
                + shape.name().toLowerCase() + "_"
                + shading.name().toLowerCase()
                + ".png";
        return filename;
    }
}
