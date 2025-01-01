package com.iglens.k8s;

import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1Status;
import java.util.List;

public class KubernetesClientTest {
    public static void main(String[] args) {
        KubernetesClient client = KubernetesClient.getInstance();
        
        // 测试命名空间操作
        testNamespaceOperations(client);
        
        // 测试Pod操作
        testPodOperations(client);
        
        // 测试Service操作
        testServiceOperations(client);
        
        // 测试Deployment操作
        testDeploymentOperations(client);
    }
    
    private static void testNamespaceOperations(KubernetesClient client) {
        System.out.println("===== Testing Namespace Operations =====");
        
        // 列出所有命名空间
        List<String> namespaces = client.listNamespace();
        System.out.println("Current namespaces: " + namespaces);
        
        // 创建新的命名空间
        String testNamespace = "test-namespace-" + System.currentTimeMillis();
        boolean created = client.createNamespace(testNamespace);
        System.out.println("Created namespace " + testNamespace + ": " + created);
    }
    
    private static void testPodOperations(KubernetesClient client) {
        System.out.println("\n===== Testing Pod Operations =====");
        
        String namespace = "default";
        
        // 创建Pod的YAML配置
        String podYaml = """
            apiVersion: v1
            kind: Pod
            metadata:
              name: test-pod
              labels:
                app: test
            spec:
              containers:
              - name: nginx
                image: nginx:latest
                ports:
                - containerPort: 80
            """;
            
        // 创建Pod
        V1Pod pod = client.yaml2V1Pod(podYaml);
        V1Pod createdPod = client.createNamespacedPod(namespace, pod);
        System.out.println("Created pod: " + (createdPod != null ? createdPod.getMetadata().getName() : "failed"));
        
        // 列出Pod
        V1PodList pods = client.listNamespacedPod(namespace);
        if (pods != null) {
            System.out.println("Pods in namespace " + namespace + ":");
            pods.getItems().forEach(p -> System.out.println("- " + p.getMetadata().getName()));
        }
        
        // 删除Pod
        if (createdPod != null) {
            V1Pod deletedPod = client.deleteNamespacedPod(namespace, createdPod.getMetadata().getName());
            System.out.println("Deleted pod: " + (deletedPod != null ? "success" : "failed"));
        }
    }
    
    private static void testServiceOperations(KubernetesClient client) {
        System.out.println("\n===== Testing Service Operations =====");
        
        String namespace = "default";
        
        // 创建Service的YAML配置
        String serviceYaml = """
            apiVersion: v1
            kind: Service
            metadata:
              name: test-service
            spec:
              selector:
                app: test
              ports:
              - port: 80
                targetPort: 80
              type: ClusterIP
            """;
            
        // 创建Service
        V1Service service = client.yaml2Service(serviceYaml);
        V1Service createdService = client.createNamespacedService(namespace, service);
        System.out.println("Created service: " + (createdService != null ? createdService.getMetadata().getName() : "failed"));
        
        // 删除Service
        if (createdService != null) {
            V1Service status = client.deleteNamespacedService(namespace, createdService.getMetadata().getName());
            System.out.println("Deleted service: " + (status != null ? "success" : "failed"));
        }
    }
    
    private static void testDeploymentOperations(KubernetesClient client) {
        System.out.println("\n===== Testing Deployment Operations =====");
        
        String namespace = "default";
        
        // 创建Deployment的YAML配置
        String deploymentYaml = """
            apiVersion: apps/v1
            kind: Deployment
            metadata:
              name: test-deployment
            spec:
              replicas: 1
              selector:
                matchLabels:
                  app: test
              template:
                metadata:
                  labels:
                    app: test
                spec:
                  containers:
                  - name: nginx
                    image: nginx:latest
                    ports:
                    - containerPort: 80
            """;
            
        // 创建Deployment
        V1Deployment deployment = client.yaml2Deployment(deploymentYaml);
        V1Deployment createdDeployment = client.createNamespacedDeployment(namespace, deployment);
        System.out.println("Created deployment: " + (createdDeployment != null ? createdDeployment.getMetadata().getName() : "failed"));
        
        // 删除Deployment
        if (createdDeployment != null) {
            V1Status status = client.deleteNamespacedDeployment(namespace, createdDeployment.getMetadata().getName());
            System.out.println("Deleted deployment: " + (status != null ? "success" : "failed"));
        }
    }
}