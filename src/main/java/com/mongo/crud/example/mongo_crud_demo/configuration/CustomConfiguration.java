package com.mongo.crud.example.mongo_crud_demo.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@org.springframework.context.annotation.Configuration
public class CustomConfiguration {

    @Bean
    public WebClient getWebClient()
    {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(getHttpClient())).build();
    }

    private HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
                .responseTimeout(Duration.ofMillis(50000))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS)));

	/*	return new HttpClient()
		{
			// For custom logging
			@Override
			public org.eclipse.jetty.client.api.Request newRequest(URI uri)
			{
				org.eclipse.jetty.client.api.Request inboundRequest = super.newRequest(uri);
				return httpClientInterceptor(inboundRequest);
			}
		};*/
    }

    /**
     * This takes a Request allowing us to intercept and log the request and response data and then and gives the request back to the flow.
     * inboundRequest {Request} The actual inbound request
     * {Request} back to the flow
     */
/*	private Request httpClientInterceptor(Request inboundRequest)
	{
		StringBuilder requestBuilder = new StringBuilder();
		inboundRequest.onRequestBegin(request -> requestBuilder.append(
				"\n---------------------- OUTBOUND REST REQUEST -------------------------").append("\nRequest URI: ").append(request.getURI()).append(
				"\nRequest Method: ").append(request.getMethod()));

		inboundRequest.onRequestHeaders(request -> {
			requestBuilder.append("\nRequest Headers: [");
			for (HttpField header : request.getHeaders())
				requestBuilder.append(header.getName()).append(" : ").append(header.getValue()).append(", ");
			requestBuilder.setLength(requestBuilder.length() - 2); //Removing the trailing comma
			requestBuilder.append("]").append("\nRequest Body: ");
		});

		// Get request body
		StringBuilder requestBody = new StringBuilder();
		inboundRequest.onRequestContent((request, content) -> requestBody.append(getBody(content)));

		StringBuilder responseBuilder = new StringBuilder();
		inboundRequest.onResponseBegin(response -> responseBuilder.append(
				"\n---------------------- INBOUND REST RESPONSE -------------------------").append("\nResponse Status: ").append(response.getStatus()));

		inboundRequest.onResponseHeaders(response -> {
			responseBuilder.append("\nResponse Headers: [");
			for (HttpField header : response.getHeaders())
				responseBuilder.append(header.getName()).append(" : ").append(header.getValue()).append(", ");
			responseBuilder.setLength(responseBuilder.length() - 2); //Removing the trailing comma
			responseBuilder.append("]").append("\nResponse Body: ");
		});

		// Get response body
		StringBuilder responseBody = new StringBuilder();
		inboundRequest.onResponseContent((response, content) -> responseBody.append(getBody(content)));

		// Actual logging the request & response data
		inboundRequest.onRequestSuccess(request -> {
			String body = requestBody.toString();
			requestBuilder.append(StringUtils.hasText(body) ? body : "[no-body]").append(
					"\n---------------------------------------------------------------------");
		});
		inboundRequest.onResponseSuccess(response -> {
			String body = responseBody.toString();
			responseBuilder.append(StringUtils.hasText(body) ? body : "[no-body]").append(
					"\n---------------------------------------------------------------------");
		});

		return inboundRequest;
	}

	private String getBody(ByteBuffer content)
	{
		// Always decode the content into CharBuffer, otherwise reading response-body will throw UnsupportedOperationException
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(content);
		return charBuffer.toString();
	}*/
}
