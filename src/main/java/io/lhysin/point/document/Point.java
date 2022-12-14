package io.lhysin.point.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import io.lhysin.point.type.PointCreatedType;
import io.lhysin.point.type.PointTransactionType;
import io.lhysin.point.type.PointType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Document(collection = "points")
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CompoundIndexes({@CompoundIndex(name = "createdByUserId", def = "{'createdBy' : -1, 'userId': 1}")})
public class Point {

    @Id
    private String id;

    @Indexed
    private String userId;

    private BigDecimal amount;

    private BigDecimal consumedAmount;

    private PointType pointType;

    private PointCreatedType pointCreatedType;

    private Boolean complete;

    private LocalDateTime expiredAt;

    private String createdBy;

    private LocalDateTime createdAt;

    private List<PointTransaction> pointTransactions;

    public List<PointTransaction> getPointTransactions() {
        return Optional.ofNullable(this.pointTransactions)
            .orElse(List.of());
    }

    public BigDecimal getAmount() {
        return Optional.ofNullable(this.amount).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getConsumedAmount() {
        return Optional.ofNullable(this.consumedAmount).orElse(BigDecimal.ZERO);
    }

    public void addConsumedAmount(BigDecimal req) {
        this.consumedAmount = this.getConsumedAmount().add(Optional.ofNullable(req).orElse(BigDecimal.ZERO));
    }

    public void subtractConsumedAmount(BigDecimal req) {
        this.consumedAmount = this.getConsumedAmount().subtract(Optional.ofNullable(req).orElse(BigDecimal.ZERO));
    }

    public void updateCompletePoint() {
        if (this.getAmount().compareTo(this.getConsumedAmount()) <= 0) {
            this.complete = true;
        } else {
            this.complete = false;
        }
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PointTransaction {

        private PointTransactionType pointTransactionType;

        private BigDecimal amount;

        private String createdBy;

        private LocalDateTime createdAt;

    }

}