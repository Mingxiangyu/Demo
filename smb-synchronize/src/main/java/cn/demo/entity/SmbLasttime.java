package cn.demo.entity;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * (SmbLasttime)表实体类
 *
 * @author Ray。
 * @date 2020-07-22 14:06:49
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SMB_LASTTIME")
public class SmbLasttime {

  /** 声明id */
  @Id @GeneratedValue private Integer id;

  /** 该月级文件夹名称 */
  private Integer month;
  /** 该日级文件夹名称 */
  private Integer day;
  /** 远程文件名 */
  private String fileName;
  /** 该文件是否迁移成功 */
  private boolean success;

  @Deprecated private String lastFileName;
  @Deprecated private Long lastModifyTime;
  @Deprecated private Boolean isNew;
  /** 创建时间 */
  @CreatedDate private Instant createStamp;

  /** 修改时间 */
  @LastModifiedDate private Instant modifyStamp;

  @Version private Integer version;
}
