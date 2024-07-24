package com.likelion.mooding.auth.presentation.dto;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public record Guest(String guestId) {
}
