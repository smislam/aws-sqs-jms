#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { AwsSqsJmsStack } from '../lib/aws-sqs-jms-stack';

const app = new cdk.App();
new AwsSqsJmsStack(app, 'AwsSqsJmsStack', {
  env: { account: process.env.CDK_DEFAULT_ACCOUNT, region: process.env.CDK_DEFAULT_REGION }
});