package bank.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Document
public class TraceRecord {
	@Id
	private long id;
	private Date date;
	private String name;
	public TraceRecord() {
	}
	public TraceRecord(Date date, String name) {
		this.date = date;
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
