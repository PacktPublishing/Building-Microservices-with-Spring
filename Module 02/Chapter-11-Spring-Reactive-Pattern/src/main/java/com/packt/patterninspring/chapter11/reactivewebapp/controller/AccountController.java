/**
 * 
 */
package com.packt.patterninspring.chapter11.reactivewebapp.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.packt.patterninspring.chapter11.reactivewebapp.model.Account;
import com.packt.patterninspring.chapter11.reactivewebapp.repository.AccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Dinesh.Rajput
 *
 */
@RestController
public class AccountController {
	
	@Autowired
    private AccountRepository repository;
 
    @GetMapping(value = "/account")
    public Flux<Account> findAll() {
        return repository.findAll().map(a -> new Account(a.getId(), a.getName(), a.getBalance(), a.getBranch()));
    }
 
    @GetMapping(value = "/account/{id}")
    public Mono<Account> findById(@PathVariable("id") Long id) {
        return repository.findById(id)
                .map(a -> new Account(a.getId(), a.getName(), a.getBalance(), a.getBranch()));
    }
 
    @PostMapping("/account")
    public Mono<Account> create(@RequestBody Publisher<Account> accountStream) {
    	return repository
    			.save(Mono.from(accountStream)
    					.map(a -> new Account(a.getId(), a.getName(), a.getBalance(), a.getBranch())))
    			.map(a -> new Account(a.getId(), a.getName(), a.getBalance(), a.getBranch()));
    }
}
