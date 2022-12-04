package entity;

import entity.column.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private String name;
    private List<Column> columns = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }
}
