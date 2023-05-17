import * as cdk from 'aws-cdk-lib';
import { Peer, Port, Vpc } from 'aws-cdk-lib/aws-ec2';
import { Cluster, ContainerImage, FargateService, FargateTaskDefinition, LogDrivers } from 'aws-cdk-lib/aws-ecs';
import { Bucket } from 'aws-cdk-lib/aws-s3';
import { Queue } from 'aws-cdk-lib/aws-sqs';
import { ApplicationLoadBalancer, ApplicationProtocol } from 'aws-cdk-lib/aws-elasticloadbalancingv2';
import { Construct } from 'constructs';

export class AwsSqsJmsStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    
    //Bucket is created for you (use SQS Java extended library to configure)
    const sqsBucket = new Bucket(this, 'my-queue-bucket', {
      bucketName: 'superqueue',
      removalPolicy: cdk.RemovalPolicy.DESTROY      
    });
    
    const sqsDlq = new Queue(this, 'mydlqqueue', {
      queueName: 'superqueue-dlq',
      removalPolicy: cdk.RemovalPolicy.DESTROY
    });

    const sqs = new Queue(this, 'myqueue', {
      queueName: 'superqueue',
      removalPolicy: cdk.RemovalPolicy.DESTROY,
      deadLetterQueue: {
        queue: sqsDlq,
        maxReceiveCount: 5
      }
    });

    const vpc = new Vpc(this, 'app-vpc', {});

    const cluster = new Cluster(this, 'Cluster', {vpc});

    const taskDefinition = new FargateTaskDefinition(this, 'TaskDef', {});
    
    //Allow taskdef to send and consume messages.
    sqs.grantSendMessages(taskDefinition.taskRole);
    sqs.grantConsumeMessages(taskDefinition.taskRole);

    const container = taskDefinition.addContainer('myContainer', {
      image: ContainerImage.fromAsset('./message'),
      memoryLimitMiB: 256,
      cpu: 256,
      portMappings: [{
        containerPort: 8080,
        hostPort: 8080
      }],
      logging: LogDrivers.awsLogs({streamPrefix: 'my-message-service'}),
    });

    const ecsService = new FargateService(this, 'Service', {
      cluster,
      taskDefinition,
      desiredCount: 1
    });

    const alb = new ApplicationLoadBalancer(this, 'alb', {
      vpc,
      internetFacing: true
    });

    const listener = alb.addListener('message-listener', {
      port: 80
    });

    listener.addTargets('message-target', {
      port: 8080,
      targets: [ecsService],
      protocol: ApplicationProtocol.HTTP,
      healthCheck: {
        healthyThresholdCount: 2,
        unhealthyThresholdCount: 10,
        timeout: cdk.Duration.seconds(20),
        interval: cdk.Duration.seconds(30)
      }
    });

    new cdk.CfnOutput(this, 'alb-url', {
      value: alb.loadBalancerDnsName,
      exportName: 'message-stack-loadBalancerDnsName'
    });
  }
}
