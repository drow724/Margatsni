package com.instagl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagl.entity.Account;
import com.instagl.entity.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

	List<Content> findByAccount(Account account);

}
