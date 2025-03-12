package set.enums;

public enum Count {
    ONE("1"),
    TWO("2"),
    THREE("3");

    private final String fileNamePart;

    Count(String fileNamePart) {
        this.fileNamePart = fileNamePart;
    }

    @Override
    public String toString() {
        return fileNamePart;
    }
}
