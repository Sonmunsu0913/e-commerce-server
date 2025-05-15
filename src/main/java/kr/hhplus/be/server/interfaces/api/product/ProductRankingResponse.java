package kr.hhplus.be.server.interfaces.api.product;

public record ProductRankingResponse(
    Long productId,
    String name,
    double score
) {}
