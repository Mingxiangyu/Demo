package com.iglens.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class KubernetesClient {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesClient.class);
    private String kubeConfigFileName = "kubeConfig.yaml";
    private ApiClient apiClient;
    private static KubernetesClient instance = new KubernetesClient();

    private KubernetesClient() {
        loadKubeConfig();
    }

    public static KubernetesClient getInstance() {
        return instance;
    }

    private void loadKubeConfig() {
        String kubeConfigPath = "";
        try {
            File file = new ClassPathResource("/" + kubeConfigFileName).getFile();
            if (null == file || !file.exists()) {
                file = ResourceUtils.getFile("classpath:" + kubeConfigFileName);
            }
            if (file.exists()) {
                kubeConfigPath = file.getAbsolutePath();
            }
            try {
                apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Configuration.setDefaultApiClient(apiClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public boolean createNamespace(String namespaceName) {
        V1Namespace namespace = new V1Namespace()
            .apiVersion("v1")
            .kind("Namespace")
            .metadata(new V1ObjectMeta()
                .name(namespaceName)
                .labels(Collections.singletonMap("name", namespaceName)));

        new CoreV1Api(apiClient).createNamespace(namespace);
        return true;
    }

    public V1Deployment yaml2Deployment(String yaml) {
        try {
            return yaml != null ? (V1Deployment) Yaml.load(yaml) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public V1Service yaml2Service(String yaml) {
        try {
            return yaml != null ? (V1Service) Yaml.load(yaml) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public V1Pod createNamespacedPod(String nameSpace, V1Pod body) {
        try {
            return new CoreV1Api(apiClient).createNamespacedPod(nameSpace, body).execute();
        } catch (ApiException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public V1Pod yaml2V1Pod(String yaml) {
        try {
            return yaml != null ? (V1Pod) Yaml.load(yaml) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public V1Pod deleteNamespacedPod(String nameSpace, String podName) {
        try {
            return new CoreV1Api(apiClient).deleteNamespacedPod(podName, nameSpace).execute();
        } catch (ApiException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public V1Service createNamespacedService(String nameSpace, V1Service body) {
        try {
            return new CoreV1Api(apiClient).createNamespacedService(nameSpace, body).execute();
        } catch (ApiException e) {
            logger.error("Creating service failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public V1Service deleteNamespacedService(String nameSpace, String serviceName) {
        try {
            return new CoreV1Api(apiClient).deleteNamespacedService(serviceName, nameSpace).execute();
        } catch (ApiException e) {
            logger.error("Deleting service failed: " + e.getMessage());
            return null;
        }
    }

    public V1Deployment createNamespacedDeployment(String nameSpace, V1Deployment body) {
        try {
            return new AppsV1Api(apiClient).createNamespacedDeployment(nameSpace, body).execute();
        } catch (ApiException e) {
            try {
                Thread.sleep(2000);
                return new AppsV1Api(apiClient).createNamespacedDeployment(nameSpace, body).execute();
            } catch (ApiException | InterruptedException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    public V1Status deleteNamespacedDeployment(String nameSpace, String deploymentName) {
        try {
            return new AppsV1Api(apiClient).deleteNamespacedDeployment(deploymentName, nameSpace).execute();
        } catch (ApiException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public List<String> listNamespace() {
        try {
            V1NamespaceList namespaceList = new CoreV1Api(apiClient).listNamespace().execute();
            List<String> namespaces = new ArrayList<>();
            if (namespaceList != null) {
                namespaceList.getItems().forEach(namespace ->
                    namespaces.add(namespace.getMetadata().getName()));
            }
            return namespaces;
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public V1PodList listNamespacedPod(String namespace) {
        try {
            return new CoreV1Api(apiClient).listNamespacedPod(namespace).execute();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public V1NodeList listNode() {
        try {
            return new CoreV1Api(apiClient).listNode().execute();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getRandomString(int length) {
        Random random = new Random();
        return random.ints(length, 0, 36)
            .mapToObj(i -> "abcdefghijklmnopqrstuvwxyz0123456789".charAt(i))
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }
}