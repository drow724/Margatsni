package com.instagl.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.instagl.entity.Account;
import com.instagl.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	
	public Optional<Account> getAccount(String username) {
		return accountRepository.findByUsername(username);
	}
	
	public Account save(Account account) {
		return accountRepository.save(account);
	}

}
