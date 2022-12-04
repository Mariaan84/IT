package entity.column;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
public class TextFileColumn extends Column {
    private static final String TXT_FORMAT = ".txt";

    public TextFileColumn(String name) {
        super(name);
    }

    @Override
    public ColumnType getType() {
        return ColumnType.TEXT_FILE;
    }

    @Override
    public boolean validate(final String value) {
        return Files.exists(Path.of(value)) && value.endsWith(TXT_FORMAT);
    }
}
