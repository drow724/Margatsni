package com.instagl.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.instagl.entity.Account;
import com.instagl.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	
	public Optional<Account> getAccount(Long id) {
		return accountRepository.findById(id);
	}
	
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	public void changeUpdateState(Long id) {
		Account account = accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("계정이 존재하지 않습니다."));
		account.changeUpdating();
		accountRepository.save(account);
	}
}
