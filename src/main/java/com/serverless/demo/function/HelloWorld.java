package com.serverless.demo.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.lambda.metric.MetricStatsConstants;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;

/**
 * This is the first program which connects with Amazon CloudWatch. 
 */
public class HelloWorld implements RequestHandler<String, String> {
    @Override
    public String handleRequest(String input, Context context) {
        String output = "Hello " + ((input != null && !input.isEmpty()) ? input : "World");
        context.getLogger().log(output);
        
        final AmazonCloudWatch cw =
        	    //AmazonCloudWatchClientBuilder.defaultClient();
        		AmazonCloudWatchClientBuilder.standard().withRegion("us-east-1").build();
        
        	ListMetricsRequest request = new ListMetricsRequest()
        	        .withMetricName("CallsPerInterval")
        	        .withNamespace("AWS/EC2");

        	System.out.println("List of metrics %s"+ request.getMetricName());
        	
        	boolean done = false;

        	while(!done) {
        	    ListMetricsResult response = cw.listMetrics(request);
        	    System.out.println("Size of metric %s"+ response.getMetrics().size());
        	    for(Metric metric : response.getMetrics()) {
        	        System.out.println("Retrieved metric %s"+ metric.getMetricName());
        	    }

        	    request.setNextToken(response.getNextToken());

        	    if(response.getNextToken() == null) {
        	        done = true;
        	    }
        	}
        
        
        
        return output;
    }
}