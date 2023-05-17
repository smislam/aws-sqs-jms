# An Example of Amazon SQS with JMS and Spring Boot using AWS CDK

This example uses Amazon SQS with JMS.  It is deployed to AWS ECS and exposed using AWS ALB.  Infrastructure deployment code is using AWS CDK,

## Implementation Details
This example uses:
* AWS ELB and AWS ECS using AWS CDK
* Uses Amazon SQS standard queue for simple messages
* Implements Dead Letter Queue to store messages that fails after five (5) retries
* Uses Spring Boot with Spring JMS to configure Amazon SQS
* Uses Amazon SQS Java extended library so that large messages can be processed
  * This example doesn't implement large messages which you will need to do it on your own

## Steps to run
* Run the pipeline. Wait for the pipeline to finish.
* Run the following command on your command window to publish a message.  Use the ALB url from the pipeline stack output.
  * ![image](send.PNG "User sends a message")
* Go to your console and see Amazon SQS queue has one (1) message waiting
  * ![image](sqs-wait.PNG "Messages waiting on SQS")
* Run the following command on your command window to consume the message
  * ![image](receive.PNG "User consumes a message")
* Go to your console and see Amazon SQS queue has zero (0) message waiting
  * ![image](sqs-consumed.PNG "Messages consumed from SQS")

### Notes
* This example uses JmsTemplate.  I have also used SQSConnectionFactory (for your reference) to send and consume messages.  If that is your requirement, uncomment those code and use them.