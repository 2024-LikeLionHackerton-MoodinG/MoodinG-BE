package com.likelion.mooding.auth.presentation.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public record Guest(String guestId) {

}
