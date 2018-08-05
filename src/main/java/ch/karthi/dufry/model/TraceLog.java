package ch.karthi.dufry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "host", "datetime", "request", "response_code", "document_size" })
@Getter
@Setter
public class TraceLog {

	private String host;

	private Datetime datetime;

	private Request request;

	@JsonProperty("response_code")
	private String responseCode;

	@JsonProperty("document_size")
	private String documentSize;
	
	
	@JsonIgnore
	public String getOnlyDayHour() {
		return this.datetime.getDay() ;
	}
	
}
