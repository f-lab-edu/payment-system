package flab.payment_system.domain.product.repository;

public interface ProductCustomRepository {

    long updateDecreaseStock(long productId);
    long updateIncreaseStock(long productId);
}
