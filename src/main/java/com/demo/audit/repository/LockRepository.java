package com.demo.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.audit.entity.Lock;


@Repository
public interface LockRepository  extends JpaRepository<Lock, String>{
	

}
