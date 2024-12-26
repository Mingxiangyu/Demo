package com.iglens.dockerDeploy;

import com.alibaba.fastjson.JSON;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.api.model.ResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DockerUtil {

  // public static void main(String[] args) {
  //   try {
  //     // Initialize DockerUtil
  //     DockerUtil dockerUtil = new DockerUtil();
  //
  //     // Configure Docker connection settings
  //     dockerUtil.serverUrl = "tcp://localhost:2375"; // Replace with your Docker server URL
  //     dockerUtil.registryUrl = "registry.example.com"; // Replace with your registry URL
  //     dockerUtil.registryUsername = "admin";
  //     dockerUtil.registryPassword = "Harbor12345";
  //
  //     // Get Docker client
  //     DockerClient dockerClient = dockerUtil.getDockerClient();
  //     System.out.println("Successfully connected to Docker daemon");
  //
  //     // Test image operations
  //     // 1. Load image from tar file
  //     File imageFile = new File("/path/to/your/image.tar"); // Replace with actual image path
  //     if (imageFile.exists()) {
  //       String imageId = dockerUtil.loadImage(imageFile, dockerClient);
  //       System.out.println("Loaded image with ID: " + imageId);
  //
  //       // 2. Tag image
  //       String projectName = "test-project";
  //       String repository = "test-repo";
  //       String version = "1.0";
  //       dockerUtil.tagImage(projectName, repository, version, imageId, dockerClient);
  //       System.out.println("Tagged image successfully");
  //
  //       // 3. Push image to registry
  //       dockerUtil.pushImage(projectName, repository, version, dockerClient);
  //       System.out.println("Pushed image to registry successfully");
  //     }
  //
  //     // Test container operations
  //     // 4. Pull image from registry
  //     String imageName = dockerUtil.getRegistryUrl() + "/test-project/test-repo:1.0";
  //     dockerUtil.pullImage(imageName, dockerClient);
  //     System.out.println("Pulled image successfully");
  //
  //     // 5. Create container
  //     String containerName = "test-container";
  //     int dockerPort = 8080;
  //     int modelPort = 80;
  //     CreateContainerResponse container = dockerUtil.createContainers(
  //         containerName,
  //         imageName,
  //         dockerPort,
  //         modelPort,
  //         dockerClient
  //     );
  //     System.out.println("Created container with ID: " + container.getId());
  //
  //     // 6. Start container
  //     dockerUtil.startContainer(container.getId(), dockerClient);
  //     System.out.println("Started container successfully");
  //
  //     // Wait for a while to let container run
  //     Thread.sleep(10000);
  //
  //     // 7. Stop container
  //     dockerUtil.stopContainer(container.getId(), dockerClient);
  //     System.out.println("Stopped container successfully");
  //
  //     // 8. Remove container
  //     dockerUtil.removeContainer(container.getId(), dockerClient);
  //     System.out.println("Removed container successfully");
  //
  //     // 9. Remove image
  //     // dockerUtil.removeImage(imageId, dockerClient);
  //     System.out.println("Removed image successfully");
  //
  //   } catch (Exception e) {
  //     System.err.println("Error occurred during Docker operations:");
  //     e.printStackTrace();
  //   }
  // }

  public static void main(String[] args) {
    DockerClient dockerClient = getDockerClient();
    // BuildImageCmd buildImageCmd = dockerClient.buildImageCmd();
    // System.out.println(buildImageCmd);
    String dockerInfo = getDockerInfo(dockerClient);
    System.out.println(dockerInfo);

    File file = new File("E:\\测试");
    String s = buildImage(dockerClient, file, "test", "1.0", System.currentTimeMillis());

    List<Image> images = imageList(dockerClient);
    for (Image image : images) {
      System.out.println(image.toString());
    }

  }
    private final static Logger logger = LoggerFactory.getLogger(DockerUtil.class);

    // docker服务端IP地址
    // @Value("DOCKER_HOST")
    public static final String DOCKER_HOST="tcp://10.130.0.13:2375";
    // docker安全证书配置路径
    public static final String DCOEKR_CERT_PATH="";
    // docker是否需要TLS认证
    public static final Boolean DOCKER_TLS_VERIFY=false;
    // Harbor仓库的IP
//    public static final String REGISTRY_URL="192.168.79.131:8443";
    public static final String REGISTRY_URL="";
    // Harbor仓库的名称
//    public static final String REGISTRY_PROJECT_NAME="test";
    public static final String REGISTRY_PROJECT_NAME="";
    // Harbor仓库的登录用户名
//    public static final String REGISTRY_USER_NAME="admin";
    public static final String REGISTRY_USER_NAME="";
    // Harbor仓库的登录密码
//    public static final String REGISTRY_PASSWORD="Harbor12345";
    public static final String REGISTRY_PASSWORD="";
    // docker远程仓库的类型，此处默认是harbor
    public static final String REGISTRY_TYPE="harbor";

    public static final String REGISTRY_PROTOCAL="https://";


    /**
     * 构建DocekrClient实例
     * @param dockerHost
     * @param tlsVerify
     * @param dockerCertPath
     * @param registryUsername
     * @param registryPassword
     * @param registryUrl
     * @return
     */
    public static DockerClient getDocekrClient(String dockerHost,boolean tlsVerify,String dockerCertPath,
                                               String registryUsername, String registryPassword,String registryUrl){
        DefaultDockerClientConfig dockerClientConfig = null;
        if(tlsVerify){
            dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(DOCKER_HOST)
                    .withDockerTlsVerify(true)
                    .withDockerCertPath(DCOEKR_CERT_PATH)
                    .withRegistryUsername(REGISTRY_USER_NAME)
                    .withRegistryPassword(REGISTRY_PASSWORD)
                    .withRegistryUrl(registryUrl)
                    .build();
        }else {
            dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(DOCKER_HOST)
                    .withDockerTlsVerify(false)
                    .withDockerCertPath(DCOEKR_CERT_PATH)
                    .withRegistryUsername(REGISTRY_USER_NAME)
                    .withRegistryPassword(REGISTRY_PASSWORD)
                    .withRegistryUrl(registryUrl)
                    .build();
        }
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .build();

        return DockerClientImpl.getInstance(dockerClientConfig,httpClient);
    }

    public static DockerClient getDockerClient(){
        return getDocekrClient(DOCKER_HOST,DOCKER_TLS_VERIFY,DCOEKR_CERT_PATH,REGISTRY_USER_NAME,REGISTRY_PASSWORD,REGISTRY_URL);
    }

    /**
     * 获取docker基础信息
     * @param dockerClient
     * @return
     */
    public static String getDockerInfo(DockerClient dockerClient){
        Info info = dockerClient.infoCmd().exec();
        return JSON.toJSONString(info);
    }

    /**
     * 给镜像打标签
     * @param dockerClient
     * @param imageIdOrFullName
     * @param respository
     * @param tag
     */
    public static void tagImage(DockerClient dockerClient, String imageIdOrFullName, String respository,String tag){
        TagImageCmd tagImageCmd = dockerClient.tagImageCmd(imageIdOrFullName, respository, tag);
        tagImageCmd.exec();
    }

    /**
     * load镜像
     * @param dockerClient
     * @param inputStream
     */
    public static void loadImage(DockerClient dockerClient, InputStream inputStream){
        LoadImageCmd loadImageCmd = dockerClient.loadImageCmd(inputStream);
        loadImageCmd.exec();
    }

    /**
     * pull镜像
     * @param dockerClient
     * @param repository
     */
    public static PullImageCmd pullImage(DockerClient dockerClient,String repository){
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(repository);
        pullImageCmd.exec(new ResultCallback<PullResponseItem>() {
            @Override
            public void onStart(Closeable closeable) {
                System.out.println("开始拉取");
            }

            @Override
            public void onNext(PullResponseItem object) {
                System.out.println("拉取下一个");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("拉取发生错误:"+throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("拉取成功");
            }

            @Override
            public void close() throws IOException {
                System.out.println("拉取结束");
            }
        });
        return pullImageCmd;
    }

    /**
     * 推送镜像
     * @param dockerClient
     * @param imageName
     * @return
     * @throws InterruptedException
     */
    public static Boolean pushImage(DockerClient dockerClient,String imageName) throws InterruptedException {
        final Boolean[] result = {true};
        ResultCallback.Adapter<PushResponseItem> callBack = new ResultCallback.Adapter<PushResponseItem>() {
            @Override
            public void onNext(PushResponseItem pushResponseItem) {
                if (pushResponseItem != null){
                    ResponseItem.ErrorDetail errorDetail = pushResponseItem.getErrorDetail();
                    if (errorDetail!= null){
                        result[0] = false;
                        logger.error(errorDetail.getMessage(),errorDetail);
                    }
                }
                super.onNext(pushResponseItem);
            }
        };
        dockerClient.pushImageCmd(imageName).exec(callBack).awaitCompletion();
        return result[0];
    }

    /**
     * 从镜像的tar文件中获取镜像名称
     * @param imagePath
     * @return
     */
    public static String getImageName(String imagePath){
        try {
            return UnCompress.getImageName(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过dockerFile构建镜像,这里为了给打成的镜像有名字和tag,多传了几个参数
     * @param dockerClient
     * @param dockerFile 这里的dockerFile是一个文件夹，里面包含 DockerFile 和 程序文件（jar包/python文件等）
     * @return
     */
    public static String buildImage(DockerClient dockerClient, File dockerFile,String imageName,String tags,long currentTimeMillis){
              // 清理和验证镜像名称和标签
        imageName = sanitizeImageName(imageName);
        tags = sanitizeTag(tags);
         // 构建符合规范的镜像标签
                String imageTag = String.format("%s-%d:%s",
                    imageName,
                    System.currentTimeMillis(),
                    tags);
        Set<String> tagsSet = new HashSet<>();

                logger.info("Building image with tag: {}", imageTag);
                tagsSet.add(imageTag);


        //拼成 name-时间戳:tag 格式
        tagsSet.add(imageName+"-"+System.currentTimeMillis()+":"+tags);

        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd(dockerFile)
                .withTags(tagsSet);
        BuildImageResultCallback buildImageResultCallback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                logger.info("{}", item);
                super.onNext(item);
            }
        };

        return buildImageCmd.exec(buildImageResultCallback).awaitImageId();
//        logger.info(imageId);
    }

    /**
     * 获取镜像列表
     * @param dockerClient
     * @return
     */
    public static List<Image> imageList(DockerClient dockerClient){
        List<Image> imageList = dockerClient.listImagesCmd().withShowAll(true).exec();
        return imageList;
    }


    /**
     * 清理镜像名称
     * @param name
     * @return
     */
    private static String sanitizeImageName(String name) {
        // 将名称转换为小写
        name = name.toLowerCase();
        // 替换不允许的字符为连字符
        name = name.replaceAll("[^a-z0-9._-]", "-");
        // 确保以字母数字开头
        if (!name.matches("^[a-z0-9].*")) {
            name = "a" + name;
        }
        return name;
    }

     /**
     * 清理镜像 tag
     * @param tag
     * @return
     */
    private static String sanitizeTag(String tag) {
        // 替换不允许的字符
        tag = tag.replaceAll("[^a-zA-Z0-9._-]", "-");
        // 确保以字母数字开头
        if (!tag.matches("^[a-zA-Z0-9].*")) {
            tag = "1" + tag;
        }
        return tag;
    }
}