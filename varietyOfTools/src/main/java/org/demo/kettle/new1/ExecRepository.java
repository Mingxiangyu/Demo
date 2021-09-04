package org.demo.kettle.new1;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

public class ExecRepository {
  public static void main(String[] args) {
    KettleEnvironments.KettleEnvironments();
    String transName = null; // 转换或作业名称
    String[] params = null; // 参数
    try {
      String result =
          ExecuteDataBaseRepTran(KettleEnvironments.repository, transName, params, null);
    } catch (KettleException e) {
      e.printStackTrace();
    }
  }

  /*执行转换文件*/
  private static String ExecuteDataBaseRepTran(
      KettleDatabaseRepository repository, String transName, String param[], String jgname)
      throws KettleException {
    // 根据变量查找到模型所在的目录对象,此步骤很重要。
    RepositoryDirectoryInterface directory = repository.findDirectory("/");
    // 创建ktr元对象
    TransMeta transformationMeta =
        ((Repository) repository).loadTransformation(transName, directory, null, true, null);
    // 创建ktr
    Trans trans = new Trans(transformationMeta);
    // 执行ktr
    trans.execute(param);
    // 等待执行完毕
    trans.waitUntilFinished();
    if (trans.getErrors() > 0) {
      return "NO";
    } else {
      return "OK";
    }
  }

  private static String execRepositoryJobs(
      KettleDatabaseRepository repository,
      RepositoryDirectoryInterface directory,
      String jobName,
      String jhid)
      throws KettleException {
    JobMeta jobMeta = ((Repository) repository).loadJob(jobName, directory, null, null);
    Job job = new Job(repository, jobMeta);
    job.setVariable("id", jhid);
    job.start();
    job.waitUntilFinished();
    if (job.getErrors() > 0) {
      return "NO";
    } else {
      return "OK";
    }
  }
}
