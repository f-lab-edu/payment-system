package flab.payment_system.domain.log.entity;

import flab.payment_system.common.data.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="app_logs")
public class AppLogs extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id", columnDefinition = "BIGINT UNSIGNED")
	private Long log_id;
	private String trace_id;
	private String logger;
	private String log_level;

	@Lob
	private String message;
	private String exception;

}
