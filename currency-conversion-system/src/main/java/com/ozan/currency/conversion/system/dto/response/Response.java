package com.ozan.currency.conversion.system.dto.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ozan.currency.conversion.system.util.helper.DateHelper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {

	private static final long serialVersionUID = -350490084976633189L;

	@Schema(description = "HTTP status code")
	@JsonProperty("status")
    private Integer status;

	@Schema(description = "Exact response time of service", example = "2021-09-13 11:58:03")
	@JsonProperty("time")
    private String time;

	@Schema(description = "Includes error details when an error occured and HTTP status is not 200")
	@JsonProperty("errors")
    private ResponseError errors;

    public Response ok() {

        Response response = new Response();
        response.setStatus(HttpStatus.OK.value());
        response.setTime(DateHelper.getNowAsStr());
        return response;
    }

    public Response notOk(Integer status) {

        Response response = new Response();
        response.setStatus(status);
        response.setTime(DateHelper.getNowAsStr());
        return response;
    }

    public void addErrorMsg(String errorCode, String errorMessage) {

        setErrors(new ResponseError(errorCode, errorMessage));
    }
}