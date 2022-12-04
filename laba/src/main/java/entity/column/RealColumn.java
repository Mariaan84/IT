package entity.column;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

@Getter
@Setter
public class RealColumn extends Column {
    public RealColumn(String name) {
        super(name);
    }

    @Override
    public ColumnType getType() {
        return ColumnType.REAL;
    }

    @Override
    public boolean validate(final String value) {
        if (Objects.isNull(value)) {
            return false;
        }
        return NumberUtils.isParsable(value);
    }
}
