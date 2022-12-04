package com.local.db.repository;

import com.local.db.model.Row;
import com.local.db.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RowRepository extends JpaRepository<Row, Long> {
    Row getRowByTableAndId(Table table, Long id);
}
