import * as ecs from '@aws-cdk/aws-ecs';
import {TaskDefinition} from "@aws-cdk/aws-ecs/lib/base/task-definition";
import {ddApiKey, ddApplicationKey, ddHostTag, deployEnv, githubSha} from "./utils";

export function addEventStoreContainer(taskDefinition: TaskDefinition): TaskDefinition {
    taskDefinition.addContainer('Event-store', {
        image: ecs.ContainerImage.fromRegistry('eventstore/eventstore:20.10.2-buster-slim'),
        portMappings: [
            {
                containerPort: 1113,
                hostPort: 1113,
                protocol: ecs.Protocol.TCP
            },
            {
                containerPort: 2113,
                hostPort: 2113,
                protocol: ecs.Protocol.TCP
            }
        ],
        environment: {
            'EVENTSTORE_CLUSTER_SIZE': '1',
            'EVENTSTORE_RUN_PROJECTIONS': 'All',
            'EVENTSTORE_START_STANDARD_PROJECTIONS': 'true',
            'EVENTSTORE_EXT_TCP_PORT': '1113',
            'EVENTSTORE_HTTP_PORT': '2113',
            'EVENTSTORE_INSECURE': 'true',
            'EVENTSTORE_ENABLE_EXTERNAL_TCP': 'true',
            'EVENTSTORE_ENABLE_ATOM_PUB_OVER_HTTP': 'true'
        }
    })

    return taskDefinition
}