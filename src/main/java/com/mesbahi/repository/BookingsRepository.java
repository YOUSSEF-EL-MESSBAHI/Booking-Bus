package com.mesbahi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mesbahi.entity.Bookings;

public interface BookingsRepository extends JpaRepository<Bookings, Integer> {

	List<Bookings> findByUserId(int userId);
}
