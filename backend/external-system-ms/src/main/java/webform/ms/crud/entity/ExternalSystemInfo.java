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
public class ExternalSystemInfo {
	@Id
	private final Integer id;

	@Column("external_system_id")
    private final Integer external_system_id;
	@Column("external_system_name")
    private final String external_system_name;
	@Column("external_system_status")
    private final Integer external_system_status;
	@Column("update_datetime")
    private final LocalDateTime update_datetime;
	@Column("update_user")
    private final String update_user;
	@Column("stop_datetime")
    private final LocalDateTime stop_datetime;
	@Column("reboot_datetime")
    private final LocalDateTime reboot_datetime;
	@Column("message")
    private final String message;
}