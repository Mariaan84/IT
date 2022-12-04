package entity.column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringColumn extends Column {
    public StringColumn(String name) {
        super(name);
    }

    @Override
    public ColumnType getType() {
        return ColumnType.STRING;
    }

    @Override
    public boolean validate(final String value) {
        return true;
    }
}
