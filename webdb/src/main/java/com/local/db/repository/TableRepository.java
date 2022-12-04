package com.local.db.repository;

import com.local.db.model.Base;
import com.local.db.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
    Table findByBaseAndNameIgnoreCase(Base base, String name);
}
