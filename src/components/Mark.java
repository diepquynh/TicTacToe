package components;

public enum Mark {
    X('X'),
    O('O'),
    BLANK(' ');

    private final char mark;

    Mark(char initMark) {
        this.mark = initMark;
    }

    public boolean isMarked() {
        return this != BLANK;
    }

    public char getMark() {
        return this.mark;
    }

    public static Mark toMark(char mark) {
        if (mark == 'X')
            return X;

        if (mark == 'O')
            return O;

        return BLANK;
    }

    @Override
    public String toString() {
        return String.valueOf(mark);
    }
}
