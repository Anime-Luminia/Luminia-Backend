package com.anime.luminia.domain.review;

public enum ReviewTier {
    S("S", 5.0),
    A_PLUS("A+", 4.5),
    A("A", 4.0),
    B_PLUS("B+", 3.5),
    B("B", 3.0),
    C_PLUS("C+", 2.5),
    C("C", 2.0),
    D("D", 1.0),
    F("F", 0.5);

    private final String name;
    private final double score;

    ReviewTier(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public double getScore() {
        return this.score;
    }

    // 평균 점수로부터 티어를 반환하는 메서드
    public static ReviewTier fromAverageScore(double averageScore) {
        if (averageScore > 4.5 && averageScore <= 5.0) {
            return S;
        } else if (averageScore > 4.0 && averageScore <= 4.5) {
            return A_PLUS;
        } else if (averageScore > 3.5 && averageScore <= 4.0) {
            return A;
        } else if (averageScore > 3.0 && averageScore <= 3.5) {
            return B_PLUS;
        } else if (averageScore > 2.5 && averageScore <= 3.0) {
            return B;
        } else if (averageScore > 2.0 && averageScore <= 2.5) {
            return C_PLUS;
        } else if (averageScore > 1.5 && averageScore <= 2.0) {
            return C;
        } else if (averageScore > 1.0 && averageScore <= 1.5) {
            return D;
        } else {
            return F;
        }
    }
}

