package com.local.db.service;

import com.local.db.model.Base;
import com.local.db.repository.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class BaseService {
    private final BaseRepository baseRepository;

    public List<Base> findAll() {
        return baseRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Base findByName(String name){
        Base base = baseRepository.findByNameIgnoreCase(name);

        if (base == null) {
            throw new RuntimeException("Base Not Found!");
        }
        return base;
    }

    public boolean addBase(Base base){
        Base dbBase = baseRepository.findByNameIgnoreCase(base.getName());
        if (dbBase != null)
            return false;

        baseRepository.save(base);
        return true;
    }

    public Base createBase(Base base) throws Exception {
        Base dbBase = baseRepository.findByNameIgnoreCase(base.getName());
        if (dbBase != null)
            throw new Exception("Base with the same name exists!");

        return baseRepository.save(base);
    }

    public void removeBase(Base base) {
        baseRepository.delete(base);
    }
}
