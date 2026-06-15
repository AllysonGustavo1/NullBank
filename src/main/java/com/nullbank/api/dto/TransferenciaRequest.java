package com.nullbank.api.dto;

public record TransferenciaRequest(int from, int to, double amount) {
}