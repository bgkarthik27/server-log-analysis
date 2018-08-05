package ch.karthi.dufry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "method", "url", "protocol", "protocol_version" })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Request {

	@JsonProperty("method")
	private String method;

	@JsonProperty("url")
	private String url;

	@JsonProperty("protocol")
	private String protocol;

	@JsonProperty("protocol_version")
	private String protocolVersion;

}