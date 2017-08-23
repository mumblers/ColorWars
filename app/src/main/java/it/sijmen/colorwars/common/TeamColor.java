package it.sijmen.colorwars.common;

enum TeamColor {

    RED(0x7FFE3030),
    BLUE(0x7F304FFE),
    GREEN(0x7F6EFE30),
    YELLOW(0x7FFEE330);

    private final int color;

    private int num = 0;

    TeamColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getNum() {
        return num++;
    }
}
