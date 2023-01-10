package com.instagl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagl.entity.Proxy;

@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {

}
