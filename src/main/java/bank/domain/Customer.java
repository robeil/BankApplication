package bank.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Customer {
	@Id
	private Long id;
	private String name;

	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;

	public Customer(String name) {
		this.name = name;
	}
}
