package entity.column;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Column {
    private String name;

    public abstract ColumnType getType();

    public abstract boolean validate(String value);

    public enum ColumnType {
        INTEGER,
        REAL,
        CHAR,
        STRING,
        TEXT_FILE,
        INTEGER_INTERVAL
    }
}
