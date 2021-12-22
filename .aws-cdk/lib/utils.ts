import {Construct} from "@aws-cdk/core";
import {StringParameter} from "@aws-cdk/aws-ssm";

const APPLICATION_NAME = 'psinder'

export function applicationName(): string {
    return APPLICATION_NAME
}

export function imageTag(): string {
    return process.env.IMAGE_TAG || 'latest'
}

export function ddApiKey(): string {
    return process.env.DD_API_KEY || 'undefined'
}

export function ddApplicationKey(): string {
    return process.env.DD_APPLICATION_KEY || 'undefined'
}

export function ddHostTag(): string {
    return envSpecificName(applicationName())
}

export function githubSha(): string {
    return process.env.GITHUB_SHA || 'undefined'
}

export function secureSsmParameter(scope: Construct, fullParameterPath: string, version = 1) {
    ensureValidParameterPath(fullParameterPath)
    return StringParameter.fromSecureStringParameterAttributes(scope, fullParameterPath, {
        parameterName: fullParameterPath,
        version
    })
}

export function secureSsmParameterForEnv(scope: Construct, fullParameterPath: string, version = 1) {
    ensureValidParameterPath(fullParameterPath)
    const resolvedPath = ssmPathForEnv(fullParameterPath)
    return StringParameter.fromSecureStringParameterAttributes(scope, resolvedPath, {
        parameterName: resolvedPath,
        version
    })
}

function ssmPathForEnv(path: string) {
    return `/${DEPLOY_ENV}${path}`
}

function ensureValidParameterPath(path: string) {
    if(!path.startsWith("/")) {
        throw new Error(`Parameter path must start with / got: ${path} `)
    }
}

export type DeployEnv = KnownDeployEnv | string

export enum KnownDeployEnv {
    prod = 'prod',
    stage = 'stage',
    dev = 'dev'
}

const DEPLOY_ENV: DeployEnv = process.env.DEPLOY_ENV || 'stage'

export function deployEnv(): DeployEnv {
    return DEPLOY_ENV;
}

export function envSpecificName(name: string) {
    const prefix = DEPLOY_ENV + "-";
    if (name.startsWith(prefix)) {
        return name
    } else {
        return `${prefix}${name}`
    }
}

export function zoneName() {
    return envSpecificName(APPLICATION_NAME).replace(/-/g, '.')
}

export function vpcCidr(): string {
    return '10.0.0.0/23'
}