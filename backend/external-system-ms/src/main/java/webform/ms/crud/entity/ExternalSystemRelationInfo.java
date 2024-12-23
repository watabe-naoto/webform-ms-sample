package webform.ms.crud.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString
public class ExternalSystemRelationInfo {
	@Id
	private final Integer id;

	@Column("orderform_type")
    private final String orderform_type;
	@Column("external_system_id")
    private final Integer external_system_id;
	@Column("add_user")
    private final String add_user;
	@Column("add_date")
    private final LocalDateTime add_date;
}