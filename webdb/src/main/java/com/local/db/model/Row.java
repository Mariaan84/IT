package com.local.db.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
public class Row {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> values = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private Table table;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Row) {
            Row temp = (Row) obj;
            return Arrays.equals(this.values.toArray(), temp.values.toArray());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.values.toArray());
    }
}
