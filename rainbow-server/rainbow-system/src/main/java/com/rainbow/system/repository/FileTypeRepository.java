package com.rainbow.system.repository;

import com.rainbow.base.repository.BaseRepository;
import com.rainbow.system.entity.FileType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FileTypeRepository extends BaseRepository<FileType,String> {

  @Query("select t from FileType t where t.mimeType = ?1 and t.extension = ?2 ")
  FileType findMimeTypeAndExtension(String mimiType, String extension);

  @Query("select t.extension from FileType t where t.approve = 1 and t.refuse = 0  ")
  List<String> findAllow();

  @Query("select t from FileType t where t.mimeType = ?1 ")
  List<FileType> findByMimeType(String mimeType);

  @Query("select t from FileType t where t.typeName = ?1 ")
  FileType findByTypName(String typeName);

  @Query("select t from FileType t where t.typeName in (?1) ")
  List<FileType> findByTypNameIn(List<String> fileExtList);


  @Query("select t from FileType t where t.extension in (?1) ")
  List<FileType> findByTyExtensionIn(List<String> fileExtList);

  @Query("select t from FileType t where t.extension= ?1 ")
  FileType findByExtension(String fileExt);

}
