package flab.payment_system.domain.product.entity;

import flab.payment_system.common.data.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", columnDefinition = "BIGINT UNSIGNED")
	private Long productId;

	@NonNull
	@Column(columnDefinition = "VARCHAR(20)")
	private String name;

	@NonNull
	@Column(columnDefinition = "INT UNSIGNED")
	private Integer price;

	@NonNull
	@Column(columnDefinition = "INT UNSIGNED")
	private Integer stock;

	public void setName(@NonNull String name) {
		this.name = name;
	}

	public void setPrice(@NonNull Integer price) {
		this.price = price;
	}

	public void setStock(@NonNull Integer stock) {
		this.stock = stock;
	}
}
