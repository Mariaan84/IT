package entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Database {
    private String name;
    private List<Table> tables = new ArrayList<>();

    public Database(String name) {
        this.name = name;
    }
}
