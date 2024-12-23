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
public class Address {
	@Id
	private final Integer id;
	@Column("todohuken_code")
    private final String todohuken_code;
	@Column("shikugunchoson_code")
    private final String shikugunchoson_code;
	@Column("ohazatsusho_code")
    private final String ohazatsusho_code;
	@Column("azachome_code")
    private final String azachome_code;
	@Column("kyu_zipcd")
    private final String kyu_zipcd;
	@Column("todohuken_name")
    private final String todohuken_name;
	@Column("shikugunchoson_name")
    private final String shikugunchoson_name;
	@Column("ohazatsusho_name")
    private final String ohazatsusho_name;
	@Column("azachome_name")
    private final String azachome_name;
	@Column("insert_datetime")
    private final LocalDateTime insert_datetime;
	@Column("create_user_id")
    private final String create_user_id;
}