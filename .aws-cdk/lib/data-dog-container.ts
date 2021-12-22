import * as ecs from '@aws-cdk/aws-ecs';
import {TaskDefinition} from "@aws-cdk/aws-ecs/lib/base/task-definition";
import {ddApiKey, ddApplicationKey, ddHostTag, deployEnv, githubSha} from "./utils";

export function addDataDogContainer(taskDefinition: TaskDefinition): TaskDefinition  {
    taskDefinition.addContainer('Datadog',  {
        image: ecs.ContainerImage.fromRegistry('datadog/agent:latest'),
        portMappings: [
            {
                containerPort: 8126,
                hostPort: 8126,
                protocol: ecs.Protocol.TCP
            }
        ],
        environment: {
            'DD_API_KEY': ddApiKey(),
            'DD_APM_ENABLED': 'true',
            'ECS_FARGATE': 'true',
            'DD_LOGS_ENABLED': 'true',
            'DD_APM_NON_LOCAL_TRAFFIC': 'true'
        }
    })

    taskDefinition.addFirelensLogRouter('Fluent-Bit-Log-Router', {
        image: ecs.ContainerImage.fromRegistry('amazon/aws-for-fluent-bit:2.18.0'),
        essential: true,
        firelensConfig: {
            type: ecs.FirelensLogRouterType.FLUENTBIT,
            options: {
                enableECSLogMetadata: true,
                configFileType: ecs.FirelensConfigFileType.FILE,
                configFileValue: '/fluent-bit/configs/parse-json.conf'
            }
        }
    })

    return taskDefinition
}

export function defaultDatadogServiceEnv(serviceName: string): {[key: string]: string} {
    return {
        TRACING_ENABLED: 'true',
        DD_API_KEY: ddApiKey(),
        DD_APPLICATION_KEY: ddApplicationKey(),
        DD_HOST_TAG: ddHostTag(),
        DD_VERSION: githubSha(),
        DD_PROFILING_ENABLED: 'true',
        DD_TRACE_AGENT_PORT: '8126',
        DD_AGENT_HOST: '127.0.0.1',
        DD_LOGS_INJECTION: 'true',
        DD_TRACE_ANALYTICS_ENABLED: 'true',
        DD_ENV: deployEnv(),
        DD_SERVICE: serviceName,
    }
}