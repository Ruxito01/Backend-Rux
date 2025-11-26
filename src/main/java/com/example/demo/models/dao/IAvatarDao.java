package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.entity.Avatar;

public interface IAvatarDao extends CrudRepository<Avatar, Long> {
}
