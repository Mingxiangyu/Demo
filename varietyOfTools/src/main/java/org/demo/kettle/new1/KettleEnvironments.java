package org.demo.kettle.new1;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

public class KettleEnvironments {
  public static KettleDatabaseRepository repository;
  public static DatabaseMeta databaseMeta;
  public static KettleDatabaseRepositoryMeta kettleDatabaseMeta;
  public static RepositoryDirectoryInterface directory;
  /*
   * KETTLE初始化*/
  public static String KettleEnvironments() {
    try {
      KettleEnvironment.init();
      repository = new KettleDatabaseRepository();
      databaseMeta =
          new DatabaseMeta(
              "ETL",
              "Oracle",
              "Native",
              "172.14.5.6",
              "cdr",
              "1521",
              "ETL",
              "xin"); // 资源库数据库地址，我这里采用oracle数据库
      kettleDatabaseMeta =
          new KettleDatabaseRepositoryMeta(
              "ETL", "ERP", "Transformation description", databaseMeta);
      repository.init(kettleDatabaseMeta);
      repository.connect("adm", "adm"); // 资源库用户名和密码
      directory = repository.loadRepositoryDirectoryTree();
    } catch (KettleException e) {
      e.printStackTrace();
      return e.getMessage();
    }
    return null;
  }
}
