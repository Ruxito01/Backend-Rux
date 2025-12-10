package com.example.demo.models.dao;

import com.example.demo.models.entity.CatalogoAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICatalogoAvatarDao extends JpaRepository<CatalogoAvatar, Long> {
}
