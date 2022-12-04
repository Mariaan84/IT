package entity.column;

import org.apache.commons.lang3.StringUtils;

public class IntervalColumn extends Column {
    private static final int LEFT_END_INDEX = 0;

    private static final int RIGHT_END_INDEX = 1;

    public IntervalColumn(String name) {
        super(name);
    }

    @Override
    public ColumnType getType() {
        return ColumnType.INTEGER_INTERVAL;
    }

    @Override
    public boolean validate(final String value) {
        final String[] intervalEnds = value.replaceAll(" ", "").split(",");
        final String leftEnd = intervalEnds[LEFT_END_INDEX];
        final String rightEnd = intervalEnds[RIGHT_END_INDEX];

        return StringUtils.isNumeric(leftEnd)
            && StringUtils.isNumeric(rightEnd)
            && Integer.parseInt(rightEnd) > Integer.parseInt(leftEnd);
    }
}
