package entity.column;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@Setter
public class IntColumn extends Column {
    public IntColumn(final String name) {
        super(name);
    }

    @Override
    public ColumnType getType() {
        return ColumnType.INTEGER;
    }

    @Override
    public boolean validate(final String value) {
        if (Objects.isNull(value)) {
            return false;
        }
        return StringUtils.isNumeric(value);
    }
}
