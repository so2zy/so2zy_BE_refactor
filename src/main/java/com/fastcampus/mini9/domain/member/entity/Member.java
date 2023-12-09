package com.fastcampus.mini9.domain.member.entity;

import java.time.LocalDate;
import java.util.List;

import com.fastcampus.mini9.domain.wish.entity.Wish;

import java.util.ArrayList;
import java.util.List;

import com.fastcampus.mini9.domain.cart.entity.Cart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String pwd;

	private String name;

	private LocalDate birthday;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cart> carts = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Wish> wishList;

	@Builder
	private Member(String email, String pwd, String name, LocalDate birthday) {
		this.email = email;
		this.pwd = pwd;
		this.name = name;
		this.birthday = birthday;
	}
}
