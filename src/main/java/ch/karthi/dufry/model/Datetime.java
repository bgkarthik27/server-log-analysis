package ch.karthi.dufry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "day", "hour", "minute", "second" })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Datetime {

	@JsonProperty("day")
	private String day;

	@JsonProperty("hour")
	private String hour;

	@JsonProperty("minute")
	private String minute;

	@JsonProperty("second")
	private String second;

}